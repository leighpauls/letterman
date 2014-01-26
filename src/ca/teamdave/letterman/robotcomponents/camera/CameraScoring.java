package ca.teamdave.letterman.robotcomponents.camera;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 * Evaluates the likelihood that some set of particles describe a vision target
 */
public class CameraScoring {
    // positions of the targets, in inches
    private static final double DYNAMIC_TARGET_WIDTH = 23.5;
    private static final double DYNAMIC_TARGET_HEIGHT = 4;

    private static final double STATIC_TARGET_WIDTH = 4;
    private static final double STATIC_TARGET_HEIGHT = 32;

    // distance between the centers of mass of the targets, in inches
    private static final double COM_DISTANCE_X = 19;
    private static final double COM_DISTANCE_Y = 14.5;

    private static TargetPairResult scorePair(
            ParticleAnalysisReport dynamicPart,
            ParticleAnalysisReport staticPart) {
        TargetSide side = dynamicPart.center_mass_x < staticPart.center_mass_x
                ? TargetSide.LEFT
                : TargetSide.RIGHT;

        double score = 1;

        // how badly was the static target's rectangle warped?
        double actualStaticRectRatio =
                (double) staticPart.boundingRectWidth / (double) staticPart.boundingRectHeight;
        double expectedStaticRectRatio = STATIC_TARGET_WIDTH / STATIC_TARGET_HEIGHT;
        double staticRectRatioScore = actualStaticRectRatio > expectedStaticRectRatio
                ? actualStaticRectRatio / expectedStaticRectRatio
                : expectedStaticRectRatio / actualStaticRectRatio;

        // TODO: weight this
        score *= staticRectRatioScore;

        // scale the rest of the processing according to the height of the static target
        double inchesPerPixel = STATIC_TARGET_HEIGHT / (double) staticPart.boundingRectHeight;

        // find how far off the position of the dynamic target is
        double dynamicXDistance =
                Math.abs(dynamicPart.center_mass_x - staticPart.center_mass_x) * inchesPerPixel;
        double dynamicYDistance =
                (dynamicPart.center_mass_y - staticPart.center_mass_y) * inchesPerPixel;
        double xError = COM_DISTANCE_X - dynamicXDistance;
        double yError = COM_DISTANCE_Y - dynamicYDistance;
        double distanceError = Math.sqrt(xError*xError + yError*yError);
        double distanceScore = MathUtils.pow(1.1, distanceError);

        // TODO: weight this
        score *= distanceScore;


        // how badly was the dynamic target's rectangle warped?
        double actualDynamicPartRectRatio =
                (double) dynamicPart.boundingRectWidth / (double) dynamicPart.boundingRectHeight;
        double expectedDynamicRectRatio = DYNAMIC_TARGET_WIDTH / DYNAMIC_TARGET_HEIGHT;
        double dynamicRectRatioScore = actualDynamicPartRectRatio > expectedDynamicRectRatio
                ? actualDynamicPartRectRatio / expectedDynamicRectRatio
                : expectedDynamicRectRatio / actualDynamicPartRectRatio;

        // TODO: weight this
        score *= dynamicRectRatioScore;

        double left = Math.min(dynamicPart.boundingRectLeft, staticPart.boundingRectLeft);
        double top = Math.min(dynamicPart.boundingRectTop, staticPart.boundingRectTop);
        double right = Math.max(
                dynamicPart.boundingRectLeft + dynamicPart.boundingRectWidth,
                staticPart.boundingRectLeft + staticPart.boundingRectWidth);
        double bottom = Math.max(
                dynamicPart.boundingRectTop + dynamicPart.boundingRectHeight,
                staticPart.boundingRectTop + staticPart.boundingRectHeight);

        return new TargetPairResult(score, left, top, right-left, bottom-top, side);
    }

    /**
     * Find the best pair of particles that form a vision target pair
     * @param particles
     * @return The best targetPairResult, no matter how bad
     */
    public static TargetPairResult getTargetPair(ParticleAnalysisReport[] particles) {
        // don't flood the cpu if tonnes of crap gets through the filter
        int maxParticles = particles.length > 10 ? 10 : particles.length;

        TargetPairResult bestResult = new TargetPairResult(
                Double.POSITIVE_INFINITY, 0, 0, 0, 0, TargetSide.LEFT);

        for (int dynamicIndex = 0; dynamicIndex < maxParticles; ++dynamicIndex) {
            for (int staticIndex = 0; staticIndex < maxParticles; ++staticIndex) {
                if (dynamicIndex == staticIndex) {
                    continue;
                }
                TargetPairResult targetPairResult = scorePair(
                        particles[dynamicIndex],
                        particles[staticIndex]);
                if (targetPairResult.score < bestResult.score) {
                    bestResult = targetPairResult;
                }
            }
        }

        return bestResult;
    }
}
