package encoding;

/**
 * Represents the entailment problem, so whether (in this case) the deriving pairs entail the goal pair.
 */
public interface EntailmentProblem {

    /**
     * Returns the answer to the entailment problem.
     *
     * @return true if deriving pairs entail goal pair.
     */
    boolean entails();
}
