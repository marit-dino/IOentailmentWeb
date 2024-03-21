package encoding.IOLogics.originals;

import encoding.IOLogics.causal.OUT_1_C;
import encoding.IOLogics.causal.OUT_2_C;
import util.DagNode;

import java.util.List;

public class OUT_2 extends OUT{
    public OUT_2(DagNode goalPair, List<DagNode> derivingPairs) {
        super(new OUT_2_C(goalPair, derivingPairs));
    }
}
