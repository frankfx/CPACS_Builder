package de.business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.types.BetPredictionType;
import de.types.TipicoDataType;
import de.utils.Utils;

public class TipicoTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// storing the objects in list
	List<TipicoModel> list;
	List<TipicoModel> mBackupListPtr;
	
	public TipicoTableModel(){
		this(null);
	}

	public TipicoTableModel(TipicoModel [] entries){
		list = new ArrayList<TipicoModel>();
		
		if(entries != null)
			for (int i = 0; i < entries.length; i++) {
				list.add(entries[i]);
			}
		mBackupListPtr = list;
	}
	
	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return TipicoDataType.getSize();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return TipicoDataType.getDataType(columnIndex).toString();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return BetPredictionType.class;			
		case 2:
			return Float.class;
		case 3:
			return Float.class;
		case 4:
			return LocalDate.class;
		default:
			return null;
		}
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
			return list.get(rowIndex).getTeam();
		case 1:
			return list.get(rowIndex).getBetPrediction();
		case 2:
			return list.get(rowIndex).getWinValue();
		case 3:
			return list.get(rowIndex).getExpenses();
		case 4:
			return list.get(rowIndex).getDate();
		default:
			return null;
		}		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			list.get(rowIndex).setTeam(aValue.toString()); break;
		case 1:
			list.get(rowIndex).setBetPrediction((BetPredictionType) aValue); break;
		case 2:
			list.get(rowIndex).setWinValue((Float) aValue); break;
		case 3:
			list.get(rowIndex).setExpenses((Float) aValue); break;
		case 4:
			list.get(rowIndex).setDate((LocalDate) aValue); break;
		}		
	}
	
	public String getIDByIndex(int idx){
		return list.get(idx).getID();
	}

	public void addRow(TipicoModel pModel){
		if(!this.list.contains(pModel))
			this.list.add(pModel);
	}
	
	public boolean hasID(String pID){
		return this.list.contains(new TipicoModel(pID));
	}
	
	public void removeRow(int pRowIndex) {
		if (pRowIndex >= 0 && pRowIndex < list.size() )
			this.list.remove(pRowIndex);	
	}
	
	public TipicoModel getTipicoModelAtRow(int pRowIndex){
		return pRowIndex >= 0 && pRowIndex < list.size() ? list.get(pRowIndex) : null;
	}
	
	public List<TipicoModel> getTipicoModelsAsList(){
		return list;
	}

	public void setList(List<TipicoModel> list) {
		this.list = list;
	}
	
	public TipicoModel[] getAsArray() {
		return list.toArray(new TipicoModel[list.size()]);
	}

	@Override
	public String toString(){
		return this.list.toString();
	}
}