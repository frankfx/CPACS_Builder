package de.presentation.bundesliga;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class StatusPanel extends JPanel implements Runnable{

	private static final long serialVersionUID = 1L;
	private JLabel mStatusMessage;
	private Thread thread = null;
	private int mDelayTime = -1;
	
	public StatusPanel(String pText, int width) {
		thread = new Thread(this);

		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.setPreferredSize(new Dimension(width, 16));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));		
		
		mStatusMessage = new JLabel(pText);
		mStatusMessage.setHorizontalAlignment(SwingConstants.LEFT);
		mStatusMessage.setForeground(Color.RED);
		this.add(mStatusMessage);
	}
	
	public StatusPanel(String pText){
		this(pText, 100);
	}
	
	public StatusPanel() {
		this(null, 100);
	}
	
	public void setStatusMessageColor(Color pColor){
		mStatusMessage.setForeground(pColor);
	}
	
	public void showStatusMessage(String pMessage){
		mStatusMessage.setText(pMessage);
	}
	
	public void showDelayedStatusMessage (String pMessage, int pDelayTimeInSec){
		mDelayTime = pDelayTimeInSec;
		mStatusMessage.setText(pMessage);
		if (thread.getState() != Thread.State.NEW){
			thread = new Thread(this);
		} 			
		thread.start();	
	}

	public void clearStatusMessage(){
		mStatusMessage.setText(null);
	}	
	
	@Override
	public void run() {
		try {
			Thread.sleep(mDelayTime);
			this.clearStatusMessage();
		} catch (InterruptedException ex) {}
	}
}