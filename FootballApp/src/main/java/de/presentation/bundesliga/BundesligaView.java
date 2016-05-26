package de.presentation.bundesliga;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;

import de.presentation.ButtonPanelContainer;
import de.presentation.IDefaultGUI;

public class BundesligaView extends JFrame implements IDefaultGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BundesligaFixtureContainer mFixturePanel;
	private BundesligaConsoleContainer mConsolenPanel;
	private TipicoBetView mTipicoPanel;
	private StatisticContainer mStatisticPanel;
	private ButtonPanelContainer mButtonPanel;
	
	private JSplitPane mSplitPaneRightHorizontal;
	private JSplitPane mSplitPaneLeftHorizontal;
	private JSplitPane mSplitPaneVertikal;


	// Menu items
	private JMenuItem menuItemLoadCSV = new JMenuItem("Load CSV");
	private JMenuItem menuItemSaveCSV = new JMenuItem("Save CSV");
	private JMenuItem menuItemDBConnect = new JMenuItem("Connect DB");	
	private JMenuItem menuItemDBDisConnect = new JMenuItem("Disconnect DB");
	private JMenuItem menuItemCommitDB = new JMenuItem("Commit DB");	
	private JMenuItem menuItemPullDB = new JMenuItem("Pull DB");		
	private JMenuItem menuItemPrint = new JMenuItem("Print");
	private JMenuItem menuItemAbout = new JMenuItem("About Football App");
	private JMenuItem menuItemExit = new JMenuItem("Exit");	
	
	@Override
	public void initView() {
		UIManager.getDefaults().put("SplitPane.border", BorderFactory.createEmptyBorder());
		setTitle("1. Bundesliga View");
		createGUIElements();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(600, 500));
		pack();
		setVisible(true);
	}

	@Override
	public void evaluateGuiState() {
		
	}

	@Override
	public void disposeGUI() {
		this.dispose();
	}	
	
	/**
	 * Creates all GUI Components
	 */	
	private void createGUIElements() {
		Container pane = new Container();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		mFixturePanel = new BundesligaFixtureContainer();
		mConsolenPanel = new BundesligaConsoleContainer();
		mStatisticPanel = new StatisticContainer();
		mTipicoPanel = new TipicoBetView();
		mButtonPanel = new ButtonPanelContainer();
		
		mSplitPaneLeftHorizontal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mConsolenPanel, mStatisticPanel);
		mSplitPaneRightHorizontal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mFixturePanel, mTipicoPanel);
		mSplitPaneVertikal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mSplitPaneLeftHorizontal, mSplitPaneRightHorizontal);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.1;
		c.weighty = 0.1;

		pane.add(mSplitPaneVertikal, c);

		getContentPane().add(pane, BorderLayout.CENTER);
		getContentPane().add(mButtonPanel, BorderLayout.SOUTH);
		
		createMenuBar();
	}

	private void createMenuBar() {
		JMenuBar bar = new JMenuBar();
		
		JMenu menuFile = new JMenu("File");
		JMenu menuConfig = new JMenu("Configure");
		JMenu menuHelp = new JMenu("Help");
		
		menuFile.add(menuItemLoadCSV);
		menuFile.add(menuItemSaveCSV);
		menuFile.addSeparator();
		menuFile.add(menuItemCommitDB);
		menuFile.add(menuItemPullDB);
		menuFile.addSeparator();
		menuFile.add(menuItemPrint);
		menuFile.addSeparator();
		menuFile.add(menuItemExit);

		menuConfig.add(menuItemDBConnect);
		menuConfig.add(menuItemDBDisConnect);
		
		menuHelp.add(menuItemAbout);
		
		bar.add(menuFile);
		bar.add(menuConfig);
		bar.add(menuHelp);
		
		this.setJMenuBar(bar);		
	}
	
	/**
	 * ========================
	 * BEGIN LISTENER
	 * ========================
	 */	
    public void setButtonExitListener(ActionListener l){
        this.mButtonPanel.getBtnExit().addActionListener(l);
    }	
    
    public void setButtonRequestListener(ActionListener l){
        this.mFixturePanel.getBtnRequest().addActionListener(l);
    }    

    public void setButtonClearListener(ActionListener l){
        this.mConsolenPanel.getBtnClear().addActionListener(l);
    }    

    public void setButtonRequestTeamListener(ActionListener l){
    	this.mStatisticPanel.getBtnTeamRequest().addActionListener(l);
    }

    public void setProgressbarListener(ChangeListener l){
    	this.mStatisticPanel.getProgressBar().addChangeListener(l);
    }    
    
    public void setMenuItemLoadCSVListener(ActionListener l){
    	this.menuItemLoadCSV.addActionListener(l);
    }  	

    public void setMenuItemSaveCSVListener(ActionListener l){
    	this.menuItemSaveCSV.addActionListener(l);
    }    
    
    public void setMenuItemCommitDBListener(ActionListener l){
    	this.menuItemCommitDB.addActionListener(l);
    }    

    public void setMenuItemPullDBListener(ActionListener l){
    	this.menuItemPullDB.addActionListener(l);
    }     
    
	public void setMenuItemExitListener(ActionListener l){
    	this.menuItemExit.addActionListener(l);
    }  

    public void setMenuItemDBConnectListener(ActionListener l){
    	this.menuItemDBConnect.addActionListener(l);
    } 

    public void setMenuItemDBDisconnectListener(ActionListener l){
    	this.menuItemDBDisConnect.addActionListener(l);
    }     
    
    public void setMenuItemAboutListener(ActionListener l){
    	this.menuItemAbout.addActionListener(l);
    }    

	public void setMenuItemPrintListener(ActionListener l) {
		this.menuItemPrint.addActionListener(l);
	}
    
	/**
	 * ========================
	 * END LISTENER
	 * ========================
	 */	    
    
    
    
    
	/**
	 * ========================
	 * BEGIN GETTER AND SETTER
	 * ========================
	 */
	public BundesligaFixtureContainer getFixturePanel() {
		return mFixturePanel;
	}

	public void setFixturePanel(BundesligaFixtureContainer lFixturePanel) {
		this.mFixturePanel = lFixturePanel;
	}

	public ButtonPanelContainer getButtonPanel() {
		return mButtonPanel;
	}

	public void setButtonPanel(ButtonPanelContainer lButtonPanel) {
		this.mButtonPanel = lButtonPanel;
	}

	public StatisticContainer getStatisticPanel() {
		return mStatisticPanel;
	}

	public void setStatisticPanel(StatisticContainer pStatisticPanel) {
		this.mStatisticPanel = pStatisticPanel;
	}

	public TipicoBetView getTipicoPanel() {
		return mTipicoPanel;
	}

	public void setTipicoPanel(TipicoBetView lTipicoPanel) {
		this.mTipicoPanel = lTipicoPanel;
	}

	public BundesligaConsoleContainer getConsolenPanel() {
		return mConsolenPanel;
	}

	public void setConsolenPanel(BundesligaConsoleContainer lConsolenPanel) {
		this.mConsolenPanel = lConsolenPanel;
	}
	/**
	 * ========================
	 * END GETTER AND SETTER
	 * ========================
	 */	
}
