package de.presentation.filter;

import java.util.ArrayList;
import java.util.List;

import de.business.TipicoModel;
import de.types.FilterOperationType;

public class CriteriaID implements ICriteria {

	private float mThreshold;
	private FilterOperationType mOperator;

	public CriteriaID(float pThreshold, FilterOperationType pOperator) {
		mThreshold = pThreshold;
		mOperator = pOperator;
	}

	@Override
	public List<TipicoModel> matchedCriteria(List<TipicoModel> pModelList) {
		List<TipicoModel> lResult = new ArrayList<TipicoModel>();

		for (TipicoModel lModel : pModelList) {
			switch (mOperator) {
			case LESS:
				if (lModel.getTnr() < mThreshold)
					lResult.add(lModel);
				break;
			case EQUAL:
				if (lModel.getTnr() == mThreshold)
					lResult.add(lModel);
				break;
			case LESS_OR_EQUAL:
				if (lModel.getTnr() <= mThreshold)
					lResult.add(lModel);
				break;				
			case GREATER:
				if (lModel.getTnr() > mThreshold)
					lResult.add(lModel);
				break;
			case GREATER_OR_EQUAL:
				if (lModel.getTnr() >= mThreshold)
					lResult.add(lModel);
				break;		
			case UNEQUAL:
				if (lModel.getTnr() != mThreshold)
					lResult.add(lModel);
				break;				
			default:
				break;
			}
		}
		return lResult;
	}
}
