package de.presentation.popups.popupViews;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import de.presentation.popups.IPopup;
import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;
import de.services.PropertyService;

public class SWPropertyPopup implements IPopup {

	private final JTable lSWPropertyTable;
	private final JPanel lTablePanel;
	private File lPropertiesFile;
	private Map<String, String> changePropertyMap;
	private static final int COLUMN_KEY = 0;
	private static final int COLUMN_VALUE = 1;
	
	public SWPropertyPopup(Object[] pParams) {
		if (pParams[0] instanceof File){
			lPropertiesFile = (File)pParams[0];
		}
		
		lTablePanel = new JPanel();
		lTablePanel.setLayout(new BorderLayout());
		
		lSWPropertyTable = new JTable(initTableHeader());
		
		lTablePanel.add(lSWPropertyTable.getTableHeader(), BorderLayout.PAGE_START);
		lTablePanel.add(new JScrollPane(lSWPropertyTable), BorderLayout.CENTER);		
		
		// must locate before the TableModelListener, otherwise the listener will triggered by during the initial fill. 
		fillTableWithProperties();
		
		// store all changes in the changeMap to modify the properties file after submit
		changePropertyMap = new TreeMap<String, String>();
		lSWPropertyTable.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent pE) {
				String key = lSWPropertyTable.getModel().getValueAt(pE.getFirstRow(), COLUMN_KEY).toString();
				String value = lSWPropertyTable.getModel().getValueAt(pE.getFirstRow(), COLUMN_VALUE).toString();
				changePropertyMap.put(key, value);
			}
		});			
	}

	@Override
	public String[] requestInputData() {

		Object[] mInput = {lTablePanel};

		JOptionPane pane = new JOptionPane(mInput,
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION);

		JDialog lDialog = pane.createDialog(null, "SWPropertyFile");
		lDialog.setVisible(true);

		Object selectedValue = pane.getValue();
		int n = -1;

		if (selectedValue == null)
			n = JOptionPane.CLOSED_OPTION;
		else
			n = Integer.parseInt(selectedValue.toString());

		lDialog.dispose();

		if (n == JOptionPane.OK_OPTION) {
			if (!changePropertyMap.isEmpty()){
				PropertyService.writeProperties(lPropertiesFile, changePropertyMap);
				return new String[]{PropertyService.PROPERTIES_CHANGED};
			}
		}
		return null;
	}

	@Override
	public List<?> requestInputDataAsObjectList() {
		return null;
	}	
	
	/**
	 * Fuegt eine neue Zeile in die Tabelle.
	 * 
	 * @param dataVec data for one table row
	 */
	private void addToTable(Vector<String> dataVec){
		DefaultTableModel lModel = (DefaultTableModel) lSWPropertyTable.getModel();
		lModel.addRow(dataVec);
	}	
	
	/**
	 * Setzt den Tabellenkopf und unterbindet das Editieren der KEY-Spalte 
	 * 
	 */		
	private TableModel initTableHeader() {
		Object columnNames[] = {"KEY", "VALUE"};
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

	/**
	 * Fuellt die Tabelle mit den Inhalten der Properties Datei. 
	 * 
	 */	
	private void fillTableWithProperties() {
		Vector<String> vec;
		Map<Object, Object> map;
		
		try {
			map = PropertyService.getProperties(new FileInputStream(lPropertiesFile));
		} catch (FileNotFoundException e) {
			PopupFactory.getPopup(PopupType.ERROR, e.getMessage());
			return;
		}

		for (Map.Entry<Object, Object> entry : map.entrySet()){
			vec = new Vector<String>();
			vec.add(entry.getKey().toString());
			vec.add(entry.getValue().toString());
			this.addToTable(vec);
		}
	}
}

