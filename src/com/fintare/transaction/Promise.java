package com.fintare.transaction;

import com.google.gson.JsonObject;

public class Promise extends TransactionData {
	private String fromAddress;
	private String toAddress;
	private long value;
	private long protectionStartTime;
	private long protectionEndTime;
	private long chargeEndTime;

	// Default constructor
	public Promise() {
		super(TransactionType.PROMISE);
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public Promise setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
		return this;
	}

	public String getToAddress() {
		return toAddress;
	}

	public Promise setToAddress(String toAddress) {
		this.toAddress = toAddress;
		return this;
	}

	public long getValue() {
		return value;
	}

	public Promise setValue(long value) throws IllegalTransactionDataException {
		if (value < 0) {
			throw new IllegalTransactionDataException("Value must not be less than 0.");
		}
		this.value = value;
		return this;
	}

	public long getProtectionStartTime() {
		return protectionStartTime;
	}

	public Promise setProtectionStartTime(long protectionStartTime) throws IllegalTransactionDataException {
		if (protectionStartTime < 0) {
			throw new IllegalTransactionDataException("Protection start time must not be less than 0.");
		}
		this.protectionStartTime = protectionStartTime;
		return this;
	}

	public long getProtectionEndTime() {
		return protectionEndTime;
	}

	public Promise setProtectionEndTime(long protectionEndTime) throws IllegalTransactionDataException {
		if (protectionEndTime < 0) {
			throw new IllegalTransactionDataException("Protection end time must not be less than 0.");
		}
		this.protectionEndTime = protectionEndTime;
		return this;
	}

	public long getChargeEndTime() {
		return chargeEndTime;
	}

	public Promise setChargeEndTime(long chargeEndTime) throws IllegalTransactionDataException {
		if (chargeEndTime < 0) {
			throw new IllegalTransactionDataException("Charge end time must not be less than 0.");
		}
		this.chargeEndTime = chargeEndTime;
		return this;
	}
	
	public JsonObject getJSONObject() {
		final JsonObject json = super.getJSONObject();
		json.addProperty("fromAddress", fromAddress);
		json.addProperty("toAddress", toAddress);
		json.addProperty("value", value);
		json.addProperty("protectionStartTime", protectionStartTime);
		json.addProperty("protectionEndTime", protectionEndTime);
		json.addProperty("chargeEndTime", chargeEndTime);
		return json;
	}
	
	public String getJSON() {
		return this.getJSONObject().toString();
	}
}