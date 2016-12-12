package de.presentation.bundesliga;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import de.business.TipicoModel;
import de.business.TipicoTableModel;
import de.services.ResourceService;
import de.types.PersistenceType;

public class TipicoBetTable extends JTable{
	private static final long serialVersionUID = 1L;
	private JMenuItem mMenuClearSelection = new JMenuItem("clear selection");

	/**
	 * Method to create a JTable with colored rows and a context menu
	 */
	public TipicoBetTable(AbstractTableModel pTableModel) {
		super(pTableModel);
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
	
				if (SwingUtilities.isRightMouseButton(e)) {
					int r = rowAtPoint(e.getPoint());
	
					if (r >= 0 && r < getRowCount()) {
						setRowSelectionInterval(r, r);
					} else {
						clearSelection();
					}
	
					int rowindex = getSelectedRow();
	
					if (rowindex >= 0 && e.getComponent() instanceof JTable) {
						JPopupMenu popup = createPopUp();
						popup.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});
	}		

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
	
	private JPopupMenu createPopUp() {
		JPopupMenu lPopup = new JPopupMenu("Test");
		lPopup.add(mMenuClearSelection);

		// Add submenu to popup menu
		return lPopup;
	}	
	
	public void setMenuClearSelectionListener(ActionListener l) {
		this.mMenuClearSelection.addActionListener(l);
	}
}