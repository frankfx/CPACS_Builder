package de.presentation.bundesliga;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import de.presentation.ButtonPanelContainer;
import de.presentation.IDefaultGUI;

public class BundesligaView extends JFrame implements IDefaultGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BundesligaFixtureContainer mFixturePanel;
	private BundesligaConsoleContainer mConsolenPanel;
	private TipicoBetContainer mTipicoPanel;
	private ButtonPanelContainer mButtonPanel;
	
	@Override
	public void initView() {
		setTitle("1. Bundesliga View");
		createGUIElements();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(400, 300));
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
		mTipicoPanel = new TipicoBetContainer();
		mButtonPanel = new ButtonPanelContainer();
		
		c.fill = GridBagConstraints.BOTH;	
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.1;
		c.weighty = 0.1;
		pane.add(mConsolenPanel, c);

		c.gridx = 1;
		pane.add(mFixturePanel, c);

		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(mTipicoPanel, c);
		
		getContentPane().add(pane, BorderLayout.CENTER);
		getContentPane().add(mButtonPanel, BorderLayout.SOUTH);
	}

	
	/**
	 * ========================
	 * BEGIN GETTER AND SETTER
	 * ========================
	 */
    public void setButtonExitListener(ActionListener l){
        this.mButtonPanel.getBtnExit().addActionListener(l);
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
	/**
	 * ========================
	 * END GETTER AND SETTER
	 * ========================
	 */	
}
