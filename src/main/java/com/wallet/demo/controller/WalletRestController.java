package com.wallet.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.demo.entity.Wallet;
import com.wallet.demo.service.ValidationErrorService;
import com.wallet.demo.service.WalletService;

/**
 * Simple Wallet Rest Controller
 * 
 * @author Iden.Teo
 * 
 */
@RestController
@RequestMapping("/wallet")
@CrossOrigin
public class WalletRestController {

	@Autowired
	private WalletService walletService;
	@Autowired
	private ValidationErrorService validationService;

	@GetMapping("/all")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(walletService.getAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable final Long id) {
		return new ResponseEntity<>(walletService.getById(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Wallet> create(@Valid @RequestBody final Wallet wallet, final BindingResult result) {
		ResponseEntity<?> errors = validationService.validate(result);
		if (errors != null)
			return (ResponseEntity<Wallet>) errors;

		Wallet walletSaved = walletService.createOrUpdate(wallet);
		return new ResponseEntity<Wallet>(walletSaved, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable final Long id, @Valid @RequestBody final Wallet wallet,
			final BindingResult result) {
		ResponseEntity<?> errors = validationService.validate(result);
		if (errors != null)
			return errors;
		wallet.setId(id);
		Wallet walletSaved = walletService.createOrUpdate(wallet);
		return new ResponseEntity<Wallet>(walletSaved, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable final Long id) {
		walletService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/{id}/{paycoin}/{exactpayment}")
	public ResponseEntity<?> payCoinFromCoinList(@PathVariable final Long id, @PathVariable final Integer paycoin,
			@PathVariable final boolean exactpayment) {

		// retrieve the wallet with provided id
		Wallet wallet = walletService.getById(id);
		// business logic to handle payment in the coin list
		List<Integer> coinList = wallet.getCoinList();
		if (CollectionUtils.isEmpty(coinList))
			return new ResponseEntity<String>("Unable proceed as coinlist is empty", HttpStatus.INTERNAL_SERVER_ERROR);

		// computation of the coin payment
		List<Integer> computedCoinList = walletService.computeCoinPayment(paycoin, coinList, exactpayment);

		// if computedCoinList is empty and pay coin > 0 mean we cannot pay throw error
		if (computedCoinList != null && computedCoinList.get(0) == -1)
			return new ResponseEntity<String>("You do not have suffice coin to pay " + paycoin,
					HttpStatus.INTERNAL_SERVER_ERROR);

		wallet.setCoinList(computedCoinList);
		Wallet walletSaved = walletService.createOrUpdate(wallet);

		return new ResponseEntity<Wallet>(walletSaved, HttpStatus.OK);
	}
}
