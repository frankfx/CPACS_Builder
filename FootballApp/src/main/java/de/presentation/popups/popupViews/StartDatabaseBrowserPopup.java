package de.presentation.popups.popupViews;

import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import de.business.TipicoModel;
import de.presentation.popups.DefaultPopup;
import de.presentation.popups.IPopup;

public class StartDatabaseBrowserPopup implements IPopup {

	public StartDatabaseBrowserPopup(Object[] pParams) {
		startDatabaseBrowser(Arrays.copyOf(pParams, pParams.length, TipicoModel[].class));
	}
	
	public void startDatabaseBrowser(TipicoModel[] pTipicos) {
		DefaultListModel<TipicoModel> lListModel = new DefaultListModel<TipicoModel>();
		
		for(TipicoModel tm : pTipicos)
			lListModel.addElement(tm);
		
		JList<TipicoModel> lList = new JList<TipicoModel>(lListModel);
		
		JScrollPane fruitListScrollPane = new JScrollPane(lList);
		
		Object[] message = {"List", fruitListScrollPane};
		
		DefaultPopup.runPopup(message, "database browser");
		
	}

	@Override
	public String[] requestInputData() {
		return null;
	}	
}
