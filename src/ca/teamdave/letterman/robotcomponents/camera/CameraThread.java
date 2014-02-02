package ca.teamdave.letterman.robotcomponents.camera;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;

/**
 * Thread which is responsible for all of the heavy lifting in image processing
 */
public class CameraThread implements Runnable {
    private final TargetInfoDelegate mTargetInfoDelegate;
    private final Thread mThread;

    public static class TargetInfoDelegate {
        private PairTargetResult mPairTargetResult;
        private StaticTargetResult mStaticTargetResult;

        private TargetInfoDelegate() {
            mPairTargetResult = PairTargetResult.WORST_POSSIBLE_RESULT;
            mStaticTargetResult = StaticTargetResult.WORST_POSSIBLE_RESULT;
        }

        public synchronized PairTargetResult getLatestTargetPairInfo() {
            return mPairTargetResult;
        }

        public synchronized StaticTargetResult getStaticTargetResult() {
            return mStaticTargetResult;
        }

        public synchronized void setLatestTargetInfo(
                PairTargetResult pairTargetResult,
                StaticTargetResult staticTargetResult) {
            mPairTargetResult = pairTargetResult;
            mStaticTargetResult = staticTargetResult;
        }
    }

    public CameraThread() {
        mTargetInfoDelegate = new TargetInfoDelegate();
        mThread = new Thread(this);
        mThread.start();
    }

    public TargetInfoDelegate getTargetInfoDelegate() {
        return mTargetInfoDelegate;
    }

    public void run() {
        AxisCamera camera = AxisCamera.getInstance("10.36.83.11");
        CriteriaCollection criteriaCollection = new CriteriaCollection();
        PairTargetResult lastAffirmativeResult = null;

        while (true) {
            try {
                ColorImage rawImage = camera.getImage();
                BinaryImage thresholdedImage = rawImage.thresholdRGB(0, 50, 121, 255, 0, 255);
                BinaryImage particleFilteredImage = thresholdedImage.particleFilter(
                        criteriaCollection);
                ParticleAnalysisReport[] particles =
                        particleFilteredImage.getOrderedParticleAnalysisReports();

                // look for a pair of targets
                PairTargetResult pairResult = CameraScoring.getTargetPair(particles);
                if (pairResult.score < PairTargetResult.NOISE_SCORE_THRESHOLD) {
                    lastAffirmativeResult = pairResult;
                }

                // look for a single static target
                StaticTargetResult staticResult = CameraScoring.getStaticPair(
                        particles, lastAffirmativeResult);

                mTargetInfoDelegate.setLatestTargetInfo(pairResult, staticResult);

                rawImage.free();
                thresholdedImage.free();
                particleFilteredImage.free();
            } catch (AxisCameraException e) {
                // System.out.println("Axis camera not available yet");
            } catch (NIVisionException e) {
                System.out.println("NI Vision exception...");
                e.printStackTrace();
            }
        }
    }
}
