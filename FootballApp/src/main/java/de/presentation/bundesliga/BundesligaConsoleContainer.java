package de.presentation.bundesliga;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.presentation.AbstractPanelContainer;

public class BundesligaConsoleContainer extends AbstractPanelContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextArea mConsole;
	private JButton mBtnClear;

	private GridBagConstraints c;

	public BundesligaConsoleContainer() {
		c = new GridBagConstraints();
		// create an default panel
		initPanel("Console", new GridBagLayout(), Color.WHITE);
	}

	@Override
	public void initView() {
		mConsole = new JTextArea();
		mBtnClear = new JButton("Clear");

		JScrollPane scroll = new JScrollPane(mConsole);
		scroll.setBounds(10, 11, 455, 249); // <-- THIS		

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.1;
		c.weighty = 0.1;
		this.add(scroll, c);

		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.0;
		this.add(mBtnClear, c);
		
		this.setPreferredSize(new Dimension(450, 250));
		this.setMinimumSize(new Dimension(300, 100));
	}

	public JTextArea getConsole() {
		return mConsole;
	}

	public void setConsole(String lText) {
		this.mConsole.setText(lText);
	}

	public void clearConsole() {
		this.setConsole("");
	}

	public void appendConsole(String lText) {
		this.mConsole.append(lText + "\n");
	}

	public JButton getBtnClear() {
		return mBtnClear;
	}

	public void setBtnClear(JButton lBtnClear) {
		this.mBtnClear = lBtnClear;
	}
}
