package de.presentation.filter;

import java.util.List;

import de.business.TipicoModel;

public interface ICriteria {
	public List<TipicoModel> matchedCriteria(List<TipicoModel> tipicoModel);
}
