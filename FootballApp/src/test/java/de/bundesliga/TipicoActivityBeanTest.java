package de.bundesliga;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.application.TipicoActivityBean;
import de.business.TipicoModel;
import de.business.TipicoTableModel;
import de.presentation.bundesliga.TipicoBetView;
import de.presentation.popups.IPopup;
import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class TipicoActivityBeanTest {
	TipicoActivityBean lSpy;
	
	@Before
	public void setUp() {
		lSpy = spy(new TipicoActivityBean(new TipicoBetView()));
	}
	
	@Test
	public void testSetTipicoModelsToSuccess(){
		List<TipicoModel> lBetsInView = new ArrayList<TipicoModel>();
		lBetsInView.add(new TipicoModel("123"));
		lBetsInView.add(new TipicoModel("456"));
		
		when(lSpy.getTipicoModelsAsList()).thenReturn(lBetsInView);
		
		List<String> lTipicoIDs = new ArrayList<String>();
		lTipicoIDs.add("456");
		
		lSpy.setTipicoModelsToSuccess(lTipicoIDs);
		
		assertFalse(lBetsInView.get(0).getSuccess());
		assertTrue(lBetsInView.get(1).getSuccess());
	}
	
	@Ignore
	@Test
	public void testActionBetValue() throws NoSuchMethodException, SecurityException{
		TipicoTableModel lTipicoTableModel = mock(TipicoTableModel.class);
		when(lTipicoTableModel.getTipicoModelAtRow(0)).thenReturn(new TipicoModel("100"));
		
		IPopup lTipicoBetValuePopup = mock(IPopup.class);
		when(lTipicoBetValuePopup.requestInputData()).thenReturn(new String[]{"5", "74"});
		
		TipicoActivityBean lController = new TipicoActivityBean(new TipicoBetView());

		Method method = lController.getClass().getDeclaredMethod("actionBetValue");
		method.setAccessible(true);
		
		try {
			method.invoke(lController);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}	
	
	
	
	
	
}

