package com.fintare.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeerClient implements Runnable {
	private String peerId;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private PeerNetwork network;
	private boolean terminate = false;
	private StringBuilder data = new StringBuilder();
	private static final Logger LOGGER = Logger.getLogger(PeerClient.class.getName());
	/**
	 * Monitor whether this client connection has completed its handshake
	 */
	private boolean ready = false;
	
	protected PeerClient(String peerId, Socket socket, PeerNetwork network) throws IOException {
		this.peerId = peerId;
		this.socket = socket;
		this.in = new DataInputStream(socket.getInputStream());
		this.out = new DataOutputStream(socket.getOutputStream());
		this.network = network;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Client for " + socket.getInetAddress() + ":" + socket.getPort());
		while (!terminate) {
			int count;
			byte[] buffer = new byte[8192];
			try {
				while ((count = in.read(buffer)) > 0) {
					data.append(Arrays.copyOfRange(buffer, 0, count - 1));
				}
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Reading from socket failed on " + socket.getInetAddress() + ":" + socket.getPort());
			}
			if (data.length() > 0) {
				this.network.addRequest(new RawData(this.peerId, data.toString()));
				data.setLength(0);
			}
		}
	}

	protected boolean out(String string) {
		try {
			this.out.writeBytes(string);
			return true;
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Reading to socket failed on " + socket.getInetAddress() + ":" + socket.getPort());
			return false;
		}
	}
	
	protected boolean isReady() {
		return ready;
	}
}
