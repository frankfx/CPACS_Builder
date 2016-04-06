package de.presentation.bundesliga;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import de.presentation.AbstractPanelContainer;

public class StatisticContainer extends AbstractPanelContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagConstraints c;
	private JTextField mBalance;
	private JProgressBar mProgressbar;
	private JTextField mDate;

	public StatisticContainer() {
		c = new GridBagConstraints(); 
		// create an default panel
		initPanel("Statistic", new GridBagLayout(), Color.WHITE);
	}
	
	@Override
	public void initView() {
		mBalance = new JTextField();
		mBalance.setEditable(false);
		
		mProgressbar = new JProgressBar();
		mProgressbar.setValue(0);
		mProgressbar.setStringPainted(true);
		mProgressbar.setToolTipText("prog");
		mProgressbar.setForeground(Color.orange);		
		
		mDate = new JTextField();
		mDate.setEditable(false);
		mDate.setText(getDaysOfWork()+"");
		
		// set layout
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		this.add(new JLabel("Balance"), c);
		
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx= 0.1;
		this.add(mBalance,c);

		c.gridy = 2;		
		this.add(new JLabel("Days since start"), c);
		
		c.gridy = 3;
		c.fill = GridBagConstraints.BOTH;
		this.add(mDate,c);			
		
		c.gridy = 4;		
		this.add(new JLabel("Progress to 1000 €"), c);
		
		c.gridy = 5;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.1;
		this.add(mProgressbar,c);	
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
	
	public JProgressBar getProgressBar() {
		return mProgressbar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.mProgressbar = progressBar;
	}

	public int getProgressBarValue(){
		return this.mProgressbar.getValue();
	}
	
	public void setProgressBarValue(int val){
		this.mProgressbar.setValue(val);
	}	
	
	private int getDaysOfWork(){
		return LocalDate.of(2016, 4, 6).until(LocalDate.now()).getDays();
	}
}
