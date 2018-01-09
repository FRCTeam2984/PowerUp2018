package org.ljrobotics.frc2018.vision;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.vision.messages.HeartbeatMessage;
import org.ljrobotics.frc2018.vision.messages.OffWireMessage;
import org.ljrobotics.frc2018.vision.messages.VisionMessage;
import org.ljrobotics.lib.util.CrashTrackingRunnable;

import edu.wpi.first.wpilibj.Timer;

/**
 * This controls all vision actions, including vision updates, capture, and interfacing with the Android phone with
 * Android Debug Bridge. It also stores all VisionUpdates (from the Android phone) and contains methods to add to/prune
 * the VisionUpdate list. Much like the subsystems, outside methods get the VisionServer instance (there is only one
 * VisionServer) instead of creating new VisionServer instances.
 * 
 * @see VisionUpdate.java
 */

public class VisionServer extends CrashTrackingRunnable {

    private static VisionServer instance = null;
    private ServerSocket serverSocket;
    private boolean running = true;
    private int port;
    private ArrayList<VisionUpdateReceiver> receivers = new ArrayList<>();
    AdbBridge adb = new AdbBridge();
    double lastMessageReceivedTime = 0;
    private boolean useJavaTime = false;

    private ArrayList<ServerThread> serverThreads = new ArrayList<>();
    private volatile boolean wantsAppRestart = false;

    public static VisionServer getInstance() {
        if (instance == null) {
            try {
				instance = new VisionServer(Constants.ANDROID_APP_TCP_PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return instance;
    }

    private boolean isConnect = false;

    public boolean isConnected() {
        return isConnect;
    }

    public void requestAppRestart() {
        wantsAppRestart = true;
    }

    protected class ServerThread extends CrashTrackingRunnable {
        private Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        public void send(VisionMessage message) {
            String toSend = message.toJson() + "\n";
            if (socket != null && socket.isConnected()) {
                try {
                    OutputStream os = socket.getOutputStream();
                    os.write(toSend.getBytes());
                } catch (IOException e) {
                    System.err.println("VisionServer: Could not send data to socket");
                }
            }
        }

        public void handleMessage(VisionMessage message, double timestamp) {
            if ("targets".equals(message.getType())) {
                VisionUpdate update = VisionUpdate.generateFromJsonString(timestamp, message.getMessage());
                receivers.removeAll(Collections.singleton(null));
                if (update.isValid()) {
                    for (VisionUpdateReceiver receiver : receivers) {
                        receiver.gotUpdate(update);
                    }
                }
            }
            if ("heartbeat".equals(message.getType())) {
                send(HeartbeatMessage.getInstance());
            }
        }

        public boolean isAlive() {
            return socket != null && socket.isConnected() && !socket.isClosed();
        }

        @Override
        public void runCrashTracked() {
            if (socket == null) {
                return;
            }
            try {
                InputStream is = socket.getInputStream();
                byte[] buffer = new byte[2048];
                int read;
                while (socket.isConnected() && (read = is.read(buffer)) != -1) {
                    double timestamp = getTimestamp();
                    lastMessageReceivedTime = timestamp;
                    String messageRaw = new String(buffer, 0, read);
                    String[] messages = messageRaw.split("\n");
                    for (String message : messages) {
                        OffWireMessage parsedMessage = new OffWireMessage(message);
                        if (parsedMessage.isValid()) {
                            handleMessage(parsedMessage, timestamp);
                        }
                    }
                }
                System.out.println("Socket disconnected");
            } catch (IOException e) {
                System.err.println("Could not talk to socket");
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Instantializes the VisionServer and connects to ADB via the specified port.
     * 
     * @param Port
     * @throws IOException 
     */
    private VisionServer(int port) throws IOException {
        this(new AdbBridge(), port, new ServerSocket(port));

        new Thread(this).start();
        new Thread(new AppMaintainanceThread()).start();
    }
    
    protected VisionServer(AdbBridge adb, int port, ServerSocket serverSocket) {
    	this.adb = adb;
		this.port = port;
		this.serverSocket = serverSocket;
		this.adb.start();
		this.adb.reversePortForward(port, port);
		try {
		    String useJavaTime = System.getenv("USE_JAVA_TIME");
		    this.useJavaTime = "true".equals(useJavaTime);
		} catch (NullPointerException e) {
		    this.useJavaTime = false;
		}
    }

    public void restartAdb() {
        adb.restartAdb();
        adb.reversePortForward(port, port);
    }

    /**
     * If a VisionUpdate object (i.e. a target) is not in the list, add it.
     * 
     * @see VisionUpdate
     */
    public void addVisionUpdateReceiver(VisionUpdateReceiver receiver) {
        if (!receivers.contains(receiver)) {
            receivers.add(receiver);
        }
    }

    public void removeVisionUpdateReceiver(VisionUpdateReceiver receiver) {
        if (receivers.contains(receiver)) {
            receivers.remove(receiver);
        }
    }

    @Override
    public void runCrashTracked() {
        while (running) {
            try {
                Socket p = serverSocket.accept();
                ServerThread s = new ServerThread(p);
                new Thread(s).start();
                serverThreads.add(s);
            } catch (IOException e) {
                System.err.println("Issue accepting socket connection!");
            } finally {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class AppMaintainanceThread extends CrashTrackingRunnable {
        @Override
        public void runCrashTracked() {
            while (true) {
                if (getTimestamp() - lastMessageReceivedTime > .1) {
                    // camera disconnected
                    adb.reversePortForward(port, port);
                    isConnect = false;
                } else {
                    isConnect = true;
                }
                if (wantsAppRestart) {
                    adb.restartApp();
                    wantsAppRestart = false;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private double getTimestamp() {
        if (useJavaTime) {
            return System.currentTimeMillis();
        } else {
            return Timer.getFPGATimestamp();
        }
    }
}
