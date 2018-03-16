package basic_block_generator;

import basic_block_generator.variable.Variable;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by buzhinsky on 4/15/17.
 */
public class Util {
    public static int denseCardinality(List<Variable> vars) {
        return vars.stream().mapToInt(Variable::cardinality).reduce(1, (x, y) -> x * y);
    }

    public static int sparseCardinality(List<Variable> vars) {
        return vars.stream().mapToInt(Variable::maxBits).sum();
    }

    public static List<Boolean> toBits(int value, int maxBits) {
        final Boolean[] bits = new Boolean[maxBits];
        for (int i = 0; i < maxBits; i++) {
            bits[i] = ((value >> i) & 1) == 1;
        }
        return Arrays.asList(bits);
    }

    public static Boolean getBit(int value, int i) {
        return ((value >> i) & 1) == 1;
    }

    public static int setBit(int value, int i, boolean bit) {
        return bit ? (value | (1 << i)) : (value & ~(1 << i));
    }

    static String indent(String s) {
        final String indent = "    ";
        final String ans = String.join("\n", Arrays.stream(s.split("\n")).map(x -> indent + x)
                .collect(Collectors.toList()));
        return s.endsWith("\n") ? (ans + "\n") : ans;
    }

    public static String stringFromCode(int num, int bits) {
        final char[] arr = new char[bits];
        for (int i = 0; i < bits; i++) {
            arr[i] = Util.getBit(num, i) ? '1' : '0';
        }
        return new String(arr);
    }

    public static int codeFromString(String code) {
        int x = 0;
        for (int i = 0; i < code.length(); i++) {
            x = Util.setBit(x, i, code.charAt(i) == '1');
        }
        return x;
    }
}
