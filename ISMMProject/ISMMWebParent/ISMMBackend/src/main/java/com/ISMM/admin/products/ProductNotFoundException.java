package com.ISMM.admin.products;

public class ProductNotFoundException extends Exception {
	
	
	/**
	 * Message is detailed based on stack trace, and returns 
	 * 	a human readable error message.
	 */
	public ProductNotFoundException(String message) {
		super(message);
	}

}
