package de.presentation.filter;

import java.util.List;

import de.business.TipicoModel;

public class AndCriteria implements ICriteria {

	private ICriteria criteria;
	private ICriteria otherCriteria;

	public AndCriteria(ICriteria criteria, ICriteria otherCriteria) {
		this.criteria = criteria;
		this.otherCriteria = otherCriteria;
	}

	@Override
	public List<TipicoModel> matchedCriteria(List<TipicoModel> tipicoModel) {
		List<TipicoModel> firstCriteriaTipico = criteria.matchedCriteria(tipicoModel);
		return otherCriteria.matchedCriteria(firstCriteriaTipico);
	}
}
