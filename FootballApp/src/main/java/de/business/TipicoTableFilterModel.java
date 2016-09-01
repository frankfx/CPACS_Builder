package de.business;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;
import de.services.ResourceService;
import de.types.FilterConnectionType;
import de.types.FilterOperationType;
import de.types.TipicoDataType;
import de.utils.FAMessages;

public class TipicoTableFilterModel {
	private JPanel mMainPanel;
	private JButton mRemoveButton;
	private JTextField mFilterValue;
	private JComboBox<TipicoDataType> mTableColumn;
	private JComboBox<FilterOperationType> mFilterOperation;
	private JComboBox<FilterConnectionType> mFilterConnector;
	
	public TipicoTableFilterModel(boolean pShowRemoveButton) {
		mTableColumn = new JComboBox<TipicoDataType>();
		mTableColumn.addItem(TipicoDataType.TEAM);
		mTableColumn.addItem(TipicoDataType.EXPENSES);
		mTableColumn.addItem(TipicoDataType.WINVALUE);
		mTableColumn.addItem(TipicoDataType.SUCCESS);
		
		mFilterOperation = new JComboBox<FilterOperationType>();
		mFilterOperation.addItem(FilterOperationType.EQUAL);
		mFilterOperation.addItem(FilterOperationType.UNEQUAL);
		mFilterOperation.addItem(FilterOperationType.LESS);
		mFilterOperation.addItem(FilterOperationType.LESS_OR_EQUAL);
		mFilterOperation.addItem(FilterOperationType.GREATER);
		mFilterOperation.addItem(FilterOperationType.GREATER_OR_EQUAL);
		
		mFilterConnector = new JComboBox<FilterConnectionType>();
		mFilterConnector.addItem(FilterConnectionType.AND);
		mFilterConnector.addItem(FilterConnectionType.OR);
		mFilterConnector.setVisible(false);
		
		mFilterValue = new JTextField();
		
		mRemoveButton = new JButton();
		
		mMainPanel = new JPanel();
		mMainPanel.setLayout(new GridLayout());
		mMainPanel.add(mTableColumn);
		mMainPanel.add(mFilterOperation);
		mMainPanel.add(mFilterValue);
		mMainPanel.add(mFilterConnector);
		mMainPanel.add(mRemoveButton);
		
		if(pShowRemoveButton){
			Image lImg = ResourceService.getInstance().IMAGE_ICON_REMOVE_RED.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
	    	if (lImg != null){
	    		mRemoveButton.setIcon(new ImageIcon(lImg));
	    	} else {
	    		mRemoveButton.setText("Remove");
	    	}
		} else {
			mRemoveButton.setVisible(false);
		}
	}
	
	public JPanel getFilterOperationModelPanel(){
		return mMainPanel;
	}
	
	public void setRemoveButtonActionListener(ActionListener l){
		this.mRemoveButton.addActionListener(l);
	}
	
	public TipicoDataType getFilterDataType(){
		return (TipicoDataType)mTableColumn.getSelectedItem();
	}

	public float getFilterValueAsFloat(){
		try{
			return Float.parseFloat(mFilterValue.getText());
		} catch(NumberFormatException | NullPointerException e){
			return Float.NaN;
		}
	}
	
	public String getFilterValue() {
		return mFilterValue.getText();
	}

	public FilterOperationType getFilterOperation() {
		return (FilterOperationType) mFilterOperation.getSelectedItem();
	}

	public FilterConnectionType getFilterConnectorType() {
		Object obj = mFilterConnector.getSelectedItem();
		
		if (obj != null) 
			return (FilterConnectionType) obj;
		
		if (mRemoveButton.isVisible()){
			PopupFactory.getPopup(PopupType.ERROR, FAMessages.MSG_NO_FILTER_CONNECTOR);
		} else { 
			return FilterConnectionType.EMPTY;
		}
		
		return null;
	}	
	
	/**
	 * getter and setter
	 * @return
	 */
	public JComboBox<FilterConnectionType> getFilterConnector() {
		return mFilterConnector;
	}

	public void setFilterConnector(JComboBox<FilterConnectionType> mFilterConnector) {
		this.mFilterConnector = mFilterConnector;
	}	
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(mTableColumn.getSelectedItem());
		sb.append('|');
		sb.append(mFilterOperation.getSelectedItem());
		sb.append('|');
		sb.append(mFilterValue.getText());
		sb.append('|');
		sb.append(mFilterConnector.getSelectedItem());
		
		return sb.toString(); 
	}
}
