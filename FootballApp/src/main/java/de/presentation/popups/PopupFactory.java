package de.presentation.popups;

import javax.swing.JOptionPane;

import de.presentation.popups.popupViews.DatabaseBrowserPopup;
import de.presentation.popups.popupViews.DatabaseConnectionPopup;
import de.presentation.popups.popupViews.DatabasePullDetailPopup;
import de.presentation.popups.popupViews.SWIDsPopup;
import de.presentation.popups.popupViews.SWPropertyPopup;
import de.presentation.popups.popupViews.TipicoAccountBalancePopup;
import de.presentation.popups.popupViews.TipicoBetValuePopup;
import de.presentation.popups.popupViews.TipicoNewPopup;
import de.presentation.popups.popupViews.TipicoTableFilterPopup;

public class PopupFactory {
	
	public static IPopup getPopup(PopupType pPopupType, Object pParams) {
		return getPopup(pPopupType, new Object[] { pParams });
	}

	// use getPopup to get object of type popup
	public static IPopup getPopup(PopupType pPopupType, Object[] pParams) {
		switch (pPopupType) {
		case TIPICO_NEW_POPUP:
			return new TipicoNewPopup(pParams);
		case TIPICO_BETVALUE_POPUP:
			return new TipicoBetValuePopup(pParams);
		case TIPICO_TABLE_FILTER_POPUP:
			return new TipicoTableFilterPopup(pParams);
		case TIPICO_ACCOUNT_BALANCE_POPUP :
			return new TipicoAccountBalancePopup(pParams);
		case DATABASE_CONNECTION_POPUP:
			return new DatabaseConnectionPopup(pParams);
		case DATABASE_BROWSER_POPUP:
			return new DatabaseBrowserPopup(pParams);
		case DATABASE_PULLDETAIL_POPUP:
			return new DatabasePullDetailPopup(pParams);
		case PROPERTIES_POPUP :
			return new SWPropertyPopup(pParams);
		case SWIDS_POPUP :
			return new SWIDsPopup(pParams);
		case HINT:
			startHintPopup(pParams[0].toString());
			return null;
		case ERROR:
			startErrorPopup(pParams[0].toString());
			return null;
		default:
			return null;
		}
	}

	public static void startHintPopup(String pMessage) {
		JOptionPane.showMessageDialog(null, pMessage, "HINT", JOptionPane.WARNING_MESSAGE);
	}

	public static void startErrorPopup(String pMessage) {
		JOptionPane.showMessageDialog(null, pMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
}
