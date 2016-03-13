package de.parsing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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

	final static Logger logger = Logger.getLogger(XMLParser.class);
	final static String SHAPEXI_HEADER_TAG = "header";
	final static String SHAPEXI_HEADER_NAME_TAG = "name";
	final static String SHAPEXI_HEADER_DESCRIPTION_TAG = "description";
	final static String SHAPEXI_HEADER_CREATOR_TAG = "creator";
	final static String SHAPEXI_HEADER_TIMESTAMP_TAG = "timestamp";
	final static String SHAPEXI_HEADER_VERSION_TAG = "version";	
	final static String SHAPEXI_HEADER_SHAPEVERSION_TAG = "shapeVersion";
	final static String SHAPEXI_COLORVECTOR_TAG = "color";
	final static String SHAPEXI_XVECTOR_TAG = "x";
	final static String SHAPEXI_YVECTOR_TAG = "y";
	final static String SHAPEXI_ZVECTOR_TAG = "z";
	final static String SHAPEXI_UID_ATTR = "id";	
	
	
	public XMLParser(){
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			System.out.println(ex.getMessage());
		}
		logger.info("init parser complete");
	}

	private boolean parse(String pInputFile){
		if (pInputFile == null)
			return false;
		else {
			try {
				doc = dBuilder.parse(pInputFile);
			} catch (SAXException ex) {
				logger.error("SAXException: " + ex.getMessage());
				return false;
			} catch (IOException ex) {
				logger.error("IOException: " + ex.getMessage());
				return false;
			}
			logger.info("parsing " + pInputFile + " complete");
			return true;	
		}
	}
	
	public boolean parseFile(String pSchemaFile, String pInputFile){
		if(!parse(pInputFile) || !this.validate(pSchemaFile, doc))
			return false;
		doc.getDocumentElement().normalize();
		return true;
	}

	public boolean parseFile(String pInputFile){
		logger.warn("Warning: parsing with no schema");
		return parse(pInputFile);
	}
	
	private boolean validate(String schemaFile, Document document) {
		if (schemaFile == null || document == null)
			return false;
		else{
			// create a SchemaFactory capable of understanding WXS schemas
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				   
			// load a WXS schema, represented by a Schema instance
			Source source = new StreamSource(new File(schemaFile));
			
			try {
				Schema schema = factory.newSchema(source);
				// create a Validator instance, which can be used to validate an instance document
			    schema.newValidator().validate(new DOMSource(document));
			} catch (Exception e1) {
				logger.error(e1.getMessage());
				return false;
			}		
			return true;
		}
	}
	
	public Node getRoot(){
		if (doc == null)
			return null;
		return doc.getDocumentElement();
	}	
	
	private Node getNodeByID(String pNode, String pUid){
		if (pNode == null || pUid == null)
			return null;
		
		NodeList lNodes = doc.getElementsByTagName(pNode);
		
		if (lNodes != null){
			Node lResult = null;
	
			for(int i=0; i<lNodes.getLength(); i++){
				lResult = lNodes.item(i);
				if (lResult.hasAttributes()){
					NamedNodeMap map = lResult.getAttributes();
					for(int k=0; k<map.getLength(); k++){
						if(map.item(k).getNodeName().equals(SHAPEXI_UID_ATTR) && map.item(k).getNodeValue().equals(pUid)){
							return lResult;
						}
					}
				}
			}
		}
		return null;
	}
	
	private List<Float> getVector(String pParam, String pUid){
		if (pParam == null || pUid == null)
			return null;
		
		String vectorType = pParam.equals("color")? "colorList" : "pointList";
		Node lCubeProfile = getNodeByID("cubeProfile", pUid);
		
		if (lCubeProfile != null){		
			NodeList lProfileData = lCubeProfile.getChildNodes();
			
			for(int i=0; i<lProfileData.getLength(); i++){
				if(lProfileData.item(i).getNodeName().equals(vectorType)){
					NodeList vectors = lProfileData.item(i).getChildNodes();
					for(int j=0; j<vectors.getLength(); j++){
						if(vectors.item(j).getNodeName().equals(pParam)){
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
		}
		return null;
	}
	
	public List<Float> getXVector(String uid) {
		return this.getVector(SHAPEXI_XVECTOR_TAG, uid);
	}
	
	public List<Float> getYVector(String uid) {
		return this.getVector(SHAPEXI_YVECTOR_TAG, uid);
	}
	
	public List<Float> getZVector(String uid) {
		return this.getVector(SHAPEXI_ZVECTOR_TAG, uid);
	}	

	public List<Float> getColorVector(String uid) {
		return this.getVector(SHAPEXI_COLORVECTOR_TAG, uid);
	}	

	public HeaderItem getHeader(){
		NodeList lHeader = doc.getElementsByTagName(SHAPEXI_HEADER_TAG);
		HeaderItem lHeaderItem = null;
		if(lHeader != null){
			lHeaderItem = new HeaderItem();
			NodeList lHeaderData = lHeader.item(0).getChildNodes();
			
			for (int i=0; i<lHeaderData.getLength(); i++){
				Node lCurNode = lHeaderData.item(i);
				if(lCurNode.getNodeName().equals(SHAPEXI_HEADER_NAME_TAG))
					lHeaderItem.setName(lCurNode.getTextContent());
				else if(lCurNode.getNodeName().equals(SHAPEXI_HEADER_DESCRIPTION_TAG))
					lHeaderItem.setDescription(lCurNode.getTextContent());
				else if(lCurNode.getNodeName().equals(SHAPEXI_HEADER_TIMESTAMP_TAG))
					lHeaderItem.setTimestamp(lCurNode.getTextContent());
				else if(lCurNode.getNodeName().equals(SHAPEXI_HEADER_CREATOR_TAG))
					lHeaderItem.setCreator(lCurNode.getTextContent());
				else if(lCurNode.getNodeName().equals(SHAPEXI_HEADER_VERSION_TAG))
					lHeaderItem.setVersion(lCurNode.getTextContent());
				else if(lCurNode.getNodeName().equals(SHAPEXI_HEADER_SHAPEVERSION_TAG))
					lHeaderItem.setShapeVersion(lCurNode.getTextContent());				
			}
		}
		return lHeaderItem;
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
		
		XMLParser parser = new XMLParser();
		boolean success = parser.parseFile(sb.toString()+"ShapeSchema.xsd", sb.toString()+"Shape.xml");
		
		System.out.println("successful parsing: " + success);
		
		System.out.println(parser.getRoot().getNodeName());
		System.out.println(parser.getXVector("uid_1"));
		System.out.println(parser.getYVector("uid_1"));
		System.out.println(parser.getZVector("uid_1"));
		System.out.println(parser.getColorVector("uid_1"));
		System.out.println(parser.getRoot());
		
		System.out.println(parser.getHeader());
	}
}
