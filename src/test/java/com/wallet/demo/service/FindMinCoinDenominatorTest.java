package com.wallet.demo.service;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit Test for FindMinCoinDenominator
 * 
 * @author Iden.Teo
 * 
 */
public class FindMinCoinDenominatorTest {

	@Tag("DEV")
	@DisplayName("Test - MinCoinDenominator() - Provide Input 3 - Return Denominator 2 and 1")
	@Test
	public void testMinCoinDenominator_IfGiveInput3_ReturnDenom2And1() {
		Assertions.assertEquals(Arrays.asList(2, 1), FindMinCoinDenominator.findMin(3));
	}

	@Tag("DEV")
	@DisplayName("Test - MinCoinDenominator() - Provide Input 6 - Return Denominator 5 and 1")
	@Test
	public void testMinCoinDenominator_IfGiveInput6_ReturnDenom5And1() {
		Assertions.assertEquals(Arrays.asList(5, 1), FindMinCoinDenominator.findMin(6));
	}

	@Tag("DEV")
	@DisplayName("Test - MinCoinDenominator() - Provide Input 5 - Return Denominator 5 only")
	@Test
	public void testMinCoinDenominator_IfGiveInput5_ReturnDenom5() {
		Assertions.assertEquals(Arrays.asList(5), FindMinCoinDenominator.findMin(5));
	}

	@Tag("DEV")
	@DisplayName("Test - MinCoinDenominator() - Provide Input 4 - Return Denominator 2, 2 only")
	@Test
	public void testMinCoinDenominator_IfGiveInput4_ReturnDenom5() {
		Assertions.assertEquals(Arrays.asList(2, 2), FindMinCoinDenominator.findMin(4));
	}

	@Tag("DEV")
	@DisplayName("Test - MinCoinDenominator() - Provide Input 30 - Return Denominator 10,10,10 only")
	@Test
	public void testMinCoinDenominator_IfGiveInput30_ReturnDenom101010() {
		Assertions.assertEquals(Arrays.asList(10, 10, 10), FindMinCoinDenominator.findMin(30));
	}
}
