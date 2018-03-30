package de.presentation.bundesliga;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;

import de.business.AbstractSWTableModel;
import de.business.SWFixtureTableModel;

public class SWFixturePanel extends SWPanel{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor calls SWPanel (super class) initView
	 */
	public SWFixturePanel() {
		super.initView();
	}

	/*
	 * ===========================================================
	 * inherited methods
	 * ===========================================================
	 */
	@Override
	public AbstractSWTableModel getSWDataModel() {
		if (getSWJTable() != null)
			return (SWFixtureTableModel) getSWJTable().getModel();
		return new SWFixtureTableModel();
	}

	@Override
	public List<JButton> getButtons() {
		return Collections.emptyList();
	}	
	
	@Override
	public ActionListener actionRefresh() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("SWFixture");
			}
		};
	}	
}
