package de.business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import de.types.SWResultDataType;

public class SWResultTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private List<SoccerwayMatchModel> mList;	
	
	public SWResultTableModel(){
		this(null);
	}

	public SWResultTableModel(SoccerwayMatchModel [] entries){
		mList = new ArrayList<SoccerwayMatchModel>();
		
		if(entries != null)
			for (SoccerwayMatchModel lMatch : entries)
				mList.add(lMatch);
	}	
	
	@Override
	public int getRowCount() {
		return mList.size();
	}

	@Override
	public int getColumnCount() {
		return SWResultDataType.getSize();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return SWResultDataType.getDataType(columnIndex).toString();
	}

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
    
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return mList.get(rowIndex).getDate();
		case 1 :
			return mList.get(rowIndex).getCompetition();
		case 2 :
			return mList.get(rowIndex).getTeam1();
		case 3 :
			return mList.get(rowIndex).getTeam2();	
		case 4 :
			return mList.get(rowIndex).getResult();
		case 5 :
			return mList.get(rowIndex).getAccept();
		default:
			return null;
		}
	}   
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 5:
			return true;
		default:
			return false;
		}
	}        
	
	/*
     * Don't need to implement this method unless your table's
     * data can change.
     */
	@Override
	public void setValueAt(Object value, int row, int col) {
		switch (col) {
		case 5 :  
			mList.get(row).setAccept((boolean)value); break;
		default:
			break;
		}
    }
	
	/**
	 * adds a new row to the table Resutl.
	 * 
	 * @param dataVec data for one table row
	 */
	public void addToTable(SoccerwayMatchModel pModel){
		if(!this.mList.contains(pModel))
			this.mList.add(pModel);		
	}
	
	public SoccerwayMatchModel getSoccerwayMatchModel(int row){
		return mList.get(row);
	}
	
	public void clear(){
		this.mList.clear();
	}
	
	public List<SoccerwayMatchModel> getDataList(){
		return mList;
	}
	
	public List<String> getDataListWithValueAccepted(){
		return mList.stream().filter(match -> match.getAccept()).map(match -> match.getTeamID()).collect(Collectors.<String>toList());
	}
}