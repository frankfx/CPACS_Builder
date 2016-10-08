package de.presentation.bundesliga;

import java.awt.BorderLayout;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import de.business.SWResultTableModel;
import de.business.SoccerwayMatchModel;

public class SWResultPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTable mResultTable;
	private JButton mAcceptButton;
	
	public SWResultPanel() {
		this.setLayout(new BorderLayout());
		
		mResultTable = new JTable(new SWResultTableModel());
		mAcceptButton = new JButton("Submit");
		
		TableColumnModel columnModel = mResultTable.getColumnModel();
		columnModel.getColumn(1).setMaxWidth(55);
		columnModel.getColumn(1).setResizable(false);
		
		this.add(mResultTable.getTableHeader(), BorderLayout.NORTH);
		this.add(new JScrollPane(mResultTable), BorderLayout.CENTER);
		this.add(mAcceptButton, BorderLayout.SOUTH);
	}


	
	public void updateTableMarker(String pID){
		SWResultTableModel lModel = (SWResultTableModel) mResultTable.getModel();
		ListSelectionModel selectionModel = mResultTable.getSelectionModel();
		String id;

		selectionModel.clearSelection();
	
		for (int row = 0; row < lModel.getRowCount(); row++){
			id = ((SoccerwayMatchModel)lModel.getDataList().get(row).g.getTeamID().toString();
			if (pID.equals(id)){
				selectionModel.addSelectionInterval(row, row);
			}
		}
	}	
	
	public void clearTable(){
		((SWResultTableModel) mResultTable.getModel()).clear();
		mResultTable.revalidate();
	}
	
	public void sortTableByDate(){
		SWResultTableModel lModel = (SWResultTableModel) mResultTable.getModel();
		lModel.getDataList().sort(new Comparator<SoccerwayMatchModel>() {
			@Override
			public int compare(SoccerwayMatchModel o1, SoccerwayMatchModel o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
	}
	
	public SWResultTableModel getSWResultTableModel(){
		return (SWResultTableModel)mResultTable.getModel();
	}

}
