package de.business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.types.SWDataType;

public class SWFixtureTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private List<SoccerwayMatchModel> mList;
	private final int COLUMN_COUNT = 5;
	
	public SWFixtureTableModel() {
		this(null);
	}
	
	public SWFixtureTableModel(SoccerwayMatchModel [] entries){
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
		return COLUMN_COUNT;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return SWDataType.getSWDataTypeFixturesTableDescription(columnIndex);
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
			return mList.get(rowIndex).getResult();
		case 2 :
			return mList.get(rowIndex).getCompetition();
		case 3 :
			return mList.get(rowIndex).getTeam1();
		case 4 :
			return mList.get(rowIndex).getTeam2();	
		default:
			return null;
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

}
