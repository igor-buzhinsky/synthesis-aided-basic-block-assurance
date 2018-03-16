package basic_block_generator.formula;

import basic_block_generator.variable.Variable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by buzhinsky on 4/18/17.
 */
public abstract class LTLFormula {
    public abstract String toEFSMToolsString(List<Variable> inputVariables, List<Variable> outputVariables);
    public abstract String toUnbeastString(List<Variable> inputVariables, List<Variable> outputVariables);
    public abstract String toNuSMVString(List<Variable> inputVariables, List<Variable> outputVariables);
    public abstract String toSpinString(List<Variable> inputVariables, List<Variable> outputVariables);
    public abstract String toG4LTLSTString(List<Variable> inputVariables, List<Variable> outputVariables);
    public abstract String toBoSyString(List<Variable> inputVariables, List<Variable> outputVariables);

    public static String tag(String tag, String text) {
        return "<" + tag + ">" + text + "</" + tag + ">";
    }

    static String par(String text) {
        return "(" + text + ")";
    }

    static String replaceName(String name, Pair<String, String>... pairs) {
        for (Pair<String, String> p : pairs) {
            if (name.equals(p.getLeft())) {
                return p.getRight();
            }
        }
        return name;
    }
}
