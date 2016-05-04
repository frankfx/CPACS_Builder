package de.utils.math;

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
}
