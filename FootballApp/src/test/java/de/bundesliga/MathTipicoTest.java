package de.bundesliga;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.business.TipicoModel;
import de.utils.math.MathTipico;

public class MathTipicoTest {
		
	@Test
	public void testComputeBetValue(){
		assertTrue(MathTipico.computeBetValue(2, 0, 3) == 1);
		assertTrue(MathTipico.computeBetValue(2, 2, 3) == 2);
		assertFalse(MathTipico.computeBetValue(2, 2, 3) == 1);
	}
		
	@Test
	public void testFinancialBetPrediction(){
		List<TipicoModel> list = new ArrayList<TipicoModel>();
			
		list.add(new TipicoModel());
		list.add(new TipicoModel());
		list.add(new TipicoModel());

		for(Object [] lRowData : MathTipico.getFinancialBetPrediction(list)){
			assertTrue(lRowData[0].equals("Default"));
			assertTrue(lRowData[1].equals("[0.5 -- 0.5]"));
			assertTrue(lRowData[2].equals("[0.75 -- 1.25]"));
			assertTrue(lRowData[3].equals("[1.13 -- 2.38]"));
			assertTrue(lRowData[4].equals("[1.69 -- 4.06]"));
			assertTrue(lRowData[5].equals("[2.53 -- 6.59]"));
			assertTrue(lRowData[6].equals("[3.8 -- 10.39]"));
			assertTrue(lRowData[7].equals("[5.7 -- 16.09]"));
			assertTrue(lRowData[8].equals("[8.54 -- 24.63]"));
			assertTrue(lRowData[9].equals("[12.81 -- 37.44]"));
			assertFalse(lRowData[9].equals("[12.81 -- 37.4]"));
		}
	}
}
