package com.fintare.block;

import java.security.NoSuchAlgorithmException;

import com.fintare.security.SecurityHelper;
import com.fintare.transaction.Transaction;
import com.fintare.util.CommonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Block {
	private String id;
	private long timestamp;
	private transient Transaction[] transactions;
	private String hash;
	private String previousHash;
	private int difficulty;
	private long nonce;

	public String getId() {
		return id;
	}

	public Block setId(String id) {
		this.id = id;
		return this;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Block setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public Transaction[] getTransactions() {
		return transactions;
	}

	public Block setTransactions(Transaction[] transactions) {
		this.transactions = transactions;
		return this;
	}

	public String getHash() {
		return hash;
	}

	public Block setHash(String hash) {
		this.hash = hash;
		return this;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public Block setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
		return this;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public Block setDifficulty(int difficulty) {
		this.difficulty = difficulty;
		return this;
	}

	public long getNonce() {
		return nonce;
	}

	public Block setNonce(long nonce) {
		this.nonce = nonce;
		return this;
	}
	
	public JsonArray getTransactionJSONArray() {
		final JsonArray json = new JsonArray();
		for(Transaction t : transactions) {
			json.add(t.getJSONObject());
		}
		return json;
	}
	
	public JsonObject getJSONObjectBeforeHash() {
		final JsonObject json = new JsonObject();
		json.addProperty("id", id);
		json.addProperty("timestamp", timestamp);
		json.addProperty("previousHash", previousHash);
		json.addProperty("difficulty", difficulty);
		json.addProperty("nonce", nonce);
		json.add("transactions", this.getTransactionJSONArray());
		return json;
	}
	
	public String getJSONBeforeHash() {
		return this.getJSONObjectBeforeHash().toString();
	}
	
	public JsonObject getJSONObject() {
		final JsonObject json = this.getJSONObjectBeforeHash();
		json.addProperty("hash", hash);
		return json;
	}
	
	public String getJSON() {
		return this.getJSONObject().toString();
	}
	
	public String calculateHash() {
		SecurityHelper s = new SecurityHelper();
		try {
			return CommonUtils.toHex(s.sha256(this.getJSONBeforeHash()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
