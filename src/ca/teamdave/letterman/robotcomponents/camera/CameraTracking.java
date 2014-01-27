package ca.teamdave.letterman.robotcomponents.camera;

import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.background.BackgroundUpdatingComponent;
import ca.teamdave.letterman.background.RobotMode;

/**
 * Handles the detection of the hot goal target
 */
public class CameraTracking implements BackgroundUpdatingComponent {
    private final CameraThread.TargetInfoDelegate mTargetInfoDelegate;

    public CameraTracking() {
        CameraThread cameraThread = new CameraThread();
        mTargetInfoDelegate = cameraThread.getTargetInfoDelegate();

        BackgroundUpdateManager.getInstance().registerComponent(this);
    }

    public void updateComponent(RobotMode mode, double deltaTime) {
        PairTargetResult latestTargetPairInfo = mTargetInfoDelegate.getLatestTargetPairInfo();
        System.out.println("Score: " + latestTargetPairInfo.score);
    }
}
