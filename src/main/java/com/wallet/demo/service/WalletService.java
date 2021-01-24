package com.wallet.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.wallet.demo.entity.Wallet;
import com.wallet.demo.exception.WalletException;
import com.wallet.demo.repository.WalletRepository;

@Service
public class WalletService {

	@Autowired
	private WalletRepository walletRepository;

	public List<Wallet> getAll() {
		return walletRepository.findAllByOrderByPriority();
	}

	public Wallet getById(final Long id) {
		Optional<Wallet> wallet = walletRepository.findById(id);
		if (wallet.isPresent()) {
			// sort in ascending order
			Collections.sort(wallet.get().getCoinList());
			return wallet.get();
		}
		throw new WalletException("Wallet with " + id + " does not exists!");
	}

	public Wallet createOrUpdate(final Wallet wallet) {
		if (wallet.getId() == null) {
			walletRepository.save(wallet);
		} else {
			walletRepository.save(wallet);
		}

		return wallet;
	}

	public boolean delete(final Long id) {
		Optional<Wallet> wallet = walletRepository.findById(id);
		if (wallet.isPresent()) {
			walletRepository.delete(wallet.get());
			return true;
		}
		throw new WalletException("Wallet with " + id + " does not exists!");
	}

	public List<Integer> computeCoinPayment(final Integer payCoin, final List<Integer> coinList,
			final boolean exactpayment) {
		List<Integer> temp = new ArrayList<Integer>();
		// FIND THE MIN NO OF DENOMINATOR COIN DERIVED FROM PAYCOIN
		List<Integer> minDenomCoinList = FindMinCoinDenominator.findMin(payCoin);

		if (exactpayment) {
			// [PAYMENT THAT DONT NEED ANY CHANGE IN RETURN]
			temp = handleCoinPaymentWithoutChange(coinList, minDenomCoinList, payCoin);
		} else {
			// [PAYMENT THAT NEED ANY SOME CHANGE IN RETURN]
			for (int j = 0; j < coinList.size(); j++) {
				int coinSum = 0;
				int coinValue = coinList.get(j);
				coinSum += coinValue;
				for (int k = 0; k < minDenomCoinList.size(); k++) {
					int denomCoinValue = minDenomCoinList.get(k);
					// [Logic that handles ODD coin payment such Pay(1) with Change]
					temp = handleOddCoinPaymentWithChange(temp, j, coinValue, denomCoinValue);
					// [Logic that handles EVEN coin payment such Pay(2) with Change]
					temp = handleEvenCoinPaymentWithChange(temp, j, coinValue, coinSum, denomCoinValue);
				}
			}

			return temp;
		}

		return coinList;
	}

	/**
	 * Business Logic to handle payment of coin that doesn't required any changes
	 * 
	 * [1st test case]: [1,1,2,2,3] become [1,2,2,3] after execute pay(1)
	 * 
	 * [2nd test case]: [1,2,2,3] become [2,3] after execute pay(3)
	 * 
	 * @param coinList         a list of coins hold inside a arrayList
	 * @param minDenomCoinList a list of denominator coins that is derive from the
	 *                         pay coin. eg pay(3) become [2,1] with denom 2 and 1
	 * @param payCoin          pay coin amount
	 * 
	 * @return final result of computed coin list
	 */
	protected List<Integer> handleCoinPaymentWithoutChange(final List<Integer> coinList,
			final List<Integer> minDenomCoinList, final int payCoin) {
		int coinSum = 0;
		for (int i = 0; i < coinList.size(); i++) {
			int coinValue = coinList.get(i);

			if (minDenomCoinList.contains(coinValue))
				coinList.remove(i);

			if (!CollectionUtils.isEmpty(coinList))
				coinSum += coinValue;
		}

		// 5th test case [Not Enough Change] return -1 so we can throw error
		if (payCoin > coinSum)
			return Arrays.asList(-1);

		return coinList;
	}

	/**
	 * Business Logic to handle payment of coin which is ODD that required changes
	 * 
	 * [3rd test case]: [2,3] become [1,3] after pay(1)
	 * 
	 * if payCoin < CoinList 1st element & payCoin is ODD number, deduce payCoin
	 * from this element and return whole array as end result computation
	 * 
	 * @param temp           temporary arrayList that hold final result of coins
	 * @param j              index
	 * @param coinValue      each individual coin value during iteration of the list
	 * @param denomCoinValue each individual pay coin value during computation
	 *                       process
	 * 
	 * @return final result of computed coin list
	 */
	protected List<Integer> handleOddCoinPaymentWithChange(final List<Integer> temp, final int j, int coinValue,
			final int denomCoinValue) {
		if (denomCoinValue < coinValue && denomCoinValue % 2 != 0) {
			if (j % 2 == 0) // deduce value from CoinList's every EVEN index
				coinValue -= denomCoinValue;
			temp.add(coinValue);
		}

		return temp;
	}

	/**
	 * Business Logic to handle payment of coin which is EVEN that required changes
	 * 
	 * [4th test case]: [1,3] become [2] after pay(2)
	 * 
	 * if payCoin is EVEN number, if payCoin < coinList's element value and if it is
	 * ODD index we deduce that element value with the payCoin value and return the
	 * whole array
	 * 
	 * 
	 * @param temp           temporary arrayList that hold final result of coins
	 * @param j              index
	 * @param coinValue      each individual coin value during iteration of the list
	 * @param coinSum        sum of the coin value in CoinList
	 * @param denomCoinValue each individual pay coin value during computation
	 *                       process
	 */
	protected List<Integer> handleEvenCoinPaymentWithChange(final List<Integer> temp, final int j, final int coinValue,
			final int coinSum, final int denomCoinValue) {
		if (denomCoinValue % 2 == 0) {
			if (denomCoinValue < coinValue && j % 2 != 0) // deduce value from every ODD element
				temp.add(coinSum - denomCoinValue);
		}

		return temp;
	}

}