package de.bundesliga;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.utils.math.MathTipico;

public class TestTest {

	@Before
	public void setUp() {
		//    	model = new SpendingModel();
		//    	item1 = new SpendingItem();//id=-1
		//    	item2 = new SpendingItem(new String[]{"1", "2016-02-02", null, "test desciption", "3.2", "true"});
	}

	
	@Test
	public void testComputeBetValue(){
		assertTrue(MathTipico.computeBetValue(2, 0, 3) == 1);
		assertTrue(MathTipico.computeBetValue(2, 2, 3) == 2);
	}
	
	@Test
	public void testCompareTo() {
		//		// item1 < item2
		//		assertTrue(item1.compareTo(item2)<0);
		//		
		//		// item1 > item2
		//		item1.setId(2);
		//		assertTrue(item1.compareTo(item2)>0);
		//
		//		// item1 = item2
		//		item1.setId(1);
		//		assertTrue(item1.compareTo(item2)==0);		
	}

	
	
	@Test
	public void testActivityEnum() {
		//		assertTrue(ActivityEnum.getType("hoLidAY").equals(ActivityEnum.HOLIDAY));
		//		assertTrue(ActivityEnum.getType(null).equals(ActivityEnum.EMPTY));
		//		assertTrue(ActivityEnum.getType("so ein lustiger Test").equals(ActivityEnum.EMPTY));
		//		assertFalse(ActivityEnum.getType("others").equals(ActivityEnum.EMPTY));
	}

	@Test
	public void testGenerateValidID() {
		//    	model.getSpendingTableModel().addItem(item1);
		//    	model.getSpendingTableModel().addItem(item2);
		//    	assertTrue(model.getSpendingTableModel().generateValidID() == 3);
		//    	item2.setId(3);
		//    	assertTrue(model.getSpendingTableModel().generateValidID() == 4);
	}
}

