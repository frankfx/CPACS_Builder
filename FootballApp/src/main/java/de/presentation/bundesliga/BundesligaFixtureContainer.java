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
	private int row = 0;
	private int col = 0;
	
	public BundesligaFixtureContainer() {	
		// create an default panel
		initPanel("Fixtures", new GridBagLayout(), Color.WHITE);
        c = new GridBagConstraints();        
    }

	/** Creates an Fixture, or null if the path was invalid. */
	public void createFixture (TeamModel pTeam1, TeamModel pTeam2, String pGoal1, String pGoal2) {	
		JLabel lTeam1 = new JLabel(pTeam1.getName(), pTeam1.getIcon(), JLabel.LEFT);
        JLabel lTeam2 = new JLabel(pTeam2.getName(), pTeam2.getIcon(), JLabel.LEFT);
        JLabel result = new JLabel(pGoal1+":"+pGoal2);
        
        c.fill = GridBagConstraints.BOTH;
       // c.weightx = 0.1;
       // c.weighty = 0.1;
        c.insets = new Insets(10, 10, 10, 10);
		c.gridx = col++;
		c.gridy = row;
		this.add(lTeam1, c);		
		
		c.gridx = col++;
		c.gridy = row;
        this.add(lTeam2, c);		
		
		c.gridx = col;
		c.gridy = row++;
		this.add(result, c);		
		
		col = 0;
	}
}
