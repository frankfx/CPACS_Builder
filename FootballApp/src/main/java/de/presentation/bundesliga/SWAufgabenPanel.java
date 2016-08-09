package de.presentation.bundesliga;


import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class SWAufgabenPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable mAufgabenTable;
	
	public SWAufgabenPanel() {
		this.setLayout(new BorderLayout());
		
		Object columnNames[] = {"Date", "Compet.", "Team1", "Team2"};
		TableModel model = new DefaultTableModel(null, columnNames);
		
		mAufgabenTable = new JTable(model);
		
		TableColumnModel columnModel = mAufgabenTable.getColumnModel();
		columnModel.getColumn(1).setMaxWidth(55);
		columnModel.getColumn(1).setResizable(false);
		
		this.add(mAufgabenTable.getTableHeader(), BorderLayout.PAGE_START);
		this.add(new JScrollPane(mAufgabenTable), BorderLayout.CENTER);
	}

	/**
	 * adds a new row to the table Aufgaben.
	 * 
	 * @param dataVec data for one table row
	 */
	public void addToTable(Vector<String> dataVec){
		DefaultTableModel lModel = (DefaultTableModel) mAufgabenTable.getModel();
		lModel.addRow(dataVec);
	}
}
