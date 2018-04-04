package basic_block_generator.main;

import basic_block_generator.MainBase;
import basic_block_generator.MealyMachine;
import basic_block_generator.ProblemInstance;
import basic_block_generator.formula.LTLFormula;
import basic_block_generator.variable.BooleanVariable;
import basic_block_generator.variable.IntegerVariable;
import basic_block_generator.variable.Variable;
import org.apache.commons.lang3.tuple.Pair;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by buzhinsky on 4/20/17.
 */
public class BoSyGenerator extends MainBase {
    private final static String INSTANCE_FILENAME = "bosy-instance.json";

    @Argument(usage = "input specification file", metaVar = "<file>", required = true)
    private String input;

    @Option(name = "--path", usage = "BoSy directory path", metaVar = "<dir>", required = true)
    private String path;

    @Option(name = "--linear", handler = BooleanOptionHandler.class,
            usage = "increase bounds linearly, not exponentially")
    private boolean linear;

    public static void main(String[] args) {
        new BoSyGenerator().run(args);
    }

    @Override
    public void launcher() throws IOException, InterruptedException {
        final ProblemInstance instance = ProblemInstance.load(input);
        System.out.println(instance);

        final List<LTLFormula> formulas = new ArrayList<>(instance.synthesisSpecs());
        formulas.addAll(traceSpecification(instance));

        final List<String> synthesisFormulas = formulas.stream()
                .map(spec -> spec.toBoSyString(instance.inputVariables(), instance.outputVariables()))
                .collect(Collectors.toList());
        final List<String> nusmvFormulas = formulas.stream()
                .map(spec -> spec.toNuSMVString(instance.inputVariables(), instance.outputVariables()))
                .collect(Collectors.toList());
        try (PrintWriter pw = new PrintWriter(INSTANCE_FILENAME)) {
            pw.println(problemDescription(instance, synthesisFormulas));
        }

        final String currentPath = Paths.get("").toAbsolutePath().toString();

        final Pair<String, String> headerFooter = MealyMachine.nuSMVHeaderAndFooter("BasicBlock",
                instance.inputVariables(), instance.outputVariables());
        final StringBuilder result = new StringBuilder();
        boolean readingResult = false;

        final ProcessBuilder pb = new ProcessBuilder(".build/release/BoSy", "--synthesize",
                "--qbf-certifier", "quabs", "--target", "smv", "--strategy", linear ? "linear" : "exponential",
                currentPath + "/" + INSTANCE_FILENAME);
        pb.directory(new File(path));
        pb.redirectErrorStream(true);
        final Process process = pb.start();
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                if (line.equals("MODULE main")) {
                    readingResult = true;
                    result.append(headerFooter.getLeft());
                    result.append("VAR -- bits for integer input variables\n");
                    for (Variable var : instance.inputVariables()) {
                        if (var instanceof IntegerVariable) {
                            final int bits = var.maxBits();
                            for (int i = 0; i < bits; i++) {
                                result.append("    b").append(i).append("_").append(var.name).append(" : boolean;\n");
                            }
                        }
                    }
                } else if (readingResult) {
                    if (line.endsWith(" : boolean;")) {
                        continue;
                    }
                    result.append(line.replace("\t", "    ").replaceAll("^    ", "")).append("\n");
                } else {
                    System.out.println(line);
                }
            }
        }
        process.waitFor();
        if (readingResult) {
            // dealing with integer variable encoding
            result.append("ASSIGN -- splitting integer input variables into bits\n");
            for (Variable var : instance.inputVariables()) {
                if (var instanceof IntegerVariable) {
                    final int bits = var.maxBits();
                    for (int i = 0; i < bits; i++) {
                        final String shiftedValue = "(" + var.name + " - (" + ((IntegerVariable) var).lowerBound + "))";
                        final String expression = "(" + shiftedValue + " / " + (1 << i) + ") mod 2 = 1";
                        result.append("    b").append(i).append("_").append(var.name).append(" := ").append(expression)
                                .append(";\n");
                        // ((var.name - var.lowerBound >> i) & 1) == 1
                    }
                }
            }
            result.append("DEFINE -- combining integer output variables from bits\n");
            for (Variable var : instance.outputVariables()) {
                if (var instanceof IntegerVariable) {
                    final List<String> sumElements = new ArrayList<>();
                    final int bits = var.maxBits();
                    for (int i = 0; i < bits; i++) {
                        sumElements.add("(b" + i + "_" + var.name + " ? " + (1 << i) + " : 0)");
                    }
                    sumElements.add("(" + ((IntegerVariable) var).lowerBound + ")");
                    result.append("    ").append(var.name).append(" := ").append(String.join(" + ", sumElements))
                            .append(";\n");
                }
            }
            result.append(headerFooter.getRight()).append("\n");
            nusmvFormulas.forEach(s -> result.append("LTLSPEC ").append(s).append("\n"));

            // dealing with boolean variable encoding
            String s = result.toString();
            for (Variable var : instance.allVariables()) {
                if (var instanceof BooleanVariable) {
                    s = s.replaceAll("\\bb0_" + var.name + "\\b", var.name);
                }
            }
            System.out.println(s);
            try (PrintWriter pw = new PrintWriter(SMV_FILENAME)) {
                pw.println(s);
            }
            checkNuSMV();
        } else {
            System.out.println("SOLUTION NOT FOUND");
        }
    }

    private static String problemDescription(ProblemInstance instance, List<String> formulas) {
        final List<String> inputs = new ArrayList<>();
        final List<String> outputs = new ArrayList<>();
        for (Variable var : instance.inputVariables()) {
            final int bits = var.maxBits();
            for (int i = 0; i < bits; i++) {
                inputs.add("b" + i + "_" + var.name);
            }
        }
        for (Variable var : instance.outputVariables()) {
            final int bits = var.maxBits();
            for (int i = 0; i < bits; i++) {
                outputs.add("b" + i + "_" + var.name);
            }
        }

        final List<String> guarantees = new ArrayList<>(formulas);
        guarantees.addAll(rangeConstraints(instance, false));
        return "{\n" +
                "  \"semantics\": \"mealy\",\n" +
                "  \"inputs\": [" + combine(inputs) + "],\n" +
                "  \"outputs\": [" + combine(outputs) + "],\n" +
                "  \"assumptions\": [" + combine(rangeConstraints(instance, true)) + "],\n" +
                "  \"guarantees\": [" + combine(guarantees) + "]\n" +
                "}\n";
    }

    private static String combine(List<String> list) {
        return String.join(", ", list.stream().map(s -> "\"" + s + "\"").collect(Collectors.toList()));
    }

    private static List<String> rangeConstraints(ProblemInstance instance, boolean input) {
        return rangeConstraints(input ? instance.inputVariables() : instance.outputVariables(),
                (v, bit) -> "b" + bit + "_" + v.name, s -> "!" + s, s -> "G(" + s + ")",
                (x, y) -> "(" + x + " && " + y + ")", (x, y) -> "(" + x + " || " + y + ")");
    }
}
