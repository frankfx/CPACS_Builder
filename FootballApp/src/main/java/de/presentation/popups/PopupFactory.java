package de.presentation.popups;

import javax.swing.JOptionPane;

import de.presentation.popups.popupViews.NewTipicoPopup;
import de.presentation.popups.popupViews.StartDatabaseBrowserPopup;
import de.presentation.popups.popupViews.StartDatabaseConnectionPopup;
import de.presentation.popups.popupViews.StartTipicoBetValuePopup;
import de.presentation.popups.popupViews.TableConfigPopup;

public class PopupFactory {
	
	public static IPopup getPopup(PopupType pPopupType, Object pParams) {
		return getPopup(pPopupType, new Object[] { pParams });
	}

	// use getPopup to get object of type popup
	public static IPopup getPopup(PopupType pPopupType, Object[] pParams) {
		if (pPopupType == null) {
			return null;
		} else if (pPopupType.equals(PopupType.NEW_TIPICO_POPUP)) {
			return new NewTipicoPopup(pParams);
		} else if (pPopupType.equals(PopupType.START_TIPICO_BETVALUE_POPUP)) {
			return new StartTipicoBetValuePopup(pParams);
		} else if (pPopupType.equals(PopupType.START_DATABASE_CONNECTION_POPUP)) {
			return new StartDatabaseConnectionPopup(pParams);
		} else if (pPopupType.equals(PopupType.START_DATABASE_BROWSER_POPUP)) {
			return new StartDatabaseBrowserPopup(pParams);
		} else if (pPopupType.equals(PopupType.START_TABLE_CONFIG_POPUP)) {
			return new TableConfigPopup(pParams);
		} else if (pPopupType.equals(PopupType.HINT)) {
			startHintPopup(pParams[0].toString());
		} else if (pPopupType.equals(PopupType.ERROR)) {
			startErrorPopup(pParams[0].toString());
		} 
		
		return null;
	}

	public static void startHintPopup(String pMessage) {
		JOptionPane.showMessageDialog(null, pMessage, "HINT", JOptionPane.WARNING_MESSAGE);
	}

	public static void startErrorPopup(String pMessage) {
		JOptionPane.showMessageDialog(null, pMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
}
