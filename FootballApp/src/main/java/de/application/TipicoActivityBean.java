package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import de.business.Database;
import de.business.TipicoModel;
import de.business.TipicoTableModel;
import de.presentation.bundesliga.TipicoBetContainer;
import de.presentation.popups.Popup;
import de.utils.FAMessages;
import de.utils.PersistenceType;


public class TipicoActivityBean implements ISubController{

	private final static String SQL_CREATE_TABLE_TIPICO = "create table Tipico (tnr int, team varchar(20), winValue float, expenses float, attempts int, pDate date, success boolean, Primary Key(tnr));";
	private final static String SQL_DROP_TABLE_TIPICO = "drop table Tipico";
	private final static String SQL_INSERT_ROW_QUERY = "insert into Tipico(tnr, team, winValue, expenses, attempts, pDate, success) values (?, ?, ?, ?, ?, ?, ?);";
	private final static String SQL_UPDATE_ROW_QUERY = "update Tipico set team=? , winValue=? , expenses=? , attempts=? , pDate=? , success=? where tnr=?";
	private final static String SQL_INSERT_UPDATE_ROW_QUERY = "insert into Tipico(tnr, team, winValue, expenses, attempts, pDate, success) values (?, ?, ?, ?, ?, ?, ?) on duplicate key update team=? , winValue=? , expenses=? , attempts=? , pDate=? , success=?;";
	private final static String SQL_SELECT_ALL_FROM_TIPICO_QUERY = "select * from Tipico;";
	
	
	private PreparedStatement mInsertBetStmt = null;
	private BundesligaActivityBean mBundesligaListener;
	private Database mDB = null;
	private TipicoBetContainer mView;

