package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import data.Tes;
import de.business.BundesligaModel;
import de.business.Match;
import de.business.LigaWService;
import de.business.teams.TeamIDEnum;
import de.business.teams.TeamModel;
import de.presentation.bundesliga.BundesligaView;
import de.presentation.popups.Popup;

public class BundesligaActivityBean {
	private BundesligaModel mModel;
	private BundesligaView mView;
	private List<ISubController> mSubController;
	private PythonInterpreter mPython;
	
	/**
	 * Controller 
	 * 
	 * @param pModel the data model
	 * @param pView the presentation widget
	 */	
	public BundesligaActivityBean(BundesligaModel pModel, BundesligaView pView) {
		this.mModel = pModel;
		this.mView = pView;
		this.mSubController = new ArrayList<ISubController>();
	}
	
	/**
	 * Add a child controller
	 * 
	 * @param pController adds a new sub controller to a controller list
	 */		
	public void addSubController(ISubController pController){
		this.mSubController.add(pController);		
	}

	/**
	 * Remove a child controller
	 * 
	 * @param pController adds a new sub controller to a controller list
	 */		
	public void removeSubController(ISubController pController){
		this.mSubController.remove(pController);		
	}
	
	/**
	 * Reads all fixtures from the Webservice and set them to the presentation view
	 * 
	 * @param lMatchday the specific match day.
	 * @param lLeague the football league.
	 * @param lYear the football season.
	 * 
	 * Example: 27, "bl1", "2015" 
	 */			
	public void initFixture(int lMatchDay, String lLeague, String lYear){
		Match [] lMatches = LigaWService.parseFootballData(lMatchDay, lLeague, lYear);
		
		Map<TeamIDEnum, TeamModel> map = mModel.getTeams();

		int i = 0;
		for(Match lMatch : lMatches){
			if (lMatch != null){
				mView.getFixturePanel().createFixture(
						map.get(TeamIDEnum.getType(lMatch.getTeam1())), 
						map.get(TeamIDEnum.getType(lMatch.getTeam2())), 
						lMatch.getScore1(), lMatch.getScore2(), i++);				
			}
		}
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
				actionRequestCompleteMatchday();
			}
		});
		
		mView.setButtonRequestTeamListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionTeamDataPythonRequest();
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
		
		/**
		 * Database connection
		 */
		mView.setMenuItemDBConnectListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String [] arr = Popup.startDatabaseConnectionPopup();
				if(arr != null)
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
		 * About frame
		 */		
		mView.setMenuItemAboutListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("About");
			}
		});
		
		/**
		 * Observer pattern to trigger this.actionUpdateConsole from the subcontroller
		 */		
		for(ISubController lController : mSubController)
			lController.setUpdateListener(this);
	}	

	/**
	 * Action Events
	 */		
	public void actionUpdateConsole(String lMessage){
		mView.getConsolenPanel().appendConsole(lMessage);
	}	
	
	public void actionRequestCompleteMatchday(){
		int lMatchday = (Integer)mView.getConsolenPanel().getComboMatchday().getSelectedItem();
		String lLeague = mView.getConsolenPanel().getComboLeague().getSelectedItem().toString();
		String lSeason = mView.getConsolenPanel().getComboSeason().getSelectedItem().toString();
		
		initFixture(lMatchday, lLeague, lSeason);
	}
	
	public void actionTeamDataPythonRequest(){
		if (mPython == null)
			this.mPython = new PythonInterpreter();
		
		String t = Tes.getInstance().SCRIPT_PYTHON_SOCCERWAY.getFile();
		
		System.out.println(t + ", " +  new File(t).exists());
		
		System.out.println("Hier1 : " + t);
		
		mPython.execfile(t);
		
		System.out.println("Hier2 : " +t);
		
		
		PyFunction pyFuntion = (PyFunction) mPython.get("getTeamData", PyFunction.class);
        
        String lId = ((TeamIDEnum) mView.getConsolenPanel().getComboTeamID().getSelectedItem()).getID();
        String lMatchType = mView.getConsolenPanel().getComboMatchType().getSelectedItem().toString();
        
        PyObject result = pyFuntion.__call__(new PyString(lId), new PyString(lMatchType));		
		
        String[][] lData = (String[][]) result.__tojava__(String[][].class);
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < lData.length; i++) {
        	sb.append("[");
        	for (int j = 0; j < lData[i].length -1 ; j++){
        		sb.append(lData[i][j]);
        		sb.append(", ");
        	}
        	sb.append(lData[i][lData[i].length-1]);
        	sb.append("]");
        	mView.getConsolenPanel().appendConsole(sb.toString());
        	sb.setLength(0);
        }
	}
	
	public void actionClose(){
		mView.dispose();
		if (mPython != null)
			mPython.close();	
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
			}
		});
	}
}
