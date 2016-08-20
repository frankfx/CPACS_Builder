package de.business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.types.BetPredictionType;
import de.types.TipicoDataType;

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
			return Integer.class;
		case 1:
			return String.class;
		case 2:
			return BetPredictionType.class;			
		case 3:
			return Float.class;
		case 4:
			return Float.class;
		case 5:
			return Integer.class;
		case 6:
			return LocalDate.class;
		case 7:
			return Boolean.class;
		default:
			return Boolean.class;
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
			return list.get(rowIndex).getTnr();
		case 1:
			return list.get(rowIndex).getTeam();
		case 2:
			return list.get(rowIndex).getBetPrediction();
		case 3:
			return list.get(rowIndex).getWinValue();
		case 4:
			return list.get(rowIndex).getExpenses();
		case 5:
			return list.get(rowIndex).getAttempts();
		case 6:
			return list.get(rowIndex).getDate();
		case 7:
			return list.get(rowIndex).getSuccess();
		default:
			return null;
		}		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			list.get(rowIndex).setTnr((Integer) aValue); break;
		case 1:
			list.get(rowIndex).setTeam(aValue.toString()); break;
		case 2:
			list.get(rowIndex).setBetPrediction((BetPredictionType) aValue); break;
		case 3:
			list.get(rowIndex).setWinValue((Float) aValue); break;
		case 4:
			list.get(rowIndex).setExpenses((Float) aValue); break;
		case 5:
			list.get(rowIndex).setAttempts((Integer) aValue); break;
		case 6:
			list.get(rowIndex).setDate((LocalDate) aValue); break;
		case 7:
			list.get(rowIndex).setSuccess((Boolean) aValue); break;
		}		
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
	
	public int generateValidID(List<Integer> pIDsFromDB) {
		int lMaxTnr;

		if (pIDsFromDB != null && !pIDsFromDB.isEmpty()) {
			lMaxTnr = pIDsFromDB.stream().mapToInt(i -> i).max().getAsInt() + 1;
		} else {
			lMaxTnr = list.size() + 1;
		}

		return rec_generateValidID(lMaxTnr);
	}
	
	private int rec_generateValidID(int id){
		if(isIDValid(id))
			return id;
		else
			return rec_generateValidID(id+1);
	}	
	
	public boolean isIDValid(int id){
		for(TipicoModel item : list)
			if(item.getTnr() == id)
				return false;
		return true;
	}	
	
	@Override
	public String toString(){
		return this.list.toString();
	}
	
	public List<TipicoModel> getAsList(){
		return list;
	}

	public void setList(List<TipicoModel> list) {
		this.list = list;
	}
	
	public TipicoModel[] getAsArray() {
		return list.toArray(new TipicoModel[list.size()]);
	}

	public List<TipicoModel> getFilterBackupList() {
		return mBackupListPtr;
	}

	public void setFilterBackupList(List<TipicoModel> pFilterList) {
		this.mBackupListPtr = new ArrayList<TipicoModel>(pFilterList);
	}
}