package de.presentation.popups;

import javax.swing.JOptionPane;

import de.presentation.popups.popupViews.DatabaseBrowserPopup;
import de.presentation.popups.popupViews.DatabaseConnectionPopup;
import de.presentation.popups.popupViews.DatabasePullDetailPopup;
import de.presentation.popups.popupViews.TableFilterPopup;
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
		case DATABASE_CONNECTION_POPUP:
			return new DatabaseConnectionPopup(pParams);
		case DATABASE_BROWSER_POPUP:
			return new DatabaseBrowserPopup(pParams);
		case DATABASE_PULLDETAIL_POPUP:
			return new DatabasePullDetailPopup(pParams);
		case HINT:
			startHintPopup(pParams[0].toString());
		case ERROR:
			startErrorPopup(pParams[0].toString());
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
