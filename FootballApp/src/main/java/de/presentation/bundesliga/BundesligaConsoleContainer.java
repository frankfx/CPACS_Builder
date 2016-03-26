package de.presentation.bundesliga;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JTextArea;

import de.presentation.AbstractPanelContainer;

public class BundesligaConsoleContainer  extends AbstractPanelContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextArea mConsole;
	private JButton mBtnClear;
	
	public BundesligaConsoleContainer() {	
		// create an default panel
		initPanel("Console", new BorderLayout(), Color.WHITE);
    }

	@Override
	public void initView() {
		mConsole = new JTextArea();
		mBtnClear = new JButton("Clear");
		
//		mConsole.setMaximumSize(new Dimension(30, 100));;
//		mBtnClear.setPreferredSize(new Dimension(20, 20));
		
		
		this.add(mConsole, BorderLayout.CENTER);
		this.add(mBtnClear, BorderLayout.SOUTH);
	}	
	
	public JTextArea getmConsole() {
		return mConsole;
	}

	public void setConsole(String lText) {
		this.mConsole.setText(lText);
	}

	public void appendConsole(String lText) {
		this.mConsole.append(lText+"\n");
	}	
	
	public JButton getBtnClear() {
		return mBtnClear;
	}

	public void setBtnClear(JButton lBtnClear) {
		this.mBtnClear = lBtnClear;
	}	
}
