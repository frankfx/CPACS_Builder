package de.business;

import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class TipicoTableModel implements TableModel{

	// storing the objects in list
	List<TipicoModel> list;
	// the headers
	String[] header;
	// mark rows with colors
	Map<Integer, Color> mRowColorMap = new HashMap<Integer, Color>();
    
	public TipicoTableModel(){
		this(null, new String[]{"ID", "TEAM", "WINVALUE", "EXPENSES", "ATTEMPTS", "DATE", "SUCCESSFUL"});
	}

	public TipicoTableModel(TipicoModel [] entries, String [] header){
		this.header = header;
		list = new ArrayList<TipicoModel>();
		
		if(entries != null)
			for (int i = 0; i < entries.length; i++) {
				list.add(entries[i]);
			}
	}
	
	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return header.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return header[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return getValueAt(0, columnIndex).getClass();		
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 1:
			return true;
		default:
			return false;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return list.get(rowIndex).getTnr();
		case 1:
			return list.get(rowIndex).getTeam();
		case 2:
			return list.get(rowIndex).getWinValue();
		case 3:
			return list.get(rowIndex).getExpenses();
		case 4:
			return list.get(rowIndex).getAttempts();
		case 5:
			return list.get(rowIndex).getDate();
		case 6:
			return list.get(rowIndex).getSuccess();
		}		
		return null;		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			list.get(rowIndex).setTnr((Integer) aValue); break;
		case 1:
			list.get(rowIndex).setTeam(aValue.toString()); break;
		case 2:
			list.get(rowIndex).setWinValue((Float) aValue); break;
		case 3:
			list.get(rowIndex).setExpenses((Float) aValue); break;
		case 4:
			list.get(rowIndex).setAttempts((Integer) aValue); break;
		case 5:
			list.get(rowIndex).setDate((LocalDate) aValue); break;
		case 6:
			list.get(rowIndex).setSuccess((Boolean) aValue); break;
		}		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}
	
	public void addRow(TipicoModel pModel){
		if(!this.list.contains(pModel))
			this.list.add(pModel);
	}
	
	public void removeRow(int pRowIndex) {
		if (pRowIndex >= 0 && pRowIndex < list.size() )
			this.list.remove(pRowIndex);	
	}
	
	public TipicoModel getTipicoModelAtRow(int pRowIndex){
		return pRowIndex >= 0 && pRowIndex < list.size() ? list.get(pRowIndex) : null;
	}
	
	public void setRowColor(int pRow, Color pColor) {
		mRowColorMap.put(pRow, pColor);
	}

	public Color getRowColor(int pRow) {
		return mRowColorMap.get(pRow);
	}	
	
	@Override
	public String toString(){
		return this.list.toString();
	}
}