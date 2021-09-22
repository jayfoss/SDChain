package com.fintare.main;

import com.fintare.wallet.Wallet;

public class Main {
	public static final void main(String[] args) {
		new Wallet("keys.pem", "test");
	}
}
