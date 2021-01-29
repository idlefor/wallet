package com.wallet.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wallet Exception Class that handle run time exception
 * 
 * @author Iden.Teo
 * 
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WalletException extends RuntimeException {

	private static final long serialVersionUID = -275507748273967912L;

	public WalletException(final String message) {
		super(message);
	}
}
