package de.application;

import de.business.ShapeModel;
import de.presentation.OGLView;

public class Controller {
	ShapeModel model;
	OGLView view;
	
	public Controller(ShapeModel model, OGLView view) {
		this.model = model;
		this.view = view;
	}

	public void runApp() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				view.initView();
			}
		});
	}
	
}
