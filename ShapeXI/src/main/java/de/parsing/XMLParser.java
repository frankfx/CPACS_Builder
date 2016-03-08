package de.parsing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class XMLParser {

	private DocumentBuilder dBuilder;
	private DocumentBuilderFactory dbFactory;
	private Document doc;

	public XMLParser(String schemaFile, String inputFile){
		init(schemaFile, inputFile);
	}
	
	public boolean init(String schemaFile, String inputFile) {
		dbFactory = DocumentBuilderFactory.newInstance();
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
			
			if(this.validate(schemaFile, doc))
				return false;
			
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	private boolean validate(String schemaFile, Document document) {
		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
			   
		// load a WXS schema, represented by a Schema instance
		Source source = new StreamSource(new File(schemaFile));
		
		try {
			Schema schema = factory.newSchema(source);
			// create a Validator instance, which can be used to validate an instance document
		    schema.newValidator().validate(new DOMSource(document));
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
			return false;
		}		
		return true;
	}
	
	
	public String getRoot(){
		doc.getDocumentElement().normalize();
		return doc.getDocumentElement().getNodeName();
	}
	
	private Node getNodeByID(String parent, String uid){
		NodeList nodes = doc.getElementsByTagName(parent);

		Node cubeProfile = null;
		
		for(int i=0; i<nodes.getLength(); i++){
			cubeProfile = nodes.item(i);
			if (cubeProfile.hasAttributes()){
				NamedNodeMap map = cubeProfile.getAttributes();
				for(int k=0; k<map.getLength(); k++){
					if(map.item(k).getNodeValue().equals(uid)){
						return cubeProfile;
					}
				}
			}
		}
		return null;		
	}
	
	private List<Float> getVector(String param, String uid){
		NodeList nodes = getNodeByID("cubeProfile", uid).getChildNodes();
		
		for(int i=0; i<nodes.getLength(); i++){
			if(nodes.item(i).getNodeName().equals("pointList")){
				NodeList vectors = nodes.item(i).getChildNodes();
				for(int j=0; j<vectors.getLength(); j++){
					if(vectors.item(j).getNodeName().equals(param)){
						String [] tmp = vectors.item(j).getTextContent().split(";");
						List<Float> list = new ArrayList<Float>();
						for(int k=0; k<tmp.length; k++){
							list.add(Float.parseFloat(tmp[k]));
						}
						return list;
					}
				}
			}
		}
		return null;
	}
	
	public List<Float> getXVector(String uid) {
		return this.getVector("x", uid);
	}
	
	public List<Float> getYVector(String uid) {
		return this.getVector("y", uid);
	}
	
	public List<Float> getZVector(String uid) {
		return this.getVector("z", uid);
	}	
	
	public static void main(String[] args) {

		StringBuffer sb = new StringBuffer();
		sb.append(System.getProperty("user.dir"));
		sb.append(File.separator);
		sb.append("src");
		sb.append(File.separator);
		sb.append("main");
		sb.append(File.separator);
		sb.append("resources");

		XMLParser parser = new XMLParser(sb.toString() + File.separator + "ShapeSchema.xsd", sb.toString() + File.separator + "Shape.xml");
		
		System.out.println(parser.getRoot());
		
		System.out.println(parser.getXVector("uid_1"));
//		System.out.println(parser.getYVector("uid_1"));
//		System.out.println(parser.getZVector("uid_1"));
		
		System.out.println(parser.getRoot());
	}
}
