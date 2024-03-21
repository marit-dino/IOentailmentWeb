package encoding.IOLogics.causal;

import util.DagNode;

import java.util.List;

public class OUT_3_C extends OUT_3_4_C{
    public OUT_3_C(DagNode goalPair, List<DagNode> derivingPairs) {
        super(goalPair, derivingPairs, derivingPairs.size() + 1);
    }
}
