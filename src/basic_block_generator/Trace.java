package basic_block_generator;

import basic_block_generator.variable.ValueCombination;

import java.util.Collections;
import java.util.List;

/**
 * Created by buzhinsky on 4/14/17.
 */
public class Trace {
    private final List<ValueCombination> inputValues;
    private final List<ValueCombination> outputValues;

    public Trace(List<ValueCombination> inputValues, List<ValueCombination> outputValues) {
        this.inputValues = inputValues;
        this.outputValues = outputValues;
        if (inputValues.size() != outputValues.size()) {
            throw new RuntimeException("List sizes must match!");
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TRACE\n");
        for (int i = 0; i < inputValues.size(); i++) {
            final ValueCombination input = inputValues.get(i);
            for (int j = 0; j < input.size(); j++) {
                sb.append(input.getValue(j)).append(" ");
            }
            sb.append("|");
            final ValueCombination output = outputValues.get(i);
            for (int j = 0; j < output.size(); j++) {
                sb.append(" ").append(output.getValue(j));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    List<ValueCombination> inputValues() {
        return Collections.unmodifiableList(inputValues);
    }

    List<ValueCombination> outputValues() {
        return Collections.unmodifiableList(outputValues);
    }

    public int size() {
        return inputValues.size();
    }
}
