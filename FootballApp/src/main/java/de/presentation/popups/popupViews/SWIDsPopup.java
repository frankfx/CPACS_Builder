package de.presentation.popups.popupViews;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import de.presentation.popups.IPopup;

public class SWIDsPopup implements IPopup {

	private final JTable mSWIDsTable;
	private final JPanel mMainPanel;
	private static final int COLUMN_KEY = 0;
	private static final int COLUMN_VALUE = 1;
	
	public SWIDsPopup(Object[] pParams) {
		
		mMainPanel = new JPanel();
		JPanel lTablePanel = new JPanel();
		
		// init table elements
		mSWIDsTable = new JTable(initTableHeader());

		lTablePanel.setLayout(new BorderLayout());
		lTablePanel.add(mSWIDsTable.getTableHeader(), BorderLayout.PAGE_START);
		lTablePanel.add(new JScrollPane(mSWIDsTable), BorderLayout.CENTER);
		
		DefaultTableModel lModel = (DefaultTableModel) mSWIDsTable.getModel();
		
		for (int i = 0; i < pParams.length-1; i+=2) {
			lModel.addRow(new Object[] {pParams[i], pParams[i+1]});
		}
		
		TableRowSorter<TableModel> lSorter = new TableRowSorter<TableModel>(lModel);
		lSorter.setSortable(COLUMN_KEY, false);
		mSWIDsTable.setRowSorter(lSorter);
		

		// init search elements
		JTextField mSearchField = new JTextField();
		mSearchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {   
				lSorter.setRowFilter(RowFilter.regexFilter(mSearchField.getText(), COLUMN_KEY, COLUMN_VALUE));
			}			
		});		
		
		// put table and search elements together
		mMainPanel.setLayout(new BorderLayout());
		mMainPanel.add(mSearchField, BorderLayout.PAGE_START);
		mMainPanel.add(lTablePanel, BorderLayout.CENTER);
	}
	
	private TableModel initTableHeader() {
		Object columnNames[] = {"ID", "TEAM"};
		TableModel model = new DefaultTableModel(null, columnNames){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == COLUMN_KEY )
					return false;
				return true;
			}
		};
		return model;
	}

	@Override
	public String[] requestInputData() {
		Object[] mInput = {mMainPanel};

		JOptionPane pane = new JOptionPane(mInput,
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION);
		
		JDialog lDialog = pane.createDialog(null, "SWIDs");
		lDialog.setVisible(true);

		Object selectedValue = pane.getValue();
		int n = -1;

		if (selectedValue == null)
			n = JOptionPane.CLOSED_OPTION;
		else
			n = Integer.parseInt(selectedValue.toString());

		lDialog.dispose();

		if (n == JOptionPane.OK_OPTION) {
			int lSelectedRow = mSWIDsTable.convertRowIndexToModel(mSWIDsTable.getSelectedRow());
		
			if ( lSelectedRow >= 0 )
				return new String[]{mSWIDsTable.getModel().getValueAt(lSelectedRow, COLUMN_KEY).toString()};
		}
		return null;
	}

	@Override
	public List<?> requestInputDataAsObjectList() {
		return null;
	}

}
