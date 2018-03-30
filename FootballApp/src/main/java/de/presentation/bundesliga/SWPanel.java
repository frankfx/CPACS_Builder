package de.presentation.bundesliga;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import de.business.AbstractSWTableModel;
import de.business.SoccerwayMatchModel;

public abstract class SWPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTable mSWJTable;
	private JButton mRefreshButton;

	public abstract AbstractSWTableModel getSWDataModel();
	
	/**
	 * No null values allowed
	 * @return
	 */
	public abstract List<JButton> getButtons();
	
	public abstract ActionListener actionRefresh();
	
	public void initView() {
		// set main layout
		this.setLayout(new BorderLayout());
				
		// create refresh button
		mRefreshButton = new JButton("Refresh");
				
		// init button panel 
		JPanel mButtonPanel = new JPanel();
		mButtonPanel.setLayout(new GridBagLayout());
		
		// set up GridBagLayout
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.weightx = 1;
		cons.gridx = 0;
				
		// add all available buttons
		mButtonPanel.add(mRefreshButton, cons);
		for (JButton lButton : this.getButtons()) {
			cons.gridx++;
			mButtonPanel.add(lButton, cons);
		}
		
		// init JTable
		mSWJTable = new JTable( this.getSWDataModel() );
				
		// add table and button panel to main layout
		this.add(new JScrollPane(mSWJTable), BorderLayout.CENTER);
		this.add(mButtonPanel, BorderLayout.SOUTH);
	}
	
	public JTable getSWJTable() {
		return mSWJTable;
	}
	
	public void clearTable(){
		getSWDataModel().clear();
		getSWJTable().revalidate();
	}
	
	/**
	 * Adds a new row to the table fixtures.
	 * 
	 * @param dataVec data for one table row
	 */
	public void addToTable(SoccerwayMatchModel dataVec){
		getSWDataModel().addToTable(dataVec);
	}
	
	/**
	 * Mark select team from tipico view in SWPanel
	 * 
	 * @param pID
	 */
	public void updateTableMarker(String pID){
		AbstractSWTableModel lModel = getSWDataModel();
		ListSelectionModel selectionModel = getSWJTable().getSelectionModel();

		selectionModel.clearSelection();
	
		for (int lRow = 0; lRow < lModel.getRowCount(); lRow++){
			if (pID.equals(lModel.getSoccerwayMatchModel(lRow).getTeamID())){
				selectionModel.addSelectionInterval(lRow, lRow);
			}
		}
	}	
	
	/**
	 * Sorts SWTableModel 
	 */
	public void sortTableByDate(){

		getSWDataModel().getDataList().sort(new Comparator<SoccerwayMatchModel>() {
			@Override
			public int compare(SoccerwayMatchModel o1, SoccerwayMatchModel o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
	}	
	
	public JButton getRefreshButton() {
		return mRefreshButton;
	}
}
