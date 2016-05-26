package de.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import de.utils.SWJSONParser;

public class SWJSONParserTest {

	SWJSONParser parser;

	@Before
	public void setUp() {
		parser = new SWJSONParser();
	}

	@Test
	public void testJSONTableLineParser() throws NoSuchMethodException, SecurityException {
		Method method = parser.getClass().getDeclaredMethod("parseHTMLTableCellContent", String.class);
		method.setAccessible(true);

		try {
			assertTrue(((String) method.invoke(parser, "")).equals(""));
			assertTrue(((String) method.invoke(parser, " ")).equals(""));
			assertTrue(((String) method.invoke(parser, "         ")).equals(""));
			assertTrue(((String) method.invoke(parser, "<span class='timestamp' data-value='1432994400' data-format='ddd'>Sat</span></td>")).equals("Sat"));
			assertTrue(((String) method.invoke(parser, "<span class='timestamp' data-value='1432994400' data-format='dd/mm/yy'>30/05/15</span></td>")).equals("30/05/15"));
			assertTrue(((String) method.invoke(parser, "<a href=\"/national/belarus/2-division/2015/group-b/r31101/\" title=\"2. Division\">2.D</a></td>")).equals("2.D"));
			assertTrue(((String) method.invoke(parser, "<a href=\"/teams/belarus/fc-torpedo-ska-minsk/198/\" title=\"Tarpeda\">Tarpeda</a></td>")).equals("Tarpeda"));
			assertTrue(
					((String) method.invoke(parser, "<a href=\"/matches/2015/05/30/belarus/2-division/fc-torpedo-ska-minsk/kletsk/2029459/\" class=\"result-loss\">0 - 3</a></td>")).equals("0 - 3"));
			assertTrue(((String) method.invoke(parser, "<a href=\"/teams/belarus/kletsk/28033/\" title=\"Kletsk\">Kletsk</a></td>")).equals("Kletsk"));
			assertTrue(((String) method.invoke(parser, "</td>")).equals(""));
			assertFalse(((String) method.invoke(parser, "</td><td></td><td>")).equals("td"));

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
