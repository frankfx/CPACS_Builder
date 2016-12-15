package de.utils.math;

import java.util.ArrayList;
import java.util.List;

import de.business.TipicoModel;

public class MathTipico {
	/**
	 * This function determines the new bet value
	 * 
	 * @param winValue the amount we want to win for one bet
	 * @param sumOldBet the amount we have lost in previous bets
	 * @param odds the rate of the game for a given result
	 * 
	 * @return the new bet value to win a specific amount and the whole stake (sum of this and all previous bets)
	 * 
	 * Formula: winValue + sumOldBet + newBet = newBet * odds	
	 *  		winValue + sumOldBet = newBet * odds - newBet
	 *  		winValue + sumOldBet = newBet * (odds-1)
	 *  		
	 *  		newBet = (winValue + sumOldBet) / (odds-1)
	 * 
	 */
	public static float computeBetValue(float winValue, float sumOldBet, float odds) {
		return odds > 1.0f ? (winValue + sumOldBet) / (odds - 1) : -1f;
	}
	
	public static List<Object []> getFinancialBetPrediction(List<TipicoModel> list){
		float lOdd = 3.0f;
		float lThreshold = 30.0f;
		
		List<Object []> lResult = new ArrayList<Object []>();
		for(TipicoModel lModel : list)
			lResult.add(getFinancialBetPrediction(lModel, lOdd, lThreshold));
		
		return lResult;
	}
	
	private static Object [] getFinancialBetPrediction(TipicoModel pTipicoModel, float pOdd, float pThreshold){
		float lExpenses = pTipicoModel.getExpenses();
		float lBetValue = 0f;
		
		List<String> list = new ArrayList<String>();
		list.add(pTipicoModel.getTeam());
		
		while (lExpenses < pThreshold){
			lBetValue = MathTipico.computeBetValue(pTipicoModel.getWinValue(), lExpenses, pOdd);
			lExpenses += lBetValue;
			list.add("[" + Math.round(100f*lBetValue)/100f + " -- " +  Math.round(100f*lExpenses)/100f + "]");
		}
		
		return list.toArray();
	}
}