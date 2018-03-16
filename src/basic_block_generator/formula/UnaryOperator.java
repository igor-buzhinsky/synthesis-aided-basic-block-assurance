package basic_block_generator.formula;

import basic_block_generator.variable.Variable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by buzhinsky on 4/18/17.
 */
public class UnaryOperator extends LTLFormula {
    private final String name;
    private final LTLFormula argument;

    public UnaryOperator(String name, LTLFormula argument) {
        this.name = name;
        this.argument = argument;
    }

    @Override
    public String toString() {
        return name + par(argument.toString());
    }

    @Override
    public String toEFSMToolsString(List<Variable> inputVariables, List<Variable> outputVariables) {
        return name + par(argument.toEFSMToolsString(inputVariables, outputVariables));
    }

    @Override
    public String toUnbeastString(List<Variable> inputVariables, List<Variable> outputVariables) {
        return tag(replaceName(name, Pair.of("!", "Not")), argument.toUnbeastString(inputVariables, outputVariables));
    }

    @Override
    public String toNuSMVString(List<Variable> inputVariables, List<Variable> outputVariables) {
        return name + par(argument.toNuSMVString(inputVariables, outputVariables));
    }

    @Override
    public String toSpinString(List<Variable> inputVariables, List<Variable> outputVariables) {
        return replaceName(name, Pair.of("G", "[]"), Pair.of("F", "<>"))
                + par(argument.toSpinString(inputVariables, outputVariables));
    }

    @Override
    public String toG4LTLSTString(List<Variable> inputVariables, List<Variable> outputVariables) {
        return replaceName(name, Pair.of("G", "ALWAYS "), Pair.of("F", "EVENTUALLY "), Pair.of("X", "NEXT "))
                + par(argument.toG4LTLSTString(inputVariables, outputVariables));
    }

    @Override
    public String toBoSyString(List<Variable> inputVariables, List<Variable> outputVariables) {
        return name + par(argument.toBoSyString(inputVariables, outputVariables));
    }
}
