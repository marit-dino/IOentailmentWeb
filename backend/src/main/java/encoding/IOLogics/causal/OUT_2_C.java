package encoding.IOLogics.causal;

import util.DagNode;

import java.util.List;

public class OUT_2_C extends OUT_1_2_C {
    public OUT_2_C(DagNode goalPair, List<DagNode> derivingPairs) {
        super(goalPair, derivingPairs, 1);
    }
}
