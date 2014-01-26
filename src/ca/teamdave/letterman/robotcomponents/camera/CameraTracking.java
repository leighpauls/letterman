package ca.teamdave.letterman.robotcomponents.camera;

import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.background.BackgroundUpdatingComponent;
import ca.teamdave.letterman.background.RobotMode;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;

/**
 * Handles the detection of the hot goal target
 */
public class CameraTracking implements BackgroundUpdatingComponent {
    private final AxisCamera mCamera;
    private final CriteriaCollection mCriteriaCollection;
    private double mTimeSinceUpdate;
    // only update the camera this often
    private static final double CAMERA_UPDATE_PERIOD = 0.2;

    public CameraTracking() {
        mCamera = AxisCamera.getInstance("10.36.83.11");
        BackgroundUpdateManager.getInstance().registerComponent(this);
        mCriteriaCollection = new CriteriaCollection();
        mCriteriaCollection.addCriteria(NIVision.MeasurementType.IMAQ_MT_AREA, 500, 10000, false);
        // update the camera on the first cycle
        mTimeSinceUpdate = CAMERA_UPDATE_PERIOD;
    }

    public void updateComponent(RobotMode mode, double deltaTime) {
        // don't waste CPU time in teleop
        if (mode == RobotMode.TELEOP) {
            return;
        }
        mTimeSinceUpdate += deltaTime;
        if (mTimeSinceUpdate < CAMERA_UPDATE_PERIOD) {
            return;
        }
        mTimeSinceUpdate -= CAMERA_UPDATE_PERIOD;
        System.out.println("Update Time: " + mTimeSinceUpdate);
        try {
            ColorImage rawImage = mCamera.getImage();
            BinaryImage thresholdedImage = rawImage.thresholdRGB(0, 50, 121, 255, 0, 255);
            BinaryImage particleFilteredImage = thresholdedImage.particleFilter(mCriteriaCollection);
            ParticleAnalysisReport[] particles = particleFilteredImage.getOrderedParticleAnalysisReports();
            TargetPairResult targetPair = CameraScoring.getTargetPair(particles);

            System.out.println("Best target score: " + targetPair.score);

            rawImage.free();
            thresholdedImage.free();
            particleFilteredImage.free();

        } catch (AxisCameraException e) {
            System.out.println("Axis camera not available yet");
        } catch (NIVisionException e) {
            e.printStackTrace();
        }
    }
}
