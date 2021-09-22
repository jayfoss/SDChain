package com.fintare.network;

import java.util.HashMap;
import java.util.Map;

import com.fintare.util.IJson;
import com.google.gson.JsonObject;

public class DataPackage implements IJson {
	private HashMap<String, String> headers = new HashMap<String, String>(10);
	private IJson body;
	
	public HashMap<String, String> getHeaders(){
		return headers;
	}
	
	public String getHeader(String key) {
		return headers.get(key);
	}
	
	public DataPackage addHeader(String key, String value) {
		headers.put(key, value);
		return this;
	}
	
	public IJson getBody() {
		return body;
	}
	
	public DataPackage setBody(IJson body) {
		this.body = body;
		return this;
	}
	
	public String toString() {
		return this.getJSON();
	}

	@Override
	public JsonObject getJSONObject() {
		final JsonObject headers = new JsonObject();
		for (Map.Entry<String, String> entry : this.headers.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();
		    headers.addProperty(key, value);
		}
		final JsonObject json = new JsonObject();
		json.add("headers", headers);
		if(this.body != null) {
			json.add("body", body.getJSONObject());
		}
		return json;
	}

	@Override
	public String getJSON() {
		return this.getJSONObject().toString();
	}
}
