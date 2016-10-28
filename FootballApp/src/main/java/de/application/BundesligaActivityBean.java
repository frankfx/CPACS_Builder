package de.application;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.business.SWResultTableModel;
import de.business.SoccerwayMatchModel;
import de.presentation.bundesliga.BundesligaView;
import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;
import de.services.PropertyService;
import de.services.SWJSONParser;
import de.types.MessageType;
import de.services.HyperlinkService;
import de.utils.FAMessages;

public class BundesligaActivityBean {
	private BundesligaView mView;
	private List<ISubController> mSubController;
	private boolean mAufgabenNochNichtErzeugtFlag = true;
	private boolean mResultsNochNichtErzeugtFlag = true;
	private final static int TAB_INDEX_SW_AUFGABEN = 1;	
	private final static int TAB_INDEX_SW_RESULTS = 2;	
	private File mPropertiesFile;

	/**
	 * Controller 
	 * 
	 * @param pModel the data model
	 * @param pView the presentation widget
	 */
	public BundesligaActivityBean(BundesligaView pView) {
		this.mView = pView;
		this.mSubController = new ArrayList<ISubController>();
	}

	/**
	 * Add a child controller
	 * 
	 * @param pController adds a new sub controller to a controller list
	 */
	public void addSubController(ISubController pController) {
		this.mSubController.add(pController);
	}

	/**
	 * Remove a child controller
	 * 
	 * @param pController adds a new sub controller to a controller list
	 */
	public void removeSubController(ISubController pController) {
		this.mSubController.remove(pController);
	}

