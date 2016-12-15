package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.business.TipicoModel;
import de.business.TipicoTableModel;
import de.presentation.bundesliga.TipicoBetView;
import de.presentation.filter.ICriteria;
import de.presentation.popups.IPopup;
import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;
import de.printing.TipicoPrintService;
import de.services.Database;
import de.services.FilterService;
import de.services.LoggerService;
import de.services.SQLService;
import de.types.BetPredictionType;
import de.types.MessageType;
import de.types.PersistenceType;
import de.utils.FAMessages;
import de.utils.Utils;

public class TipicoActivityBean implements ISubController{

	private BundesligaActivityBean mBundesligaListener;
	private Database mDB = null;
	private TipicoBetView mView;

	final static Logger logger = LoggerService.getInstance().getLogger();
	
	public TipicoActivityBean(TipicoBetView pView) {
		logger.setLevel(Level.ALL);
		this.mView = pView;
		this.addListener();
	}
	
 // ========================
 // START INIT LISTENER
 // ========================	
		
	/**
	 * sets all listeners from the presentation view
	 */		
	private void addListener() {

		this.mView.setButtonNewBetListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionNew();
			}
		});		
			
		this.mView.setButtonModifyListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionModify();
			}
		});
			
		this.mView.setButtonBetValueListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionBetValue();
			}
		});
			
		this.mView.setButtonDeleteListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionDelete();
			}
		});
			
		this.mView.setButtonCommitListerner(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionCommit();
			}
		});
			
		this.mView.setButtonPullListerner(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionInitTable();
			}
		});	

		this.mView.setButtonPullDetailListerner(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionPullDetailChild();
			}
		});

		this.mView.setButtonRemoveListerner(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionRemove();
			}
		});			
		
		this.mView.setButtonRevertListerner(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionDropCreateTable();
			}
		});		
		
		this.mView.setButtonDBBrowserListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionDBBrowser();
			}
		});

		this.mView.setMenuClearSelectionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mView.getTable().getSelectionModel().clearSelection();
			}
		});

		this.mView.setFilterPopupListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					actionStartSQLPrompt();
				}
			}
		});
		
		this.mView.setTableSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()){
					int row_idx = mView.getTable().getSelectedRow();
					if (row_idx != -1){
						String id =((TipicoTableModel) mView.getTable().getModel()).getIDByIndex( mView.getTable().convertRowIndexToModel(row_idx));
						
						logger.log(Level.INFO, "message 1");
						
						mBundesligaListener.actionUpdateFixturesTable(id);
						mBundesligaListener.actionUpdateResultsTable(id);
					}
				} else {
					System.out.println("leave");
				}
				
			}
		});
	}
	
	/**
	 * creates a listener for the main controller to listen events in the subcontroller
	 * Is mainly used to update the console in the main controller
	 */			
	@Override
	public void setUpdateListener(BundesligaActivityBean pListener){
		this.mBundesligaListener = pListener;
	}
	
 // ========================
 // END INIT LISTENER
 // ========================	
	

 // =========================
 // START DATABASE OPERATIONS
 // =========================
		
	/**
	 * use initBean to establish the connection to the database
	 */			
	@Override
	public void initBean(String [] pCredentials) {
		String lResult;
		
		if (mDB == null)
			if(initDB(pCredentials[0], pCredentials[1], pCredentials[2], pCredentials[3], pCredentials[4])){
				lResult = "connection successfull";
				mBundesligaListener.actionUpdateStatistics(getBalance());
			}
			else {
				lResult = "connection refused";
			}
		else 
			lResult = "already connected";

		mBundesligaListener.actionUpdateConsole(lResult);
	} 
		
	/**
	 * use updateBean to disconnect from database
	 */		
	@Override
	public void updateBean(){
		if (mDB != null){
			mDB.disconnect();
			mDB = null;
			mBundesligaListener.actionUpdateConsole("disconnected");
			mBundesligaListener.actionUpdateStatistics(getBalance());
		}
	}		
		
	/**
	 * initialize database
	 * 
	 * @param pHost the host of the connection
	 * @param pPort the port to connect to database
	 * @param pDatabase the name of the database
	 * @param pUser the user name 
	 * @param pPass the password
	 * 
	 * @return true if the connection was successful otherwise false
	 * 
	 */		
	public boolean initDB(String pHost, String pPort, String pDatabase, String pUser, String pPass){
		mDB = new Database();
		
		if (mDB.connect(pHost, pPort, pDatabase, pUser, pPass)){
			return true;
		} else {
			mDB = null;
			return false;
		}
	}

	/**
	 * creates the tipico table with all attributes
	 * 
	 * @return true if the creation was successful otherwise false
	 */		
	public boolean createTableTipico(){
		return mDB != null ? mDB.updateDB(SQLService.SQL_CREATE_TABLE_TIPICO) : false;
	}

	/**
	 * drops the complete tipico table
	 * 
	 * @return true if the table was successful dropped otherwise false
	 */		
	public boolean dropTableTipico(){
		return mDB != null ? mDB.updateDB(SQLService.SQL_DROP_TABLE_TIPICO) : false;
	}
	
	/**
	 * deletes a row given by a id (tnr) in table tipico
	 * 
	 * @param pTnr the unique id of the model
	 * 
	 * @return true if the row was successful removed otherwise false
	 */		
	public boolean deleteRowInTipico(String pID){
		if (mDB != null){
			try {
				PreparedStatement prepStmt = mDB.getConnection().prepareStatement(SQLService.SQL_REMOVE_TIPICO);
				prepStmt.setString(1, pID);
				prepStmt.execute();
				return true;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}	

	/**
	 * inserts a model (row) to the database
	 * 
	 * @param pTipicoModel a model with a new id (tnr)
	 * 
	 * @return true if the model was successful inserted otherwise false
	 */		
	public boolean insertRowInTipico(TipicoModel pTipicoModel){
		return this.setModelToTipico(SQLService.SQL_INSERT_ROW, pTipicoModel);
	}

	/**
	 * updates a model (row) to the database
	 * 
	 * @param pTipicoModel a model with a existing id (tnr) in the database
	 * 
	 * @return true if the model was successful updated otherwise false
	 */			
	public boolean updateRowInTipico(TipicoModel pTipicoModel){
		return this.setModelToTipico(SQLService.SQL_UPDATE_ROW, pTipicoModel);
	}	

	/**
	 * inserts a model to the database, if the model key doesn't already exist otherwise the existing model will be updated
	 * 
	 * @param pTipicoModel the model to insert or update in the database
	 * 
	 * @return true if the model was successful inserted/updated otherwise false
	 */		
	public boolean insertOrUpdateRowInTipico(TipicoModel pTipicoModel){
		return this.setModelToTipico(SQLService.SQL_INSERT_UPDATE_ROW, pTipicoModel);
	}
	
	/**
	 * intern private helper method to insert or update a model to the database 
	 */		
	private boolean setModelToTipico(String pSql, TipicoModel pModel){
		String lResult;
		
		if(mDB == null)
			lResult = FAMessages.MSG_NO_DATABASE;
		else{
			try {
				PreparedStatement mInsertBetStmt = mDB.getConnection().prepareStatement(pSql);

				mInsertBetStmt.setString(1, pModel.getID());
				mInsertBetStmt.setString(2, pModel.getTeam());
				mInsertBetStmt.setString(3, pModel.getBetPrediction().toString());
				mInsertBetStmt.setFloat(4, pModel.getWinValue());
				mInsertBetStmt.setFloat(5, pModel.getExpenses());
				mInsertBetStmt.setDate(6, Date.valueOf(pModel.getDate()));
				mInsertBetStmt.setBoolean(7, pModel.getSuccess());
				mInsertBetStmt.setString(8, pModel.getTeam());	
				mInsertBetStmt.setString(9, pModel.getBetPrediction().toString());
				mInsertBetStmt.setFloat(10, pModel.getWinValue());
				mInsertBetStmt.setFloat(11, pModel.getExpenses());
				mInsertBetStmt.setDate(12, Date.valueOf(pModel.getDate()));
				mInsertBetStmt.setBoolean(13, pModel.getSuccess());
				
				mInsertBetStmt.execute();	
				mBundesligaListener.actionUpdateConsole(FAMessages.MSG_DATABASE_UPDATE_SUCCESS);
				return true;
			} catch (SQLException e) {
				lResult = e.getMessage();
			}
		}
		mBundesligaListener.actionUpdateConsole(lResult);
		return false;
	}	

	/**
	 * updates only the team in the database
	 * 
	 * @param pId the id of the table entry
	 * @param pTeam the team to update in the data
	 *  
	 * @return true if the update was successful otherwise false
	 */		
	public boolean updateTeamInTableTipico(int pId, String pTeam){
		return mDB != null ? mDB.updateDB("UPDATE Tipico set team='" + pTeam + "' where tnr=" + pId) : false;  
	}

	/**
	 * updates only the winValue in the database
	 * 
	 * @param pId the id of the table entry
	 * @param pWinValue the win value to update in the database
	 *  
	 * @return true if the update was successful otherwise false
	 */		
	public boolean updateWinValueInTableTipico(int pId, float pWinValue){
		return mDB != null ? mDB.updateDB("UPDATE Tipico set winValue=" + pWinValue + " where tnr=" + pId) : false;  
	}	

	/**
	 * updates only the expenses in the database
	 * 
	 * @param pId the id of the table entry
	 * @param pExpenses the expenses to update in the database
	 *  
	 * @return true if the update was successful otherwise false
	 */			
	public boolean updateExpensesInTableTipico(int pId, float pExpenses){
		return mDB != null ? mDB.updateDB("UPDATE Tipico set expenses=" + pExpenses + " where tnr=" + pId) : false;  
	}		
	
	/**
	 * updates only the attempts in the database
	 * 
	 * @param pId the id of the table entry
	 * @param pAttempts the attempts value to update in the database
	 *  
	 * @return true if the update was successful otherwise false
	 */			
	public boolean updateAttemptsInTableTipico(int pId, float pAttempts){
		return mDB != null ? mDB.updateDB("UPDATE Tipico set attempts=" + pAttempts + " where tnr=" + pId) : false;  
	}	

	/**
	 * updates only the date in the database
	 * 
	 * @param pId the id of the table entry
	 * @param pDate the date to update in the database
	 *  
	 * @return true if the update was successful otherwise false
	 */		
	public boolean updateDateInTableTipico(int pId, float pDate){
		return mDB != null ? mDB.updateDB("UPDATE Tipico set pDate='" + pDate + "' where tnr=" + pId) : false;  
	}		

	/**
	 * updates only the success flag in the database
	 * 
	 * @param pId the id of the table entry
	 * @param pSuccess the success flag value to update in the database
	 *  
	 * @return true if the update was successful otherwise false
	 */		
	public boolean updateSuccessInTableTipico(int pId, float pSuccess){
		return mDB != null ? mDB.updateDB("UPDATE Tipico set success=" + pSuccess + " where tnr=" + pId) : false;  
	}		

	/**
	 * initialized the table with all entries of the database
	 */		
	public void actionInitTable(){
		if (readFromDatabaseToTableModel(mView.getTableModel(), SQLService.SQL_SELECT_ALL_FROM_TIPICO)) {
			mView.getTableModel().fireTableDataChanged();
		}
	}	
	
	public void actionPullDetailChild() {
		String[] arr = PopupFactory.getPopup(PopupType.DATABASE_PULLDETAIL_POPUP, null).requestInputData();

		if (arr != null && readFromDatabaseToTableModel(mView.getTableModel(), arr[0])) {
			mView.getTableModel().fireTableDataChanged();
		}
	}

	private List<String> getTipicoIDsFromDB() {
		return this.getTipicoIDsFromDB(SQLService.SQL_SELECT_TNR_FROM_TIPICO);
	}

	public List<String> getTipicoOpenGameIDsFromDB() {
		return this.getTipicoIDsFromDB(SQLService.SQL_SELECT_OPEN_TNR_FROM_TIPICO);
	}	
	
	private List<String> getTipicoIDsFromDB(String pIDSearchCriterion) {
		List<String> lResult = null;
		if (mDB!=null && mDB.isConnected()){
			lResult = new ArrayList<String>();
			
			mDB.query(pIDSearchCriterion);
			try {
				while (mDB.getResultSet().next()) {
					lResult.add(Utils.getIDWithoutSuffix(mDB.getResultSet().getString(1)));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		return lResult;
	}

	private boolean readFromDatabaseToTableModel(TipicoTableModel pModel, String pSQL) {
		if (mDB != null && mDB.isConnected()){
			mDB.query(pSQL);
	
			try {
				TipicoModel data;
				String id;
				while(mDB.getResultSet().next()){
					id = mDB.getResultSet().getString(1);
					
					if (!pModel.hasID(id)){
						data = new TipicoModel();
						data.setID(id);
						data.setTeam(mDB.getResultSet().getString(2));
						data.setBetPrediction(BetPredictionType.getType(mDB.getResultSet().getString(3)));
						data.setWinValue(mDB.getResultSet().getFloat(4));
						data.setExpenses(mDB.getResultSet().getFloat(5));
						data.setDate(mDB.getResultSet().getDate(6));
						data.setSuccess(mDB.getResultSet().getBoolean(7));
						data.setPersistenceType(PersistenceType.OTHER);
						
						pModel.addRow(data);
					}
				}
				mBundesligaListener.actionUpdateStatistics(getBalance());
				return true;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} 
		}
		return false;
	}
	
 // ========================
 // END DATABASE OPERATIONS
 // ========================	
	
	
	
 // ============================
 // START LOCAL TABLE OPERATIONS
 // ============================		
	
	/**
	 * get the Tnr of the selected table row
	 */		
	public int getTnrOfSelectedTableRow(){
		int lSelectedRow = getSelectedRow();
		
		if(lSelectedRow < 0){
			PopupFactory.getPopup(PopupType.HINT, FAMessages.MSG_NO_ROW_SELECTED);
			return -1;
		}
			
		return Integer.parseInt(mView.getTable().getModel().getValueAt(lSelectedRow, 0).toString());		
	}
	
	public List<TipicoModel> getModelsOfSelectedRows(){
		List<TipicoModel> lResult = new ArrayList<TipicoModel>();
		int [] rows = mView.getTable().getSelectedRows();
		
		for (int i : rows){
			lResult.add(mView.getTableModel().getTipicoModelAtRow(i));
		}
		
		return lResult;
	}
	
	/**
	 * get index of the selected table row
	 */		
	private int getSelectedRow(){
		int lSelectedRow = mView.getTable().getSelectedRow();

		if(lSelectedRow < 0){
			PopupFactory.getPopup(PopupType.HINT, FAMessages.MSG_NO_ROW_SELECTED);
			return -1;
		}

		return mView.getTable().convertRowIndexToModel(lSelectedRow);
	}
	
 // ============================
 // END LOCAL TABLE OPERATIONS
 // ============================		

		
	/**
	 * private helper method to start an input dialog to manipulate the table attributes
	 */	
	private boolean startTableInputPopup(TipicoModel pTipicoModel, boolean isNewEntry){
		String lResult;
		
		if (pTipicoModel == null){
			lResult = FAMessages.MSG_NO_VALID_TABLE_MODEL + -1;
		} else{
			String[] lValues = PopupFactory.getPopup(PopupType.TIPICO_NEW_POPUP, new Object[] { pTipicoModel }).requestInputData();
			if (lValues == null){
				mBundesligaListener.actionUpdateStatusBar(MessageType.ERROR, FAMessages.MSG_NO_VALID_INPUT);
				return false;
			} else if(!updateTipicoEntry(pTipicoModel, lValues, isNewEntry)){
				lResult = FAMessages.MSG_NO_VALID_INPUT;
			} else
				return true;
		}
		
		PopupFactory.getPopup(PopupType.ERROR, lResult);
		return false;
	}

	/**
	 * Function to update a TipicoModel with given arguments
	 * 
	 * @param pTipicoModel the data model to update
	 * @param args the new arguments for the data model
	 * 
	 * @return true if the update was successful otherwise false
	 * 
	 */	
	private boolean updateTipicoEntry(TipicoModel pTipicoModel, String [] args, boolean isNewEntry){
		if(args == null || pTipicoModel == null)
			return false;
		
		pTipicoModel.setID(isNewEntry ? Utils.createInternalID(args[0], mDB) : args[0]);
		pTipicoModel.setTeam(args[1]);
		pTipicoModel.setBetPrediction(BetPredictionType.getType(args[2]));
		pTipicoModel.setWinValue(Float.parseFloat(args[3]));
		pTipicoModel.setExpenses(Float.parseFloat(args[4]));
		pTipicoModel.setDate(LocalDate.parse(args[5]));
		pTipicoModel.setSuccess(Boolean.parseBoolean(args[6]));
		pTipicoModel.setPersistenceType(PersistenceType.NEW);
		return true;
	}	

 // ========================
 // BEGIN ACTION
 // ========================
	

	private void actionStartSQLPrompt() {
      	// call child widget to get the filter assertions
		final IPopup popup = PopupFactory.getPopup(PopupType.TIPICO_TABLE_FILTER_POPUP, null); 
      	List<?> lFilterExpressions = popup.requestInputDataAsObjectList();
      	
      	// filter list with the filter expressions given in the child popup widget
		if (lFilterExpressions != null) {
			// revert table list for unfilter operation (empty lFilterExpressions) 
			
			// creates the complete coherent filter expression
			ICriteria lCriteria = FilterService.getCompleteCriteriaExpressionRec(lFilterExpressions);
				
			List<TipicoModel> lResultList = lCriteria == null ? null : lCriteria.matchedCriteria(mView.getTableModel().getAsList());
				
			updateTableInFilterMode(lResultList);
		}	
	}

	/**
	 * Local table function to create a new entry
	 */		
	private void actionNew(){
		TipicoModel lModel = new TipicoModel();
		
		if(startTableInputPopup(lModel, true)){
			mView.getTableModel().addRow(lModel);
			updateTable();
		}
	}

	/**
	 * Local function to delete to selected row
	 */		
	private void actionDelete(){
		int lStart = getSelectedRow(); 
		int lEnd = lStart + mView.getTable().getSelectedRowCount();				

		for(int i = lStart; i<lEnd; i++){
			mView.getTableModel().removeRow(lStart);
		}		
		
//		if(mView.getTable().getRowCount() == 0)
//			mView.getTable().getSelectionModel().clearSelection();

		updateTable();		
	}	
	
	
	/**
	 * Local table function to modify the selected entry
	 */		
	private void actionModify(){
		int lRowIndex = getSelectedRow();
			
		if(lRowIndex < 0)
			return;

		TipicoModel lModel = mView.getTableModel().getTipicoModelAtRow(lRowIndex);
		
		if(startTableInputPopup(lModel, false)){
			updateTable();
			mBundesligaListener.actionUpdateConsole("Table updated");			
		}
		
	}	

	/**
	 * Local table function to compute the bet value
	 */		
	private void actionBetValue(){
		String lResult;
		
		TipicoModel lModel = mView.getTableModel().getTipicoModelAtRow( getSelectedRow());		
		
		float lWinValue = lModel == null ? 1.0f : lModel.getWinValue();
		float lExpanses = lModel == null ? 0.0f : lModel.getExpenses();
		
		String[] arr = PopupFactory.getPopup(PopupType.TIPICO_BETVALUE_POPUP, new Object[] { lWinValue, lExpanses }).requestInputData();

		if (arr != null) {
			if (lModel == null) {
				lModel = new TipicoModel();
				
				lModel.setID(Utils.generateValidID(this.getTipicoIDsFromDB())+"");
				mView.getTableModel().addRow(lModel);
			}
			lModel.setWinValue(Float.parseFloat(arr[0]));
			lModel.setExpenses(Float.parseFloat(arr[1]) + lModel.getExpenses());
			lModel.setDate(LocalDate.now());
			lModel.setPersistenceType(PersistenceType.NEW);
			
			logger.info(lModel.getID() + " : " + lModel.getTeam() + " : " + Float.parseFloat(arr[1]));
			
			lResult = FAMessages.MSG_SUCCESS;
			this.updateTable();
		} else {
			lResult = FAMessages.MSG_CANCEL_POPUP;
		}
		
		mBundesligaListener.actionUpdateConsole(lResult);
	}	
	
	/**
	 * Database function to commit to selected row
	 */	
	private void actionCommit() {
		int lStart = getSelectedRow(); 
		int lEnd = lStart + mView.getTable().getSelectedRowCount();
		
		for(int i = lStart; i<lEnd; i++){
			TipicoModel lModel = mView.getTableModel().getTipicoModelAtRow(i);
			if(insertOrUpdateRowInTipico(lModel))
				lModel.setPersistenceType(PersistenceType.OTHER);
		}
		mBundesligaListener.actionUpdateStatistics(getBalance());
	}	

	/**
	 * Database function to show the db browser
	 */		
	private void actionDBBrowser(){
		TipicoTableModel lModel = new TipicoTableModel();
		
		if (readFromDatabaseToTableModel(lModel, SQLService.SQL_SELECT_ALL_FROM_TIPICO)) {
			PopupFactory.getPopup(PopupType.DATABASE_BROWSER_POPUP, lModel.getAsArray());
			//Popups.startDatabaseBrowser(lModel.getAsList());
		} else
			PopupFactory.getPopup(PopupType.HINT, FAMessages.MSG_NO_DATABASE);
	}

	/**
	 * Database function to remove item from database
	 */		
	private void actionRemove(){
		int lStart = getSelectedRow(); 
		int lEnd = lStart + mView.getTable().getSelectedRowCount();		
		
		for(int i = lStart; i<lEnd; i++){
			TipicoModel lModel = mView.getTableModel().getTipicoModelAtRow(i);
			
			if (!lModel.getPersistenceType().equals(PersistenceType.NEW)){

				if (JOptionPane.showConfirmDialog(null, "Remove from Database ... are you sure?", "WARNING",
				        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				    // yes option
					if(deleteRowInTipico(lModel.getID()))
						lModel.setPersistenceType(PersistenceType.NEW);
				}
			}
		}
		mBundesligaListener.actionUpdateStatistics(getBalance());
	}

	/**
	 * Database function drop and recreate tipico table
	 */		
	private void actionDropCreateTable(){
		if (JOptionPane.showConfirmDialog(null, "Drop and recreate table tipico ... are you sure?", "WARNING",
		        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
		    // yes option
			if(dropTableTipico())
				createTableTipico();
			mBundesligaListener.actionUpdateStatistics(getBalance());
		}		
	}
	
 // ========================
 // END ACTION
 // ========================
 
	
	@Override
	public void print() {
		TipicoPrintService.printTipicoTableModel(mView.getTableModel().getAsList());
	}

 // ========================
 // BEGIN FUNCTION
 // ========================
	public float getBalance(){
		if (mDB != null) {
			mDB.query(SQLService.SQL_SELECT_COMPUTE_BALANCE);

			try{
				return Float.parseFloat(mDB.getNextResult(1));
			} catch (NumberFormatException e){
				return Float.NaN;
			}
		} else {
			return Float.NaN;
		}
	}
	
	public void updateTable(){
		this.mView.updateTable();
		mBundesligaListener.actionUpdateStatistics(getBalance());
	}
	
	public void updateTableInFilterMode(List<TipicoModel> pList){
		if (pList != null){
			mView.getTableModel().setList(pList);
		}
		updateTable();
	}
	
	public void setTipicoModelsToSuccess(List<String> pTipicoIDs){
		List<TipicoModel> lBets = mView.getTableModel().getAsList();
		pTipicoIDs.stream().forEach(lId -> lBets.stream().filter(lModel -> lModel.getID().equals(lId)).forEach(lModel -> lModel.setSuccessTrue()));
	}
 // ========================
 // END FUNCTION
 // ========================
	
	
	
	
}
