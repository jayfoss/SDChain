package com.fintare.transaction;

import com.google.gson.JsonObject;

public class Transaction {
	private String fromSignature;
	private String toSignature;
	private TransactionData data;

	public Transaction(TransactionData data, String fromSignature, String toSignature) {
		this.data = data;
		this.fromSignature = fromSignature;
		this.toSignature = toSignature;
	}

	public String getFromSignature() {
		return this.fromSignature;
	}

	public String getToSignature() {
		return this.toSignature;
	}

	public TransactionData getData() {
		return this.data;
	}
	
	public JsonObject getJSONObject() {
		final JsonObject json = new JsonObject();
		json.addProperty("fromSignature", fromSignature);
		json.addProperty("toSignature", toSignature);
		json.add("data", data.getJSONObject());
		return json;
	}
	
}