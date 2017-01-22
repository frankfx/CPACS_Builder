package de.bundesliga;

import static org.junit.Assert.*;

import org.junit.Test;

import de.utils.Utils;

public class UtilsTest {

	@Test
	public void testGetIDWithoutSuffix() {
		assertNull(Utils.getIDWithoutSuffix(null));
		assertTrue(Utils.getIDWithoutSuffix("123_2").equals("123"));
		assertTrue(Utils.getIDWithoutSuffix("123").equals("123"));
		assertTrue(Utils.getIDWithoutSuffix("123_dasdgasdasd").equals("123"));
	}
}
