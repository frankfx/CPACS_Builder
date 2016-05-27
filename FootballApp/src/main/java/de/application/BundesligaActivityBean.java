package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.business.BundesligaModel;
import de.business.LigaWService;
import de.business.Match;
import de.business.SoccerwayMatchModel;
import de.business.teams.TeamIDEnum;
import de.business.teams.TeamModel;
import de.presentation.bundesliga.BundesligaView;
import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;
import de.types.SoccerwayMatchType;
import de.utils.SWJSONParser;

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
	 * Reads all fixtures from the Webservice and set them to the presentation view
	 * 
	 * @param lMatchday the specific match day.
	 * @param lLeague the football league.
	 * @param lYear the football season.
	 * 
	 * Example: 27, "bl1", "2015" 
	 */
	public void initFixture(int lMatchDay, String lLeague, String lYear) {
		Match[] lMatches = LigaWService.parseFootballData(lMatchDay, lLeague, lYear);

		Map<TeamIDEnum, TeamModel> map = mModel.getTeams();

		int i = 0;
		for (Match lMatch : lMatches) {
			if (lMatch != null) {
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

	public void actionRequestCompleteMatchday() {
		int lMatchday = (Integer) mView.getFixturePanel().getComboMatchday().getSelectedItem();
		String lLeague = mView.getFixturePanel().getComboLeague().getSelectedItem().toString();
		String lSeason = mView.getFixturePanel().getComboSeason().getSelectedItem().toString();

		initFixture(lMatchday, lLeague, lSeason);
	}

	public void actionTeamDataPythonRequest() {
		String lId = ((TeamIDEnum) mView.getStatisticPanel().getComboTeamID().getSelectedItem()).getID();
		String lMatchType = mView.getStatisticPanel().getComboMatchType().getSelectedItem().toString();

		Iterator<SoccerwayMatchModel> iter = SWJSONParser.getTeamData(lId, SoccerwayMatchType.getType(lMatchType));

		while (iter.hasNext()) {
			mView.getConsolenPanel().appendConsole(iter.next().toString());
		}
	}

	public void actionClose() {
		mView.dispose();
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
				mSubController.get(0).initBean(new String[] { "85.10.205.173", "3306", "testdb_tipico", "frankfx", "130386" });
				//END FAST DATABASE ACCESS ONLY FOR TESTING
			}
		});
	}
}
