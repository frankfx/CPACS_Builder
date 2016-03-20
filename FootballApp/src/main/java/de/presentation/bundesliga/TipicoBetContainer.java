package de.presentation.bundesliga;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import de.presentation.AbstractPanelContainer;

public class TipicoBetContainer extends AbstractPanelContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTable mTable;
	private JScrollPane mTablePane;	
	private JButton mBtnSubmit;
	private JButton mBtnNew;
	private JButton mBtnCancel;
	private JButton mBtnLoad;	
	
	public TipicoBetContainer() {	
		// create an default panel
		initPanel("Tipico", new GridBagLayout(), Color.WHITE);
        createTipico();
    }

	/** Creates Tipico panel. */
	public void createTipico() {
		mBtnSubmit = new JButton("Submit");
		mBtnNew = new JButton("New");
		mBtnCancel = new JButton("Cancel");
		mBtnLoad = new JButton("Load");
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.weightx=0.1;
		c.weighty=0.1;		
		
		mTable = new JTable();
		mTable.setPreferredScrollableViewportSize(mTable.getPreferredSize());
        mTable.setFillsViewportHeight(true);		
        mTablePane = new JScrollPane(mTable);
        mTablePane.setVisible(true);
	    
		this.add(mTablePane, c);
		
		
		c.fill = GridBagConstraints.HORIZONTAL;		
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx=0.1;
		c.weighty=0.0;
		this.add(mBtnSubmit, c);
		c.gridx = 1;
		c.gridy = 1;
		this.add(mBtnLoad, c);
		c.gridx = 2;
		c.gridy = 1;
		this.add(mBtnNew, c);
		c.gridx = 3;
		c.gridy = 1;
		this.add(mBtnCancel, c);		
		
		
		//		
//		
//		
//		
//		int row = 0;
//		int col = 0;
//		
//		
//		
//		
//        c.fill = GridBagConstraints.BOTH;
//       // c.weightx = 0.1;
//       // c.weighty = 0.1;
//        c.insets = new Insets(10, 10, 10, 10);
//		c.gridx = col++;
//		c.gridy = row;
////		this.add(lTeam1, c);		
//		
//		c.gridx = col++;
//		c.gridy = row;
////        this.add(lTeam2, c);		
//		
//		c.gridx = col;
//		c.gridy = row++;
////		this.add(result, c);		
//		
//		col = 0;
	}

	public void setButtonNewBetListener(ActionListener l){
		this.mBtnNew.addActionListener(l);
	}
	
	public void setButtonLadenListener(ActionListener l){
		this.mBtnLoad.addActionListener(l);
	}	
	
	
	public JButton getBtnSubmit() {
		return mBtnSubmit;
	}

	public void setBtnSubmit(JButton pBtnSubmit) {
		this.mBtnSubmit = pBtnSubmit;
	}

	public JButton getBtnNew() {
		return mBtnNew;
	}

	public void setBtnNew(JButton pBtnNew) {
		this.mBtnNew = pBtnNew;
	}

	public JButton getBtnCancel() {
		return mBtnCancel;
	}

	public void setBtnCancel(JButton pBtnCancel) {
		this.mBtnCancel = pBtnCancel;
	}

	public JButton getBtnLoad() {
		return mBtnLoad;
	}

	public void setmBtnLoad(JButton pBtnLoad) {
		this.mBtnLoad = pBtnLoad;
	}	
	public JTable getTable() {
		return mTable;
	}
	public void setTable(JTable pTable) {
		this.mTable = pTable;
	}	
}