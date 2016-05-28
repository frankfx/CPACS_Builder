package de.utils;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class DoubleInputVerifier extends InputVerifier {

	private final String pattern = "-?\\d+(\\.\\d+)?";

	@Override
	public boolean verify(final JComponent input) {

		JTextField field = (JTextField) input;

		if (isValid(field.getText())) {
			field.setBackground(Color.WHITE);
			field.setText(format(field.getText()));
			return true;
		} else {
			field.setBackground(Color.CYAN);
			return false;
		}
	}

	public boolean isValid(String pInput) {
		return pInput.matches(pattern) || pInput.equals("");
	}

	public boolean isValid(final String existing, final char newChar, final int insertPos) {
		// no spaces allowed:
		if (Character.isSpaceChar(newChar))
			return false;

		// must be possible to delete:
		if (KeyEvent.VK_BACK_SPACE == newChar)
			return true;

		// allow dots for decimals
		if ('.' == newChar)
			return true;

		return isValid(new StringBuffer(existing).insert(insertPos, newChar).toString());
	}

	public String format(final String input) {
		return input.equals("") ? input : Double.parseDouble(input) + "";
	}
}
