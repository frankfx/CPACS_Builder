package de;

import java.util.List;

import javax.swing.JFrame;

import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyInstance;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import de.application.BundesligaActivityBean;
import de.business.BundesligaModel;
import de.presentation.bundesliga.BundesligaView;
import de.utils.RessourceService;

public class Main {
	public static void main(String[] args) {

        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
		BundesligaModel model = new BundesligaModel();
		BundesligaView view = new BundesligaView();
		
		System.out.println("rest");
		
		BundesligaActivityBean controller = new BundesligaActivityBean(model, view);
		controller.runApp();
	}
}
