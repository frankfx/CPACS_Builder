package de.presentation.bundesliga;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import de.business.TipicoModel;
import de.business.TipicoTableModel;
import de.presentation.AbstractPanelContainer;
import de.presentation.JSplitButton;
import de.types.PersistenceType;

public class TipicoBetView extends AbstractPanelContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTable mTable;
	private TipicoTableModel mTableModel;
	private JScrollPane mTablePane;	
	private JButton mBtnBetValue;
	private JButton mBtnNew;
	private JButton mBtnModify;	
	private JButton mBtnDelete;
	private JSplitButton mBtnDBSplit;
	private JMenuItem mMenuClearSelection = new JMenuItem("clear selection");
	
	public TipicoBetView() {	
		// create an default panel
		initPanel("Tipico", new GridBagLayout(), Color.WHITE);
    }

	/** Creates Tipico panel. */
	@Override
	public void initView() {
		mBtnBetValue = new JButton("Compute");
		mBtnNew = new JButton("New");
		mBtnModify = new JButton("Modify");
		mBtnDelete = new JButton("Delete");
		mBtnDBSplit = new JSplitButton("DB");
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 5;
		c.weightx=0.1;
		c.weighty=0.1;		

		mTableModel = new TipicoTableModel();
		mTable = createNewJTable();
		mTable.setPreferredScrollableViewportSize(mTable.getPreferredSize());
        mTable.setFillsViewportHeight(true);		
        
		TableRowSorter<TipicoTableModel> sorter = new TableRowSorter<TipicoTableModel>(mTableModel);
		mTable.setRowSorter(sorter);
		
        mTablePane = new JScrollPane(mTable);
        mTablePane.setVisible(true);
	    
		this.add(mTablePane, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;		
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx=0.1;
		c.weighty=0.0;
		this.add(mBtnBetValue, c);
		c.gridx = 1;
		c.gridy = 1;
		this.add(mBtnModify, c);
		c.gridx = 2;
		c.gridy = 1;
		this.add(mBtnNew, c);
		c.gridx = 3;
		c.gridy = 1;
		this.add(mBtnDelete, c);
		c.gridx = 4;
		c.gridy = 1;		
		c.weightx = 0.0;
		this.add(mBtnDBSplit, c);
	}

	/*
	 * Method to create a JTable with colored rows and a context menu
	 */
	private JTable createNewJTable() {
		// initialize JTable and set background rendering
		final JTable lTable = new JTable(mTableModel) {
			private static final long serialVersionUID = 1L;

//			@Override
//			public TableCellEditor getCellEditor(int row, int column) {
//			   Object value = super.getValueAt(row, column);
//			   if(value != null) {
//			      if(value instanceof JComboBox) {
//			           return new DefaultCellEditor((JComboBox<BetPredictionType>)value);
//			      }
//			            return getDefaultEditor(value.getClass());
//			   }
//			   return super.getCellEditor(row, column);
//			}			
			
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
				Component c = super.prepareRenderer(renderer, row, column);
				
				if (!isRowSelected(row)) {
					int modelRow = convertRowIndexToModel(row);
					TipicoModel lModel = ((TipicoTableModel) getModel()).getTipicoModelAtRow(modelRow);
					
					if (lModel.getPersistenceType().equals(PersistenceType.NEW))
						c.setBackground(Color.YELLOW);
					else if (lModel.getSuccess())
						c.setBackground(Color.GREEN);
					else if (LocalDate.now().isAfter(lModel.getDate()))
						c.setBackground(Color.MAGENTA);
					else
						c.setBackground(getBackground());
				}

				return c;
			}
		};
		
		
		// set mouse listener for context menu
		lTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				if (SwingUtilities.isRightMouseButton(e)) {
					int r = lTable.rowAtPoint(e.getPoint());

					if (r >= 0 && r < lTable.getRowCount()) {
						lTable.setRowSelectionInterval(r, r);
					} else {
						lTable.clearSelection();
					}

					int rowindex = lTable.getSelectedRow();

					if (rowindex >= 0 && e.getComponent() instanceof JTable) {
						JPopupMenu popup = createPopUp();
						popup.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});

		return lTable;
	}

	/**
	 * Updates the Tipico JTable
	 */		
	public void updateTable(){
		mTableModel.fireTableDataChanged();

		//mTable.revalidate();
    	//mTable.invalidate();
    	//mTablePane.repaint();		
	}

	
	/**
	 * ========================
	 * BEGIN LISTENER
	 * ========================
	 */	
	public void setButtonNewBetListener(ActionListener l){
		this.mBtnNew.addActionListener(l);
	}
	
	public void setButtonModifyListener(ActionListener l){
		this.mBtnModify.addActionListener(l);
	}	

	public void setButtonLadenListener(ActionListener l){

	}	
	
	public void setButtonBetValueListener(ActionListener l){
		this.mBtnBetValue.addActionListener(l);
	}	
	
	public void setButtonDeleteListener(ActionListener l){
		this.mBtnDelete.addActionListener(l);
	}	
	
	public void setButtonCommitListerner(ActionListener l){
		this.mBtnDBSplit.getCommitItem().addActionListener(l);
	}

	public void setButtonPullListerner(ActionListener l){
		this.mBtnDBSplit.getPullItem().addActionListener(l);
	}

	public void setButtonPullDetailListerner(ActionListener l) {
		this.mBtnDBSplit.getPullDetailItem().addActionListener(l);
	}

	public void setButtonRemoveListerner(ActionListener l){
		this.mBtnDBSplit.getRemoveItem().addActionListener(l);
	}	
	
	public void setButtonRevertListerner(ActionListener l){
		this.mBtnDBSplit.getRevertItem().addActionListener(l);
	}	
	
	public void setButtonDBBrowserListener(ActionListener l){
		this.mBtnDBSplit.getDBBrowserItem().addActionListener(l);
	}

	public void setMenuClearSelectionListener(ActionListener l) {
		this.mMenuClearSelection.addActionListener(l);
	}

	public void setFilterPopupListener(MouseAdapter l) {
		this.mTable.getTableHeader().addMouseListener(l);
	}
	
	/**
	 * ========================
	 * END LISTENER
	 * ========================
	 */	
	
	
	/**
	 * ========================
	 * BEGIN GETTER AND SETTER
	 * ========================
	 */	
	public JButton getBtnBetValue() {
		return mBtnBetValue;
	}

	public void setBtnBetValue(JButton pBtnBetValue) {
		this.mBtnBetValue = pBtnBetValue;
	}

	public JButton getBtnNew() {
		return mBtnNew;
	}

	public void setBtnNew(JButton pBtnNew) {
		this.mBtnNew = pBtnNew;
	}

	public JButton getBtnModify() {
		return mBtnModify;
	}

	public void setmBtnModify(JButton pBtnModify) {
		this.mBtnModify = pBtnModify;
	}
	
	public JButton getBtnDelete() {
		return mBtnDelete;
	}

	public void setBtnDelete(JButton pBtnDelete) {
		this.mBtnDelete = pBtnDelete;
	}	
	
	public JTable getTable() {
		return mTable;
	}
	
	public void setTable(JTable pTable) {
		this.mTable = pTable;
	}
	
	public TipicoTableModel getTableModel() {
		return mTableModel;
	}

	public void setTableModel(TipicoTableModel pTableModel) {
		this.mTableModel = pTableModel;
	}	
	
	/**
	 * ========================
	 * END GETTER AND SETTER
	 * ========================
	 */

	public JPopupMenu createPopUp() {
		JPopupMenu lPopup = new JPopupMenu("Test");
		lPopup.add(mMenuClearSelection);

		// Add submenu to popup menu
		return lPopup;
	}
}
