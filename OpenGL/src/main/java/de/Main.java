package de;

import java.io.File;

import de.application.Controller;
import de.business.ShapeModel;
import de.presentation.OGLView;

public class Main {
	public static void main(String[] args) {
		//"/home/rene/Documents/git/OpenGL/src/main/resources/myData.xml"
		StringBuffer sb = new StringBuffer();
		sb.append(System.getProperty("user.dir"));
		sb.append(File.separator);
		sb.append("src");
		sb.append(File.separator);
		sb.append("main");
		sb.append(File.separator);
		sb.append("resources");
		sb.append(File.separator);		
		
		// allocate the openGL application
		ShapeModel model = new ShapeModel(sb.toString()+"ShapeSchema.xsd", sb.toString()+"Shape.xml");
		model.parseVertices("uid_1");
		model.parseColorArray("uid_1");
		
		OGLView view = new OGLView(model.getVerticesAxis(), model.getColorAxis(), model.getVertices(), model.getColorArray());
		
		Controller controller = new Controller(model, view);
		controller.runApp();
	}
}
