package de.services;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerService {
	
	private static LoggerService instance = new LoggerService();
	private Logger logger;
	
	private LoggerService(){
		try {
			logger = Logger.getLogger(LoggerService.class.getName());
			FileHandler fh = new FileHandler("loggerExample.log", false);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
			logger.setLevel(Level.CONFIG);
			
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public Logger getLogger(){
		return logger;
	}
	
	public static LoggerService getInstance(){
		return instance;
	}
}
