package de.services;

public class SQLService {
	public final static String SQL_SELECT_ALL_FROM_TIPICO = "select * from Tipico;";
	public final static String SQL_SELECT_TNR_FROM_TIPICO = "select tnr from Tipico;";	
	public final static String SQL_SELECT_OPEN_TNR_FROM_TIPICO = "select tnr from Tipico where success=false;";	
	public final static String SQL_SELECT_OPEN_GAMES = "select * from Tipico where success=false;";
//	public final static String SQL_SELECT_COMPUTE_BALANCE = "select 127.26 + ifnull(sum(t.winValue), 0) - (select ifnull(sum(t1.expenses), 0) from Tipico t1 where success=0) from Tipico t where success=1;";

	public final static String SQL_SELECT_COMPUTE_BALANCE = "select (select accountBalance from TipicoConfig) + ifnull(sum(t.winValue), 0) - (select ifnull(sum(t1.expenses), 0) from Tipico t1 where success=0) from Tipico t where success=1;";
	public final static String SQL_UPDATE_BALANCE = "update TipicoConfig set accountBalance=?;";
	public final static String SQL_SELECT_BALANCE = "select accountBalance from TipicoConfig;";
	
	public final static String SQL_INSERT_ROW = "insert into Tipico(tnr, team, betPrediction, winValue, expenses, pDate, success, description) values (?, ?, ?, ?, ?, ?, ?, ?);";
	public final static String SQL_UPDATE_ROW = "update Tipico set team=? , betPrediction=? , winValue=? , expenses=? , pDate=? , success=? , description=? where tnr=?";
	public final static String SQL_INSERT_UPDATE_ROW = "insert into Tipico(tnr, team, betPrediction, winValue, expenses, pDate, success, description) values (?, ?, ?, ?, ?, ?, ?, ?) on duplicate key update team=? , betPrediction=? , winValue=? , expenses=? , pDate=? , success=?, description=?;";

	public final static String SQL_CREATE_TABLE_TIPICO = "create table Tipico (tnr int, team varchar(20), betPrediction varchar(20), winValue float, expenses float, pDate date, success boolean, description varchar(100), Primary Key(tnr));";
	public final static String SQL_DROP_TABLE_TIPICO = "drop table if exists Tipico";

	public final static String SQL_REMOVE_TIPICO = "delete from Tipico where tnr=?";
	
	public final static String SQL_CHECK_ID_EXISTS = "SELECT 1 FROM Tipico WHERE tnr=?";
	
}