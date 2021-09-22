package com.fintare.network;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerNetwork {
	private HashMap<String, PeerClient> peers = new HashMap<String, PeerClient>();
	private ExecutorService executor = Executors.newFixedThreadPool(5);
	
	public void addRequest(RawData request) {
		Runnable worker = new RequestWorkerThread(request, this);
		executor.execute(worker);
	}
	
	public PeerClient getPeer(String id) {
		return peers.get(id);
	}
}