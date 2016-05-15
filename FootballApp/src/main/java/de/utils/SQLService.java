package de.utils;

public class SQLService {

	public final static String SQL_CREATE_TABLE_TIPICO = "create table Tipico (tnr int, team varchar(20), winValue float, expenses float, attempts int, pDate date, success boolean, Primary Key(tnr));";
	public final static String SQL_DROP_TABLE_TIPICO = "drop table if exists Tipico";
	public final static String SQL_INSERT_ROW_QUERY = "insert into Tipico(tnr, team, winValue, expenses, attempts, pDate, success) values (?, ?, ?, ?, ?, ?, ?);";
	public final static String SQL_UPDATE_ROW_QUERY = "update Tipico set team=? , winValue=? , expenses=? , attempts=? , pDate=? , success=? where tnr=?";
	public final static String SQL_INSERT_UPDATE_ROW_QUERY = "insert into Tipico(tnr, team, winValue, expenses, attempts, pDate, success) values (?, ?, ?, ?, ?, ?, ?) on duplicate key update team=? , winValue=? , expenses=? , attempts=? , pDate=? , success=?;";
	public final static String SQL_SELECT_ALL_FROM_TIPICO_QUERY = "select * from Tipico;";
	public final static String SQL_SELECT_TNR_FROM_TIPICO_QUERY = "select tnr from Tipico;";
	public final static String SQL_COMPUTE_BALANCE = "select 41.1 + sum(t.winValue) - (select sum(t1.expenses) from Tipico t1 where success=0) from Tipico t where success=1;";

	public final static String SQL_PULLDETAILS_OPEN = "select * from Tipico where success=false;";
}
