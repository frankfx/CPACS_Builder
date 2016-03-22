package de.presentation.bundesliga;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import de.presentation.ButtonPanelContainer;
import de.presentation.IDefaultGUI;

public class BundesligaView extends JFrame implements IDefaultGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private BundesligaTableContainer mTablePanel;
	private BundesligaFixtureContainer mFixturePanel;
	private ButtonPanelContainer mButtonPanel;
	private TipicoBetContainer mTipicoPanel;
	private BundesligaConsoleContainer mConsolenPanel;
	
	@Override
	public void initView() {
		setTitle("1. Bundesliga View");
		createGUIElements();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(400, 300));
		pack();
		setVisible(true);
	}

	private void createGUIElements() {
		Container pane = new Container();
		pane.setLayout(new GridLayout(2, 2));
		
		mTablePanel = new BundesligaTableContainer();
		mFixturePanel = new BundesligaFixtureContainer();
		mConsolenPanel = new BundesligaConsoleContainer();
		mTipicoPanel = new TipicoBetContainer();
		mButtonPanel = new ButtonPanelContainer();
		
		pane.add(mTablePanel);
		pane.add(mFixturePanel);
		pane.add(mConsolenPanel);
		pane.add(mTipicoPanel);
		
		getContentPane().add(pane, BorderLayout.CENTER);
		getContentPane().add(mButtonPanel, BorderLayout.SOUTH);

//		createMenuBar();
	}

	@Override
	public void evaluateGuiState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disposeGUI() {
		this.dispose();
	}

    public void setButtonExitListener(ActionListener l){
        this.mButtonPanel.getBtnExit().addActionListener(l);
    }
    

	public BundesligaTableContainer getTablePanel() {
		return mTablePanel;
	}

	public void setTablePanel(BundesligaTableContainer lTablePanel) {
		this.mTablePanel = lTablePanel;
	}

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

	public TipicoBetContainer getTipicoPanel() {
		return mTipicoPanel;
	}

	public void setTipicoPanel(TipicoBetContainer lTipicoPanel) {
		this.mTipicoPanel = lTipicoPanel;
	}

	public BundesligaConsoleContainer getConsolenPanel() {
		return mConsolenPanel;
	}

	public void setConsolenPanel(BundesligaConsoleContainer lConsolenPanel) {
		this.mConsolenPanel = lConsolenPanel;
	}
    
    
}
