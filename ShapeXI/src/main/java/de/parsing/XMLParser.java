package de.parsing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;

import javax.xml.parsers.*;

public class XMLParser {

	private DocumentBuilder dBuilder;
	private DocumentBuilderFactory dbFactory;
	private Document doc;

	public XMLParser(String inputFile){
		init(inputFile);
	}
	
	public boolean init(String inputFile) {
		dbFactory = DocumentBuilderFactory.newInstance();

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
			NodeList cubeProfiles = nodes.item(i).getChildNodes();
			for (int j=0; j<cubeProfiles.getLength(); j++){
				if (cubeProfiles.item(j).hasAttributes()){
					NamedNodeMap map = cubeProfiles.item(j).getAttributes();
					for(int k=0; k<map.getLength(); k++){
						if(map.item(k).getNodeValue().equals(uid)){
							return cubeProfiles.item(j);
						}
					}
				}
			}
		}
		return cubeProfile;		
	}
	
	private List<Float> getVector(String param, String uid){
		NodeList nodes = getNodeByID("cubeProfiles", uid).getChildNodes();
		
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
		sb.append(File.separator);
		sb.append("myData.xml");

		XMLParser parser = new XMLParser(sb.toString());
		
		System.out.println(parser.getRoot());
		
		System.out.println(parser.getXVector("uid_1"));
		System.out.println(parser.getYVector("uid_1"));
		System.out.println(parser.getZVector("uid_1"));
		
		System.out.println(parser.getRoot());
	}
}