	/**
	 * Add Listener
	 */
	private void addListener() {
		mView.setButtonExitListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionClose();
			}
		});

		mView.setButtonClearListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mView.getConsolenPanel().clearConsole();
			}
		});

		mView.setProgressbarListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println("progress bar changed");
			}
		});

		mView.setMenuItemExitListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mView.dispose();
			}
		});

		mView.setMenuItemLoadCSVListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Load CSV");
			}
		});

		mView.setMenuItemSaveCSVListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Save CSV");
			}
		});

		mView.setMenuItemCommitDBListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Commit DB");
			}
		});

		mView.setMenuItemPullDBListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//actionRunPython();
			}
		});

		this.mView.setMenuItemPrintListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (ISubController s : mSubController)
					s.print();
			}
		});

		/**
		 * Database connection
		 */
		mView.setMenuItemDBConnectListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] arr = PopupFactory.getPopup(PopupType.DATABASE_CONNECTION_POPUP, null).requestInputData();
				if (arr != null)
					mSubController.get(0).initBean(arr);
			}
		});

		/**
		 * Database Disconnection
		 */
		mView.setMenuItemDBDisconnectListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mSubController.get(0).updateBean();
			}
		});

		/**
		 * Link to tipico
		 */
		mView.setMenuItemLinkTipico(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HyperlinkService.openUrlInBrowser("https://www.tipico.de/de/online-sportwetten/");
			}
		});

		/**
		 * Link to soccerway
		 */
		mView.setMenuItemLinkSoccerway(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HyperlinkService.openUrlInBrowser("http://de.soccerway.com/");
			}
		});		

		
		/**
		 * About frame
		 */
		mView.setMenuItemAboutListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
				    "Java FootballApp\nVersion: 0.1\n\n2016-open Rene Frank\n\nOpenSoure",
					"About",
					JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		/**
		 * Show Properties file
		 */
		mView.setMenuItemShowPropertiesListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				if (!mAufgabenNochNichtErzeugtFlag){
					String [] res =PopupFactory.getPopup(PopupType.PROPERTIES_POPUP, new Object[]{mPropertiesFile}).requestInputData();
					if (res != null && res[0].equals(PropertyService.PROPERTIES_CHANGED)){
						createAufgaben(null);
						createResults(null);
					}
				} else 
					PopupFactory.getPopup(PopupType.HINT, FAMessages.MSG_NO_PROPERTIES_FILE);
			}
		});
		
		/**
		 * Opens Properties file
		 */		
		mView.setMenuItemLoadPropertiesListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				File choosenFile = PropertyService.choosePropertiesFile();
				if (choosenFile != null){
					mPropertiesFile = choosenFile;
					createAufgaben(null);
					createResults(null);
				}
			}
		});		

		/**
		 * Creates match tasks when tab was changed the first time 
		 */		
		mView.addTabChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent pE) {
				if (mView.getTabbedPane().getSelectedIndex() == TAB_INDEX_SW_AUFGABEN && mAufgabenNochNichtErzeugtFlag){
					TipicoActivityBean m = (TipicoActivityBean) mSubController.get(0);
					List<String> list = m.getTipicoOpenGameIDsFromDB();
					
					createAufgaben(list);
//					if (!isPropertiesFileAvailalble())
//						loadPropertiesFile();
//					createAufgaben();
				} else if (mView.getTabbedPane().getSelectedIndex() == TAB_INDEX_SW_RESULTS && mResultsNochNichtErzeugtFlag){
//					if (!isPropertiesFileAvailalble())
//						loadPropertiesFile();
					TipicoActivityBean m = (TipicoActivityBean) mSubController.get(0);
					List<String> list = m.getTipicoOpenGameIDsFromDB();
					createResults(list);
				}
				
			}
		});		
		
		/**
		 * Observer pattern to trigger this.actionUpdateConsole from the subcontroller
		 */
		for (ISubController lController : mSubController)
			lController.setUpdateListener(this);
	}

	/**
	 * Action Events
	 */
	public void actionUpdateConsole(String pMessage) {
		mView.getConsolenPanel().appendConsole(pMessage);
	}
	
	public void actionUpdateFixturesTable(String pID){
		mView.getAufgabenPanel().updateTableMarker(pID);
	}
	
	public void actionUpdateResultsTable(String pID){
		mView.getSWResultPanel().updateTableMarker(pID);
	}
	
	public void actionUpdateStatusBar(MessageType pType, String pMessage){
		switch (pType) {
		case ERROR:
			mView.getStatusMessagePanel().setStatusMessageColor(Color.RED);
			break;
		default:
			mView.getStatusMessagePanel().setStatusMessageColor(Color.BLACK);
			break;
		}
		//mView.getStatusMessagePanel().showDelayedStatusMessage(pMessage, 10000);
		mView.getStatusMessagePanel().showStatusMessage(pMessage);
	}

	public void actionUpdateStatistics(float pBalance) {
		mView.getStatisticPanel().setBalanceValue(pBalance);
		int n = (int) (mView.getStatisticPanel().getProgressBar().getValue() + mView.getStatisticPanel().getBalanceValue()) / 10;
		mView.getStatisticPanel().getProgressBar().setValue(n);
	}


	public void actionClose() {
		mView.dispose();
	}

	private void createAufgaben(List<String> pIDList){
		Iterator<SoccerwayMatchModel> iter = null;
		
		mAufgabenNochNichtErzeugtFlag = false;
		
		try {
			if (pIDList != null){
				iter = SWJSONParser.getAufgabenBySWObserverIDs(pIDList, 7);
			} else if (mPropertiesFile != null) {
				iter = SWJSONParser.getAufgabenBySWObserverPropertyFile(new FileInputStream(mPropertiesFile));
			}
		} catch (FileNotFoundException e) {
			PopupFactory.getPopup(PopupType.ERROR, e.getMessage());
			return;
		}

		mView.getAufgabenPanel().clearTable();
		
		// add only one soccerwayMatchModel to each row (column 0)
		Vector<SoccerwayMatchModel> vec;
		SoccerwayMatchModel match;
		
		if(iter != null){
			while (iter.hasNext()){
				match = iter.next();
				vec = new Vector<SoccerwayMatchModel>();
				vec.add(match);
				mView.getAufgabenPanel().addToTable(vec);			
			}
			
			mView.getAufgabenPanel().sortTableByDate();
		}
	}
	
	private void createResults(List<String> pIDList){
		Iterator<SoccerwayMatchModel> iter = null;
		
		mResultsNochNichtErzeugtFlag = false;
		
		try {
			if (pIDList != null){
				iter = SWJSONParser.getResultsBySWObserverIDs(pIDList, 7);
			} else if (mPropertiesFile != null) {
				iter = SWJSONParser.getResultsBySWObserverPropertyFile(new FileInputStream(mPropertiesFile));
			}
		} catch (FileNotFoundException e) {
			PopupFactory.getPopup(PopupType.ERROR, e.getMessage());
			return;
		}

		mView.getSWResultPanel().clearTable();
		
		// add only one soccerwayMatchModel to each row (column 0)
		SoccerwayMatchModel match;
		
		if(iter != null){
			while (iter.hasNext()){
				match = iter.next();
				mView.getSWResultPanel().addToTable(match);			
			}
			
			mView.getSWResultPanel().sortTableByDate();
		}		
	}
	
//	private boolean isPropertiesFileAvailalble(){
//		return mPropertiesFile != null || isDefaultPropertiesFile;
//	}
	
//	/**
//	 * Falls kein Properties file vorhanden ist (bspw. beim ersten oeffen), wird ein FileDialog geoeffnet,
//	 * um die zu verwendente Datei zu bestimmen. Wird die Dateiauswahl abgebrochen wird ein Default File verwendet. 
//	 * 
//	 */
//	private void loadPropertiesFile() {
//		if (mPropertiesFile == null){
//			mPropertiesFile = PropertyService.choosePropertiesFile();
//			if (mPropertiesFile != null){
//				isDefaultPropertiesFile = false;
//			} else {
//				isDefaultPropertiesFile = true;
//				actionUpdateStatusBar(MessageType.HINT, FAMessages.MSG_DEFAULT_PROPERTY);
//			}
//		}
//	}	
	
	/**
	 * Start App
	 */
	public void runApp() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				mView.initView();
				addSubController(new TipicoActivityBean(mView.getTipicoPanel()));
				addListener();

				//BEGIN FAST DATABASE ACCESS ONLY FOR TESTING
				//mSubController.get(0).initBean(new String[] { "85.10.205.173", "3306", "testdb_tipico", "frankfx", "" });
				//END FAST DATABASE ACCESS ONLY FOR TESTING
			}
		});
	}
}
