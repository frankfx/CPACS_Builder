package de.application;

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

import de.business.SoccerwayMatchModel;
import de.presentation.bundesliga.BundesligaView;
import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;
import de.services.PropertyService;
import de.services.ResourceService;
import de.services.SWJSONParser;
import de.services.WorldWideWebService;
import de.utils.FAMessages;
import de.utils.Tupel;

public class BundesligaActivityBean {
	private BundesligaView mView;
	private List<ISubController> mSubController;
	private boolean mAufgabenNochNichtErzeugtFlag = true;
	private final static int TAB_INDEX_SW_AUFGABEN = 1;	
	private File mPropertiesFile;
	private boolean isDefaultPropertiesFile;

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

		mView.setButtonRequestListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//actionRequestCompleteMatchday();
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
				WorldWideWebService.openUrlInBrowser("https://www.tipico.de/de/online-sportwetten/");
			}
		});

		/**
		 * Link to soccerway
		 */
		mView.setMenuItemLinkSoccerway(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WorldWideWebService.openUrlInBrowser("http://de.soccerway.com/");
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
				if (mPropertiesFile == null){
					loadPropertiesFile();
				}
				PopupFactory.getPopup(PopupType.PROPERTIES_POPUP, new Object[]{new Tupel<File, Boolean>(mPropertiesFile, isDefaultPropertiesFile)}).requestInputData();
			}
		});
		
		/**
		 * Opens Properties file
		 */		
		mView.setMenuItemOpenPropertiesListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				File choosenFile = PropertyService.choosePropertiesFile();
				if (choosenFile != null)
					mPropertiesFile = choosenFile;
			}
		});		

		/**
		 * Creates match tasks when tab was changed the first time 
		 */		
		mView.addTabChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent pE) {
				if (mView.getTabbedPane().getSelectedIndex() == TAB_INDEX_SW_AUFGABEN && mAufgabenNochNichtErzeugtFlag){
					loadPropertiesFile();
					if (mPropertiesFile != null){
						createAufgabe();
						mAufgabenNochNichtErzeugtFlag = false;
					} else {
						PopupFactory.getPopup(PopupType.HINT, FAMessages.MESSAGE_NO_PROPERTIES_FILE);
					}
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
	public void actionUpdateConsole(String lMessage) {
		mView.getConsolenPanel().appendConsole(lMessage);
	}

	public void actionUpdateStatistics(float pBalance) {
		mView.getStatisticPanel().setBalanceValue(pBalance);
		int n = (int) (mView.getStatisticPanel().getProgressBar().getValue() + mView.getStatisticPanel().getBalanceValue()) / 10;
		mView.getStatisticPanel().getProgressBar().setValue(n);
	}


	public void actionClose() {
		mView.dispose();
	}

	private void createAufgabe(){
		Vector<String> vec;
		Iterator<SoccerwayMatchModel> iter;
		
		if (isDefaultPropertiesFile){
			iter = SWJSONParser.getResultsBySWObserverPropertyFile(ResourceService.getInstance().getResourcePropertyFile(ResourceService.DEFAULT_PROPERTIES_FILE));
		} else {
			try {
				iter = SWJSONParser.getResultsBySWObserverPropertyFile(new FileInputStream(mPropertiesFile));
			} catch (FileNotFoundException e) {
				PopupFactory.getPopup(PopupType.ERROR, e.getMessage());
				return;
			}
		}
		
		SoccerwayMatchModel match;
		
		while (iter.hasNext()){
			match = iter.next();
			vec = new Vector<String>();
			vec.add(match.getDate().toString());
			vec.add(match.getCompetition());
			vec.add(match.getTeam1());
			vec.add(match.getTeam2());
			mView.getAufgabenPanel().addToTable(vec);			
		}
		
		mView.getAufgabenPanel().sortTableByDate();
	}	
	
	/**
	 * Falls kein Properties file vorhanden ist (bspw. beim ersten oeffen), wird ein FileDialog geoeffnet,
	 * um die zu verwendente Datei zu bestimmen. Wird die Dateiauswahl abgebrochen wird ein Default File verwendet. 
	 * 
	 */
	private void loadPropertiesFile() {
		if (mPropertiesFile == null){
			mPropertiesFile = PropertyService.choosePropertiesFile();
			if (mPropertiesFile != null){
				isDefaultPropertiesFile = false;
			} else {
				isDefaultPropertiesFile = true;
				PopupFactory.getPopup(PopupType.HINT, FAMessages.MESSAGE_DEFAULT_PROPERTY);
				mPropertiesFile = new File(ResourceService.DEFAULT_PROPERTIES_FILE);
			}
		}
	}	
	
	/**
	 * Start App
	 */
	public void runApp() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				mView.initView();
				addSubController(new TipicoActivityBean(mView.getTipicoPanel()));
				addListener();

				//BEGIN FAST DATABASE ACCESS ONLY FOR TESTING
				//mSubController.get(0).initBean(new String[] { "85.10.205.173", "3306", "testdb_tipico", "frankfx", "130386" });
				//END FAST DATABASE ACCESS ONLY FOR TESTING
			}
		});
	}
}
