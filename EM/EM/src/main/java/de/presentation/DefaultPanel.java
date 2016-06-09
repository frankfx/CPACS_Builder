package de.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public abstract class DefaultPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String mTitle;
	public JPanel panelMain;

	public abstract void addToContentPanel(Object arg);

	
	public DefaultPanel(String pTitle){
		mTitle = pTitle;
		panelMain = new JPanel();
		
		// add a suitable layoutmanager
		this.setLayout(new BorderLayout());
        // Create and set up the window.
		this.addComponents();
	}

	private void addComponents() {
		this.add(createPanelHeader(), BorderLayout.NORTH);
		this.add(createPanelMain(), BorderLayout.CENTER);
		this.add(createPanelFooter(), BorderLayout.SOUTH);
	}

	private JPanel createPanelHeader(){
		JPanel panelHeader = new JPanel();
		JLabel lGroupLabel = new JLabel(mTitle);
		lGroupLabel.setForeground(Color.WHITE);
		lGroupLabel.setFont(new Font("Serif", Font.BOLD, 30));
		panelHeader.setLayout(new GridBagLayout());
		panelHeader.setBackground(Color.RED);
		panelHeader.add(lGroupLabel);
		
		return panelHeader;
	}	
	
	private Component createPanelMain() {
		JScrollPane scrollPane = new JScrollPane (panelMain, 
	            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
	            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		panelMain.setBackground(Color.WHITE);
		panelMain.setLayout(new GridBagLayout());
		
		return scrollPane;
	}

	private Component createPanelFooter() {
		JPanel panelFooter = new JPanel();
		panelFooter.setBackground(Color.BLACK);
		return panelFooter;
	}

}
