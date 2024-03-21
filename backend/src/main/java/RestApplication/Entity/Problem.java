package RestApplication.Entity;

import util.Type;

import java.util.Objects;

public class Problem {
    private String derivingPairs;
    private String goalPair;
    private String type;

    public Problem(String derivingPairs, String goalPair, String type) {
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
        Problem problem = (Problem) o;
        return Objects.equals(derivingPairs, problem.derivingPairs) && Objects.equals(goalPair, problem.goalPair) && type == problem.type;
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
}
