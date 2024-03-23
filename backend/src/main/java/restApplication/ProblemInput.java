package restApplication;

import java.util.Objects;

/**
 * Represent the entailment problem.
 */
public class ProblemInput {
    private String derivingPairs;
    private String goalPair;
    private String type;

    public ProblemInput(String derivingPairs, String goalPair, String type) {
        this.derivingPairs = derivingPairs;
        this.goalPair = goalPair;
        this.type = type;
    }

    public String getDerivingPairs() {
        return derivingPairs;
    }

    public void setDerivingPairs(String derivingPairs) {
        this.derivingPairs = derivingPairs;
    }

    public String getGoalPair() {
        return goalPair;
    }

    public void setGoalPair(String goalPair) {
        this.goalPair = goalPair;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProblemInput problemInput = (ProblemInput) o;
        return Objects.equals(derivingPairs, problemInput.derivingPairs) && Objects.equals(goalPair, problemInput.goalPair) && type == problemInput.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(derivingPairs, goalPair, type);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Problem{" +
                "derivingPairs='" + derivingPairs + '\'' +
                ", goalPair='" + goalPair + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public boolean isFieldNull(){
        return (this.derivingPairs == null) || (this.goalPair == null) || (this.type == null);
    }
}
