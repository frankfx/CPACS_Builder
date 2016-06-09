package de.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JPanel;

import de.business.MatchModel;

public class MainFrame extends JDialog{

	// Static fields.
	private static final long serialVersionUID = 1L;
	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 450;
	
	// Member fields.
	private MatchDayPanel mMatchDayPanel;
	private JPanel mTeamPanel;
	private TablePanel mTablePanel;
	
	public MainFrame(String pMainFrameTitle){
        this.setTitle(pMainFrameTitle);
		this.getContentPane().setLayout(new BorderLayout());

		// Create the panel and add it to the frame.
		mMatchDayPanel = new MatchDayPanel("tse");
		this.getContentPane().add(mMatchDayPanel);
		
		mTablePanel = new TablePanel("ffff");
		this.getContentPane().add(mMatchDayPanel);
		
		// Set the frame properties and show it.
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - FRAME_WIDTH) / 2, (screenSize.height - FRAME_HEIGHT) / 2);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setResizable(false);
		this.setVisible(true);			
	}
	
	public void initPanelBundesliga(MatchModel pMatch){
		//panel.panelBundesliga.addToContentPanel(pMatch);
	}
}
