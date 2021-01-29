package com.wallet.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wallet Not Enough Change Exception Class that handle run time exception
 * 
 * @author Iden.Teo
 * 
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class WalletNotEnoughPayException extends RuntimeException {

	private static final long serialVersionUID = -27550774855787912L;

	public WalletNotEnoughPayException(final String message) {
		super(message);
	}
}
