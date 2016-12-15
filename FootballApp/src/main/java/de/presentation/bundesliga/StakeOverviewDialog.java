package de.presentation.bundesliga;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class StakeOverviewDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable mTable;
	private JTable mHeaderTable;
	
	private final int TABLE_ROW = 1; 
	private final int TABLE_COL = 1; 
	
	// should be final because of its usage to initialize a rowSortListener in initView()
	private final boolean mSortLineNumber = true;
	
	public StakeOverviewDialog(JFrame pParent, String pTitle, int pWidth, int pHeight, List<Object[]> pData) {
		super(pParent);
		this.setModal(true);
		
		setTitle(pTitle);
		setSize(pWidth, pHeight);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		initView();
		fillTable(pData);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void fillTable(List<Object[]> pData){
		DefaultTableModel model = (DefaultTableModel) mTable.getModel();
		int lColCount = model.getColumnCount();

		for(Object [] lRowData : pData){
			while(lColCount < lRowData.length){
				model.addColumn(null);
				lColCount++;
			}
			model.addRow(lRowData);
		}
	}
	
	private void actionAddNewRow() {
		DefaultTableModel model = (DefaultTableModel) mTable.getModel();
		model.addRow(new Integer[]{1, 2, 2});
		
		((AbstractTableModel) mHeaderTable.getModel()).fireTableDataChanged();
	}	
	
	
	public void initView(){
		mTable = new JTable(TABLE_ROW, TABLE_COL);
		
		// set sort construct with default model without the new empty header element for row numbers
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(mTable.getModel());
		mTable.setRowSorter(sorter);
		
		// model for row numbers with one column
		final AbstractTableModel model = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public int getColumnCount() {
				return 1;
			}
			
			@Override
			public Object getValueAt(int row, int column) {
				if (mSortLineNumber)
					return mTable.convertRowIndexToModel(row); // use only if the line number should be sorted
				else 
					return row;
			}

			@Override
			public int getRowCount() {
				return mTable.getRowCount();
			}
		};
		
		// header (table) for row numbers with this one column model
		mHeaderTable = new JTable(model);
		mHeaderTable.setShowGrid(false);
		mHeaderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		mHeaderTable.setPreferredScrollableViewportSize(new Dimension(30, 0));
		mHeaderTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		
		// set format for row numbers (column 0)
		mHeaderTable.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable x, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				boolean lRowSelected = mTable.getSelectionModel().isSelectedIndex(row);
				
				// draws each cell like the header row (see the -1) of mTable
				Component component = mTable.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(mTable, value, false, false, -1, -2);
				((JLabel) component).setHorizontalAlignment(JLabel.CENTER);
				// draw line number bold if line was selected
				if (lRowSelected) {
					component.setFont(component.getFont().deriveFont(Font.BOLD));
				} else {
					component.setFont(component.getFont().deriveFont(Font.PLAIN));
				}
				return component;
			}
		});
		
		// listen mTable selection to update the the above cell rendering for row numbers
		mTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				model.fireTableRowsUpdated(0, model.getRowCount() - 1);
			}
		});		
				
		// listen for mTable sorting action to update/sort (if row number sort is activated) the row number order 
		if(mSortLineNumber) {
			mTable.getRowSorter().addRowSorterListener(new RowSorterListener() {
	
				@Override
				public void sorterChanged(RowSorterEvent e) {
					model.fireTableDataChanged();
				}
			});
		}
		
		// button to a new row
		JButton mButtonAddRow = new JButton("add row");
		mButtonAddRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pArg0) {
				actionAddNewRow();
			}
		});
		
		// button to filter the table content
		JButton mButtonFilter = new JButton("Toggle filter");
		mButtonFilter.addActionListener(new ActionListener() {
			private RowFilter<TableModel, Object> filter = new RowFilter<TableModel, Object>() {
				
				@Override
				public boolean include(javax.swing.RowFilter.Entry<? extends TableModel, ? extends Object> entry) {
					if (entry.getValue(0) != null){
						//return ((Number) entry.getValue(0)).intValue() % 2 == 0;
						return ((String) entry.getValue(0)).length()>0 ;
						//return ((Date) entry.getValue(0)).getTime()< ;
					}
					else{
						return true;
					}
				}
			};
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sorter.getRowFilter() != null) {
					sorter.setRowFilter(null);
					sorter.setRowFilter(RowFilter.regexFilter("t"));
				} else {
					sorter.setRowFilter(filter);
				}
			}
		});
		
		JScrollPane pane = new JScrollPane(mTable);
		pane.setRowHeaderView(mHeaderTable);
		
		getContentPane().add(pane);
		getContentPane().add(mButtonFilter, BorderLayout.PAGE_END);
		getContentPane().add(mButtonAddRow, BorderLayout.WEST);
	}
}