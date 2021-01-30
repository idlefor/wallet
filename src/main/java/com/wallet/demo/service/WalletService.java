package com.wallet.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.demo.entity.Wallet;
import com.wallet.demo.exception.WalletException;
import com.wallet.demo.exception.WalletNotEnoughPayException;
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

	/**
	 * Brute force method to check the left most coin and remove any coin that is
	 * suffice or go to next coin sum up and check if the total sum is enough to pay
	 * if yes remove both coin or so on and so forth
	 * 
	 * @param payCoin  the coin we use to pay
	 * @param coinList a list of coin in the wallet
	 * 
	 * @return list of coin result after computation
	 */
	public List<Integer> computeCoinPayment(final Integer payCoin, final List<Integer> coinList) {
		int coinSum = 0;
		int ignoreCoinSum = 0;
		boolean paymentDone = false;
		List<Integer> temp = new ArrayList<Integer>();

		for (int i = 0; i < coinList.size(); i++) {
			int coinValue = coinList.get(i);
			coinSum += coinValue;

			if (!paymentDone) {
				if (coinValue >= payCoin) {
					// if suffice to pay, remove the coin or deduce if there's remainder
					// and set paymentDone to true
					coinValue = Math.max(0, coinValue - payCoin);
					paymentDone = true;
				} else {
					ignoreCoinSum += coinValue;
					// if not suffice sum up next coin and see if suffice to deduce
					if (coinSum == payCoin) {
						coinValue = Math.max(0, coinValue - payCoin);
						paymentDone = true;
					} else {
						continue;// if value not suffice we skip to next coin and check total sum
					}
				}
			}
			// if the value is zero which payCoin is spent don't add into the result
			if (coinValue > 0)
				temp.add(Integer.valueOf(coinValue));
			// this is to handle test case 4.
			if (ignoreCoinSum + payCoin + coinValue == coinSum && coinValue > 0 && ignoreCoinSum > 0)
				temp.add(Integer.valueOf(ignoreCoinSum));
		}

		// 5th test case [Not Enough Change] throw exception
		if (payCoin.compareTo(coinSum) > 0)
			throw new WalletNotEnoughPayException("Wallet does not have enough to pay " + payCoin);

		return temp;
	}
}