package framework.widget.table;

import org.apache.log4j.Logger;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.event.TableModelEvent;
import javax.swing.Icon;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Color;

/**
 * Created by IntelliJ IDEA.
 * User: trappmaR
 * Date: Jun 11, 2003
 * Time: 1:35:09 PM
 * To change this template use Options | File Templates.
 */
public class DataModel extends AbstractTableModel {

    private ArrayList<TableCell[]> mRows;

    private int mColCount = -1;
    private String[] mColIdentifiers;

    private static final Logger sLogger = Logger.getLogger("framework.widget.table.TableImplementationDataModel");


    public DataModel() {
        mRows = new ArrayList<TableCell[]>(100);
    }

    /**
     * Clears the complete table-data
     * with one method-call.
     */
    public void clearData() {
        mRows.clear();
    }


    /**
     * this method is used to insert many rows at once
     * and then later call fireTableRowsInserted() one time
     * @param pRowData
     */
    public void addRowWithoutFireEventRowsInserted(TableCell[] pRowData) {
        mRows.add(pRowData);
    }


    public void addRow(TableCell[] pRowData) {
        mRows.add(pRowData);
        int row = mRows.size()-1;
        //fireTableStructureChanged();
        fireTableRowsInserted(row, row);
    }


    public void addRowAt(int pRowIndex, TableCell[] pRowData) {
        if (pRowIndex != -1) {
            mRows.add(pRowIndex, pRowData);
            int row = mRows.size()-1;
            fireTableRowsInserted(row, row);
        } else {
            sLogger.debug("given rowIndex out of range : DataModel line 61");
        }
    }


    public void deleteRowAt(int pRowIndex) {
        if (pRowIndex != -1){
            mRows.remove(pRowIndex);
            fireTableRowsDeleted(pRowIndex, pRowIndex);
        } else {
            sLogger.debug("given rowIndex out of range : DataModel line 71");
        }
    }

    public int getColumnCount() {
        int lResult = 0;
        if (mColCount > 0) {
            lResult = mColCount;
        }
        return lResult;
    }

    /**
     * Überschrieben, um eigene Spaltenbezeichner verwenden zu können
     *
     * @param k Index der Spalte, deren Bezeichner abgefragt werden soll
     * @return Der String, welcher die Bezeichnung darstellt
     */

    public String getColumnName(int k) {
        return mColIdentifiers[k];
    }


    public int getRowCount() {
        return mRows.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TableCell lResult = null;
        if (rowIndex != -1 && columnIndex != -1 && rowIndex < mRows.size() && columnIndex < getColumnCount()) {
            lResult =  mRows.get(rowIndex)[columnIndex];
            if (lResult != null) {
                lResult = mRows.get(rowIndex)[columnIndex];
                return lResult;
            } else {
                return new TableCellInvalid();
            }
        } else {
            sLogger.debug("DataModel: rowIndex out of range at DataModel line 108");
            return  new TableCellInvalid();
        }
    }


    @Override
    public void setValueAt(Object value, int row, int col) {
        // if mRows not empty and given row exists and row and col != -1
        if (row != -1 && col != -1 && !mRows.isEmpty() && row < mRows.size() ) {
            TableCell lCellObject =  mRows.get(row)[col];
            if (lCellObject.isTableCellObject()){
                // if there is a TableCellObject we have to fill the CellValue
                ((TableCellObject)lCellObject).setCellValue(((TableCellObject)value).getCellValue());
            } else {
                 mRows.get(row)[col] = (TableCell) value;
            }
            fireTableCellUpdated(row, col);
        } else {
            sLogger.debug("DataModel: rowIndex out of range at DataModel line 125");
        }
    }


    /**
     * Setting the data to be displayed in a table
     *
     * @param pData An ArrayList which contains the table-data
     */
    public void setTableData(ArrayList<TableCell[]> pData) {
        mRows = pData;
        fireTableStructureChanged();
    }

    /**
     * Sets the identifiers for each table column
     *
     * @param pColIdentifiers A String-Array that contains the identifiers for each column
     */
    public void setColumnIdentifiers(String[] pColIdentifiers) {
        mColIdentifiers = pColIdentifiers;
        mColCount = pColIdentifiers.length;
    }

    /**
     * Setting the data to be displayed in a table, as well as the table-headers
     *
     * @param pData           An ArrayList which contains the table-data
     * @param pColIdentifiers A String-Array that contains the identifiers for each column
     */
    public void initTable(ArrayList<TableCell[]> pData, String[] pColIdentifiers) {
        this.setColumnIdentifiers(pColIdentifiers);
        this.setTableData(pData);
    }

    /**
     * If not overwritten, edited data is not stored in the correct CellFormat. Integers
     * are converted into Strings.
     *
     * @param c The queried column
     * @return The class of the data in the specified column
     */
    public Class<? extends TableCell> getColumnClass(int c) {
        TableCell lResult = (TableCell) getValueAt(0, c);
        if (lResult != null) {
            return lResult.getClass();
        } else {
            return TableCellInvalid.class;
        }
    }


    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public ArrayList<TableCell[]> getDatas() {
        return mRows;
    }


