package de.presentation.bundesliga;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class SWResultPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTable mResultTable;
	private JButton mAcceptButton;
	
	public SWResultPanel() {
		this.setLayout(new BorderLayout());
		
		Object columnNames[] = {"Date", "Compet.", "Team1", "Team2", "Result", "Accept"};
		TableModel model = new DefaultTableModel(null, columnNames);
		
		mResultTable = new JTable(model){
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
                    case 5:
                        return Boolean.class;     
                    default:
                        return null;
                }
            }
        };
		mAcceptButton = new JButton("Submit");
		
		TableColumnModel columnModel = mResultTable.getColumnModel();
		columnModel.getColumn(1).setMaxWidth(55);
		columnModel.getColumn(1).setResizable(false);
		
		this.add(mResultTable.getTableHeader(), BorderLayout.NORTH);
		this.add(new JScrollPane(mResultTable), BorderLayout.CENTER);
		this.add(mAcceptButton, BorderLayout.SOUTH);
	}

	/**
	 * adds a new row to the table Aufgaben.
	 * 
	 * @param dataVec data for one table row
	 */
	public void addToTable(Vector<Object> dataVec){
		DefaultTableModel lModel = (DefaultTableModel) mResultTable.getModel();
		lModel.addRow(dataVec);
	}
	
	public void clearTable(){
		DefaultTableModel lModel = (DefaultTableModel) mResultTable.getModel();
		lModel.setRowCount(0);
		mResultTable.revalidate();
	}
	
	@SuppressWarnings("unchecked")
	public void sortTableByDate(){
		DefaultTableModel lModel = (DefaultTableModel) mResultTable.getModel();
		lModel.getDataVector().sort(new Comparator<Vector<LocalDate>>() {
			@Override
			public int compare(Vector<LocalDate> o1, Vector<LocalDate> o2) {
				return o1.get(0).compareTo(o2.get(0));
			}
		});
	}
	

}
