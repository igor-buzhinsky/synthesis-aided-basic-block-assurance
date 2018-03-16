package basic_block_generator.formula;

import basic_block_generator.Util;
import basic_block_generator.variable.ValueCombination;
import basic_block_generator.variable.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buzhinsky on 4/18/17.
 */
public class Proposition extends LTLFormula {
    private final String varName;
    private final String constant;
    private final int parsedValue;

    private Variable var;
    private int varIndex;
    private boolean isInputVar;

    private void findVariable(List<Variable> inputVariables, List<Variable> outputVariables) {
        if (var != null) {
            return;
        }
        for (int i = 0; i < inputVariables.size(); i++) {
            final Variable v = inputVariables.get(i);
            if (v.name.equals(varName)) {
                var = v;
                isInputVar = true;
                varIndex = i;
                return;
            }
        }
        for (int i = 0; i < outputVariables.size(); i++) {
            final Variable v = outputVariables.get(i);
            if (v.name.equals(varName)) {
                var = v;
                isInputVar = false;
                varIndex = i;
                return;
            }
        }
        throw new RuntimeException("Unknown variable " + varName + "!");
    }

    public Proposition(String varName, String constant) {
        this.varName = varName;
        this.constant = constant;
        parsedValue = ValueCombination.parseValue(constant);
    }

    @Override
    public String toString() {
        return varName + " = " + constant;
    }

    @Override
    public String toEFSMToolsString(List<Variable> inputVariables, List<Variable> outputVariables) {
        findVariable(inputVariables, outputVariables);
        final int varValue = ValueCombination.parseValue(constant);
        return isInputVar
                ? translateInputVariable(inputVariables, varIndex, varValue)
                : translateOutputVariable(outputVariables, varIndex, varValue);
    }

    @Override
    public String toUnbeastString(List<Variable> inputVariables, List<Variable> outputVariables) {
        findVariable(inputVariables, outputVariables);
        return encodeWithBitsUnbeast(var);
    }

    @Override
    public String toNuSMVString(List<Variable> inputVariables, List<Variable> outputVariables) {
        findVariable(inputVariables, outputVariables);
        return var.nuSMVCondition(parsedValue);
    }

    @Override
    public String toSpinString(List<Variable> inputVariables, List<Variable> outputVariables) {
        findVariable(inputVariables, outputVariables);
        return par(var.spinCondition(parsedValue));
    }

    @Override
    public String toG4LTLSTString(List<Variable> inputVariables, List<Variable> outputVariables) {
        findVariable(inputVariables, outputVariables);
        return encodeWithBits(var, true);
    }

    @Override
    public String toBoSyString(List<Variable> inputVariables, List<Variable> outputVariables) {
        findVariable(inputVariables, outputVariables);
        return encodeWithBits(var, false);
    }

    private String encodeWithBitsUnbeast(Variable v) {
        final int num = v.encode(ValueCombination.parseValue(constant));
        final List<Boolean> bits = Util.toBits(num, v.maxBits());
        final List<String> parts = new ArrayList<>();
        for (int i = 0; i < bits.size(); i++) {
            final String varStr = tag("Var", "b" + i + "_" + varName);
            parts.add(bits.get(i) ? varStr : tag("Not", varStr));
        }
        return tag("And", String.join("", parts));
    }

    private String encodeWithBits(Variable v, boolean toLowerCase) {
        final int num = v.encode(ValueCombination.parseValue(constant));
        final List<Boolean> bits = Util.toBits(num, v.maxBits());
        final List<String> parts = new ArrayList<>();
        for (int i = 0; i < bits.size(); i++) {
            final String varStr = "b" + i + "_" + (toLowerCase ? varName.toLowerCase() : varName);
            parts.add((bits.get(i) ? "" : "!") + varStr);
        }
        return par(String.join(" && ", parts));
    }

    private static String translateInputVariable(List<Variable> variables, int index, int value) {
        final int cardinality = Util.denseCardinality(variables);
        final List<String> statements = new ArrayList<>();
        for (int i = 0; i < cardinality; i++) {
            final ValueCombination vc = ValueCombination.denseDecode(variables, i);
            if (vc.getValue(index) == value) {
                statements.add("A" + i);
            }
        }
        return "event" + par(String.join(", ", statements));
    }

    private static String translateOutputVariable(List<Variable> variables, int index, int value) {
        final Variable v = variables.get(index);
        final List<Boolean> bits = Util.toBits(v.encode(value), v.maxBits());
        final List<String> statements = new ArrayList<>();
        for (int i = 0; i < bits.size(); i++) {
            statements.add((bits.get(i) ? "" : "!") + "action(v" + index + "b" + i + ")");
        }
        return String.join(" && ", statements);
    }

}
