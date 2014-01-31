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

    private static PairTargetResult scorePair(
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

        return new PairTargetResult(
                score,
                new BoundingRectangle(left, top, right-left, bottom-top),
                side);
    }

    /**
     * Find the best pair of particles that form a vision target pair
     * @param particles
     * @return The best targetPairResult, no matter how bad
     */
    public static PairTargetResult getTargetPair(ParticleAnalysisReport[] particles) {
        // don't flood the cpu if tonnes of crap gets through the filter
        int maxParticles = particles.length > 10 ? 10 : particles.length;

        PairTargetResult bestResult = PairTargetResult.WORST_POSSIBLE_RESULT;

        for (int dynamicIndex = 0; dynamicIndex < maxParticles; ++dynamicIndex) {
            for (int staticIndex = 0; staticIndex < maxParticles; ++staticIndex) {
                if (dynamicIndex == staticIndex) {
                    continue;
                }
                PairTargetResult pairTargetResult = scorePair(
                        particles[dynamicIndex],
                        particles[staticIndex]);
                if (pairTargetResult.score < bestResult.score) {
                    bestResult = pairTargetResult;
                }
            }
        }

        return bestResult;
    }

    private static StaticTargetResult scoreParticle(
            ParticleAnalysisReport particle,
            PairTargetResult lastPair) {
        if (lastPair == null) {
            // need a past target pair to verify a static target
            return StaticTargetResult.WORST_POSSIBLE_RESULT;
        }

        double score = 1;

        // what was the scale of the last pair?
        double expectedPairWidthInches = COM_DISTANCE_Y
                + DYNAMIC_TARGET_HEIGHT / 2.0
                + STATIC_TARGET_HEIGHT / 2.0;
        double expectedPairHeightInches = COM_DISTANCE_X
                + DYNAMIC_TARGET_WIDTH / 2.0
                + STATIC_TARGET_WIDTH / 2.0;

        // find the scaling factors of the original pair target
        double widthInchesPerPixel = expectedPairWidthInches / (double) lastPair.bounds.width;
        double heightInchesPerPixel = expectedPairHeightInches / (double) lastPair.bounds.height;

        double particleWidthInches = (double) particle.boundingRectWidth * widthInchesPerPixel;
        double particleHeightInches = (double) particle.boundingRectHeight * heightInchesPerPixel;

        // find the skew with respect to the expected target
        double widthScore = particleWidthInches > STATIC_TARGET_WIDTH
                ? particleWidthInches / STATIC_TARGET_WIDTH
                : STATIC_TARGET_WIDTH / particleWidthInches;
        double heightScore = particleHeightInches > STATIC_TARGET_HEIGHT
                ? particleHeightInches / STATIC_TARGET_HEIGHT
                : STATIC_TARGET_HEIGHT / particleHeightInches;

        score *= widthScore * heightScore;

        // Find the expected position of the target on the image in pixels
        double expectedCOMXPixels = (lastPair.side == TargetSide.LEFT)
                ? (lastPair.bounds.x + lastPair.bounds.width
                - (STATIC_TARGET_WIDTH / widthInchesPerPixel) / 2.0)
                : (lastPair.bounds.x + (STATIC_TARGET_WIDTH / widthInchesPerPixel) / 2.0);
        double expectedCOMYPixels = lastPair.bounds.y
                + (DYNAMIC_TARGET_HEIGHT / 2 + COM_DISTANCE_Y) / heightInchesPerPixel;

        // Find the expected position error in inches
        double xErrorInches = (expectedCOMXPixels - particle.center_mass_x) * widthInchesPerPixel;
        double yErrorInches = (expectedCOMYPixels - particle.center_mass_y) * heightInchesPerPixel;
        double distError = Math.sqrt(xErrorInches * xErrorInches + yErrorInches * yErrorInches);

        // score the position error
        score *= MathUtils.pow(1.1, distError);

        return new StaticTargetResult(
                score,
                new BoundingRectangle(
                        particle.boundingRectLeft,
                        particle.boundingRectTop,
                        particle.boundingRectWidth,
                        particle.boundingRectHeight));
    }

    public static StaticTargetResult getStaticPair(
            ParticleAnalysisReport[] particles,
            PairTargetResult lastPair) {
        // don't flood the cpu if tonnes of crap gets through the filter
        int maxParticles = particles.length > 10 ? 10 : particles.length;
        StaticTargetResult bestResult = StaticTargetResult.WORST_POSSIBLE_RESULT;

        for (int i = 0; i < maxParticles; ++i) {
            StaticTargetResult result = scoreParticle(particles[i], lastPair);
            if (result.score < bestResult.score) {
                bestResult = result;
            }
        }

        return bestResult;
    }
}
