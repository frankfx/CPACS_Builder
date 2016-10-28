package de.presentation.bundesliga;


import java.awt.BorderLayout;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import de.business.SoccerwayMatchModel;
import de.utils.Utils;

public class SWAufgabenPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable mFixtureTable;
	
	public SWAufgabenPanel() {
		this.setLayout(new BorderLayout());
		
		Object columnNames[] = {"Date", "Time", "Compet.", "Team1", "Team2"};
		TableModel model = new DefaultTableModel(null, columnNames);
		
		mFixtureTable = new JTable(model){
			private static final long serialVersionUID = 1L;

            /*@Override
            public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();
            }*/
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return LocalDate.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return String.class;
                    case 4:
                        return String.class;    
                    default:
                        return String.class;
                }
            }
            
        	@Override
        	public Object getValueAt(int rowIndex, int columnIndex) {
        		SoccerwayMatchModel model = (SoccerwayMatchModel) mFixtureTable.getModel().getValueAt(rowIndex, 0);
				switch (columnIndex) {
				case 0:
					return model.getDate();
				case 1:
					return model.getDay();
				case 2 :
					return model.getCompetition();
				case 3 :
					return model.getTeam1();
				case 4 :
					return model.getTeam2();							
				default:
					return null;
				}
        	}
        };
		
		TableColumnModel columnModel = mFixtureTable.getColumnModel();
		columnModel.getColumn(1).setMaxWidth(55);
		columnModel.getColumn(1).setResizable(false);
		
		this.add(mFixtureTable.getTableHeader(), BorderLayout.PAGE_START);
		this.add(new JScrollPane(mFixtureTable), BorderLayout.CENTER);
	}

	/**
	 * adds a new row to the table Aufgaben.
	 * 
	 * @param dataVec data for one table row
	 */
	public void addToTable(Vector<SoccerwayMatchModel> dataVec){
		DefaultTableModel lModel = (DefaultTableModel) mFixtureTable.getModel();
		lModel.addRow(dataVec);
	}
	
	public void updateTableMarker(String pID){
		DefaultTableModel lModel = (DefaultTableModel) mFixtureTable.getModel();
		ListSelectionModel selectionModel = mFixtureTable.getSelectionModel();
		
		String lFixtureIds, lSelectedId;
		lSelectedId = Utils.getIDWithoutSuffix(pID);
		
		selectionModel.clearSelection();
	
		for (int row = 0; row < lModel.getRowCount(); row++){
			lFixtureIds = ((SoccerwayMatchModel)lModel.getValueAt(row, 0)).getTeamID().toString();
			
			if (lSelectedId.equals(lFixtureIds)){
				selectionModel.addSelectionInterval(row, row);
			}
		}
	}	
	
	public void clearTable(){
		DefaultTableModel lModel = (DefaultTableModel) mFixtureTable.getModel();
		lModel.setRowCount(0);
		mFixtureTable.revalidate();
	}
	
	@SuppressWarnings("unchecked")
	public void sortTableByDate(){
		DefaultTableModel lModel = (DefaultTableModel) mFixtureTable.getModel();
		lModel.getDataVector().sort(new Comparator<Vector<SoccerwayMatchModel>>() {
			@Override
			public int compare(Vector<SoccerwayMatchModel> o1, Vector<SoccerwayMatchModel> o2) {
				return o1.get(0).getDate().compareTo(o2.get(0).getDate());
			}
		});
	}
}
