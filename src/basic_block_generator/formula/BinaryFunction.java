package basic_block_generator.formula;

import basic_block_generator.variable.Variable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by buzhinsky on 4/18/17.
 */
public class BinaryFunction extends LTLFormula {
    private final String name;
    private final LTLFormula leftArgument;
    private final LTLFormula rightArgument;

    public BinaryFunction(String name, LTLFormula leftArgument, LTLFormula rightArgument) {
        this.name = name;
        this.leftArgument = leftArgument;
        this.rightArgument = rightArgument;
    }

    @Override
    public String toString() {
        return name + par(leftArgument + ", " + rightArgument);
    }

    @Override
    public String toEFSMToolsString(List<Variable> inputVariables, List<Variable> outputVariables) {
        if (name.equals("W")) {
            return excludeWeakUntil().toEFSMToolsString(inputVariables, outputVariables);
        }
        final String arg1 = leftArgument.toEFSMToolsString(inputVariables, outputVariables);
        final String arg2 = rightArgument.toEFSMToolsString(inputVariables, outputVariables);
        return name + par(arg1 + ", " + arg2);
    }

    @Override
    public String toUnbeastString(List<Variable> inputVariables, List<Variable> outputVariables) {
        if (name.equals("R")) {
            return excludeRelease().toUnbeastString(inputVariables, outputVariables);
        } else if (name.equals("W")) {
            return excludeWeakUntil().toUnbeastString(inputVariables, outputVariables);
        }
        final String arg1 = leftArgument.toUnbeastString(inputVariables, outputVariables);
        final String arg2 = rightArgument.toUnbeastString(inputVariables, outputVariables);
        return tag(name, arg1 + arg2);
    }

    @Override
    public String toNuSMVString(List<Variable> inputVariables, List<Variable> outputVariables) {
        if (name.equals("R")) {
            return excludeRelease().toNuSMVString(inputVariables, outputVariables);
        } else if (name.equals("W")) {
            return excludeWeakUntil().toNuSMVString(inputVariables, outputVariables);
        }
        final String arg1 = leftArgument.toNuSMVString(inputVariables, outputVariables);
        final String arg2 = rightArgument.toNuSMVString(inputVariables, outputVariables);
        return par(arg1 + " " + name + " " + arg2);
    }

    @Override
    public String toSpinString(List<Variable> inputVariables, List<Variable> outputVariables) {
        final String arg1 = leftArgument.toSpinString(inputVariables, outputVariables);
        final String arg2 = rightArgument.toSpinString(inputVariables, outputVariables);
        return par(arg1 + " " + replaceName(name, Pair.of("R", "V")) + " " + arg2);
    }

    @Override
    public String toG4LTLSTString(List<Variable> inputVariables, List<Variable> outputVariables) {
        if (name.equals("R")) {
            return excludeRelease().toG4LTLSTString(inputVariables, outputVariables);
        } else if (name.equals("W")) {
            return excludeWeakUntil().toG4LTLSTString(inputVariables, outputVariables);
        }
        final String arg1 = leftArgument.toG4LTLSTString(inputVariables, outputVariables);
        final String arg2 = rightArgument.toG4LTLSTString(inputVariables, outputVariables);
        return par(arg1 + " UNTIL " + arg2);
    }

    @Override
    public String toBoSyString(List<Variable> inputVariables, List<Variable> outputVariables) {
        if (name.equals("R")) {
            return excludeRelease().toBoSyString(inputVariables, outputVariables);
        } else if (name.equals("W")) {
            return excludeWeakUntil().toBoSyString(inputVariables, outputVariables);
        }
        final String arg1 = leftArgument.toBoSyString(inputVariables, outputVariables);
        final String arg2 = rightArgument.toBoSyString(inputVariables, outputVariables);
        return par(arg1 + " " + name + " " + arg2);
    }

    private LTLFormula excludeRelease() {
        return new UnaryOperator("!", new BinaryFunction("U", new UnaryOperator("!", leftArgument),
                new UnaryOperator("!", rightArgument)));
    }

    private LTLFormula excludeWeakUntil() {
        return new BinaryOperator("|", new BinaryFunction("U", leftArgument, rightArgument),
                new UnaryOperator("G", leftArgument));
    }
}
