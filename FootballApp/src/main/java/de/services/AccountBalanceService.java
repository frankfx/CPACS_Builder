package de.services;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountBalanceService {
	
	/**
	 * updates the default account balance value
	 * 
	 * @param pDB
	 * @param pBalance
	 */
	public static void updateDefaultBalanceValue(Database pDB, float pBalance) {
		if (pDB != null && pDB.isConnected()){
			PreparedStatement prepStmt;
			try {
				prepStmt = pDB.getConnection().prepareStatement(SQLService.SQL_UPDATE_BALANCE);
				prepStmt.setFloat(1, pBalance);
				prepStmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
	}
	
	/**
	 * returns the current balance if possible
	 * 
	 * @param pDB
	 * @param pBalance
	 */
	public static float getBalance(Database pDB){
		if (pDB != null && pDB.isConnected()){
			pDB.query(SQLService.SQL_SELECT_COMPUTE_BALANCE);

			try{
				return Float.parseFloat(pDB.getNextResult(1));
			} catch (NumberFormatException e){
				return Float.NaN;
			}
		} else {
			return Float.NaN;
		}
	}

	/**
	 * returns the default balance value
	 * 
	 * @param pDB
	 * @param pBalance
	 */	
	public static float getDefaultBalanceValue(Database pDB){
		if (pDB != null && pDB.isConnected()){
			pDB.query(SQLService.SQL_SELECT_BALANCE);

			try{
				return Float.parseFloat(pDB.getNextResult(1));
			} catch (NumberFormatException e){
				return Float.NaN;
			}
		} else {
			return Float.NaN;
		}
	}	
}
