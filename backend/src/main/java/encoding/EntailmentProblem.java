package encoding;

import encoding.IOLogics.CounterModel;

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

    /**
     * Returns a counter model if the previous usage of entails() returned false, else null is returned.
     * Can only be called once, as the context is closed after getting the model (clean up)
     * @return a counter model
     */
    CounterModel getCounterModel();
}
