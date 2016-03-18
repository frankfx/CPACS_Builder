package de.application;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.business.Database;


public class TipicoActivityBean {
	
	Database db = null;
	
	public TipicoActivityBean() {
		db = new Database();
		if (db.connect()){
			System.out.println("connection successfull");
		} else {
			db = null;
		}
	}
	
	private final static String SQL_CREATE_TABLE_TIPICO = "create table Tipico (tnr int, team varchar(20), winValue float, expenses float, bet float, profit float, Primary Key(tnr));";
	private final static String SQL_DROP_TABLE_TIPICO = "drop table Tipico";
	private final static String SQL_INSERT_ROW_QUERY = "insert into Tipico(tnr, team, winValue, expenses, bet, profit) values (?, ?, ?, ?, ?, ?);";
	
	private PreparedStatement insertBetStmt = null;
	
	
	public boolean createTableTipico(){
		return db.updateDB(SQL_CREATE_TABLE_TIPICO);
	}
	
	public boolean dropTableTipico(){
		return db.updateDB(SQL_DROP_TABLE_TIPICO);
	}
	
	public boolean insertRowInTipico(int pId, String pTeam, float pWinValue, float pExpenses, float pBet, float pProfit){
		try {
			insertBetStmt = db.getConnection().prepareStatement(SQL_INSERT_ROW_QUERY);
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
		return db.updateDB("delete from Tipico where tnr=" + pId);
	}
	
	public boolean updateTeamInTableTipico(int pId, String pTeam){
		return db.updateDB("UPDATE Tipico set team='" + pTeam + "' where tnr=" + pId);  
	}

	public boolean updateWinValueInTableTipico(int pId, float pWinValue){
		return db.updateDB("UPDATE Tipico set winValue=" + pWinValue + " where tnr=" + pId);  
	}	

	public boolean updateExpensesInTableTipico(int pId, float pExpenses){
		return db.updateDB("UPDATE Tipico set expenses=" + pExpenses + " where tnr=" + pId);  
	}		
	
	public boolean updateBetInTableTipico(int pId, float pbet){
		return db.updateDB("UPDATE Tipico set bet=" + pbet + " where tnr=" + pId);  
	}	
	
	public boolean updateProfitInTableTipico(int pId, float pProfit){
		return db.updateDB("UPDATE Tipico set profit=" + pProfit + " where tnr=" + pId);  
	}	
	
	public boolean addToExpenses(int pId, float pSummmand) {
		if(db.query("select expenses from Tipico where tnr=" + pId + ";")){
			try {
				db.getResultSet().next();
				float lResult = db.getResultSet().getFloat(1);
				this.updateExpensesInTableTipico(pId, lResult + pSummmand);
				return true;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}
		return false;
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
	public double computeBetValue(double winValue, double sumOldBet, double odds){
		return odds > 1.0 ? (winValue + sumOldBet) / (odds-1) : -1;
	}
	
	
	public static void main(String[] args) throws SQLException {
		TipicoActivityBean main = new TipicoActivityBean();
		
		if (main.db.connect()){
			
			// create table
			//main.db.updateDB(SQL_CREATE_TABLE_TIPICO);
			
			// drop table
			//main.db.updateDB(SQL_DROP_TABLE_TIPICO);

			// delete row by tnr in table
			//main.deleteRowInTipico(2);
			
			// insert a new row
			//main.insertRowInTipico(2, "HSV", 3.40f, 1.0f, 1.0f, 0.0f);
			//main.insertRowInTipico(2, "Aalen", 3.10f, 1.0f, 1.0f, 0.0f);
			
			// update row
			//main.updateWinValueInTableTipico(1, 3.4f);

			
			//main.addToExpenses(2, 3);
			
			main.updateExpensesInTableTipico(1, 1);
			main.updateExpensesInTableTipico(2, 1);
			
			//select all entries from Tipico
			main.db.query("select * from Tipico;");
//
//			main.db.getResultSet().next();
//			System.out.println(main.db.getResultSet().getInt(1));
//			System.out.println(main.db.getResultSet().getString(2));
//			System.out.println(main.db.getResultSet().getFloat(3));
			
			// print the complete result set
			main.db.printResultSet(6);
			
			main.db.disconnect();
		}
	}
}
