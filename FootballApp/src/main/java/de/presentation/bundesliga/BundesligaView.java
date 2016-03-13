package de.presentation.bundesliga;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.presentation.ButtonPanelContainer;
import de.presentation.IDefaultGUI;

public class BundesligaView extends JFrame implements IDefaultGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BundesligaTableContainer mTablePanel;
	BundesligaFixtureContainer mFixturePanel;
	ButtonPanelContainer mButtonPanel;
	
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
		pane.setLayout(new GridLayout(1, 2));
		
		mTablePanel = new BundesligaTableContainer();
		mFixturePanel = new BundesligaFixtureContainer();
		mButtonPanel = new ButtonPanelContainer();
		
		pane.add(mTablePanel);
		pane.add(mFixturePanel);
		
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
}
