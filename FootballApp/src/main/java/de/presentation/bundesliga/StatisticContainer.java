package de.presentation.bundesliga;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import de.presentation.AbstractPanelContainer;

public class StatisticContainer extends AbstractPanelContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagConstraints c;
	private JTextField mBalance;
	

	public StatisticContainer() {
		c = new GridBagConstraints(); 
		// create an default panel
		initPanel("Statistic", new GridBagLayout(), Color.WHITE);
	}
	
	@Override
	public void initView() {
		mBalance = new JTextField();
		mBalance.setEditable(false);
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		this.add(new JLabel("Balance"), c);
		
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx= 0.1;
		this.add(mBalance,c);
	}

	public JTextField getBalance() {
		return mBalance;
	}

	public void setBalance(JTextField pBalance) {
		this.mBalance = pBalance;
	}	
	
	public float getBalanceValue() {
		return Float.parseFloat(mBalance.getText());
	}

	public void setBalanceValue(float lBalance) {
		this.mBalance.setText(lBalance+"");
	}	
}
