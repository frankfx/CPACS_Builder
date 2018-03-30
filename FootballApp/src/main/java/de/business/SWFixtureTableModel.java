package de.business;

import java.time.LocalDate;

import de.types.SWDataType;

public class SWFixtureTableModel extends AbstractSWTableModel {

	private static final long serialVersionUID = 1L;
	
	private final int COLUMN_COUNT = 5;
	
	public SWFixtureTableModel() {
		// creats an empty data list (mList) in the superclass
		this(null);
	}
	
	public SWFixtureTableModel(SoccerwayMatchModel [] entries){
		// creats an data list (mList) in the superclass with entries
		super.initDataList(entries);
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
			return getDataList().get(rowIndex).getDate();
		case 1 :
			return getDataList().get(rowIndex).getResult();
		case 2 :
			return getDataList().get(rowIndex).getCompetition();
		case 3 :
			return getDataList().get(rowIndex).getTeam1();
		case 4 :
			return getDataList().get(rowIndex).getTeam2();	
		default:
			return null;
		}
	}

}
