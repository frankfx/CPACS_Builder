package de.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.business.teams.TeamModel;


public class TablePanel extends DefaultPanel {

	private GridBagConstraints gbConstraints;
	private static final long serialVersionUID = 1L;
	
	public TablePanel(String pTitle) {
		super(pTitle);
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridy = 0;
		gbConstraints.fill = GridBagConstraints.HORIZONTAL;
		gbConstraints.weightx = 0.1;		
	}

	@Override
	public void addToContentPanel(Object arg) {
		JPanel content = getContent(arg);
		gbConstraints.gridy = gbConstraints.gridy + 1;
		panelMain.add(content, gbConstraints);
		this.revalidate();
	}	
	
	private JPanel getContent(Object arg) {
		if (arg instanceof TeamModel){
			TeamModel lTeam = (TeamModel) arg;
			
			JPanel panelResult = new JPanel();
			panelResult.setBackground(Color.WHITE);
			panelResult.setLayout(new BorderLayout());
			
			panelResult.add(new JLabel(lTeam.getPosition().getPlace() + ""), BorderLayout.WEST);
			// Team 1
			JLabel lTeam1 = new JLabel(lTeam.getName());
			lTeam1.setIcon(lTeam.getIcon());
			panelResult.add(lTeam1, BorderLayout.CENTER);
			panelResult.add(new JLabel(lTeam.getPosition().getPoints() + " Pkt"), BorderLayout.EAST);
			
			return panelResult;
		} 
		return new JPanel();		
	}	

	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new MatchDayPanel("Tabelle Gruppe A");
            }
        });
	}

}
