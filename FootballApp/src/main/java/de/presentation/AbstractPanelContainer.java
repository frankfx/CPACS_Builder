package de.presentation;

import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public abstract class AbstractPanelContainer extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void initPanel(String title, LayoutManager layout, Color backgroundColor){
		this.setBorder(BorderFactory.createTitledBorder(title));
		this.setLayout(layout);
		this.setBackground(backgroundColor);
		
		initView();
	}
	
	public abstract void initView();
}
