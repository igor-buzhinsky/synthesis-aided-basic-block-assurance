package basic_block_generator.variable;

/**
 * Created by buzhinsky on 4/13/17.
 */
public class IntegerVariable extends Variable {
    public final int lowerBound;
    public final int upperBound;

    public IntegerVariable(String name, int lowerBound, int upperBound) {
        super(name);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        if (lowerBound > upperBound) {
            throw new RuntimeException();
        }
    }

    @Override
    public int encode(int value) {
        validityCheck(value);
        return value - lowerBound;
    }

    @Override
    public int decode(int value) {
        validityCheck(value + lowerBound);
        return value + lowerBound;
    }

    private void validityCheck(int value) {
        if (!isValidValue(value)) {
            throw new RuntimeException("Invalid value " + value + " of variable " + name + "! Allowed range: ["
                    + lowerBound + ", " + upperBound + "]");
        }
    }

    @Override
    public int cardinality() {
        return upperBound - lowerBound + 1;
    }

    @Override
    public boolean isValidValue(int value) {
        return value >= lowerBound && value <= upperBound;
    }

    @Override
    public String nuSMVCondition(int value) {
        return name + " = " + value;
    }

    @Override
    public String nuSMVValue(int value) {
        return String.valueOf(value);
    }

    @Override
    public String nuSMVType() {
        return lowerBound + ".." + upperBound;
    }

    @Override
    public String spinCondition(int value) {
        return name + " == " + value;
    }

    @Override
    public String spinType() {
        return "int";
    }

    @Override
    public String spinRange() {
        return nuSMVType();
    }

    @Override
    public String spinValue(int value) {
        return String.valueOf(value);
    }

    @Override
    public String toString() {
        return name + ":" + nuSMVType();
    }
}
