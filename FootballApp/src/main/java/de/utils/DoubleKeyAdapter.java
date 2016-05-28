package de.utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class DoubleKeyAdapter extends KeyAdapter {
	private JTextField field;
	
	public DoubleKeyAdapter(JTextField field) {
		this.field = field;
	}
		 
	@Override
	public void keyTyped(final KeyEvent e) {
	    DoubleInputVerifier verifier = (DoubleInputVerifier) field.getInputVerifier();
		if (!verifier.isValid(field.getText(), e.getKeyChar(), field.getCaretPosition())) {
		    e.consume();
		}
	}
}
