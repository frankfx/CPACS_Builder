package de.services;

import java.awt.Desktop;
import java.net.URL;

public class HyperlinkService {
	
	public static void openUrlInBrowser(String pUrl) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				URL url = new URL(pUrl);
				desktop.browse(url.toURI());
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}
}
