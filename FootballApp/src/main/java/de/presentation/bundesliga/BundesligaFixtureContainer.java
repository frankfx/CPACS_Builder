package de.presentation.bundesliga;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.business.teams.TeamModel;
import de.presentation.AbstractPanelContainer;

public class BundesligaFixtureContainer extends AbstractPanelContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagConstraints c;
	private JLabel [] mMatch = new JLabel[27];
	
	private JPanel mMatchPanel;
	private JButton mBtnRequest;
	private JComboBox<String> mComboLeague;
	private JComboBox<String> mComboSeason;
	private JComboBox<Integer> mComboMatchday;	
	
	
	public BundesligaFixtureContainer() {	
		c = new GridBagConstraints(); 
		// create an default panel
		initPanel("Fixtures", new GridBagLayout(), Color.WHITE);
    }

	@Override
	public void initView() {
		mMatchPanel = new JPanel();
		mMatchPanel.setBackground(Color.WHITE);
		mMatchPanel.setLayout(new GridLayout(9, 3));
		
		mBtnRequest = new JButton("Request");
		
		mComboLeague = new JComboBox<String>();
		mComboSeason = new JComboBox<String>();
		mComboMatchday = new JComboBox<Integer>();
		
		mComboLeague.addItem("bl1");
		mComboSeason.addItem("2015");
		mComboSeason.addItem("2016");
		for(int i = 1; i < 35; i++)
			mComboMatchday.addItem(i);
		
		mComboMatchday.setSelectedIndex(18);
		
		// ===================================
		// layout
		// ===================================
        c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.1;
		c.weighty = 0.1;
		this.add(mMatchPanel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.0;
		this.add(mComboMatchday, c);
		
		c.gridx = 1;
		this.add(mComboLeague, c);

		c.gridx = 2;
		this.add(mComboSeason, c);	
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
        this.add(mBtnRequest, c);		
	}	
	
	/**  
	 * Creates an Fixture, or null if the path was invalid.
	 * 
	 * match-Array definition : [team1_x, team2_x, result_x] where x in {0,1,2,3,4,5,6,7,8}
	 * match-Array example : [team1_0, team2_0, result_0, ... , team1_8, team2_8, result_8]
	 */
	public void createFixture (TeamModel pTeam1, TeamModel pTeam2, int pGoal1, int pGoal2, int pGameId) {
		if(pTeam1 == null || pTeam2 == null){
			pTeam1 = pTeam1 != null ? pTeam1 : new TeamModel("Null");
			pTeam2 = pTeam2 != null ? pTeam2 : new TeamModel("Null");
		}
		
		if(mMatch[3*pGameId] == null)
			mMatch[3*pGameId] = new JLabel();
		
		mMatch[3*pGameId].setText(pTeam1.getName());
		mMatch[3*pGameId].setIcon(pTeam1.getIcon());
		mMatch[3*pGameId].setHorizontalAlignment(JLabel.LEFT);
		
		if(mMatch[3*pGameId+1] == null)
			mMatch[3*pGameId+1] = new JLabel();
		
		mMatch[3*pGameId+1].setText(pTeam2.getName());
		mMatch[3*pGameId+1].setIcon(pTeam2.getIcon());
		mMatch[3*pGameId+1].setHorizontalAlignment(JLabel.LEFT);		

		if(mMatch[3*pGameId+2] == null)
			mMatch[3*pGameId+2] = new JLabel();		
		
		mMatch[3*pGameId+2].setText(pGoal1+":"+pGoal2);
		
		this.mMatchPanel.add(mMatch[3*pGameId]);		
        this.mMatchPanel.add(mMatch[3*pGameId+1]);		
		this.mMatchPanel.add(mMatch[3*pGameId+2]);		
	}
	
	public JButton getBtnRequest() {
		return mBtnRequest;
	}

	public void setBtnRequest(JButton pBtnRequest) {
		this.mBtnRequest = pBtnRequest;
	}

	public JComboBox<String> getComboLeague() {
		return mComboLeague;
	}

	public void setComboLeague(JComboBox<String> pComboLeague) {
		this.mComboLeague = pComboLeague;
	}

	public JComboBox<String> getComboSeason() {
		return mComboSeason;
	}

	public void setComboSeason(JComboBox<String> pComboSeason) {
		this.mComboSeason = pComboSeason;
	}

	public JComboBox<Integer> getComboMatchday() {
		return mComboMatchday;
	}

	public void setComboMatchday(JComboBox<Integer> pComboMatchday) {
		this.mComboMatchday = pComboMatchday;
	}	
}
