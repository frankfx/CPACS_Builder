package de.application;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import de.business.Database;
import de.business.TipicoModel;
import de.presentation.bundesliga.TipicoBetContainer;
import de.presentation.popups.Popup;


public class TipicoActivityBean {

	private final static String SQL_CREATE_TABLE_TIPICO = "create table Tipico (tnr int, team varchar(20), winValue float, expenses float, bet float, profit float, Primary Key(tnr));";
	private final static String SQL_DROP_TABLE_TIPICO = "drop table Tipico";
	private final static String SQL_INSERT_ROW_QUERY = "insert into Tipico(tnr, team, winValue, expenses, bet, profit) values (?, ?, ?, ?, ?, ?);";
	
	private PreparedStatement insertBetStmt = null;
		
	
	private Database mDB = null;
	private TipicoModel mModel;
	private TipicoBetContainer mView;

	public TipicoActivityBean(TipicoModel pModel, TipicoBetContainer pView) {
		this.mModel = pModel;
		this.mView = pView;

		mDB = new Database();
		if (mDB.connect()){
			System.out.println("connection successfull");
		} else {
			mDB = null;
		}
		
		this.initTable();
		this.addListener();
	}
	
	public boolean createTableTipico(){
		return mDB.updateDB(SQL_CREATE_TABLE_TIPICO);
	}
	
	public boolean dropTableTipico(){
		return mDB.updateDB(SQL_DROP_TABLE_TIPICO);
	}
	
	public boolean insertRowInTipico(int pId, String pTeam, float pWinValue, float pExpenses, float pBet, float pProfit){
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
		return mDB.updateDB("delete from Tipico where tnr=" + pId);
	}
	
	public boolean updateTeamInTableTipico(int pId, String pTeam){
		return mDB.updateDB("UPDATE Tipico set team='" + pTeam + "' where tnr=" + pId);  
	}

	public boolean updateWinValueInTableTipico(int pId, float pWinValue){
		return mDB.updateDB("UPDATE Tipico set winValue=" + pWinValue + " where tnr=" + pId);  
	}	

	public boolean updateExpensesInTableTipico(int pId, float pExpenses){
		return mDB.updateDB("UPDATE Tipico set expenses=" + pExpenses + " where tnr=" + pId);  
	}		
	
	public boolean updateBetInTableTipico(int pId, float pbet){
		return mDB.updateDB("UPDATE Tipico set bet=" + pbet + " where tnr=" + pId);  
	}	
	
	public boolean updateProfitInTableTipico(int pId, float pProfit){
		return mDB.updateDB("UPDATE Tipico set profit=" + pProfit + " where tnr=" + pId);  
	}	
	
	public boolean updateDBWithModel(){
		return mDB.updateDB("UPDATE Tipico set team='" + mModel.getTeam() + "' , winValue=" + mModel.getWinValue() 
						+ " , expenses=" + mModel.getExpenses() + " , bet=" + mModel.getBet() 
						+ " , profit=" + mModel.getProfit() + " where tnr=1;");
	}
	
	public boolean addToExpenses(int pId, float pSummmand) {
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
				actionLoad();
			}
		});
	}
	
	public void initTable(){
		DefaultTableModel lModel = new DefaultTableModel();
		
		
		
		String[] columnNames = {"ID", "TEAM", "WINVALUE", "EXPENSES", "BET", "PROFIT", "SUCCESSFUL"};
		
		lModel.setColumnIdentifiers(columnNames);
		
		Object[] data = new Object[7];
		
		mDB.query("select * from Tipico;");

		
		
		try {
			while(mDB.getResultSet().next()){
				data[0] = mDB.getResultSet().getInt(1);
				data[1] = mDB.getResultSet().getString(2);
				data[2] = mDB.getResultSet().getFloat(3);
				
				data[3] = mDB.getResultSet().getFloat(4);
				data[4] = mDB.getResultSet().getFloat(5);
				data[5] = mDB.getResultSet().getFloat(6);
				data[6] = true;
				
				lModel.addRow(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		
		mView.getTable().setModel(lModel);
	}
	
	
	
	
	public void actionLoad(){
		int lSelectedRow = mView.getTable().getSelectedRow();
		
		if(lSelectedRow < 0){
			Popup.startHintPopup("No row selected");
			return;
		}
			
		int lTnr = Integer.parseInt(mView.getTable().getModel().getValueAt(lSelectedRow, 0).toString());
		
		mDB.query("select * from Tipico where tnr=" + lTnr + ";");
		try {
			mDB.getResultSet().next();
			
			Popup.setPopupInputValues(1, mDB.getResultSet().getString(2), 
					 mDB.getResultSet().getFloat(3), 
					 mDB.getResultSet().getFloat(4),
					 mDB.getResultSet().getFloat(5), 
					 mDB.getResultSet().getFloat(6), false);

			boolean validData = startInputPopup();
				
			Popup.resetPopupInputValues();		
						
			if (validData)
				updateDBWithModel();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void actionNew(){
		startInputPopup();
	}
	
	private boolean startInputPopup(){
		String [] res = Popup.startTipicoPopupNew();
		if (res == null)
			return false;
		
		if (!parseNewTipicoEntry(res)){
			Popup.startPopupError("Wrong input");
			return false;
		}		
		return true;
	}
	
	private boolean parseNewTipicoEntry(String [] args){
		if(args == null)
			return false;
		try{
			TipicoModel lModel = new TipicoModel();
			
			lModel.setTnr(Integer.parseInt(args[0]));
			lModel.setTeam(args[1]);
			lModel.setWinValue(Float.parseFloat(args[2]));
			lModel.setExpenses(Float.parseFloat(args[3]));
			lModel.setBet(Float.parseFloat(args[4]));
			lModel.setProfit(Float.parseFloat(args[5]));
			
			this.mModel = lModel;
			return true;
		} catch (Exception e){
			System.out.println(e.getMessage());
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
	
	
//	public static void main(String[] args) throws SQLException {
//		
//		
//		System.out.println(computeBetValue(3.4, 2.91, 3.3));
		
//		TipicoActivityBean main = new TipicoActivityBean();
//		
//		if (main.mDB.connect()){
//			
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
//			//main.insertRowInTipico(2, "Aalen", 3.10f, 1.0f, 1.0f, 0.0f);
//			
//			// update row
//			//main.updateWinValueInTableTipico(1, 3.4f);
//
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
//		}
//	}
}
