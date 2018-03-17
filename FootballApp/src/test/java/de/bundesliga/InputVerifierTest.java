package de.bundesliga;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.utils.DoubleInputVerifier;
import de.utils.TeamIDInputVerifier;

public class InputVerifierTest {

	DoubleInputVerifier lDoubleVerifier;
	TeamIDInputVerifier lTeamIDVerifier;
	
	@Before
	public void setUp() {
		lDoubleVerifier = new DoubleInputVerifier();
		lTeamIDVerifier = new TeamIDInputVerifier();
	}
	
	@Test
	public void testIsValidDoubleVerifier(){
		assertTrue(lDoubleVerifier.isValid("133.1", '1', 3));
		assertTrue(lDoubleVerifier.isValid("133", '6', 0));
		assertTrue(lDoubleVerifier.isValid("133.1", '1', 3));
		assertFalse(lDoubleVerifier.isValid("133.1t", '1', 3));
		assertFalse(lDoubleVerifier.isValid("", ' ', 3));
	}
	
	@Test
	public void testIsValidTeamIDVerifier(){
		assertTrue(lTeamIDVerifier.isValid("1313132", '1', 3));
		assertTrue(lTeamIDVerifier.isValid("133_", 't', 4));
		assertTrue(lTeamIDVerifier.isValid("", '1', 0));
		assertFalse(lTeamIDVerifier.isValid("133.1t", '1', 3));
		assertFalse(lTeamIDVerifier.isValid("1344_", '1', 3));
		assertFalse(lTeamIDVerifier.isValid("aaa", 'a', 3));
	}	
}

