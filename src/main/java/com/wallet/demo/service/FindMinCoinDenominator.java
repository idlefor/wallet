package com.wallet.demo.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility Class that use algorithm to find the no of denominator per input
 * Integer and will order them in descending sequence of a list.
 * 
 * @author Iden.Teo
 * 
 */
public class FindMinCoinDenominator {

	// All denominations of Coin Currency
	static int deno[] = { 1, 2, 5, 10, 50, 100, 1000 };
	static int n = deno.length;

	static List<Integer> findMin(int V) {
		// Initialize result
		List<Integer> ans = new ArrayList<Integer>();

		// Traverse through all denomination
		for (int i = n - 1; i >= 0; i--) {
			// Find denominations
			while (V >= deno[i]) {
				V -= deno[i];
				ans.add(deno[i]);
			}
		}

		return ans;
	}

}
