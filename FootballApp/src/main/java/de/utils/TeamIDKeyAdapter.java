package de.utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class TeamIDKeyAdapter extends KeyAdapter {
	private JTextField field;
	
	public TeamIDKeyAdapter(JTextField field) {
		this.field = field;
	}
		 
	@Override
	public void keyTyped(final KeyEvent e) {
	    TeamIDInputVerifier verifier = (TeamIDInputVerifier) field.getInputVerifier();
		if (!verifier.isValid(field.getText(), e.getKeyChar(), field.getCaretPosition())) {
		    e.consume();
		}
	}
}
