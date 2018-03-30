package de.business;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class AbstractSWTableModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<SoccerwayMatchModel> mList;

	public AbstractSWTableModel() {
		mList = new ArrayList<SoccerwayMatchModel>();
	}
	
	@Override
	public int getRowCount() {
		return mList.size();
	}
	
	/**
	 * Fills the data list with some entries 
	 * 
	 * @param entries
	 */
	public void initDataList(SoccerwayMatchModel [] entries) {
		List<SoccerwayMatchModel> lList = getDataList();
		
		if(entries != null)
			for (SoccerwayMatchModel lMatch : entries)
				lList.add(lMatch);
	}
	
	/**
	 * Returns the data list
	 * 
	 * @return ArrayList with SoccerwayMatchModels if exists otherwise 
	 * a new ArrayList
	 */
	public List<SoccerwayMatchModel> getDataList(){
		return mList != null ? mList : new ArrayList<SoccerwayMatchModel>();
	}

	/**
	 * Removes all elements from the data list
	 */
	public void clear(){
		this.mList.clear();
	}	
	
	/**
	 * Returns the SoccerwayMatchModel at a specific row
	 * 
	 * @param row
	 * @return
	 */
	public SoccerwayMatchModel getSoccerwayMatchModel(int row){
		return mList.get(row);
	}
	
	/**
	 * Adds a new row to the data list.
	 * 
	 * @param dataVec data for one table row
	 */
	public void addToTable(SoccerwayMatchModel pModel){
		if(!this.mList.contains(pModel))
			this.mList.add(pModel);		
	}
}
