package org.ljrobotics.frc2018.vision;

/**
 * Tests the vision system by getting targets
 * Remember to set the environment variable USE_JAVA_TIME to "true" or an error will be thrown
 */
public class VisionServerTest {
    public static class TestReceiver implements VisionUpdateReceiver {
        @Override
        public void gotUpdate(VisionUpdate update) {
            System.out.println("num targets: " + update.getTargets().size());
            for (int i = 0; i < update.getTargets().size(); i++) {
                TargetInfo target = update.getTargets().get(i);
                System.out.println("Target: " + target.getDistance() + ", " + target.getRotation());
            }
        }
    }

    public static void main(String[] args) {
        VisionServer visionServer = VisionServer.getInstance();
        visionServer.addVisionUpdateReceiver(new TestReceiver());
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
