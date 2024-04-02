package encoding.IOLogics;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Countermodel for the causal I/O logics.
 */
public class CounterModelWorlds implements CounterModel {
    private static final Logger logger = LogManager.getLogger();

    private Map<String, Map<String, Boolean>> in;
    private Map<String, Map<String, Boolean>> out;

    public CounterModelWorlds() {
        this.in = new HashMap<>();
        this.out = new HashMap<>();
    }

    /**
     * Adds the value to the input set.
     * @param var name
     * @param world of var
     * @param value of var
     */
    public void addToIn(String var, String world, boolean value){
        logger.trace("addToIn({}, {}, {})", var, world, value);
        if(!in.containsKey(var)){
            in.put(var, new HashMap<>());
        }
        in.get(var).put(world, value);
    }

    /**
     * Adds the value to the output set.
     * @param var name
     * @param world of var
     * @param value of var
     */
    public void addToOut(String var, String world, boolean value){
        logger.trace("addToOut({}, {}, {})", var, world, value);
        if(!out.containsKey(var)){
            out.put(var, new HashMap<>());
        }
        out.get(var).put(world, value);
    }

    public Map<String, Map<String, Boolean>> getIn() {
        return in;
    }

    public void setIn(Map<String, Map<String, Boolean>> in) {
        this.in = in;
    }

    public Map<String, Map<String, Boolean>> getOut() {
        return out;
    }

    public void setOut(Map<String, Map<String, Boolean>> out) {
        this.out = out;
    }
}
