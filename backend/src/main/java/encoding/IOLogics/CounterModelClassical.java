package encoding.IOLogics;

import java.util.HashMap;
import java.util.Map;

/**
 * Countermodel for the original I/O logics, in case the X1, ..., Xn |= Y fails
 */
public class CounterModelClassical implements CounterModel {
    private Map<String, Boolean> vals;

    public CounterModelClassical() {
        this.vals = new HashMap<>();
    }

    /**
     * Adds the value to the value set
     * @param var name
     * @param val value of var
     */
    public void add(String var, boolean val){
        logger.trace("add({}, {})", var, val);
        vals.put(var, val);
    }

    public Map<String, Boolean> getVals() {
        return vals;
    }

    public void setVals(Map<String, Boolean> vals) {
        this.vals = vals;
    }

}
