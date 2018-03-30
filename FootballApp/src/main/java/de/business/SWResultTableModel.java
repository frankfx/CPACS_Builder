package de.business;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import de.types.SWDataType;

public class SWResultTableModel extends AbstractSWTableModel {

	private static final long serialVersionUID = 1L;
	
	private final int COLUMN_COUNT = 6;
	
	public SWResultTableModel(){
		// creats an empty data list (mList) in the superclass
		this(null);
	}

	public SWResultTableModel(SoccerwayMatchModel [] entries){
		// creats an data list (mList) in the superclass with entries
		super.initDataList(entries);
	}	
	
	@Override
	public int getColumnCount() {
		return COLUMN_COUNT;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return SWDataType.getSWDataTypeResultsTableDescription(columnIndex);
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
			return getDataList().get(rowIndex).getDate();
		case 1 :
			return getDataList().get(rowIndex).getCompetition();
		case 2 :
			return getDataList().get(rowIndex).getTeam1();
		case 3 :
			return getDataList().get(rowIndex).getTeam2();	
		case 4 :
			return getDataList().get(rowIndex).getResult();
		case 5 :
			return getDataList().get(rowIndex).getAccept();
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
			getDataList().get(row).setAccept((boolean)value); break;
		default:
			break;
		}
    }
	
	public List<String> getDataListWithValueAccepted(){
		return getDataList().stream().filter(match -> match.getAccept()).map(match -> match.getTeamID()).collect(Collectors.<String>toList());
	}
}