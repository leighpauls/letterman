package ca.teamdave.letterman.robotcomponents.camera;

/**
* Describes the location of a detected target, and scores how probably it actually is a target
*/
public class PairTargetResult {
    // effectively the "no target at all" result
    public static final PairTargetResult WORST_POSSIBLE_RESULT = new PairTargetResult(
            Double.POSITIVE_INFINITY,
            new BoundingRectangle(0, 0, 0, 0),
            TargetSide.LEFT);

    public final double score;
    public final BoundingRectangle bounds;
    public final TargetSide side;

    public PairTargetResult(
            double score,
            BoundingRectangle bounds,
            TargetSide side) {
        this.score = score;
        this.bounds = bounds;
        this.side = side;
    }

    public String toString() {
        return "PairTargetResult:\n"
                + side + "\n"
                + "Score: " + score + "\n"
                + "Position: " + bounds + "\n";

    }
}
