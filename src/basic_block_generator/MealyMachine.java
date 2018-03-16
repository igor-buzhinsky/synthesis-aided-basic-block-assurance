package basic_block_generator;

import basic_block_generator.variable.BooleanVariable;
import basic_block_generator.variable.IntegerVariable;
import basic_block_generator.variable.ValueCombination;
import basic_block_generator.variable.Variable;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.Source;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by buzhinsky on 4/13/17.
 */
public class MealyMachine {
    private final String name;
    final int size;
    private final int initialState;
    private final Integer[][] transitions;
    private final ValueCombination[][] outputs;
    private final List<Variable> inputVariables;
    private final List<Variable> outputVariables;
    private final Variable stateVar;

    public MealyMachine(String name, int size, int initialState, Integer[][] transitions, ValueCombination[][] outputs,
                        List<Variable> inputVariables, List<Variable> outputVariables) {
        this.name = name;
        this.size = size;
        this.initialState = initialState;
        this.transitions = transitions;
        this.outputs = outputs;
        this.inputVariables = inputVariables;
        this.outputVariables = outputVariables;
        stateVar = new IntegerVariable("state", 0, size - 1);
    }

    public static MealyMachine fromG4LTLSTCommandLine(String filename, String name, List<Variable> inputVariables,
                                                      List<Variable> outputVariables) throws IOException {
        int initialState = -1;
        final List<Integer> newNumberToOldNumber = new ArrayList<>();
        final Map<Integer, Integer> oldNumberToNewNumber = new TreeMap<>();

        final Function<String, Integer> convertState = str -> {
            final int s = Integer.parseInt(str);
            Integer num = oldNumberToNewNumber.get(s);
            if (num == null) {
                num = newNumberToOldNumber.size();
                newNumberToOldNumber.add(s);
                oldNumberToNewNumber.put(s, num);
            }
            return num;
        };

        final List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
        final Map<Pair<Integer, Integer>, Pair<Integer, Integer>> transitionMap = new HashMap<>();
        for (String line : lines) {
            final String[] tokens = line.split(" ");
            final String type = tokens[0];
            if (type.equals("initial")) {
                initialState = convertState.apply(tokens[1]);
            } else if (type.equals("transition")) {
                final int from = convertState.apply(tokens[1]);
                final int input = Util.codeFromString(tokens[2]);
                final int to = convertState.apply(tokens[3]);
                final int output = Util.codeFromString(tokens[4]);
                transitionMap.put(Pair.of(from, input), Pair.of(to, output));
            }
        }
        final int size = newNumberToOldNumber.size();
        final int inputsNum = Util.denseCardinality(inputVariables);
        final Integer[][] transitions = new Integer[size][inputsNum];
        final ValueCombination[][] outputs = new ValueCombination[size][inputsNum];
        for (int src = 0; src < size; src++) {
            for (int in = 0; in < inputsNum; in++) {
                final int sparseIn = ValueCombination.denseDecode(inputVariables, in).sparseEncode();
                final Pair<Integer, Integer> p = transitionMap.get(Pair.of(src, sparseIn));
                transitions[src][in] = p.getLeft();
                outputs[src][in] = ValueCombination.sparseDecode(outputVariables, p.getRight());
            }
        }

        return new MealyMachine(name, size, initialState, transitions, outputs, inputVariables, outputVariables);
    }

    public static MealyMachine fromEFSMTools(String filename, String name, List<Variable> inputVariables,
                                             List<Variable> outputVariables) throws FileNotFoundException {
        final int possibleInputs = Util.denseCardinality(inputVariables);
        final List<List<Integer>> data = new ArrayList<>();

        try (final Scanner sc = new Scanner(new File(filename))) {
            while (sc.hasNextLine()) {
                final String line = sc.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#") || !line.contains(" -> ")) {
                    continue;
                }
                final String[] tokens = line.split("[ ,\\[\\]=\";\\->()]+");
                final int from = Integer.parseInt(tokens[0]);
                final int to = Integer.parseInt(tokens[1]);
                final int eventCode = Integer.parseInt(tokens[3].substring(1));

                final int[] outputCodes = new int[outputVariables.size()];
                for (int i = 5; i < tokens.length; i++) {
                    final String[] moreTokens = tokens[i].split("[vb]");
                    final int var = Integer.parseInt(moreTokens[1]);
                    final int bit = Integer.parseInt(moreTokens[2]);
                    outputCodes[var] |= 1 << bit;
                }
                for (int i = 0; i < outputCodes.length; i++) {
                    // convert to proper variable values
                    outputCodes[i] = outputVariables.get(i).decode(outputCodes[i]);
                }
                final List<Integer> row = new ArrayList<>();
                row.addAll(Arrays.asList(from, to, eventCode));
                row.addAll(Arrays.asList(ArrayUtils.toObject(outputCodes)));
                data.add(row);
            }
        }

