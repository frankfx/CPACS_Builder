package de;

import de.services.Database;

public class Context {

	public Database mDB = new Database();
	private static Context instance;

	private Context() {
		mDB = new Database();
	}

	public static Context getInstance() {
		if (Context.instance == null) {
			Context.instance = new Context();
		}
		return Context.instance;
	}

	public boolean authenticate(String pUsername, String pPassword) {
		if (mDB.connect("85.10.205.173", "3306", "testdb_tipico", pUsername, pPassword)) {
			return true;
		} else {
			mDB = null;
			return false;
		}
	}
	
	public Database getDatabase(){
		return mDB;
	}
}
