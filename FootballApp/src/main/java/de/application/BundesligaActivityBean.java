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
import de.presentation.bundesliga.StakeOverviewDialog;
import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;
import de.services.PropertyService;
import de.services.SWJSONParser;
import de.types.MessageType;
import de.services.HyperlinkService;
import de.services.LoggerService;
import de.utils.FAMessages;
import de.utils.math.MathTipico;

public class BundesligaActivityBean {
	private BundesligaView mView;
	private Map<String,ISubController> mSubController;
	private boolean mFixturesErzeugtFlag = false;
	private boolean mResultsErzeugtFlag = false;
	private final static int TAB_INDEX_SW_AUFGABEN = 1;	
	private final static int TAB_INDEX_SW_RESULTS = 2;	
	private File mPropertiesFile;

	private final static String TIPICO_CONTROLLER_KEY ;
	private final static String SOCCERWAY_CONTROLLER_KEY;
	private final static String STATISTIC_CONTROLLER_KEY;
	
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
		 * Minimize left panel
		 */
		mView.setMenuItemMinimizeView(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mView.getPanelLeftHorizontal().setVisible(!mView.getPanelLeftHorizontal().isVisible());
				mView.getSplitPaneVertikal().updateUI();
			}
		});		

		/**
		 * Open prediction dialog for selected rows
		 */
		mView.setMenuItemBetPrediction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TipicoActivityBean mTipicoController = (TipicoActivityBean) mSubController.get(TIPICO_CONTROLLER_KEY);
		    	new StakeOverviewDialog(mView, "Test", 100, 100, MathTipico.getFinancialBetPrediction(mTipicoController.getTipicoModelsAsList()));
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
				if (mFixturesErzeugtFlag){
					String [] res = PopupFactory.getPopup(PopupType.PROPERTIES_POPUP, new Object[]{mPropertiesFile}).requestInputData();
					if (res != null && res[0].equals(PropertyService.PROPERTIES_CHANGED)){
						createFixtures(null);
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
					createFixtures(null);
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
				if (mView.getTabbedPane().getSelectedIndex() == TAB_INDEX_SW_AUFGABEN && !mFixturesErzeugtFlag){
					TipicoActivityBean lTipico = (TipicoActivityBean) mSubController.get(TIPICO_CONTROLLER_KEY);
					createFixtures( lTipico.getTipicoOpenGameIDsFromDB() );
				} else if (mView.getTabbedPane().getSelectedIndex() == TAB_INDEX_SW_RESULTS && !mResultsErzeugtFlag){
					TipicoActivityBean lTipico = (TipicoActivityBean) mSubController.get(TIPICO_CONTROLLER_KEY);
					createResults( lTipico.getTipicoOpenGameIDsFromDB() );
				}
			}
		});		
		
		/**
		 * Observer pattern to trigger e.g. this.actionUpdateConsole from the subcontroller
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
	
	/**
	 * Select all fixtures with the given id
	 */	
	public void actionUpdateFixturesTable(String pID){
		mView.getFixturesPanel().updateTableMarker(pID);
	}

	/**
	 * Select all results with the given id
	 */		
	public void actionUpdateResultsTable(String pID){
		mView.getSWResultPanel().updateTableMarker(pID);
	}

	/**
	 * Update the status bar with a message
	 * 
	 * @param pType message type {ERROR, HINT, etc}
	 * @param pMessage text to display in the status bar
	 * @param pDuration time for showing the message
	 */
	public void actionUpdateStatusBar(MessageType pType, String pMessage, int pDuration){
		switch (pType) {
		case ERROR:
			mView.getStatusMessagePanel().setStatusMessageColor(Color.RED); break;
		default:
			mView.getStatusMessagePanel().setStatusMessageColor(Color.BLACK); break;
		}
		
		if (pDuration > 0)
			mView.getStatusMessagePanel().showDelayedStatusMessage(pMessage, pDuration);
		else
			mView.getStatusMessagePanel().showStatusMessage(pMessage);
	}

	/**
	 * update all statistic elements by balance parameter 
	 */		
	public void actionUpdateStatistics(float pBalance) {
		((StatisticActivityBean) mSubController.get(STATISTIC_CONTROLLER_KEY)).updateStatisticByBalance(pBalance);
	}

	/**
	 * close the application 
	 */		
	public void actionClose() {
		mView.dispose();
	}

	/**
	 * creates fixtures for the given soccerway ids or from property file
	 * @param pIDList ids from soccerway to create fixtures. If this parameter is null the property file is used. 
	 */
	private void createFixtures(List<String> pIDList){
		Iterator<SoccerwayMatchModel> iter = null;
		
		mFixturesErzeugtFlag = true;
		
		try {
			if (pIDList != null){
				iter = SWJSONParser.getFixturesBySWObserverIDs(pIDList, 7);
			} else if (mPropertiesFile != null) {
				iter = SWJSONParser.getAufgabenBySWObserverPropertyFile(new FileInputStream(mPropertiesFile));
			}
		} catch (FileNotFoundException e) {
			PopupFactory.getPopup(PopupType.ERROR, e.getMessage());
			return;
		}

		mView.getFixturesPanel().clearTable();
		
		// add only one soccerwayMatchModel to each row (column 0)
		Vector<SoccerwayMatchModel> vec;
		// TODO create own model to avoid Vector (see createResults)
		if(iter != null){
			while (iter.hasNext()){
				vec = new Vector<SoccerwayMatchModel>();
				vec.add(iter.next());
				mView.getFixturesPanel().addToTable(vec);			
			}
			mView.getFixturesPanel().sortTableByDate();
		}
	}
	
	private void createResults(List<String> pIDList){
		Iterator<SoccerwayMatchModel> iter = null;
		
		mResultsErzeugtFlag = true;
		
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
		
		if(iter != null){
			while (iter.hasNext()){
				// add only one soccerwayMatchModel to each row (column 0)
				mView.getSWResultPanel().addToTable(iter.next());			
			}
			mView.getSWResultPanel().sortTableByDate();
		}		
	}
	
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

				logger.warning("init finished");
				//BEGIN FAST DATABASE ACCESS ONLY FOR TESTING
				//mSubController.get(TIPICO_CONTROLLER_KEY).initBean(new String[] { "85.10.205.173", "3306", "testdb_tipico", "frankfx", "" });
				//END FAST DATABASE ACCESS ONLY FOR TESTING
			}
		});
	}
}
