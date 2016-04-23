package de.presentation.filter;

import java.util.List;

import de.business.TipicoModel;

public class OrCriteria implements ICriteria {

	private ICriteria criteria;
	private ICriteria otherCriteria;

	public OrCriteria(ICriteria criteria, ICriteria otherCriteria) {
		this.criteria = criteria;
		this.otherCriteria = otherCriteria;
	}

	@Override
	public List<TipicoModel> matchedCriteria(List<TipicoModel> tipicoModel) {
		List<TipicoModel> firstCriteriaItems = criteria.matchedCriteria(tipicoModel);
		List<TipicoModel> otherCriteriaItems = otherCriteria.matchedCriteria(tipicoModel);

		for (TipicoModel person : otherCriteriaItems) {
			if (!firstCriteriaItems.contains(person)) {
				firstCriteriaItems.add(person);
			}
		}
		return firstCriteriaItems;
	}
}
