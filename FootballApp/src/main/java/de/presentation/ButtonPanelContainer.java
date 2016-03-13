package de.presentation;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;

public class ButtonPanelContainer extends AbstractPanelContainer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton btnExit = new JButton("Exit");
	

	public ButtonPanelContainer() {
		initPanel("Button Panel", new GridLayout(1,1), Color.WHITE);
		this.add(btnExit);
	}

	public JButton getBtnExit() {
		return btnExit;
	}

	public void setBtnExit(JButton btnExit) {
		this.btnExit = btnExit;
	}
}