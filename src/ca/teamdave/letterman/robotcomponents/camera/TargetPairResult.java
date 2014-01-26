package ca.teamdave.letterman.robotcomponents.camera;

/**
* Describes the location of a detected target, and scores how probably it actually is a target
*/
public class TargetPairResult {
    public final double score;
    public final double boundingLeft;
    public final double boundingTop;
    public final double boundingWidth;
    public final double boundingHeight;
    public final TargetSide side;

    public TargetPairResult(
            double score,
            double boundingLeft,
            double boundingTop,
            double boundingWidth,
            double boundingHeight,
            TargetSide side) {
        this.score = score;
        this.boundingLeft = boundingLeft;
        this.boundingTop = boundingTop;
        this.boundingWidth = boundingWidth;
        this.boundingHeight = boundingHeight;
        this.side = side;
    }
}
