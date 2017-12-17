package org.ljrobotics.frc2018.vision;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.vision.VisionServer.ServerThread;
import org.ljrobotics.frc2018.vision.messages.HeartbeatMessage;
import org.ljrobotics.frc2018.vision.messages.VisionMessage;
import org.ljrobotics.lib.util.DummyFPGATimer;
import org.ljrobotics.lib.util.DummyInputStream;
import org.mockito.ArgumentCaptor;

import edu.wpi.first.wpilibj.Timer;

public class VisionServerTest {

	private class DummyVisionUpdateReceiver implements VisionUpdateReceiver{

		private boolean updated = false;
		
		@Override
		public void gotUpdate(VisionUpdate update) {
			this.updated = true;
		}
		
		public boolean hasUpdated() {
			boolean updated = this.updated;
			this.updated = false;
			return updated;
		}
		
	}
	
	private VisionServer server;
	private AdbBridge adb;
	private ServerSocket serverSocket;
	private DummyFPGATimer timer;
	private DummyVisionUpdateReceiver updateReceiver;
	
	@Before
	public void before() {
		this.adb = mock(AdbBridge.class);
		this.serverSocket = mock(ServerSocket.class);
		this.server = new VisionServer(this.adb, Constants.ANDROID_APP_TCP_PORT, this.serverSocket);
		this.updateReceiver = new DummyVisionUpdateReceiver();
		this.timer = new DummyFPGATimer();
		
		this.server.addVisionUpdateReceiver(this.updateReceiver);
		Timer.SetImplementation(this.timer);
	}
	
	@Test
	public void sendWritesJSONToOutputStream() throws IOException {
		Socket socket = mock(Socket.class);
		when(socket.isConnected()).thenReturn(true);
		
		OutputStream os = mock(OutputStream.class);
		final ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);
		when(socket.getOutputStream()).thenReturn(os);
		
		VisionMessage message = HeartbeatMessage.getInstance();
		
		ServerThread serverThread = this.server.new ServerThread(socket);
		serverThread.send(message);
		
		verify(os).write(captor.capture());
		assertEquals("{\"type\":\"heartbeat\",\"message\":\"{}\"}\n", new String(captor.getValue()));
	}
	
	@Test
	public void recievedHeartbeatSendsAnotherHeartbeat() throws IOException {
		Socket socket = mock(Socket.class);
		when(socket.isConnected()).thenReturn(true);
		
		OutputStream os = mock(OutputStream.class);
		final ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);
		when(socket.getOutputStream()).thenReturn(os);
		
		VisionMessage message = HeartbeatMessage.getInstance();
		
		ServerThread serverThread = this.server.new ServerThread(socket);
		serverThread.handleMessage(message, 0);
		
		verify(os).write(captor.capture());
		assertEquals("{\"type\":\"heartbeat\",\"message\":\"{}\"}\n", new String(captor.getValue()));
	}
	
	@Test
	public void recievedTargetGetsPropogatedToRecivers() throws IOException {
		Socket socket = mock(Socket.class);
		when(socket.isConnected()).thenReturn(true);
		
		String messageJSON = makeVisionUpdate() + "\n";
		DummyInputStream input = new DummyInputStream(messageJSON, true);
		when(socket.getInputStream()).thenReturn(input);
		
		ServerThread serverThread = this.server.new ServerThread(socket);
		serverThread.runCrashTracked();
		assertTrue(this.updateReceiver.hasUpdated());
	}
	
	@SuppressWarnings("unchecked")
	public String makeVisionUpdate() {
		JSONObject json = new JSONObject();
		json.put("type", "targets");
		
		JSONObject visionUpdate = new JSONObject();
		visionUpdate.put("capturedAgoMs", 10);
		JSONArray targets = new JSONArray();
		targets.add(makeTargetInfoJson(0,0));
		visionUpdate.put("targets", targets);
		
		json.put("message", visionUpdate.toJSONString());
		return json.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject makeTargetInfoJson(double distance, double rotation) {
		JSONObject targetInfoJson = new JSONObject();
		targetInfoJson.put("distance", distance);
		targetInfoJson.put("rotation", rotation);
		return targetInfoJson;
	}
	
}
