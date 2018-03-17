package de.utils;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class TeamIDInputVerifier extends InputVerifier {

	private final String pattern = "\\d+(_.+)?";

	@Override
	public boolean verify(final JComponent input) {

		JTextField field = (JTextField) input;

		if (isValid(field.getText())) {
			field.setBackground(Color.WHITE);
			return true;
		} else {
			field.setBackground(Color.CYAN);
			return false;
		}
	}

	private boolean isValid(String pInput) {
		return pInput.matches(pattern) || pInput.equals("");
	}

	public boolean isValid(final String existing, final char newChar, final int insertPos) {
		// no spaces allowed:
		if (Character.isSpaceChar(newChar))
			return false;

		// must be possible to delete:
		if (KeyEvent.VK_BACK_SPACE == newChar)
			return true;

		// allow underscore for multiple ids
		if ('_' == newChar)
			return true;

		return isValid(new StringBuffer(existing).insert(insertPos, newChar).toString());
	}
}
