package com.fintare.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RequestWorkerThread implements Runnable {
	private RawData data;
	private PeerNetwork network;
	
	protected RequestWorkerThread(RawData data, PeerNetwork network) {
		this.data = data;
		this.network = network;
	}
	
	public void run() {
		JsonParser parser = new JsonParser();
		JsonElement parsed = parser.parse(data.getContent());
		if(parsed == null || !parsed.isJsonObject()) {
			this.sendBadContentFormat("No JSON could be parsed from the received data.");
			return;
		}
		JsonObject json = (JsonObject) parsed;
		if(!json.get("headers").isJsonObject()) {
			this.sendBadContentFormat("Received content missing headers.");
			return;
		}
		JsonObject headers = json.get("headers").getAsJsonObject();
		if(!headers.has("type")) {
			this.sendBadContentFormat("Received content missing type");
			return;
		}
	}
	
	protected void sendBadContentFormat(String message) {
		this.sendError("bad_content_format", message);
	}
	
	protected void sendError() {
		this.sendError("error", "An error has occurred.");
	}
	
	protected void sendError(String code, String message) {
		DataPackage response = new DataPackage();
		response.addHeader("type", "response");
		response.addHeader("error", "true");
		response.addHeader("errorCode", code);
		response.addHeader("errorMessage", message);
		this.sendToPeer(response.getJSONObject());
	}
	
	public void sendToPeer(JsonObject json) {
		network.getPeer(data.getPeerId()).out(json.toString());
	}
	
}
