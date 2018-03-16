package basic_block_generator.main;

import basic_block_generator.MainBase;
import basic_block_generator.ProblemInstance;
import basic_block_generator.Trace;
import basic_block_generator.variable.ValueCombination;
import basic_block_generator.variable.Variable;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by buzhinsky on 4/20/17.
 */
public class NuSMVTraceRecorder extends MainBase {
    @Argument(usage = "input specification file", metaVar = "<file>", required = true)
    private String input;

    @Option(name = "--model", usage = "NuSMV model to generate traces from", metaVar = "<file>", required = true)
    private String model;

    @Option(name = "--traceNum", usage = "number of traces (default 10)", metaVar = "<tn>")
    private int traceNum = 10;

    @Option(name = "--traceLen", usage = "trace length (default 10)", metaVar = "<tl>")
    private int traceLen = 10;

    public static void main(String[] args) {
        new NuSMVTraceRecorder().run(args);
    }

    @Override
    public void launcher() throws IOException, InterruptedException {
        final ProblemInstance instance = ProblemInstance.load(input);
        final ProcessBuilder pb = new ProcessBuilder(("NuSMV -int " + model).split(" "));
        pb.redirectErrorStream(true);
        final Process process = pb.start();
        try (
                final Scanner inputScanner = new Scanner(process.getInputStream());
                final PrintWriter writer = new PrintWriter(process.getOutputStream(), true);
        ) {
            writer.println("go");
            for (int i = 0; i < traceNum; i++) {
                randomSimulation(instance, inputScanner, writer);
            }
            writer.println("quit");
        }
        process.waitFor();
    }

    private void randomSimulation(ProblemInstance instance, Scanner inputScanner, PrintWriter writer)
            throws IOException, InterruptedException {
        boolean reading = false;
        //final int valuesToRead = instance.inputVariables().size() + instance.outputVariables().size();
        final List<ValueCombination> inputs = new ArrayList<>();
        final List<ValueCombination> outputs = new ArrayList<>();
        final Map<String, Integer> values = new TreeMap<>();
        writer.println("pick_state -r");
        writer.println("simulate -r " + (traceLen - 1));
        writer.println("show_traces -v; echo");
        while (inputs.size() < traceLen) {
            final String line = inputScanner.nextLine().trim();
            if (line.contains("=") && line.contains(".")
                    || line.startsWith("NuSMV >")
                    || line.startsWith("-> State: ") && line.endsWith(".1 <-")) {
                continue;
            } else if (line.equals("Trace Type: Simulation")) {
                reading = true;
            } else if (reading) {
                final String[] tokens = line.split(" = ");
                if (tokens.length != 2) {
                    final List<Integer> inputValues = new ArrayList<>();
                    for (Variable var : instance.inputVariables()) {
                        if (!values.containsKey(var.name)) {
                            System.out.println(values);
                            throw new RuntimeException("Invalid trace: no variable " + var.name
                                    + "; check your NuSMV model!");
                        }
                        inputValues.add(values.get(var.name));
                    }
                    inputs.add(new ValueCombination(inputValues, instance.inputVariables()));
                    final List<Integer> outputValues = new ArrayList<>();
                    for (Variable var : instance.outputVariables()) {
                        if (!values.containsKey(var.name)) {
                            throw new RuntimeException("Invalid trace: no variable " + var.name
                                    + "; check your NuSMV model!");
                        }
                        outputValues.add(values.get(var.name));
                    }
                    outputs.add(new ValueCombination(outputValues, instance.outputVariables()));
                    values.clear();
                } else {
                    values.put(tokens[0], ValueCombination.parseValue(tokens[1]));
                }
            }
        }
        final Trace trace = new Trace(inputs, outputs);
        System.out.println(trace.toString());
    }
}
