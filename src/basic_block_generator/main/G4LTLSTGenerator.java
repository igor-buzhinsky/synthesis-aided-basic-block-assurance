package basic_block_generator.main;

import basic_block_generator.MainBase;
import basic_block_generator.MealyMachine;
import basic_block_generator.ProblemInstance;
import basic_block_generator.variable.Variable;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by buzhinsky on 4/13/17.
 */
public class G4LTLSTGenerator extends MainBase {
    private final static String OUTPUT_PATH = "problem.txt";
    private final static String RESULT_FILENAME = "g4ltl-st.txt";

    @Argument(usage = "input specification file", metaVar = "<file>", required = true)
    private String input;

    @Option(name = "--path", usage = "G4LTL-ST command-line jar path", metaVar = "<file>", required = true)
    private String path;

    @Option(name = "--minimize", handler = BooleanOptionHandler.class,
            usage = "greedily minimize the generated state machine")
    private boolean minimize;

    public static void main(String[] args) {
        new G4LTLSTGenerator().run(args);
    }

    @Override
    public void launcher() throws IOException, InterruptedException {
        final ProblemInstance instance = ProblemInstance.load(input);
        System.out.println(instance);

        final List<String> formulas = instance.synthesisSpecs().stream()
                .map(spec -> spec.toG4LTLSTString(instance.inputVariables(), instance.outputVariables()))
                .collect(Collectors.toList());
        formulas.addAll(traceSpecification(instance).stream().map(f -> f.toG4LTLSTString(instance.inputVariables(),
                instance.outputVariables())).collect(Collectors.toList()));

        final ProblemDescription problemDescription = problemDescription(instance, formulas);
        System.out.println(problemDescription.spec);
        try (PrintWriter pw = new PrintWriter(OUTPUT_PATH)) {
            pw.println(problemDescription.spec);
        }

        final String inputs = String.join(", ", problemDescription.inputs);
        final String outputs = String.join(", ", problemDescription.outputs);

        final long time = System.currentTimeMillis();

        int lastUnroll = 0;

        for (int i = 0; i < 100; i++) {
            final int unroll = (int) Math.round(Math.floor(Math.pow(1.25, i)));
            if (unroll == lastUnroll) {
                continue;
            }
            lastUnroll = unroll;
            final String[] command = new String[] { "java", "-jar", "-Xmx4G", path, inputs, outputs, OUTPUT_PATH,
                    String.valueOf(unroll), "true", "true", RESULT_FILENAME };
            System.out.println("\"" + String.join("\" \"", command) + "\"");
            final ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            final Process process = pb.start();
            boolean found = false;
            try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    if (line.equals("FOUND SOLUTION")) {
                        found = true;
                    }
                    System.out.println(line);
                }
            }
            process.waitFor();

            if (found) {
                MealyMachine sm = MealyMachine.fromG4LTLSTCommandLine(RESULT_FILENAME, "BasicBlock",
                        instance.inputVariables(), instance.outputVariables());
                System.out.println("Total execution time: " + (System.currentTimeMillis() - time) + " ms");
                if (minimize) {
                    sm = minimize(sm, instance, false);
                }
                output(sm, instance);
                checkSolution(sm, instance);
                return;
            }
        }
    }

    private static class ProblemDescription {
        final List<String> inputs = new ArrayList<>();
        final List<String> outputs = new ArrayList<>();
        String spec;
    }

    private static ProblemDescription problemDescription(ProblemInstance instance, List<String> formulas) {
        final ProblemDescription p = new ProblemDescription();
        for (Variable var : instance.inputVariables()) {
            final int bits = var.maxBits();
            for (int i = 0; i < bits; i++) {
                p.inputs.add("b" + i + "_" + var.name.toLowerCase());
            }
        }
        for (Variable var : instance.outputVariables()) {
            final int bits = var.maxBits();
            for (int i = 0; i < bits; i++) {
                p.outputs.add("b" + i + "_" + var.name.toLowerCase());
            }
        }
        final StringBuilder sb = new StringBuilder();
        rangeConstraints(instance, true).forEach(f -> sb.append("ASSUME ").append(f).append("\n"));
        rangeConstraints(instance, false).forEach(f -> sb.append(f).append("\n"));
        formulas.forEach(f -> sb.append(f).append("\n"));
        p.spec = sb.toString();
        return p;
    }

    private static List<String> rangeConstraints(ProblemInstance instance, boolean input) {
        return rangeConstraints(input ? instance.inputVariables() : instance.outputVariables(),
                (v, bit) -> "b" + bit + "_" + v.name.toLowerCase(), s -> "!" + s, s -> "ALWAYS (" + s + ")",
                (x, y) -> "(" + x + " && " + y + ")", (x, y) -> "(" + x + " || " + y + ")");
    }
}
