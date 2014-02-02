package ca.teamdave.letterman.robotcomponents.camera;

/**
 * Describes the a possible static target detection
 */
public class StaticTargetResult {
    // TODO: find this maximum
    public static final double NOISE_SCORE_THRESHOLD = 1000;

    public static final StaticTargetResult WORST_POSSIBLE_RESULT = new StaticTargetResult(
            Double.POSITIVE_INFINITY,
            new BoundingRectangle(0, 0, 0, 0));

    public final double score;
    public final BoundingRectangle bounds;

    public StaticTargetResult(double score, BoundingRectangle bounds) {
        this.score = score;
        this.bounds = bounds;
    }
}