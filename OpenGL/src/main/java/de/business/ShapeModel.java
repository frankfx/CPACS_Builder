package de.business;

import java.util.List;

import de.parsing.XMLParser;

public class ShapeModel {
	
	// Data for drawing Axis
	private float mVerticesAxis[] = { -1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f };
	private float mColorAxis[] = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, };	
	
	// Data for triangle x,y,z,w
	private float [] mVertices; // = { 0.8f, 0.2f, .0f, 1.0f, 0.2f, 0.2f, .0f, 1.0f, 0.5f, 0.8f, .0f, 1.0f };
	
	// Data RGBA
	private float mColorArray[] = { 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f };
	
	
	public ShapeModel(String file){
		if(file == null)
			return;
		setVertices(file);		
	}
	
	public float[] getVertices() {
		return mVertices;
	}

	public void setVertices(float[] mVertices) {
		this.mVertices = mVertices;
	}
	
	public void setVertices(String inputFile){
		XMLParser parser = new XMLParser(inputFile);		
		List<Float> a = parser.getXVector("uid_1");
		List<Float> b = parser.getYVector("uid_1");
		List<Float> c = parser.getZVector("uid_1");
		
		mVertices = new float[a.size()*3];
		
		for (int i=0; i<a.size(); i++){
			mVertices[3*i] =  a.get(i); 
			mVertices[3*i+1] =  b.get(i);
			mVertices[3*i+2] =  c.get(i);
		}
	}	
	
	public float[] getVerticesAxis() {
		return mVerticesAxis;
	}

	public void setVerticesAxis(float[] verticesAxis) {
		this.mVerticesAxis = verticesAxis;
	}

	public float[] getColorAxis() {
		return mColorAxis;
	}

	public void setColorAxis(float[] colorAxis) {
		this.mColorAxis = colorAxis;
	}
	
	public float[] getColorArray() {
		return mColorArray;
	}

	public void setColorArray(float[] colorArray) {
		this.mColorArray = colorArray;
	}	
}