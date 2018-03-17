package de.presentation.bundesliga;

import java.awt.BorderLayout;
import java.util.Comparator;
import java.util.List;

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
		
		SWResultTableModel model = new SWResultTableModel();
		
		mResultTable = new JTable(model);
		mAcceptButton = new JButton("Submit");
		
		TableColumnModel columnModel = mResultTable.getColumnModel();
		columnModel.getColumn(1).setMaxWidth(55);
		columnModel.getColumn(1).setResizable(false);
		
		this.add(new JScrollPane(mResultTable), BorderLayout.CENTER);
		this.add(mAcceptButton, BorderLayout.SOUTH);
	}

	public void addToTable(SoccerwayMatchModel dataVec){
		((SWResultTableModel) mResultTable.getModel()).addToTable(dataVec);
	}
	
	public void updateTableMarker(String pID){
		SWResultTableModel lModel = (SWResultTableModel) mResultTable.getModel();
		ListSelectionModel selectionModel = mResultTable.getSelectionModel();

		selectionModel.clearSelection();
	
		for (int row = 0; row < lModel.getRowCount(); row++){
			if (pID.equals(lModel.getSoccerwayMatchModel(row).getTeamID())){
				selectionModel.addSelectionInterval(row, row);
			}
		}
	}	
	
	public void clearTable(){
		((SWResultTableModel) mResultTable.getModel()).clear();
		mResultTable.revalidate();
	}

	public JButton getSubmitButton() {
		return mAcceptButton;
	}	
	
	public List<String> getDataListWithAcceptedValues(){
		return ((SWResultTableModel) mResultTable.getModel()).getDataListWithValueAccepted();
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
}
