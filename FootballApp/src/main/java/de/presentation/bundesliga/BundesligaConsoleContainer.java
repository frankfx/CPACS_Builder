package de.presentation.bundesliga;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JTextArea;

import de.presentation.AbstractPanelContainer;

public class BundesligaConsoleContainer  extends AbstractPanelContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagConstraints c;
	
	private JTextArea mConsole;
	private JButton mBtnCancel;
	
	public BundesligaConsoleContainer() {	
		// create an default panel
		initPanel("Console", new GridBagLayout(), Color.WHITE);
        c = new GridBagConstraints();      
    }

	@Override
	public void initView() {
		mConsole = new JTextArea();
		mBtnCancel = new JButton("Cancel");
		this.add(mConsole);
		this.add(mConsole);
	}	
	
	public JTextArea getmConsole() {
		return mConsole;
	}

	public void setConsole(String lText) {
		this.mConsole.setText(lText);
	}

	public void appendConsole(String lText) {
		this.mConsole.append(lText);
	}	
	
	public JButton getBtnCancel() {
		return mBtnCancel;
	}

	public void setBtnCancel(JButton lBtnCancel) {
		this.mBtnCancel = lBtnCancel;
	}	
	
}