	public TipicoActivityBean(TipicoBetContainer pView) {
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
				initTable();
			}
		});	
			
		this.mView.setButtonRevertListerner(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("revert");
			}
		});		
		
		this.mView.setButtonDBBrowserListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionDBBrowser();
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
			if(initDB(pCredentials[0], pCredentials[1], pCredentials[2], pCredentials[3], pCredentials[4]))
				lResult = "connection successfull";
			else
				lResult = "connection refused";
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
		return mDB != null ? mDB.updateDB(SQL_CREATE_TABLE_TIPICO) : false;
	}

	/**
	 * drops the complete tipico table
	 * 
	 * @return true if the table was successful dropped otherwise false
	 */		
	public boolean dropTableTipico(){
		return mDB != null ? mDB.updateDB(SQL_DROP_TABLE_TIPICO) : false;
	}
	
	/**
	 * deletes a row given by a id (tnr) in table tipico
	 * 
	 * @param pTnr the unique id of the model
	 * 
	 * @return true if the row was successful removed otherwise false
	 */		
	public boolean deleteRowInTipico(int pTnr){
		return mDB != null ? mDB.updateDB("delete from Tipico where tnr=" + pTnr) : false;
	}	

	/**
	 * inserts a model (row) to the database
	 * 
	 * @param pTipicoModel a model with a new id (tnr)
	 * 
	 * @return true if the model was successful inserted otherwise false
	 */		
	public boolean insertRowInTipico(TipicoModel pTipicoModel){
		return this.setModelToTipico(SQL_INSERT_ROW_QUERY, pTipicoModel.getTnr(), pTipicoModel.getTeam(), pTipicoModel.getWinValue(), 
				pTipicoModel.getExpenses(), pTipicoModel.getAttempts(), pTipicoModel.getDate(), pTipicoModel.getSuccess());
	}

	/**
	 * updates a model (row) to the database
	 * 
	 * @param pTipicoModel a model with a existing id (tnr) in the database
	 * 
	 * @return true if the model was successful updated otherwise false
	 */			
	public boolean updateRowInTipico(TipicoModel pTipicoModel){
		return this.setModelToTipico(SQL_UPDATE_ROW_QUERY, pTipicoModel.getTnr(), pTipicoModel.getTeam(), pTipicoModel.getWinValue(), 
				pTipicoModel.getExpenses(), pTipicoModel.getAttempts(), pTipicoModel.getDate(), pTipicoModel.getSuccess());
	}	

	/**
	 * inserts a model to the database, if the model key doesn't already exist otherwise the existing model will be updated
	 * 
	 * @param pTipicoModel the model to insert or update in the database
	 * 
	 * @return true if the model was successful inserted/updated otherwise false
	 */		
	public boolean insertOrUpdateRowInTipico(TipicoModel pTipicoModel){
		return this.setModelToTipico(SQL_INSERT_UPDATE_ROW_QUERY, pTipicoModel.getTnr(), pTipicoModel.getTeam(), pTipicoModel.getWinValue(), 
				pTipicoModel.getExpenses(), pTipicoModel.getAttempts(), pTipicoModel.getDate(), pTipicoModel.getSuccess());
	}
	
	/**
	 * intern private helper method to insert or update a model to the database 
	 */		
	private boolean setModelToTipico(String pSql , int pId, String pTeam, float pWinValue, float pExpenses, int pAttempts, LocalDate pDate, boolean pSuccess){
		String lResult;
		
		if(mDB == null)
			lResult = FAMessages.MESSAGE_NO_DATABASE;
		else{
			try {
				mInsertBetStmt = mDB.getConnection().prepareStatement(pSql);

				mInsertBetStmt.setInt(1, pId);
				mInsertBetStmt.setString(2, pTeam);
				mInsertBetStmt.setFloat(3, pWinValue);
				mInsertBetStmt.setFloat(4, pExpenses);
				mInsertBetStmt.setInt(5, pAttempts);
				mInsertBetStmt.setDate(6, Date.valueOf(pDate));
				mInsertBetStmt.setBoolean(7, pSuccess);
				mInsertBetStmt.setString(8, pTeam);	
				mInsertBetStmt.setFloat(9, pWinValue);
				mInsertBetStmt.setFloat(10, pExpenses);
				mInsertBetStmt.setInt(11, pAttempts);
				mInsertBetStmt.setDate(12, Date.valueOf(pDate));
				mInsertBetStmt.setBoolean(13, pSuccess);
				
				mInsertBetStmt.execute();	
				mBundesligaListener.actionUpdateConsole("update successfull");
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
	public void initTable(){
		if(readFromDatabaseToTableModel(mView.getTableModel()))
			mView.getTable().updateUI();
	}	
	
	private boolean readFromDatabaseToTableModel(TipicoTableModel pModel){
		if (mDB != null && mDB.isConnected()){
			mDB.query(SQL_SELECT_ALL_FROM_TIPICO_QUERY);
	
			try {
				TipicoModel data;
				while(mDB.getResultSet().next()){
					data = new TipicoModel();
					data.setTnr(mDB.getResultSet().getInt(1));
					data.setTeam(mDB.getResultSet().getString(2));
					data.setWinValue(mDB.getResultSet().getFloat(3));
					data.setExpenses(mDB.getResultSet().getFloat(4));
					data.setAttempts(mDB.getResultSet().getInt(5));
					data.setDate(mDB.getResultSet().getDate(6));
					data.setSuccess(mDB.getResultSet().getBoolean(7));
					data.setPersistenceType(PersistenceType.OTHER);
					
					pModel.addRow(data);
				}
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
			Popup.startHintPopup("No row selected");
			return -1;
		}
			
		return Integer.parseInt(mView.getTable().getModel().getValueAt(lSelectedRow, 0).toString());		
	}
	
	/**
	 * get index of the selected table row
	 */		
	private int getSelectedRow(){
		int lSelectedRow = mView.getTable().getSelectedRow();
		
		if(lSelectedRow < 0){
			Popup.startHintPopup("No row selected");
			return -1;
		}
			
		return lSelectedRow;		
	}
	
 // ============================
 // END LOCAL TABLE OPERATIONS
 // ============================		

		
	/**
	 * private helper method to start an input dialog to manipulate the table attributes
	 */	
	private boolean startTableInputPopup(TipicoModel pTipicoModel, boolean pIDEnable){
		String lResult;
		
		if (pTipicoModel == null){
			lResult = FAMessages.MESSAGE_NO_VALID_TABLE_MODEL + -1;
		} else{
			String [] lValues = Popup.startTipicoPopupNew(pTipicoModel, pIDEnable);
			if (lValues == null){
				return false;
			} else if(!updateTipicoEntry(pTipicoModel, lValues)){
				lResult = FAMessages.MESSAGE_NO_VALID_FORMULAR_DATA;
			} else
				return true;
		}
		
		Popup.startErrorPopup(lResult);
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
	private boolean updateTipicoEntry(TipicoModel pTipicoModel, String [] args){
		if(args == null || pTipicoModel == null)
			return false;
		
		pTipicoModel.setTnr(Integer.parseInt(args[0]));
		pTipicoModel.setTeam(args[1]);
		pTipicoModel.setWinValue(Float.parseFloat(args[2]));
		pTipicoModel.setExpenses(Float.parseFloat(args[3]));
		pTipicoModel.setAttempts(Integer.parseInt(args[4]));
		pTipicoModel.setDate(LocalDate.parse(args[5]));
		pTipicoModel.setSuccess(Boolean.parseBoolean(args[6]));
		pTipicoModel.setPersistenceType(PersistenceType.NEW);
		return true;
	}	

	/**
	 * This function determines the new bet value
	 * 
	 * @param winValue the amount we want to win for one bet
	 * @param sumOldBet the amount we have lost in previous bets
	 * @param odds the rate of the game for a given result
	 * 
	 * @return the new bet value to win a specific amount and the whole stake (sum of this and all previous bets)
	 * 
	 * Formula: winValue + sumOldBet + newBet = newBet * odds	
	 *  		winValue + sumOldBet = newBet * odds - newBet
	 *  		winValue + sumOldBet = newBet * (odds-1)
	 *  		
	 *  		newBet = (winValue + sumOldBet) / (odds-1)
	 * 
	 */
	public static double computeBetValue(double winValue, double sumOldBet, double odds){
		return odds > 1.0 ? (winValue + sumOldBet) / (odds-1) : -1;
	}
	
	
 // ========================
 // BEGIN ACTION
 // ========================
	
	/**
	 * Local table function to create a new entry
	 */		
	private void actionNew(){
		TipicoModel lModel = new TipicoModel();
		lModel.setTnr(mView.getTableModel().generateValidID());
		
		System.out.println("out of if");
		
		if(startTableInputPopup(lModel, true)){
			
			System.out.println("in if : " + lModel);
			mView.getTableModel().addRow(lModel);
			mView.updateTable();
		}
		
		System.out.println(mView.getTableModel().getAsList());
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
			mView.updateTable();
			mBundesligaListener.actionUpdateConsole("Table updated");			
		}
		
	}	

	/**
	 * Local table function to compute the bet value
	 */		
	private void actionBetValue(){
		String lResult;
		
		int lRow = getSelectedRow();
		TipicoModel lModel = mView.getTableModel().getTipicoModelAtRow( lRow);		
		
		if(lModel == null)
			lResult = FAMessages.MESSAGE_NO_VALID_TABLE_MODEL + (lRow);
		else{
			String [] arr = Popup.startTipicoPopupBetValue(lModel.getWinValue());
			if(arr != null)
				lResult = ""+computeBetValue(Float.parseFloat(arr[1]), lModel.getExpenses(), Float.parseFloat(arr[0]));
			else
				lResult = FAMessages.MESSAGE_NO_VALID_FORMULAR_DATA;
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
	}	

	/**
	 * Database function to delete to selected row
	 */		
	private void actionDelete(){
		int row = getSelectedRow();
		
		mView.getTableModel().removeRow(row);
		
		if(mView.getTable().getRowCount() == 0)
			mView.getTable().getSelectionModel().clearSelection();
		
		mView.updateTable();		
	}

	/**
	 * Database function to show the db browser
	 */		
	private void actionDBBrowser(){
		TipicoTableModel lModel = new TipicoTableModel();
		
		if(readFromDatabaseToTableModel(lModel)){
			Popup.startDatabaseBrowser(lModel.getAsList());
		} else
			Popup.startHintPopup(FAMessages.MESSAGE_NO_DATABASE);
	}
	
 // ========================
 // END ACTION
 // ========================
}
