package de.application;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.Context;
import de.business.SoccerwayMatchModel;
import de.presentation.bundesliga.BundesligaView;
import de.presentation.bundesliga.StakeOverviewDialog;
import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;
import de.services.AccountBalanceService;
import de.services.Database;
import de.services.HyperlinkService;
import de.services.LoggerService;
import de.services.PropertyService;
import de.services.SWHTMLParser;
import de.types.MessageType;
import de.utils.FAMessages;
import de.utils.Utils;
import de.utils.math.MathTipico;

public class BundesligaActivityBean implements IController{
	
	private BundesligaView mView;
	private boolean mFixturesErzeugtFlag = false;
	private boolean mResultsErzeugtFlag = false;
	private final static int TAB_INDEX_SW_FIXTURES = 1;	
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
		 * Open dialog to change the account balance
		 */
		mView.setMenuItemAccountBalance(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Database lDB = Context.getInstance().mDB;
				
				float lBalance = AccountBalanceService.getDefaultBalanceValue(lDB);
				
				String[] arr = PopupFactory.getPopup(PopupType.TIPICO_ACCOUNT_BALANCE_POPUP, new Object[]{lBalance}).requestInputData();
				if (arr != null) {
					
					lBalance = Float.parseFloat(arr[0]);
					
					// update default balance value
					AccountBalanceService.updateDefaultBalanceValue(lDB, lBalance);

					// compute balance with new default balance value and update the statistic panel
					lBalance = AccountBalanceService.getBalance(lDB);
					((StatisticActivityBean) mSubController.get(STATISTIC_CONTROLLER_KEY)).updateStatisticByBalance(lBalance);
				}
					
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
		mView.setSWButtonSubmitSelectedResultsListener(new ActionListener() {
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
		 * Refresh SW-Results table
		 */
		mView.setSWResultButtonRefreshListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TipicoActivityBean lTipico = (TipicoActivityBean) mSubController.get(TIPICO_CONTROLLER_KEY);
				Utils.executeFunctionWithWaitingDialogHint(mView, x -> createResults(x), lTipico.getTipicoOpenGameIDsFromDB());
			}
		});
		
		/**
		 * Refresh SW-Fixtures table
		 */
		mView.setSWFixturesResultButtonRefreshListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TipicoActivityBean lTipico = (TipicoActivityBean) mSubController.get(TIPICO_CONTROLLER_KEY);
				Utils.executeFunctionWithWaitingDialogHint(mView, x -> createFixtures(x), lTipico.getTipicoOpenGameIDsFromDB());
			}
		});

		/**
		 * Creates match tasks when tab was changed the first time 
		 */		
		mView.addTabChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent pE) {
				if (mView.getTabbedPane().getSelectedIndex() == TAB_INDEX_SW_FIXTURES && !mFixturesErzeugtFlag){
					TipicoActivityBean lTipico = (TipicoActivityBean) mSubController.get(TIPICO_CONTROLLER_KEY);
					Utils.executeFunctionWithWaitingDialogHint(mView, x -> createFixtures(x), lTipico.getTipicoOpenGameIDsFromDB());
				} else if (mView.getTabbedPane().getSelectedIndex() == TAB_INDEX_SW_RESULTS && !mResultsErzeugtFlag){
					TipicoActivityBean lTipico = (TipicoActivityBean) mSubController.get(TIPICO_CONTROLLER_KEY);
					Utils.executeFunctionWithWaitingDialogHint(mView, x -> createResults(x), lTipico.getTipicoOpenGameIDsFromDB());
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
				//TODO REGELWERK: create a Regelwerk for the magic number (7 days)
				iter = SWHTMLParser.getFixturesBySWObserverIDs(pIDList, 7);
			} else if (mPropertiesFile != null) {
				iter = SWHTMLParser.getFixtuesBySWObserverPropertyFile(new FileInputStream(mPropertiesFile));
			}
		} catch (IOException e) {
			PopupFactory.getPopup(PopupType.ERROR, e.getMessage());
			return;
		}

		mView.getFixturesPanel().clearTable();
		
		if(iter != null){
			while (iter.hasNext()){
				// add only one soccerwayMatchModel to each row (column 0)
				mView.getFixturesPanel().addToTable(iter.next());			
			}
			mView.getFixturesPanel().sortTableByDate();
			mView.getFixturesPanel().getSWJTable().revalidate();
		}
	}
	
	private void createResults(List<String> pIDList){
		Iterator<SoccerwayMatchModel> iter = null;
		
		mResultsErzeugtFlag = true;
		
		try {
			if (pIDList != null){
				// create a Regelwerk for the magic number (7 days)
				iter = SWHTMLParser.getResultsBySWObserverIDs(pIDList, 7);
			} else if (mPropertiesFile != null) {
				iter = SWHTMLParser.getResultsBySWObserverPropertyFile(new FileInputStream(mPropertiesFile));
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
			mView.getSWResultPanel().getSWJTable().revalidate();
		}		
	}
	
	public boolean isTabFixturesOrResultsFocused (){
		int lIndex = mView.getTabbedPane().getSelectedIndex();
		return lIndex == TAB_INDEX_SW_FIXTURES || lIndex == TAB_INDEX_SW_RESULTS;
	}
	
	/**
	 * implement abstract method of IController to initialize controller
	 */
	@Override
	public void initRunnable() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				mView.initView();
				addSubController(TIPICO_CONTROLLER_KEY, new TipicoActivityBean(mView.getTipicoPanel()));
				addSubController(SOCCERWAY_CONTROLLER_KEY, new SoccerwayActivityBean(mView.getConsolenPanel()));
				addSubController(STATISTIC_CONTROLLER_KEY, new StatisticActivityBean(mView.getStatisticPanel()));
				addListener();

				logger.warning("init finished");
			}
		});		
	}
}
