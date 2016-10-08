package de.business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.types.SWResultDataType;

public class SWResultTableModel extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<SoccerwayMatchModel> list;	
	
	public SWResultTableModel(){
		this(null);
	}

	public SWResultTableModel(SoccerwayMatchModel [] entries){
		list = new ArrayList<SoccerwayMatchModel>();
		
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
			return list.get(rowIndex).getDate();
		case 1 :
			return list.get(rowIndex).getCompetition();
		case 2 :
			return list.get(rowIndex).getTeam1();
		case 3 :
			return list.get(rowIndex).getTeam2();	
		case 4 :
			return list.get(rowIndex).getResult();
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
//		switch (col) {
//		case 5 :  
//			list.get(rowIndex).setID(aValue.toString()); break;
//			
//			break;
//
//		default:
//			break;
//		}
//		
//		mResultTable.getModel().setValueAt(aValue, rowIndex, columnIndex);
//		rowData[row][col] = value;
//       // fireTableCellUpdated(row, col);
    }
	
	/**
	 * adds a new row to the table Resutl.
	 * 
	 * @param dataVec data for one table row
	 */
	public void addToTable(SoccerwayMatchModel pModel){
		if(!this.list.contains(pModel))
			this.list.add(pModel);		
	}
	
	public void clear(){
		this.list.clear();
	}
	
	public List<SoccerwayMatchModel> getDataList(){
		return list;
	}
}