    public TableCell[] getDataAt(int pRow) {
        if (pRow == -1){
            sLogger.debug("given row is not in TableCell[]: DataModel line 196");
            return new TableCell[]{};
        }
        return  mRows.get(pRow);
    }


    /**
     * This fine grain notification tells listeners the exact range
     * of cells, rows, or columns that changed.
     */
    public void tableChanged(TableModelEvent e) {

    }

    //******************************** Cell Formating **********************************//

    // manipulation of TableCells
    // set's the Renderer of a TableCell
    public void setCellRenderer(TableCellRenderer pCellRenderer, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellRenderer(pCellRenderer);
        }
    }

    // set's the Editor of a TableCell
    public void setCellEditor(TableCellEditor pCellEditor, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellEditor(pCellEditor);
        }
    }

    // set's the Icon of a TableCell
    public void setCellIcon(Icon pIcon, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellIcon(pIcon);
        }
    }
    
    public Icon getCellIcon(int pRow, int pCol){
        Icon lResult = null;
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            lResult = ((TableCellObject) cell).getCellIcon();
        }
        return lResult;
    }


    // set's the Background of a TableCell
    public void setCellBackground(Color pColor, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellBackground(pColor);
        }
    }

    // get's the Background of a TableCell
    public Color getCellBackground(int pRow, int pCol) {
        Color lResult = Color.WHITE;
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            lResult = ((TableCellObject) cell).getCellBackground();
        }
        return lResult;
    }

    // get's the Old Background of a TableCell
    public Color getOldCellBackground(int pRow, int pCol) {
        Color lResult = Color.WHITE;
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            lResult = ((TableCellObject) cell).getOldCellBackground();
        }
        return lResult;
    }


    // set's the Foreground of a TableCell
    public void setCellForeground(Color pColor, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellForeground(pColor);
        }
    }

    // get's the Foreground of a TableCell
    public Color getCellForeground(int pRow, int pCol) {
        Color lResult = Color.WHITE;
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            lResult = ((TableCellObject) cell).getCellForeground();
        }
        return lResult;
    }

    // get's the OldForeground of a TableCell
    public Color getOldCellForeground(int pRow, int pCol) {
        Color lResult = Color.WHITE;
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            lResult = ((TableCellObject) cell).getOldCellForeground();
        }
        return lResult;
    }


    // set's the Font of a TableCell
    public void setCellFont(Font pFont, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellFont(pFont);
        }
    }

    public Font getCellFont(int pRow, int pCol){
        Font lResult = null;
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            lResult =  ((TableCellObject) cell).getCellFont();
        }
        return lResult;
    }
    

    // set's the Editable Flag of a TableCell
    public void setCellEditable(boolean pEditable, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellEditable(pEditable);
        }
    }

    public boolean getCellEditable(int pRow, int pCol) {
        boolean lResult = true;
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            lResult = ((TableCellObject) cell).getCellEditable();
        }
        return lResult;
    }

    // set's the Editable Flag of a TableCell
    public void setCellEnabled(boolean pEnabled, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellEnabled(pEnabled);
        }
    }

    public boolean getCellEnabled(int pRow, int pCol) {
        boolean lResult = true;
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            lResult = ((TableCellObject) cell).getCellEnabled();
        }
        return lResult;
    }

    // set's the Focusable Flag of a TableCell
    public void setCellFocusable(boolean pFocusable, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellFocusable(pFocusable);
        }
    }

    // set's the Visibility Flag of a TableCell
    public void setCellVisible(boolean pVisible, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellVisible(pVisible);
        }
    }

    // set's the Alignment of a TableCell
    public void setCellAlignment(int pAlignment, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellAlignment(pAlignment);
        }
    }

    // set's the Masked Flag of a TableCell
    public void setCellMasked(boolean pMasked, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellMasked(pMasked);
        }
    }

    // set's the Editable Flag of a TableCell
    public void setCellCombined(boolean pCombined, int pRow, int pCol) {
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            ((TableCellObject) cell).setCellCombined(pCombined);
        }
    }

    public boolean getCellCombined(int pRow, int pCol) {
        boolean lResult = true;
        TableCell cell = getCell(pRow, pCol);
        if (cell.isTableCellObject()) {
            lResult = ((TableCellObject) cell).getCellCombined();
        }
        return lResult;
    }


    private TableCell getCell(int pRow, int pCol) {
        if (pRow != -1 && pCol != -1 && (getRowCount() > pRow) && (getColumnCount() > pCol)) {
            return (TableCell) getValueAt(pRow, pCol);
        }
        else {
        	return new TableCellInvalid();
        }
    }
}
