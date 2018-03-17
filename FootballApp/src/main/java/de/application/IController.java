package de.application;

import java.util.HashMap;
import java.util.Map;

public interface IController {
	
	Map<String,ISubController> mSubController = new HashMap<String, ISubController>();
	
	/**
	 * Add a child controller
	 * 
	 * @param pController adds a new sub controller to a controller list
	 */	
	default void addSubController(String pKey, ISubController pController) {
		mSubController.put(pKey, pController);
	}	
	
	/**
	 * Remove a child controller
	 * 
	 * @param pController adds a new sub controller to a controller list
	 */
	default void removeSubController(String pKey) {
		mSubController.remove(pKey);
	}	
	
	/**
	 * Start App
	 */
	default void runApp(){
		this.initRunnable();
	};

	public void initRunnable();
}
