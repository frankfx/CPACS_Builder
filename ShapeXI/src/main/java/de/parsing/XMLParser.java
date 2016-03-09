package de.parsing;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

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

	public XMLParser(){
		init();
	}
	
	public void init() {
		dbFactory = DocumentBuilderFactory.newInstance();
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public boolean parseFile(String schemaFile, String inputFile){
		try {
			doc = dBuilder.parse(inputFile);
		} catch (SAXException ex) {
			System.out.println("SAXException: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("IOException: " + ex.getMessage());
		}
		
		if(this.validate(schemaFile, doc))
			return false;
		
		doc.getDocumentElement().normalize();
		
		return true;
	}
	
	private boolean validate(String schemaFile, Document document) {
		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			   
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
	
	public Node getRoot(){
		if (doc == null)
			return null;
		return doc.getDocumentElement();
	}	
	
	private Node getNodeByID(String node, String uid){
		NodeList nodes = doc.getElementsByTagName(node);

		Node cubeProfile = null;

		for(int i=0; i<nodes.getLength(); i++){
			cubeProfile = nodes.item(i);
			if (cubeProfile.hasAttributes()){
				NamedNodeMap map = cubeProfile.getAttributes();
				for(int k=0; k<map.getLength(); k++){
					if(map.item(k).getNodeName().equals("id") && map.item(k).getNodeValue().equals(uid)){
						return cubeProfile;
					}
				}
			}
		}
		return null;		
	}
	
	private List<Float> getVector(String param, String uid){
		String vectorType = param.equals("color")? "colorList" : "pointList";
		
		NodeList cubeProfiles = getNodeByID("cubeProfile", uid).getChildNodes();
		
		for(int i=0; i<cubeProfiles.getLength(); i++){
			if(cubeProfiles.item(i).getNodeName().equals(vectorType)){
				NodeList vectors = cubeProfiles.item(i).getChildNodes();
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

	public List<Float> getColorVector(String uid) {
		return this.getVector("color", uid);
	}	
	
	public static void main(String[] args) {

		StringBuffer sb = new StringBuffer();
		sb.append(System.getProperty("user.dir"));
		sb.append(File.separator);
		sb.append("src");
		sb.append(File.separator);
//		sb.append("main");
//		sb.append(File.separator);
//		sb.append("resources");

		System.out.println(sb.toString()+"ShapeShema.xsd");
		
		XMLParser parser = new XMLParser();
		parser.parseFile(sb.toString()+"ShapeSchema.xsd", sb.toString()+"Shape.xml");
		
	//	XMLParser parser = new XMLParser(sb.toString()+"contacts.xsd", sb.toString()+"contacts.xml");
		
		System.out.println(parser.getRoot().getNodeName());
		
		System.out.println(parser.getXVector("uid_1"));
//		System.out.println(parser.getYVector("uid_1"));
//		System.out.println(parser.getZVector("uid_1"));
		
		System.out.println(parser.getColorVector("uid_1"));
		
		System.out.println(parser.getRoot());
	}
}
