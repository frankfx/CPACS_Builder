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
	private JButton mBtnRequest;
	
	public ButtonPanelContainer() {
		initPanel("Button Panel", new GridLayout(1,2), Color.WHITE);
		this.add(mBtnRequest);
		this.add(mBtnExit);
	}

	public JButton getBtnExit() {
		return mBtnExit;
	}

	public JButton getBtnRequest() {
		return mBtnRequest;
	}	
	
	public void setBtnExit(JButton pBtnExit) {
		this.mBtnExit = pBtnExit;
	}

	public void setBtnRequest(JButton pBtnRequest) {
		this.mBtnRequest = pBtnRequest;
	}	

	
	@Override
	public void initView() {
		mBtnExit = new JButton("Exit");
		mBtnRequest = new JButton("Request");
	}
}