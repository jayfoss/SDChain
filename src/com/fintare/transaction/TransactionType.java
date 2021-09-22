package com.fintare.transaction;

public enum TransactionType {
	PROMISE("promise"), PUSH("push"), PULL("pull"), CHARGE("charge"), REVOKE("revoke");
	private String type;

	public String toString() {
		return this.type;
	}

	private TransactionType(String type) {
		this.type = type;
	}
}