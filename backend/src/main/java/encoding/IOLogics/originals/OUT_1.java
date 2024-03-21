package encoding.IOLogics.originals;

import encoding.IOLogics.causal.OUT_1_C;
import encoding.IOLogics.causal.OUT_C;
import util.DagNode;

import java.util.List;

public class OUT_1 extends OUT{
    public OUT_1(DagNode goalPair, List<DagNode> derivingPairs) {
        super(new OUT_1_C(goalPair, derivingPairs));
    }
}
