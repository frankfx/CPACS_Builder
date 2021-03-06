package de.presentation.bundesliga;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;

import de.presentation.ButtonPanelContainer;
import de.presentation.IDefaultGUI;

public class BundesligaView extends JFrame implements IDefaultGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTabbedPane mTabbedPane;	
	private ConsolenPanel mConsolenPanel;
	private SWFixturePanel mFixturesPanel;
	private SWResultPanel mSWResultPanel;
	private TipicoBetView mTipicoPanel;
	private StatisticPanel mStatisticPanel;
	private ButtonPanelContainer mButtonPanel;
	private StatusPanel mStatusMessagePanel;
	private JSplitPane mSplitPaneVertikal;
	private JPanel mPanelLeftHorizontal;

	// Menu items
	private JMenuItem menuItemDBConnect = new JMenuItem("Connect DB");	
	private JMenuItem menuItemDBDisConnect = new JMenuItem("Disconnect DB");
	private JMenuItem menuItemMinimizeView = new JMenuItem("Minimize");	
	private JMenuItem menuItemPredictionDialog = new JMenuItem("Bet prediction");		
	private JMenuItem menuItemAccountBalanceDialog = new JMenuItem("Account balance");	
	private JMenuItem menuItemPrint = new JMenuItem("Print");
	private JMenuItem menuItemAbout = new JMenuItem("About Football App");
	private JMenuItem menuItemLinkTipico = new JMenuItem("tipico.de");
	private JMenuItem menuItemLinkSoccerway = new JMenuItem("soccerway.com");
	private JMenuItem menuItemExit = new JMenuItem("Exit");	
	private JMenuItem menuItemShowProperties = new JMenuItem("Show...");
	private JMenuItem menuItemLoadProperties = new JMenuItem("Load...");
	
	@Override
	public void initView() {
		UIManager.getDefaults().put("SplitPane.border", BorderFactory.createEmptyBorder());
		setTitle("Tipico bet tool");
		createGUIElements();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(750, 500));
		setSize(850,500);
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
		
		mStatusMessagePanel = new StatusPanel(LocalDate.now().toString(), this.getWidth());
		mTabbedPane = new JTabbedPane();
		mConsolenPanel = new ConsolenPanel();
		mFixturesPanel = new SWFixturePanel();
		mSWResultPanel = new SWResultPanel();
		mStatisticPanel = new StatisticPanel();
		mTipicoPanel = new TipicoBetView();
		mButtonPanel = new ButtonPanelContainer();
		
		mTabbedPane.addTab("Console", mConsolenPanel);
		mTabbedPane.addTab("Fixtures", mFixturesPanel);
		mTabbedPane.addTab("Results", mSWResultPanel);
		
		mPanelLeftHorizontal = new JPanel();
		mPanelLeftHorizontal.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 1;		
		c.weightx = 0.1;
		mPanelLeftHorizontal.add(mStatisticPanel, c);		
		
		c.gridy = 2;
		pane.add(mButtonPanel, c);		
		
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 0;
		c.weightx = 0.1;
		c.weighty = 0.1;		
		mPanelLeftHorizontal.add(mTabbedPane, c);

		mSplitPaneVertikal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mPanelLeftHorizontal, mTipicoPanel);

		pane.add(mSplitPaneVertikal, c);
		
		getContentPane().add(pane, BorderLayout.CENTER);
		//getContentPane().add(mButtonPanel, BorderLayout.SOUTH);
		getContentPane().add(mStatusMessagePanel, BorderLayout.SOUTH);
		
		createMenuBar();
	}

	private void createMenuBar() {
		JMenuBar bar = new JMenuBar();
		
		JMenu menuFile = new JMenu("File");
		JMenu menuConfig = new JMenu("Configure");
		JMenu menuProperties = new JMenu("Properties");
		JMenu menuLinks = new JMenu("Links");
		JMenu menuExtras = new JMenu("Extras");
		JMenu menuHelp = new JMenu("Help");
		
		menuFile.addSeparator();
		menuFile.add(menuItemPrint);
		menuFile.addSeparator();
		menuFile.add(menuItemExit);

		menuConfig.add(menuItemDBConnect);
		menuConfig.add(menuItemDBDisConnect);
		
		menuProperties.add(menuItemShowProperties);
		menuProperties.add(menuItemLoadProperties);
		
		menuLinks.add(menuItemLinkTipico);
		menuLinks.add(menuItemLinkSoccerway);

		menuExtras.add(menuItemMinimizeView);
		menuExtras.add(menuItemPredictionDialog);
		menuExtras.add(menuItemAccountBalanceDialog);
		
		menuHelp.add(menuItemAbout);
		
		bar.add(menuFile);
		bar.add(menuConfig);
		bar.add(menuProperties);
		bar.add(menuLinks);
		bar.add(menuExtras);
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
    
    public void setSWButtonSubmitSelectedResultsListener(ActionListener l){
    	this.mSWResultPanel.getSubmitButton().addActionListener(l);
    }
    
    public void setSWResultButtonRefreshListener(ActionListener l){
    	this.mSWResultPanel.getRefreshButton().addActionListener(l);
    }
    
    public void setSWFixturesResultButtonRefreshListener(ActionListener l){
    	this.mFixturesPanel.getRefreshButton().addActionListener(l);
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

	public void setMenuItemMinimizeView(ActionListener l) {
		this.menuItemMinimizeView.addActionListener(l);
	}    
	
	public void setMenuItemBetPrediction(ActionListener l){
		this.menuItemPredictionDialog.addActionListener(l);
	}
	
	public void setMenuItemAccountBalance(ActionListener l){
		this.menuItemAccountBalanceDialog.addActionListener(l);
	}	
    
	public void setMenuItemLinkTipico(ActionListener l) {
		this.menuItemLinkTipico.addActionListener(l);
	}
	
	public void setMenuItemLinkSoccerway(ActionListener l) {
		this.menuItemLinkSoccerway.addActionListener(l);
	}

    public void setMenuItemAboutListener(ActionListener l){
    	this.menuItemAbout.addActionListener(l);
    }    

	public void setMenuItemShowPropertiesListener(ActionListener l){
		menuItemShowProperties.addActionListener(l);
	}	
	
	public void setMenuItemLoadPropertiesListener(ActionListener l){
		menuItemLoadProperties.addActionListener(l);
	}

	public void setMenuItemPrintListener(ActionListener l) {
		this.menuItemPrint.addActionListener(l);
	}

	public void addTabChangeListener(ChangeListener l){
		this.mTabbedPane.addChangeListener(l);
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
	public ButtonPanelContainer getButtonPanel() {
		return mButtonPanel;
	}

	public void setButtonPanel(ButtonPanelContainer lButtonPanel) {
		this.mButtonPanel = lButtonPanel;
	}

	public StatisticPanel getStatisticPanel() {
		return mStatisticPanel;
	}

	public void setStatisticPanel(StatisticPanel pStatisticPanel) {
		this.mStatisticPanel = pStatisticPanel;
	}

	public TipicoBetView getTipicoPanel() {
		return mTipicoPanel;
	}

	public void setTipicoPanel(TipicoBetView lTipicoPanel) {
		this.mTipicoPanel = lTipicoPanel;
	}

	public ConsolenPanel getConsolenPanel() {
		return mConsolenPanel;
	}

	public void setConsolenPanel(ConsolenPanel lConsolenPanel) {
		this.mConsolenPanel = lConsolenPanel;
	}
	
	public SWFixturePanel getFixturesPanel(){
		return mFixturesPanel;
	}
	
	public SWResultPanel getSWResultPanel() {
		return mSWResultPanel;
	}

	public JTabbedPane getTabbedPane(){
		return mTabbedPane;
	}
	
	public StatusPanel getStatusMessagePanel() {
		return mStatusMessagePanel;
	}

	public void setStatusMessagePanel(StatusPanel pStatusMessagePanel) {
		this.mStatusMessagePanel = pStatusMessagePanel;
	}	
	
	public JPanel getPanelLeftHorizontal() {
		return mPanelLeftHorizontal;
	}	
	
	public JSplitPane getSplitPaneVertikal() {
		return mSplitPaneVertikal;
	}	
	/**
	 * ========================
	 * END GETTER AND SETTER
	 * ========================
	 */	
}
