package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import de.business.BundesligaModel;
import de.business.Match;
import de.business.LigaWService;
import de.business.teams.TeamIDEnum;
import de.business.teams.TeamModel;
import de.presentation.bundesliga.BundesligaView;
import de.presentation.popups.Popup;
import de.utils.RessourceService;

public class BundesligaActivityBean {
	private BundesligaModel mModel;
	private BundesligaView mView;
	private List<ISubController> mSubController;
	
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
				mView.dispose();
			}
		});
		
		mView.setButtonRequestListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionRequestCompleteMatchday();
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
				actionRunPython();
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
	
	public void actionRunPython(){
		PythonInterpreter lPython = new PythonInterpreter();
		lPython.execfile(RessourceService.SCRIPT_PYTHON_SOCCERWAY.getFile());
		
        PyFunction pyFuntion = (PyFunction) lPython.get("getTeamData", PyFunction.class);
        PyObject result = pyFuntion.__call__(new PyString("994"), new PyString("home"));		
		
        String [] str = result.toString().split("],");
        
        for (String s : str)
        	System.out.println(s);
		
		lPython.close();
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
