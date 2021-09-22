package com.fintare.transaction;

import com.fintare.util.IJson;
import com.google.gson.JsonObject;

public abstract class TransactionData implements IJson{
	private String id;
	private TransactionType type;
	private long authTime;
	private String promise;

	// Default constructor
	TransactionData(TransactionType type) {
		this.type = type;
	}
	
	public String getId() {
		return id;
	}
	
	public TransactionData setId(String id) {
		this.id = id;
		return this;
	}
	
	public TransactionType getType() {
		return this.type;
	}
	
	public long getAuthTime() {
		return authTime;
	}

	public TransactionData setAuthTime(long authTime) throws IllegalTransactionDataException {
		if (authTime < 0) {
			throw new IllegalTransactionDataException("Auth time must not be less than 0.");
		}
		this.authTime = authTime;
		return this;
	}
	
	public String getPromise() {
		return promise;
	}

	public TransactionData setPromise(String promise) {
		this.promise = promise;
		return this;
	}
	
	public String getJSON() {
		return this.getJSONObject().toString();
	}
	
	public JsonObject getJSONObject() {
		final JsonObject json = new JsonObject();
		json.addProperty("id", id);
		json.addProperty("type", type.toString());
		json.addProperty("authTime", authTime);
		json.addProperty("promise", promise);
		return json;
	}
}