package basic_block_generator.variable;

import basic_block_generator.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by buzhinsky on 4/13/17.
 */
public class ValueCombination {
    private final List<Integer> values;
    private final List<Variable> variables;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueCombination that = (ValueCombination) o;
        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    public int size() {
        return values.size();
    }

    public int getValue(int index) {
        return values.get(index);
    }

    public Variable getVariable(int index) {
        return variables.get(index);
    }

    @Override
    public String toString() {
        return toNuSMVString();
    }

    public String toNuSMVString() {
        final List<String> parts = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            parts.add(variables.get(i).nuSMVCondition(values.get(i)));
        }
        return String.join(" & ", parts);
    }

    public ValueCombination(List<Integer> values, List<Variable> variables) {
        this.values = values;
        this.variables = variables;
        if (values.size() != variables.size()) {
            throw new RuntimeException("Size mismatch!");
        }
        for (int i = 0; i < values.size(); i++) {
            if (!variables.get(i).isValidValue(values.get(i))) {
                throw new RuntimeException("Invalid value!");
            }
        }
    }

    public int denseEncode() {
        int x = 0;
        int factor = 1;
        for (int i = 0; i < variables.size(); i++) {
            final Variable var = variables.get(i);
            final int val = values.get(i);
            x += var.encode(val) * factor;
            factor *= var.cardinality();
        }
        //System.out.println("DENSE_ENCODE(" + this + ") = " + x);
        return x;
    }

    public int sparseEncode() {
        int x = 0;
        int currentBit = 0;
        for (int i = 0; i < variables.size(); i++) {
            final Variable var = variables.get(i);
            final int encodedValue = var.encode(values.get(i));
            final int bits = var.maxBits();
            for (int j = 0; j < bits; j++) {
                x = Util.setBit(x, currentBit++, Util.getBit(encodedValue, j));
            }
        }
        return x;
    }

    public static ValueCombination denseDecode(List<Variable> variables, int x) {
        final List<Integer> values = new ArrayList<>();
        int factor = 1;
        int val = x;
        final List<Integer> factors = new ArrayList<>();
        for (Variable var : variables) {
            factors.add(factor);
            factor *= var.cardinality();
        }
        for (int i = variables.size() - 1; i >= 0; i--) {
            values.add(variables.get(i).decode(val / factors.get(i)));
            val %= factors.get(i);
        }
        Collections.reverse(values);
        //System.out.println("DENSE_DECODE(" + x + ") = " + new ValueCombination(values, variables));
        return new ValueCombination(values, variables);
    }

    public static ValueCombination sparseDecode(List<Variable> variables, int sparseCode) {
        int currentBit = 0;
        final List<Integer> intValues = new ArrayList<>();
        for (final Variable var : variables) {
            final int bits = var.maxBits();
            int encodedValue = 0;
            for (int j = 0; j < bits; j++) {
                encodedValue = Util.setBit(encodedValue, j, Util.getBit(sparseCode, currentBit++));
            }
            intValues.add(var.decode(encodedValue));
        }
        return new ValueCombination(intValues, variables);
    }

    public static ValueCombination fromList(List<Variable> variables, List<String> strValues) {
        return new ValueCombination(strValues.stream().map(ValueCombination::parseValue)
                .collect(Collectors.toList()), variables);
    }

    public static int parseValue(String strValue) {
        final String value = strValue.toLowerCase();
        if (value.equals("false")) {
            return 0;
        } else if (value.equals("true")) {
            return 1;
        }
        return Integer.parseInt(value);
    }
}
