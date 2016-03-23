package de.business;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.ResultSetMetaData;

public class Database {
	
	// JDBC driver name and database url
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/TestData";
	
	// Database credentials
	static final String USER = "root";
	static final String PASS = "130386";
	
	// Connection and result set
	private Connection mConnection = null;
	private Statement mStatement = null;
	private ResultSet mResultSet = null;

	/** Constructor */	
	public Database() {}
	
	/**
	 * connect to specified database and create connection object
	 * 
	 * @return true if the connection was established otherwise false
	 */	
	public boolean connect(){
		try{
			// STEP 2: Register JDBC driver (/usr/share/java/mysql)
			Class.forName("com.mysql.jdbc.Driver");		
			// STEP 3: Open connection
			mConnection = DriverManager.getConnection(DB_URL, USER, PASS);		
			return true;
		} catch(SQLException e){
			System.out.println("ERROR: SQL-EXCEPTION");
		} catch(ClassNotFoundException e){
			System.out.println("MYSQL-DRIVER not found");
		}
		return false;
	}

	/**
	 * close the current database connection
	 * 
	 * @return true if the connection was successfully closed otherwise false
	 */
	public boolean disconnect(){
		if (mConnection == null)
			return true;
		else 
			try{
				mConnection.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return false;
	}
	
	
	/**
	 * queries the SQL statement and updates the ResultSet object
	 *
	 * @param sql statement to query e.g. "select * from Persons"
	 * @return true if the query was successful otherwise false
	 */
	public boolean query(String sql){
		try {
			mStatement = mConnection.createStatement();
			mResultSet = mStatement.executeQuery(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return false;
	}
	
	/**
	 * updates the database
	 *
	 * @param sql statement to query e.g. "select * from Persons"
	 * @return true if the database update was successful otherwise false
	 */	
	public boolean updateDB(String sql){
		try {
			mStatement = mConnection.createStatement();
			mStatement.executeUpdate(sql);
			System.out.println("update succesful");
			return true;
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
		}		
		return false;
	}
	
	/**
	 * Determine the the column size of the ResultSet
	 *
	 * @return the number of columns of the table or -1 if no columns are available
	 */	
	public int getNumberOfColums() {
		ResultSetMetaData metadata;
		try {
			metadata = (ResultSetMetaData) mResultSet.getMetaData();
			return metadata.getColumnCount();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return -1;
	}

	/**
	 * prints the complete ResultSet.
	 *
	 * @param numberOfColumns the number of columns which should be printed
	 */		
	public void printResultSet(int numberOfColumns){
		if (mResultSet == null)
			return;
		
		StringBuilder sb = new StringBuilder();
		
		try {
			while (mResultSet.next()){
				sb.append("{");	
				
				for (int i = 1; i < numberOfColumns; i++) {
					sb.append(mResultSet.getString(i));
					sb.append(", ");
				}
				
				sb.append(mResultSet.getString(numberOfColumns));
				sb.append("}\n");
			}
			System.out.println(sb.toString());
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the next row (result) of the ResultSet.
	 *
	 * @param numberOfColumns the number of columns which should be printed
	 * @return the next row as a String object if exists otherwise null
	 */		
	public String getNextResult(int numberOfColumns){
		
		if (mResultSet == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		
		try {
			if (mResultSet.next()){
				
				for (int i = 1; i < numberOfColumns; i++) {
					sb.append(mResultSet.getString(i));
					sb.append(", ");
				}
				sb.append(mResultSet.getString(numberOfColumns));
				sb.append("}");
				
				return sb.toString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Connection getConnection() {
		return mConnection;
	}

	public void setConnection(Connection lConnection) {
		this.mConnection = lConnection;
	}

	public ResultSet getResultSet() {
		return mResultSet;
	}

	public void setResultSet(ResultSet lResultSet) {
		this.mResultSet = lResultSet;
	}	
	
	public static void main(String[] args) throws SQLException {
		Database db = new Database();
		if (db.connect()){
		//	db.updateDB("drop table Tipico");
			
		//	db.updateDB("create table Tipico (tnr int, team varchar(20), winValue decimal, expenses decimal, bet decimal, profit decimal, Primary Key(tnr));");
			
		//	db.updateDB("insert into Tipico(tnr, team, winValue, expenses, bet, profit) values (1, 'HSV', 3.40, 1.0, 1.0, 0.0);");
		//	db.query("select * from Team;");
			
			// print the complete result set
		//	db.printResultSet(3);
			
			// set the cursor back to the first row
		//	db.rs.beforeFirst();
			
			// print the first row 
		//	System.out.println(db.getNextResult(db.getNumberOfColums()));
			
			db.disconnect();
		}
	}
}