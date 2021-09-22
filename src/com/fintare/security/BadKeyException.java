package com.fintare.security;

public class BadKeyException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3904408070022467142L;

	public BadKeyException() {
		super();
	}

	public BadKeyException(String message) {
		super(message);
	}
}
