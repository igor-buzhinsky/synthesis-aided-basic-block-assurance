package basic_block_generator.formula;

import basic_block_generator.variable.Variable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by buzhinsky on 4/18/17.
 */
public class BinaryOperator extends LTLFormula {
    private final String name;
    private final LTLFormula leftArgument;
    private final LTLFormula rightArgument;

    public BinaryOperator(String name, LTLFormula leftArgument, LTLFormula rightArgument) {
        this.name = name;
        this.leftArgument = leftArgument;
        this.rightArgument = rightArgument;
    }

    @Override
    public String toString() {
        return par(leftArgument + " " + name + " " + rightArgument);
    }

    @Override
    public String toEFSMToolsString(List<Variable> inputVariables, List<Variable> outputVariables) {
        if (name.equals("->")) {
            return excludeImplication().toEFSMToolsString(inputVariables, outputVariables);
        } else if (name.equals("<->")) {
            return excludeEquivalence().toEFSMToolsString(inputVariables, outputVariables);
        }
        final String arg1 = leftArgument.toEFSMToolsString(inputVariables, outputVariables);
        final String arg2 = rightArgument.toEFSMToolsString(inputVariables, outputVariables);
        return par(arg1 + " " + replaceName(name, Pair.of("&", "&&"), Pair.of("|", "||")) + " " + arg2);
    }

    @Override
    public String toUnbeastString(List<Variable> inputVariables, List<Variable> outputVariables) {
        if (name.equals("->")) {
            return excludeImplication().toUnbeastString(inputVariables, outputVariables);
        } else if (name.equals("<->")) {
            return excludeEquivalence().toUnbeastString(inputVariables, outputVariables);
        }
        final String arg1 = leftArgument.toUnbeastString(inputVariables, outputVariables);
        final String arg2 = rightArgument.toUnbeastString(inputVariables, outputVariables);
        return tag(replaceName(name, Pair.of("&", "And"), Pair.of("|", "Or")), arg1 + arg2);
    }

    @Override
    public String toNuSMVString(List<Variable> inputVariables, List<Variable> outputVariables) {
        final String arg1 = leftArgument.toNuSMVString(inputVariables, outputVariables);
        final String arg2 = rightArgument.toNuSMVString(inputVariables, outputVariables);
        return par(arg1 + " " + name + " " + arg2);
    }

    @Override
    public String toSpinString(List<Variable> inputVariables, List<Variable> outputVariables) {
        final String arg1 = leftArgument.toSpinString(inputVariables, outputVariables);
        final String arg2 = rightArgument.toSpinString(inputVariables, outputVariables);
        return par(arg1 + " " + replaceName(name, Pair.of("&", "&&"), Pair.of("|", "||")) + " " + arg2);
    }

    @Override
    public String toG4LTLSTString(List<Variable> inputVariables, List<Variable> outputVariables) {
        if (name.equals("<->")) {
            return excludeEquivalence().toG4LTLSTString(inputVariables, outputVariables);
        }
        final String arg1 = leftArgument.toG4LTLSTString(inputVariables, outputVariables);
        final String arg2 = rightArgument.toG4LTLSTString(inputVariables, outputVariables);
        return par(arg1 + " " + replaceName(name, Pair.of("&", "&&"), Pair.of("|", "||")) + " " + arg2);
    }

    @Override
    public String toBoSyString(List<Variable> inputVariables, List<Variable> outputVariables) {
        if (name.equals("<->")) {
            return excludeEquivalence().toBoSyString(inputVariables, outputVariables);
        }
        final String arg1 = leftArgument.toBoSyString(inputVariables, outputVariables);
        final String arg2 = rightArgument.toBoSyString(inputVariables, outputVariables);
        return par(arg1 + " " + replaceName(name, Pair.of("&", "&&"), Pair.of("|", "||")) + " " + arg2);
    }

    private LTLFormula excludeImplication() {
        return new BinaryOperator("|", new UnaryOperator("!", leftArgument), rightArgument);
    }

    private LTLFormula excludeEquivalence() {
        return new BinaryOperator("|", new BinaryOperator("&", leftArgument, rightArgument),
                new BinaryOperator("&", new UnaryOperator("!", leftArgument),
                        new UnaryOperator("!", rightArgument)));
    }
}
