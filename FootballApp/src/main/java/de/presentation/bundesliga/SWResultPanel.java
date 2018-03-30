package de.presentation.bundesliga;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.TableColumnModel;

import de.business.AbstractSWTableModel;
import de.business.SWResultTableModel;

public class SWResultPanel extends SWPanel{

	private static final long serialVersionUID = 1L;
	private JButton mAcceptButton = new JButton("Submit");
	
	/**
	 * Constructor calls SWPanel (super class) constructor initView
	 */
	public SWResultPanel() {
		super.initView();
		
		TableColumnModel columnModel = getSWJTable().getColumnModel();
		columnModel.getColumn(1).setMaxWidth(55);
		columnModel.getColumn(1).setResizable(false);
	}
	
	/*
	 * ===========================================================
	 * inherited methods
	 * ===========================================================
	 */	
	@Override
	public AbstractSWTableModel getSWDataModel() {
		if (getSWJTable() != null)
			return (SWResultTableModel) getSWJTable().getModel();
		return new SWResultTableModel();
	}

	@Override
	public List<JButton> getButtons() {
		return Arrays.asList(mAcceptButton);
	}

	@Override
	public ActionListener actionRefresh() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("SWResult");
			}
		};
	}	
	
	public JButton getSubmitButton() {
		return mAcceptButton;
	}	
	
	public List<String> getDataListWithAcceptedValues(){
		return ((SWResultTableModel) getSWDataModel()).getDataListWithValueAccepted();
	}
}
