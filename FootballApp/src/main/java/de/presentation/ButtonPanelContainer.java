package de.presentation;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JTextField;

public class ButtonPanelContainer extends AbstractPanelContainer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton mBtnExit;
	
	public ButtonPanelContainer() {
		initPanel("Button Panel", new GridLayout(1,1), Color.WHITE);
		this.add(mBtnExit);
	}

	public JButton getBtnExit() {
		return mBtnExit;
	}

	public void setBtnExit(JButton pBtnExit) {
		this.mBtnExit = pBtnExit;
	}

	@Override
	public void initView() {
		mBtnExit = new JButton("Exit");
	}
}