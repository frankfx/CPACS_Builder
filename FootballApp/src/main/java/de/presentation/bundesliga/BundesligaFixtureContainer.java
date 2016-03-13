package de.presentation.bundesliga;

import java.awt.BorderLayout;
import java.awt.Color;

import de.presentation.AbstractPanelContainer;

public class BundesligaFixtureContainer extends AbstractPanelContainer {
	// create an default panel
	
	public BundesligaFixtureContainer() {	
		initPanel("Fixtures", new BorderLayout(), Color.WHITE);
	}
}
