package de.presentation.popups.popupViews;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import de.business.TipicoTableFilterModel;
import de.presentation.popups.IPopup;
import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;
import de.services.ResourceService;
import de.types.TipicoDataType;
import de.utils.FAMessages;

public class TipicoTableFilterPopup extends JDialog implements IPopup {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int maxFilterSize = 4;
	private int curFilterSize = 0;
	private boolean successFlag = false;
	private List<TipicoTableFilterModel> mTipicoTableFilterModels = new ArrayList<TipicoTableFilterModel>();
	public static boolean isFilterActivated = false;
	
	public TipicoTableFilterPopup(Object[] pParams) {
		// the parent must wait for the dialog
		setModalityType(ModalityType.APPLICATION_MODAL);
		// creates the dialog
		this.initView();
	}	
	
	public void initView(){
		// Create and set up the window.
        this.setTitle("Extras");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new GridLayout(maxFilterSize+2,1));
		
		JButton lExitButton = createExitButton();
        JButton lAddButton = createAddNewFilterRowButton();
		JButton lOKButton = createOKButton();
        JCheckBox lFilterActivatedCheckBox = createFilterActivatedCheckBox();
		
		// Add control panel to add new filter rows or close the window
		JPanel lControlPanel = new JPanel();
		lControlPanel.setLayout(new GridLayout(1, 4));
		lControlPanel.add(lFilterActivatedCheckBox);
		lControlPanel.add(lAddButton);
		lControlPanel.add(lOKButton);
		lControlPanel.add(lExitButton);

		this.getContentPane().add(lControlPanel);        
        
		// Add first fixed filter row
		TipicoTableFilterModel m = new TipicoTableFilterModel(false);
		mTipicoTableFilterModels.add(m);
		getContentPane().add(m.getFilterOperationModelPanel());
		
        //Display the window.
		this.setSize(new Dimension(650, 200));
		this.setResizable(false);
        this.setVisible(true);		
	}

	private JCheckBox createFilterActivatedCheckBox() {
		JCheckBox lFilterActivatedCheckbox = new JCheckBox("Filter enabled");
		lFilterActivatedCheckbox.setSelected(isFilterActivated);
		lFilterActivatedCheckbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				isFilterActivated = lFilterActivatedCheckbox.isSelected();
			}
		});
		return lFilterActivatedCheckbox;
	}

	private JButton createOKButton() {
		JButton lOKButton = new JButton("OK");
	
		lOKButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validateFilterInput()){
					successFlag = true;
					dispose();
				}
			}
		});
		
		return lOKButton;
	}

	private JButton createExitButton(){
		JButton lExitButton = new JButton("Exit");
		
		lExitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				successFlag = false;
				dispose();
			}
		});        
        
		return lExitButton;
	}
	
	private JButton createAddNewFilterRowButton() {
		JButton lAddButton = new JButton();
        
        Image lImg = ResourceService.getInstance().IMAGE_ICON_ADD_GREEN.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		if (lImg != null){
			lAddButton.setIcon(new ImageIcon(lImg));
		} else {
			lAddButton.setText("Add");
		}

		// Add listener to add a new filter row
		lAddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				if(curFilterSize < maxFilterSize){
					final TipicoTableFilterModel tempFilter = new TipicoTableFilterModel(true);
					
					// Add listener to remove an existing filter row
					tempFilter.setRemoveButtonActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent pE) {
							mTipicoTableFilterModels.remove(tempFilter);
							getContentPane().remove(tempFilter.getFilterOperationModelPanel());
							revalidate();
							repaint();
							curFilterSize--;
						}
					});

					mTipicoTableFilterModels.add(tempFilter);
					getContentPane().add(tempFilter.getFilterOperationModelPanel());
					revalidate();
					curFilterSize++;
				} else {
					PopupFactory.getPopup(PopupType.ERROR, FAMessages.MESSAGE_FILTER_ADD_ROW_FAILED);
				}
			}
		});
		return lAddButton;
	}
	
	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new TipicoTableFilterPopup(null);
            }
        });
	}

	private boolean validateFilterInput() {
		for (TipicoTableFilterModel lModel: mTipicoTableFilterModels){
			TipicoDataType lDataType = lModel.getFilterDataType();
			boolean isNumericalDataType = lDataType.equals(TipicoDataType.ID) || lDataType.equals(TipicoDataType.WINVALUE) || 
					lDataType.equals(TipicoDataType.EXPENSES) || lDataType.equals(TipicoDataType.ATTEMPTS);
			
			boolean isInvalidNumericalValue = isNumericalDataType && Float.isNaN(lModel.getFilterValueAsFloat());
			boolean isInvalidStringValue = !isNumericalDataType && (lModel.getFilterValue() == null || lModel.getFilterValue().equals(""));

			if (isInvalidNumericalValue || isInvalidStringValue){
				PopupFactory.getPopup(PopupType.ERROR, FAMessages.MESSAGE_WRONG_FILTER_INPUT_VALUE + " (\"" + lModel.getFilterValue() +"\")");
				return false;					
			}
		}
		return true;
	}	
	
	
	@Override
	public String[] requestInputData() {
		if (successFlag)
			return mTipicoTableFilterModels.stream().map(i -> i.toString()).toArray(String[]::new);
		else
			return null;
	}

	@Override
	public List<TipicoTableFilterModel> requestInputDataAsObjectList() {
		if (successFlag){
			return mTipicoTableFilterModels;
		} else
			return null;
	}
}
