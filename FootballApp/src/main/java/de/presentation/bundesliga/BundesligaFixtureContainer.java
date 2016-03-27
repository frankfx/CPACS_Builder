package de.presentation.bundesliga;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

import de.business.teams.TeamModel;
import de.presentation.AbstractPanelContainer;

public class BundesligaFixtureContainer extends AbstractPanelContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagConstraints c;
	private int mRow = 0;
	private int mCol = 0;
	private JLabel [] mMatch = new JLabel[27];
	
	public BundesligaFixtureContainer() {	
		// create an default panel
		initPanel("Fixtures", new GridBagLayout(), Color.WHITE);
        c = new GridBagConstraints();   
    }

	@Override
	public void initView() {}	
	
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
		
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 10, 0, 0);
		c.gridx = mCol++;
		c.gridy = mRow;
		this.add(mMatch[3*pGameId], c);		
		
		c.gridx = mCol++;
		c.gridy = mRow;
        this.add(mMatch[3*pGameId+1], c);		
		
		c.gridx = mCol;
		c.gridy = mRow++;
		this.add(mMatch[3*pGameId+2], c);		
		
		mCol = 0;
	}
}
