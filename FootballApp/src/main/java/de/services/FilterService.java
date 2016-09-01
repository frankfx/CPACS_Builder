package de.services;

import java.util.List;

import de.business.TipicoTableFilterModel;
import de.presentation.filter.AndCriteria;
import de.presentation.filter.CriteriaExpenses;
import de.presentation.filter.CriteriaWinValue;
import de.presentation.filter.ICriteria;
import de.presentation.filter.OrCriteria;

public class FilterService {
	private static ICriteria getCriteriaByTipicoDataType(TipicoTableFilterModel pModel){
//		ID, TEAM, WINVALUE, EXPENSES, ATTEMPTS, DATE, SUCCESS
		switch (pModel.getFilterDataType()) {
		case WINVALUE: 
			return new CriteriaWinValue(pModel.getFilterValueAsFloat(), pModel.getFilterOperation());
		case EXPENSES: 
			return new CriteriaExpenses(pModel.getFilterValueAsFloat(), pModel.getFilterOperation());
		default:
			return null;
		}		
	}
	
	public static ICriteria getCompleteCriteriaExpressionRec(List<?> pList){
		if (pList != null && !pList.isEmpty())
			return getCompleteCriteriaExpressionRec(0, pList.size(), pList);
		return null;
	}
	
	private static ICriteria getCompleteCriteriaExpressionRec(int curPos, int max, List<?> pList){
		boolean isLastFilterRow = max <= curPos + 1;
		TipicoTableFilterModel lModel = (TipicoTableFilterModel) pList.get(curPos);
		ICriteria curFilterCriteria = getCriteriaByTipicoDataType( (TipicoTableFilterModel) pList.get(curPos)); 
		
		if (isLastFilterRow){
			return curFilterCriteria;
		} else {
			switch(lModel.getFilterConnectorType()){
			case AND : 
				return new AndCriteria(curFilterCriteria, getCompleteCriteriaExpressionRec(curPos+1, max, pList));
			case OR : 
				return new OrCriteria(curFilterCriteria, getCompleteCriteriaExpressionRec(curPos+1, max, pList));
			default:
				return null;
			}
		}
	}
}
