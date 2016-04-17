package de.presentation.filter;

import java.util.ArrayList;
import java.util.List;

import de.business.TipicoModel;

public class CriteriaExpenses implements Criteria {

	private float lThreshold = 5.0f;

	@Override
	public List<TipicoModel> matchedCriteria(List<TipicoModel> pModelList) {
		List<TipicoModel> lResult = new ArrayList<TipicoModel>();

		for (TipicoModel lModel : pModelList) {
			if (lModel.getExpenses() > lThreshold)
				lResult.add(lModel);
		}
		return lResult;
	}
}
