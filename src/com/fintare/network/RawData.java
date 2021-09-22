package com.fintare.network;

public class RawData {
	private String peerId;
	private String content;
	
	public RawData(String peerId, String content) {
		this.peerId = peerId;
		this.content = content;
	}

	public String getPeerId() {
		return peerId;
	}

	public String getContent() {
		return content;
	}

}
