package de.application;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.business.SoccerwayMatchModel;
import de.presentation.bundesliga.BundesligaView;
import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;
import de.services.PropertyService;
import de.services.SWJSONParser;
import de.types.MessageType;
import de.types.PersistenceType;
import de.services.HyperlinkService;
import de.services.LoggerService;
import de.utils.FAMessages;

public class BundesligaActivityBean {
	private BundesligaView mView;
	private Map<String,ISubController> mSubController;
	private boolean mAufgabenNochNichtErzeugtFlag = true;
	private boolean mResultsNochNichtErzeugtFlag = true;
	private final static int TAB_INDEX_SW_AUFGABEN = 1;	
	private final static int TAB_INDEX_SW_RESULTS = 2;	
	private File mPropertiesFile;

	private static String TIPICO_CONTROLLER_KEY ;
	private static String SOCCERWAY_CONTROLLER_KEY;
	private static String STATISTIC_CONTROLLER_KEY;
	
	static{
		SOCCERWAY_CONTROLLER_KEY = "sw_contr";
		TIPICO_CONTROLLER_KEY = "tipico_contr";
		STATISTIC_CONTROLLER_KEY = "statistic_contr";
	};
	
	private final static Logger logger = LoggerService.getInstance().getLogger();
	
	/**
	 * Controller 
	 * 
	 * @param pModel the data model
	 * @param pView the presentation widget
	 */
	public BundesligaActivityBean(BundesligaView pView) {
		this.mView = pView;
		this.mSubController = new HashMap<String,ISubController>();
	}

	/**
	 * Add a child controller
	 * 
	 * @param pController adds a new sub controller to a controller list
	 */
	public void addSubController(String pKey, ISubController pController) {
		this.mSubController.put(pKey, pController);
	}

	/**
	 * Remove a child controller
	 * 
	 * @param pController adds a new sub controller to a controller list
	 */
	public void removeSubController(String pKey) {
		this.mSubController.remove(pKey);
	}

	/**
	 * Add Listener
	 */
	private void addListener() {
		/**
		 * Exit
		 */
		mView.setMenuItemExitListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionClose();
			}
		});

		/**
		 * CSV loading
		 */
		mView.setMenuItemLoadCSVListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PopupFactory.getPopup(PopupType.HINT, FAMessages.MSG_NOT_IMPLEMENTED_YET);
			}
		});		

		/**
		 * CSV saving
		 */		
		mView.setMenuItemSaveCSVListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PopupFactory.getPopup(PopupType.HINT, FAMessages.MSG_NOT_IMPLEMENTED_YET);
			}
		});

		/**
		 * Tipico printing
		 */		
		this.mView.setMenuItemPrintListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mSubController.get(TIPICO_CONTROLLER_KEY).print();
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
					mSubController.get(TIPICO_CONTROLLER_KEY).initBean(arr);
			}
		});

		/**
		 * Database disconnection
		 */
		mView.setMenuItemDBDisconnectListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mSubController.get(TIPICO_CONTROLLER_KEY).updateBean();
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
					String [] res = PopupFactory.getPopup(PopupType.PROPERTIES_POPUP, new Object[]{mPropertiesFile}).requestInputData();
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
		 * Exit by button
		 */
		mView.setButtonExitListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionClose();
			}
		});

		/**
		 * Apply soccerway result to tipico
		 */		
		mView.setButtonSubmitSelectedResultsListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> lResultList = mView.getSWResultPanel().getDataListWithAcceptedValues();
				
				if(!lResultList.isEmpty()){
					// TODO ueber gebe an subcontroller
					TipicoActivityBean mTipicoController = (TipicoActivityBean) mSubController.get(TIPICO_CONTROLLER_KEY);
					
					mTipicoController.setTipicoModelsToSuccess(lResultList);
					mTipicoController.updateTable();
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
					TipicoActivityBean m = (TipicoActivityBean) mSubController.get(TIPICO_CONTROLLER_KEY);
					List<String> list = m.getTipicoOpenGameIDsFromDB();
					
					createAufgaben(list);
//					if (!isPropertiesFileAvailalble())
//						loadPropertiesFile();
//					createAufgaben();
				} else if (mView.getTabbedPane().getSelectedIndex() == TAB_INDEX_SW_RESULTS && mResultsNochNichtErzeugtFlag){
//					if (!isPropertiesFileAvailalble())
//						loadPropertiesFile();
					TipicoActivityBean m = (TipicoActivityBean) mSubController.get(TIPICO_CONTROLLER_KEY);
					List<String> list = m.getTipicoOpenGameIDsFromDB();
					createResults(list);
				}
				
			}
		});		
		
		/**
		 * Observer pattern to trigger this.actionUpdateConsole from the subcontroller
		 */
		Iterator<Entry<String, ISubController>> iter = mSubController.entrySet().iterator();
		while(iter.hasNext()){
			iter.next().getValue().setUpdateListener(this);
		}
	}

	/**
	 * Action Events
	 */
	public void actionUpdateConsole(String pMessage) {
		((SoccerwayActivityBean) mSubController.get(SOCCERWAY_CONTROLLER_KEY)).appendConsole(pMessage);
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
		((StatisticActivityBean) mSubController.get(STATISTIC_CONTROLLER_KEY)).updateStatisticByBalance(pBalance);
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
				addSubController(TIPICO_CONTROLLER_KEY, new TipicoActivityBean(mView.getTipicoPanel()));
				addSubController(SOCCERWAY_CONTROLLER_KEY, new SoccerwayActivityBean(mView.getConsolenPanel()));
				addSubController(STATISTIC_CONTROLLER_KEY, new StatisticActivityBean(mView.getStatisticPanel()));
				addListener();

				//BEGIN FAST DATABASE ACCESS ONLY FOR TESTING
				//mSubController.get(TIPICO_CONTROLLER_KEY).initBean(new String[] { "85.10.205.173", "3306", "testdb_tipico", "frankfx", "" });
				//END FAST DATABASE ACCESS ONLY FOR TESTING
			}
		});
	}
}
