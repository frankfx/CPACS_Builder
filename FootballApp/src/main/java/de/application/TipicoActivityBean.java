package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import de.business.Database;
import de.business.TipicoModel;
import de.business.TipicoTableModel;
import de.presentation.bundesliga.TipicoBetContainer;
import de.presentation.popups.Popup;


public class TipicoActivityBean implements ISubController{

	private final static String SQL_CREATE_TABLE_TIPICO = "create table Tipico (tnr int, team varchar(20), winValue float, expenses float, bet float, profit float, Primary Key(tnr));";
	private final static String SQL_DROP_TABLE_TIPICO = "drop table Tipico";
	private final static String SQL_INSERT_ROW_QUERY = "insert into Tipico(tnr, team, winValue, expenses, bet, profit) values (?, ?, ?, ?, ?, ?);";
	
	private PreparedStatement insertBetStmt = null;
		
	private BundesligaActivityBean mBundesligaListener;
	
	private Database mDB = null;
	private TipicoModel mModel;
	private TipicoBetContainer mView;

	
	public TipicoActivityBean(TipicoModel pModel, TipicoBetContainer pView) {
		this.mModel = pModel;
		this.mView = pView;

		this.addListener();
	}

	@Override
	public void initBean(String [] pCredentials) {
		if (mDB == null){
			if(initDB(pCredentials[0], pCredentials[1], pCredentials[2], pCredentials[3], pCredentials[4])){
				System.out.println("connection successfull");
				initTable();
			}
			else{
				System.out.println("connection refused");
			}
		} else {
			System.out.println("already connected");
		}
	}
	
	@Override
	public void updateBean(){
		if (mDB != null){
			mDB.disconnect();
			mDB = null;
		}
	}
		
	public boolean initDB(String pHost, String pPort, String pDatabase, String pUser, String pPass){
		mDB = new Database();
		
		if (mDB.connect(pHost, pPort, pDatabase, pUser, pPass)){
			return true;
		} else {
			mDB = null;
			return false;
		}
	}
	
	public void setUpdateListener(BundesligaActivityBean pListener){
		this.mBundesligaListener = pListener;
	}
	
	public boolean createTableTipico(){
		return mDB != null ? mDB.updateDB(SQL_CREATE_TABLE_TIPICO) : false;
	}
	
	public boolean dropTableTipico(){
		return mDB != null ? mDB.updateDB(SQL_DROP_TABLE_TIPICO) : false;
	}
	
