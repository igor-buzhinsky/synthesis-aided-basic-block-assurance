package basic_block_generator.variable;

/**
 * Created by buzhinsky on 4/13/17.
 */
public abstract class Variable {
    public final String name;

    public Variable(String name) {
        this.name = name;
    }

    public abstract int encode(int value);
    public abstract int decode(int value);
    public abstract int cardinality();
    public abstract boolean isValidValue(int value);

    public abstract String nuSMVCondition(int value);
    public abstract String nuSMVValue(int value);
    public abstract String nuSMVType();

    public abstract String spinCondition(int value);
    public abstract String spinType();
    public abstract String spinRange();
    public abstract String spinValue(int value);

    public static Variable read(String traceString) {
        final String[] tokens = traceString.split(":");
        final String name = tokens[0];
        final String type = tokens[1];
        if (type.equals("bool")) {
            return new BooleanVariable(name);
        } else if (type.contains("..")) {
            final String[] moreTokens = type.split("\\.\\.");
            final int lowerBound = Integer.parseInt(moreTokens[0]);
            final int upperBound = Integer.parseInt(moreTokens[1]);
            return new IntegerVariable(name, lowerBound, upperBound);
        } else {
            throw new RuntimeException();
        }
    }

    public int maxBits() {
        return (int) Math.round(Math.ceil(Math.log(cardinality()) / Math.log(2)));
    }
}
