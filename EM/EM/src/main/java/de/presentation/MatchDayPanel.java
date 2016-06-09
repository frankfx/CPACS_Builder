package de.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import de.business.MatchModel;


public class MatchDayPanel extends DefaultPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagConstraints gbConstraints;
	
	
	public MatchDayPanel(String pTitle) {
		super(pTitle);
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridy = 0;
		gbConstraints.fill = GridBagConstraints.HORIZONTAL;
		gbConstraints.weightx = 0.1;		
	}

	@Override
	public void addToContentPanel(Object arg){
		JPanel content = getContent(arg);
		gbConstraints.gridy = gbConstraints.gridy + 1;
		panelMain.add(content, gbConstraints);
		this.revalidate();
	}
	
	private JPanel getContent(Object arg) {
		if (arg instanceof MatchModel){
			MatchModel lMatch = (MatchModel) arg;
			
			JPanel panelResult = new JPanel();
			panelResult.setBackground(Color.WHITE);
			panelResult.setLayout(new BorderLayout());
			panelResult.setBorder(BorderFactory.createTitledBorder("title"));
			
			// Team 1
			JLabel lTeam1 = new JLabel(lMatch.getTeam1().getName());
			lTeam1.setIcon(lMatch.getTeam1().getIcon());
			panelResult.add(lTeam1, BorderLayout.WEST);

			// Result
			JPanel res = new JPanel();
			res.setBackground(Color.WHITE);
			res.setLayout(new GridBagLayout());
			
			JTextField score1 = new JTextField();
			score1.setText(""+lMatch.getScore1());
			res.add(score1);
			
			res.add(new JLabel(" : "));

			JTextField score2 = new JTextField();
			score2.setText(""+lMatch.getScore2());
			res.add(score2);			
			
			panelResult.add(res, BorderLayout.SOUTH);
			
			// Team 2
			JLabel lTeam2 = new JLabel(lMatch.getTeam2().getName());
			lTeam2.setIcon(lMatch.getTeam2().getIcon());
			lTeam2.setHorizontalTextPosition(SwingConstants.LEFT);
			panelResult.add(lTeam2, BorderLayout.EAST);
			return panelResult;
		} 
		return new JPanel();
	}	
}
