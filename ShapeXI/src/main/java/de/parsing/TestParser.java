package de.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class TestParser {
	static final String HEADER = "header";
	static final String NAME = "name";
	static final String DESCRIPTION = "description";
	static final String CREATOR = "creator";
	static final String VERSION = "version";
	static final String SHAPEVERSION = "shapeVersion";

	public HeaderItem read(String file) throws XMLStreamException, IOException{
		// First, create a new XMLInputFactory
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();

		// Setup a new eventReader
		InputStream in = new FileInputStream(file);
		XMLEventReader eventReader = inputFactory.createXMLEventReader(in);		
	
		HeaderItem itemHeader = new HeaderItem();
		
		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();		
			
			if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals(HEADER)) {
				this.readHeader(eventReader, event, itemHeader);
				//return item;
			}
			
			if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("cube")){
				
			}
		}
		in.close();
		eventReader.close();
		return itemHeader;
	}
	
	private void readHeader(XMLEventReader eventReader, XMLEvent event, HeaderItem item) throws XMLStreamException{
		while (eventReader.hasNext()) {
			event = eventReader.nextEvent();
			
			if (event.isStartElement()){
				StartElement startElem = event.asStartElement();
				
				String tmp = startElem.getName().getLocalPart();
				
				if(tmp.equals(NAME)){
					event = eventReader.nextEvent(); 
					item.setName(event.asCharacters().getData());
				} else if(tmp.equals(DESCRIPTION)){
					event = eventReader.nextEvent(); 
					item.setDescription(event.asCharacters().getData());
				} else if(tmp.equals(CREATOR)){
					event = eventReader.nextEvent(); 
					item.setCreator(event.asCharacters().getData());
				} else if(tmp.equals(VERSION)){
					event = eventReader.nextEvent(); 
					item.setVersion(event.asCharacters().getData());
				} else if(tmp.equals(SHAPEVERSION)){
					event = eventReader.nextEvent(); 
					item.setShapeVersion(event.asCharacters().getData()); break;
				}	
			} else if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(HEADER)){
				break;
			}
		}
	}
	
//		if (event.isStartElement()) {
//			StartElement startElem = event.asStartElement();
//
//			// If we have an item element, we create a new item
//			if (startElem.getName().getLocalPart() == HEADER) {
//				item = new HeaderItem();
//
//				// We read the attributes from this tag and add the date
//				// attribute to our object
//				Iterator<Attribute> attributes = startElem.getAttributes();
//
//				while (attributes.hasNext()) {
//					Attribute attribute = attributes.next();
//					if (attribute.getName().equals(NAME)) {
//						item.setName(attribute.getValue());
//					}
//				}
//			}
//		}
//	}


	public static void main(String[] args) throws XMLStreamException, IOException {
		TestParser test = new TestParser();

		// C:\Users\U018445\Documents\workspace\XMLParsen == user.dir
		
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
		
		System.out.println(test.read(sb.toString()));
	}
}
