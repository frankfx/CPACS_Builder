package de.services;

import java.util.ArrayList;
import java.util.List;

import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;

import de.business.TipicoTableFilterModel;
import de.business.TipicoTableModel;
import de.types.TipicoDataType;
import de.utils.Tupel;

public class FilterService {
	private static RowFilter<TipicoTableModel, Object> getCriteriaByTipicoDataType(TipicoTableFilterModel pModel){
		// ID, TEAM, WINVALUE, EXPENSES, ATTEMPTS, DATE, SUCCESS
		Tupel<ComparisonType, Float> tupel;
		
		switch (pModel.getFilterDataType()) {
		case TEAM:
			return RowFilter.regexFilter(pModel.getFilterValue(), TipicoDataType.TEAM.ordinal());
		case WINVALUE: 
			tupel = pModel.getFilterOperationAsTupelComparisonTypeFloat();
			return RowFilter.numberFilter(tupel.getFirst(), tupel.getSecond(), TipicoDataType.WINVALUE.ordinal()); 
		case EXPENSES: 
			tupel = pModel.getFilterOperationAsTupelComparisonTypeFloat();
			return RowFilter.numberFilter(tupel.getFirst(), tupel.getSecond(), TipicoDataType.EXPENSES.ordinal()); 
		default:
			return null;
		}		
	}	
	
	public static RowFilter<TipicoTableModel, Object> getCompleteCriteriaExpressionRec(List<?> pList){
		if (pList != null && !pList.isEmpty())
			return getCompleteCriteriaExpressionRec(0, pList.size(), pList);
		return null;
	}
	
	private static RowFilter<TipicoTableModel, Object> getCompleteCriteriaExpressionRec(int curPos, int max, List<?> pList){
		boolean isLastFilterRow = max <= curPos + 1;
		TipicoTableFilterModel lModel = (TipicoTableFilterModel) pList.get(curPos);
		
		RowFilter<TipicoTableModel, Object> curRowFilter = getCriteriaByTipicoDataType( lModel );
		
		if (isLastFilterRow){
			return curRowFilter;
		} else {
			switch(lModel.getFilterConnectorType()){
			case AND : 
				List<RowFilter<TipicoTableModel, Object>> list = new ArrayList<RowFilter<TipicoTableModel, Object>>();
				list.add(curRowFilter);
				list.add(getCompleteCriteriaExpressionRec(curPos+1, max, pList));
				return RowFilter.andFilter(list);
			case OR : 
				list = new ArrayList<RowFilter<TipicoTableModel, Object>>();
				list.add(curRowFilter);
				list.add(getCompleteCriteriaExpressionRec(curPos+1, max, pList));
				return RowFilter.orFilter(list);
			default:
				return null;
			}
		}
	}	
	
}
