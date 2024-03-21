package encoding.IOLogics.causal;

import util.DagNode;

import java.util.List;

public class OUT_1_C extends OUT_1_2_C {
    public OUT_1_C(DagNode goalPair, List<DagNode> derivingPairs) {
        super(goalPair, derivingPairs, derivingPairs.size() + 1);
    }
}