        final int size = data.stream().mapToInt(l -> l.get(0)).max().getAsInt() + 1;
        final Integer[][] transitions = new Integer[size][possibleInputs];
        final ValueCombination[][] outputs = new ValueCombination[size][possibleInputs];
        for (List<Integer> transition : data) {
            final int from = transition.get(0);
            final int to = transition.get(1);
            final int eventCode = transition.get(2);
            transitions[from][eventCode] = to;
            outputs[from][eventCode] = new ValueCombination(transition.subList(3, transition.size()), outputVariables);
        }

        return new MealyMachine(name, size, 0, transitions, outputs, inputVariables, outputVariables);
    }

    boolean checkTraceCompliance(ProblemInstance instance) {
        for (Trace trace : instance.traces()) {
            int state = initialState;
            for (int pos = 0; pos < trace.size(); pos++) {
                final ValueCombination input = trace.inputValues().get(pos);
                final int code = input.denseEncode();
                final ValueCombination output = outputs[state][code];
                if (!output.equals(trace.outputValues().get(pos))) {
                    return false;
                }
                state = transitions[state][code];
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + inputVariables + " " + outputVariables;
    }

    String toSPINString() {
        final StringBuilder sb = new StringBuilder();
        Stream.concat(inputVariables.stream(), outputVariables.stream())
                .forEach(v -> sb.append(v.spinType()).append(" ").append(v.name).append(";\n"));
        sb.append("\n");
        sb.append("int ").append(stateVar.name).append(" = ").append(initialState).append(";\n");
        sb.append("\n");
        sb.append("init { do :: atomic { \n");
        for (Variable v : inputVariables) {
            sb.append("  select(").append(v.name).append(" : ").append(v.spinRange()).append(");\n");
        }
        sb.append("\n");
        sb.append("  d_step {\n");
        for (int k = 0; k < outputVariables.size(); k++) {
            sb.append("    ");
            final List<Integer> varValues = new ArrayList<>();
            final List<String> subtrees = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                varValues.add(i);
                subtrees.add(spinOutputsToString(k, i));
            }
            sb.append(smartIndent(compactSubtreesToString(stateVar, varValues, subtrees, SPIN_CONST)));
        }
        sb.append("    ");
        final List<Integer> varValues = new ArrayList<>();
        final List<String> subtrees = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            varValues.add(i);
            subtrees.add(spinStatesToString(i));
        }
        sb.append(smartIndent(compactSubtreesToString(stateVar, varValues, subtrees, SPIN_CONST)));
        sb.append("  }\n");
        sb.append("} od }\n");
        return sb.toString();
    }

    public static Pair<String, String> nuSMVHeaderAndFooter(String moduleName, List<Variable> inputVariables,
                                                            List<Variable> outputVariables) {
        final String inputString = "(" + String.join(", ", inputVariables.stream().map(v -> v.name)
                .collect(Collectors.toList())) + ")";
        final String header = "MODULE " + moduleName + inputString + "\n";
        final StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("MODULE main\n");
        sb.append("VAR\n");
        for (Variable v : inputVariables) {
            sb.append("    ").append(v.name).append(" : ").append(v.nuSMVType()).append(";\n");
        }
        sb.append("    block : ").append(moduleName).append(inputString).append(";\n");
        sb.append("DEFINE\n");
        for (Variable v : outputVariables) {
            sb.append("    ").append(v.name).append(" := block.").append(v.name).append(";\n");
        }
        return Pair.of(header, sb.toString());
    }

    public String toNuSMVString() {
        final Pair<String, String> headerFooter = nuSMVHeaderAndFooter(name, inputVariables, outputVariables);
        final StringBuilder sb = new StringBuilder(headerFooter.getLeft());
        if (size > 1) {
            sb.append("VAR\n");
            sb.append("    ").append(stateVar.name).append(" : 0..").append(size - 1).append(";\n");
            sb.append("ASSIGN\n");
            sb.append("    init(").append(stateVar.name).append(") := ").append(initialState).append(";\n");
            sb.append("    next(").append(stateVar.name).append(") := ");
            final List<Integer> varValues = new ArrayList<>();
            final List<String> subtrees = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                varValues.add(i);
                subtrees.add(nuSMVStatesToString(i));
            }
            sb.append(smartIndent(compactSubtreesToString(stateVar, varValues, subtrees, NUSMV_CONST)));
        }
        sb.append("DEFINE\n");
        for (int k = 0; k < outputVariables.size(); k++) {
            final Variable v = outputVariables.get(k);
            sb.append("    ").append(v.name).append(" := ");
            final List<Integer> varValues = new ArrayList<>();
            final List<String> subtrees = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                varValues.add(i);
                subtrees.add(nuSMVOutputsToString(k, i));
            }
            sb.append(smartIndent(compactSubtreesToString(stateVar, varValues, subtrees, NUSMV_CONST)));
        }
        sb.append(headerFooter.getRight());
        return sb.toString();
    }

    private class FunctionToString<FT> {
        private final int state;
        private final LanguageConst c;
        private final FT[][] valueFunction;
        private final Function<FT, String> subtreeFunction;

        FunctionToString(int state, LanguageConst c, FT[][] valueFunction,
                         Function<FT, String> subtreeFunction) {
            this.state = state;
            this.c = c;
            this.valueFunction = valueFunction;
            this.subtreeFunction = subtreeFunction;
        }

        private int vcIndex = 0;

        private String generate(int varIndex) {
            final Variable inVar = inputVariables.get(varIndex);
            final List<Integer> varValues = new ArrayList<>();
            final List<String> subtrees = new ArrayList<>();
            for (int i = 0; i < inVar.cardinality(); i++) {
                varValues.add(inVar.decode(i));
                subtrees.add(varIndex == 0
                        ? subtreeFunction.apply(valueFunction[state][vcIndex++]) + "\n"
                        : generate(varIndex - 1));
            }
            return compactSubtreesToString(inVar, varValues, subtrees, c);
        }

        String generate() {
            return generate(inputVariables.size() - 1);
        }
    }

    private String smartIndent(String s) {
        return Util.indent(s).substring(4);
    }

    private static class LanguageConst {
        final String caseStart;
        final String caseEnd;
        final String valueStart;
        final String valueEnd;
        final String strOr;
        final String strTrue;
        final String strEq;
        final BiFunction<Variable, Integer, String> conditionFunction;
        final BiFunction<Collection<Integer>, String, String> expressWithIntervals;

        LanguageConst(String caseStart, String caseEnd, String valueStart, String valueEnd, String strOr,
                      String strTrue, String strEq, BiFunction<Variable, Integer, String> conditionFunction,
                      BiFunction<Collection<Integer>, String, String> expressWithIntervals) {
            this.caseStart = caseStart;
            this.caseEnd = caseEnd;
            this.valueStart = valueStart;
            this.valueEnd = valueEnd;
            this.strOr = strOr;
            this.strTrue = strTrue;
            this.strEq = strEq;
            this.conditionFunction = conditionFunction;
            this.expressWithIntervals = expressWithIntervals;
        }
    }

    private static final LanguageConst NUSMV_CONST = new LanguageConst("case", "esac;", "", ": ", "|", "TRUE", "=",
            Variable::nuSMVCondition, MealyMachine::expressWithIntervalsNuSMV);
    private static final LanguageConst SPIN_CONST = new LanguageConst("if", "fi", ":: ", " -> ", "||", "true", "==",
            Variable::spinCondition, MealyMachine::expressWithIntervalsSPIN);

    private String compactSubtreesToString(Variable var, List<Integer> varValues, List<String> subtrees, LanguageConst c) {
        final Map<String, List<Integer>> map = new LinkedHashMap<>();
        for (int i = 0; i < varValues.size(); i++) {
            map.computeIfAbsent(subtrees.get(i), k -> new ArrayList<>()).add(i);
        }

        final int totalEntries = map.size();
        if (totalEntries == 1) {
            return subtrees.get(0);
        } else {
            final StringBuilder sb = new StringBuilder(c.caseStart + "\n");
            int index = 0;
            for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
                final String valueList;
                if (++index == totalEntries) {
                    valueList = c.strTrue;
                } else {
                    final List<Integer> pref = entry.getValue().stream().map(varValues::get)
                            .collect(Collectors.toList());
                    if (var instanceof BooleanVariable) {
                        valueList = String.join(" " + c.strOr + " ", pref.stream()
                                .map(p -> c.conditionFunction.apply(var, p)).collect(Collectors.toList()));
                    } else {
                        valueList = c.expressWithIntervals.apply(new ArrayList<>(pref), var.name);
                    }
                }
                sb.append(Util.indent(c.valueStart + valueList + c.valueEnd + entry.getKey()));
            }
            sb.append("    ").append(c.caseEnd).append("\n");
            return sb.toString();
        }
    }

    private String nuSMVOutputsToString(int outputIndex, int state) {
        final Variable outVar = outputVariables.get(outputIndex);
        return this.new FunctionToString<>(state, NUSMV_CONST, outputs,
                x -> outVar.nuSMVValue(x.getValue(outputIndex)) + ";").generate();
    }

    private String nuSMVStatesToString(int state) {
        return this.new FunctionToString<>(state, NUSMV_CONST, transitions,
                x -> x + ";").generate();
    }

    private String spinOutputsToString(int outputIndex, int state) {
        final Variable outVar = outputVariables.get(outputIndex);
        return this.new FunctionToString<>(state, SPIN_CONST, outputs,
                x -> outVar.name + " = " + outVar.spinValue(x.getValue(outputIndex)) + ";").generate();
    }

    private String spinStatesToString(int state) {
        return this.new FunctionToString<>(state, SPIN_CONST, transitions,
                x -> "state = " + x + ";").generate();
    }

    private static List<Pair<Integer, Integer>> intervals(Collection<Integer> values) {
        final List<Pair<Integer, Integer>> intervals = new ArrayList<>();
        int min = -1;
        int max = -1;
        for (int value : values) {
            if (min == -1) {
                min = max = value;
            } else if (value == max + 1) {
                max = value;
            } else if (value <= max) {
                throw new AssertionError("Input set must contain increasing values.");
            } else {
                intervals.add(Pair.of(min, max));
                min = max = value;
            }
        }
        intervals.add(Pair.of(min, max));
        return intervals;
    }

    private static String expressWithIntervalsNuSMV(Collection<Integer> values, String varName) {
        if (values.size() == 1) {
            return varName + " = " + values.iterator().next();
        }
        final List<Pair<Integer, Integer>> intervals = intervals(values);
        final List<String> stringIntervals = new ArrayList<>();
        final Set<Integer> separate = new TreeSet<>();
        for (Pair<Integer, Integer> interval : intervals) {
            if (interval.getLeft() + 1 >= interval.getRight()) {
                separate.add(interval.getLeft());
                separate.add(interval.getRight());
            } else {
                stringIntervals.add(interval.getLeft() + ".." + interval.getRight());
            }
        }
        if (!separate.isEmpty()) {
            stringIntervals.add(separate.toString().replace("[", "{").replace("]", "}"));
        }
        return varName + " in " + String.join(" union ", stringIntervals);
    }

    private static String expressWithIntervalsSPIN(Collection<Integer> values, String varName) {
        final List<Pair<Integer, Integer>> intervals = intervals(values);
        final List<String> stringIntervals = new ArrayList<>();
        final Set<Integer> separate = new TreeSet<>();
        for (Pair<Integer, Integer> interval : intervals) {
            if (interval.getLeft() + 1 >= interval.getRight()) {
                separate.add(interval.getLeft());
                separate.add(interval.getRight());
            } else {
                stringIntervals.add(varName + " >= " + interval.getLeft() + " && " + varName + " <= "
                        + interval.getRight());
            }
        }
        if (!separate.isEmpty()) {
            stringIntervals.addAll(separate.stream().map(value -> varName + " == " + value)
                    .collect(Collectors.toList()));
        }
        return String.join(" || ", stringIntervals);
    }

    /*
     * delete state n2, redirecting transitions to n1
     */
    MealyMachine mergeNodes(int n1, int n2) {
        final Function<Integer, Integer> replace = n1 < n2
                ? (x -> x < n2 ? x : x == n2 ? n1 : (x - 1))
                : (x -> x < n2 ? x : x == n2 ? (n1 - 1) : (x - 1));
        final int sizeNew = size - 1;
        final int initialStateNew = replace.apply(initialState);
        final int inputsNum = transitions[0].length;
        final Integer[][] transitionsNew = new Integer[sizeNew][inputsNum];
        final ValueCombination[][] outputsNew = new ValueCombination[sizeNew][];

        for (int i = 0; i < size; i++) {
            if (i == n2) {
                continue;
            }
            final int index = i - (i < n2 ? 0 : 1);
            outputsNew[index] = outputs[i];
            for (int j = 0; j < inputsNum; j++) {
                transitionsNew[index][j] = replace.apply(transitions[i][j]);
            }
        }

        return new MealyMachine(name, sizeNew, initialStateNew, transitionsNew, outputsNew, inputVariables,
                outputVariables);
    }
}