	public boolean insertRowInTipico(int pId, String pTeam, float pWinValue, float pExpenses, float pBet, float pProfit){
		if(mDB == null)
			return false;
		
		try {
			insertBetStmt = mDB.getConnection().prepareStatement(SQL_INSERT_ROW_QUERY);
			insertBetStmt.setInt(1, pId);
			insertBetStmt.setString(2, pTeam);
			insertBetStmt.setFloat(3, pWinValue);
			insertBetStmt.setFloat(4, pExpenses);
			insertBetStmt.setFloat(5, pBet);
			insertBetStmt.setFloat(6, pProfit);
			
			insertBetStmt.execute();	
			System.out.println("update successfull");
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public boolean deleteRowInTipico(int pId){
		return mDB != null ? mDB.updateDB("delete from Tipico where tnr=" + pId) : false;
	}
	
	public boolean updateTeamInTableTipico(int pId, String pTeam){
		return mDB != null ? mDB.updateDB("UPDATE Tipico set team='" + pTeam + "' where tnr=" + pId) : false;  
	}

	public boolean updateWinValueInTableTipico(int pId, float pWinValue){
		return mDB != null ? mDB.updateDB("UPDATE Tipico set winValue=" + pWinValue + " where tnr=" + pId) : false;  
	}	

	public boolean updateExpensesInTableTipico(int pId, float pExpenses){
		return mDB != null ? mDB.updateDB("UPDATE Tipico set expenses=" + pExpenses + " where tnr=" + pId) : false;  
	}		
	
	public boolean updateBetInTableTipico(int pId, float pbet){
		return mDB != null ? mDB.updateDB("UPDATE Tipico set bet=" + pbet + " where tnr=" + pId) : false;  
	}	
	
	public boolean updateProfitInTableTipico(int pId, float pProfit){
		return mDB != null ? mDB.updateDB("UPDATE Tipico set profit=" + pProfit + " where tnr=" + pId) : false;  
	}	
	
	public boolean updateDBWithModel(){
		return mDB != null ? mDB.updateDB("UPDATE Tipico set team='" + mModel.getTeam() + "' , winValue=" + mModel.getWinValue() 
						+ " , expenses=" + mModel.getExpenses() + " , attempts=" + mModel.getAttempts() 
						+ " , pDate='" + mModel.getSQLDate() + "' , success=" + mModel.getSuccess() 
						+ " where tnr=" + mModel.getTnr() + ";") : false;
	}
	
	public boolean addToExpenses(int pId, float pSummmand) {
		if (mDB == null) return false;
		
		if(mDB.query("select expenses from Tipico where tnr=" + pId + ";")){
			try {
				mDB.getResultSet().next();
				float lResult = mDB.getResultSet().getFloat(1);
				this.updateExpensesInTableTipico(pId, lResult + pSummmand);
				return true;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}
		return false;
	}
	
	private void addListener() {
		
		this.mView.setButtonNewBetListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionNew();
			}
		});		
		
		this.mView.setButtonLadenListener(new ActionListener() {
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
				mView.getTableModel().removeRow(mView.getTable().getSelectedRow());
				mView.updateTable();
			}
		});
	}
	
	public void initTable(){
		if (mDB != null){
			mDB.query("select * from Tipico;");
	
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
					
					mView.getTableModel().addRow(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			
			mView.getTable().setModel(mView.getTableModel());
			mView.getTable().updateUI();
		}
	}
	

	
	
	public void actionModify(){
		if(mDB != null){
			int lTnr = getTnrSelectRow();
			
			if(lTnr == -1)
				return;
			
			mDB.query("select * from Tipico where tnr=" + lTnr + ";");
			try {
				mDB.getResultSet().next();
				
				Popup.setPopupInputValues(lTnr, mDB.getResultSet().getString(2), 
						 mDB.getResultSet().getFloat(3), 
						 mDB.getResultSet().getFloat(4),
						 mDB.getResultSet().getInt(5), 
						 mDB.getResultSet().getDate(6).toLocalDate(), 
						 mDB.getResultSet().getBoolean(7), false);
	
				boolean validData = startInputPopup();
					
				Popup.resetInputValues();		
							
				if (validData && updateDBWithModel()){
					updateTableData();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void updateTableData(){
		TipicoTableModel lModel = mView.getTableModel();
		
		for(int row=0; row < lModel.getRowCount(); row++){
			if(Float.parseFloat(lModel.getValueAt(row, 0).toString()) == mModel.getTnr()){
				lModel.setValueAt(mModel.getTeam(), row, 1);
				lModel.setValueAt(mModel.getWinValue(), row, 2);
				lModel.setValueAt(mModel.getExpenses(), row, 3);
				lModel.setValueAt(mModel.getAttempts(), row, 4);
				lModel.setValueAt(mModel.getDate(), row, 5);
				lModel.setValueAt(mModel.getSuccess(), row, 6);
			}
		}

		mView.updateTable();
		mBundesligaListener.actionUpdateConsole("Table updated");
	}
	
	
	private void actionBetValue(){
		TipicoTableModel lTableModel = mView.getTableModel();

		int rowIndex = getTnrSelectRow()-1;

		if(rowIndex == -1)
			return;
			
		float winValue = (float) lTableModel.getValueAt(rowIndex, 2);
		float expenses = (float) lTableModel.getValueAt(rowIndex, 3);
				
		String [] arr = Popup.startTipicoPopupBetValue(winValue);
			
		if(arr != null)
			mBundesligaListener.actionUpdateConsole(""+computeBetValue(Float.parseFloat(arr[1]), expenses, Float.parseFloat(arr[0])));
		else
			mBundesligaListener.actionUpdateConsole("No row selected");
	}

	private int getTnrSelectRow(){
		int lSelectedRow = mView.getTable().getSelectedRow();
		
		if(lSelectedRow < 0){
			Popup.startHintPopup("No row selected");
			return -1;
		}
			
		return Integer.parseInt(mView.getTable().getModel().getValueAt(lSelectedRow, 0).toString());		
	}
	
	public void actionNew(){
		startInputPopup();
	}
	
	private boolean startInputPopup(){
		String [] res = Popup.startTipicoPopupNew();
		if (res == null)
			return false;
		
		if (!parseNewTipicoEntry(res)){
			Popup.startErrorPopup("Wrong input");
			return false;
		}		
		return true;
	}
	
	private boolean parseNewTipicoEntry(String [] args){
		if(args == null)
			return false;
		try{
			TipicoModel lModel = new TipicoModel();
			
			System.out.println("1");
			lModel.setTnr(Integer.parseInt(args[0]));
			lModel.setTeam(args[1]);
			lModel.setWinValue(Float.parseFloat(args[2]));
			System.out.println("2");
			lModel.setExpenses(Float.parseFloat(args[3]));
			System.out.println("3" + args[4]);
			lModel.setAttempts(Integer.parseInt(args[4]));
			System.out.println("noch ok");
			lModel.setDate(LocalDate.parse(args[5]));
			System.out.println("ende");
			lModel.setSuccess(Boolean.parseBoolean(args[6]));

			this.mModel = lModel;
			return true;
		} catch (Exception e){
			mBundesligaListener.actionUpdateConsole(e.getMessage());
			return false;
		}
	}	

	/**
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

	
	
//			if (main.mDB.connect()){
//			// create table
//			//main.db.updateDB(SQL_CREATE_TABLE_TIPICO);
//			
//			// drop table
//			//main.db.updateDB(SQL_DROP_TABLE_TIPICO);
//
//			// delete row by tnr in table
//			//main.deleteRowInTipico(2);
//			
//			// insert a new row
//			//main.insertRowInTipico(2, "HSV", 3.40f, 1.0f, 1.0f, 0.0f);
//			
//			// update row
//			//main.updateWinValueInTableTipico(1, 3.4f);
//			
//			//main.addToExpenses(2, 3);
//			
//			main.updateExpensesInTableTipico(1, 1);
//			main.updateExpensesInTableTipico(2, 1);
//			
//			//select all entries from Tipico
//			main.mDB.query("select * from Tipico;");
////
////			main.db.getResultSet().next();
////			System.out.println(main.db.getResultSet().getInt(1));
////			System.out.println(main.db.getResultSet().getString(2));
////			System.out.println(main.db.getResultSet().getFloat(3));
//			
//			// print the complete result set
//			main.mDB.printResultSet(6);
//			
//			main.mDB.disconnect();
}
