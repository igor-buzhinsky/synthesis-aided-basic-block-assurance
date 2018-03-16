package basic_block_generator.variable;

/**
 * Created by buzhinsky on 4/13/17.
 */
public class BooleanVariable extends Variable {
    BooleanVariable(String name) {
        super(name);
    }

    @Override
    public int encode(int value) {
        if (!isValidValue(value)) {
            throw new RuntimeException("Invalid value!");
        }
        return value;
    }

    @Override
    public int decode(int value) {
        if (!isValidValue(value)) {
            throw new RuntimeException("Invalid value " + value + " of variable " + this + "!");
        }
        return value;
    }

    @Override
    public int cardinality() {
        return 2;
    }

    @Override
    public boolean isValidValue(int value) {
        return value == 0 || value == 1;
    }

    @Override
    public String nuSMVCondition(int value) {
        return (value == 0 ? "!" : "") + name;
    }

    @Override
    public String nuSMVValue(int value) {
        return value == 0 ? "FALSE" : "TRUE";
    }

    @Override
    public String nuSMVType() {
        return "boolean";
    }

    @Override
    public String spinCondition(int value) {
        return nuSMVCondition(value);
    }

    @Override
    public String spinType() {
        return "bool";
    }

    @Override
    public String spinRange() {
        return "0..1";
    }

    @Override
    public String spinValue(int value) {
        return value == 0 ? "false" : "true";
    }

    @Override
    public String toString() {
        return name + ":" + nuSMVType();
    }
}
