package de;

import de.services.Database;

public class Context {

	public Database mDB;
	private static Context instance;

	private Context() {}

	public static Context getInstance() {
		if (Context.instance == null) {
			Context.instance = new Context();
		}
		return Context.instance;
	}

	public Database initDB(){
		mDB = new Database();
		return mDB;
	}
	
	public boolean authenticate(String pUsername, String pPassword) {
		if (mDB.connect(Database.sDefaultDBHost, Database.sDefaultDBPort, Database.sDefaultDBName, pUsername, pPassword)) {
			return true;
		} else {
			mDB = null;
			return false;
		}
	}
	
//	public Database getDatabase(){
//		return mDB;
//	}
}
