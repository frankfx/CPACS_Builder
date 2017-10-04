package de.presentation.popups.popupViews;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import de.presentation.popups.IPopup;

public class SWIDsPopup implements IPopup {

	private final JTable lSWIDsTable;
	private final JPanel lTablePanel;
	private static final int COLUMN_KEY = 0;
	private static final int COLUMN_VALUE = 1;
	
	public SWIDsPopup(Object[] pParams) {
		
		lTablePanel = new JPanel();
		lTablePanel.setLayout(new BorderLayout());
	
		lSWIDsTable = new JTable(initTableHeader());
	
		lTablePanel.add(lSWIDsTable.getTableHeader(), BorderLayout.PAGE_START);
		lTablePanel.add(new JScrollPane(lSWIDsTable), BorderLayout.CENTER);
		
		DefaultTableModel lModel = (DefaultTableModel) lSWIDsTable.getModel();
		
		for (int i = 0; i < pParams.length-1; i+=2) {
			lModel.addRow(new Object[] {pParams[i], pParams[i+1]});
		}
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
		Object[] mInput = {lTablePanel};

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
			return new String[]{lSWIDsTable.getModel().getValueAt(lSWIDsTable.getSelectedRow(), COLUMN_KEY).toString()};
		}
		return null;
	}

	@Override
	public List<?> requestInputDataAsObjectList() {
		return null;
	}

}
