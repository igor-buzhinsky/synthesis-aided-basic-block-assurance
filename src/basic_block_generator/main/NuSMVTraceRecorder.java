package basic_block_generator.main;

import basic_block_generator.MainBase;
import basic_block_generator.ProblemInstance;
import basic_block_generator.Trace;
import basic_block_generator.variable.ValueCombination;
import basic_block_generator.variable.Variable;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Consumer;

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

    @Option(name = "--verbose", handler = BooleanOptionHandler.class, usage = "print some info")
    private boolean verbose;

    @Option(name = "--flags", usage = "NuSMV flags after -int", metaVar = "<flags>")
    private String flags = "";

    @Option(name = "--aprosDir", usage = "output traces in the Apros format "
        + "(compatible with EFSM-tools plant model construction) into the specified directory",
            metaVar = "<dir>")
    private String aprosDir = null;

    public static void main(String[] args) {
        new NuSMVTraceRecorder().run(args);
    }

    private void log(Object s) {
        if (verbose) {
            System.out.println(s);
        }
    }

    @Override
    public void launcher() throws IOException, InterruptedException {
        if (aprosDir != null) {
            new File(aprosDir).mkdirs();
        }
        final ProblemInstance instance = ProblemInstance.load(input);
        final String nuSMVString = "NuSMV -int " + flags + " " + model;
        log(nuSMVString);
        final ProcessBuilder pb = new ProcessBuilder(nuSMVString.split(" +"));
        pb.redirectErrorStream(true);
        final Process process = pb.start();
        log("INFO: started NuSMV");
        log(instance);
        try (
                final Scanner inputScanner = new Scanner(process.getInputStream());
                final PrintWriter writer = new PrintWriter(process.getOutputStream(), true);
        ) {
            writer.println("go");
            for (int i = 0; i < traceNum; i++) {
                log("INFO: starting simulation " + i);
                randomSimulation(instance, inputScanner, writer, i);
                log("INFO: finished simulation " + i);
            }
            writer.println("quit");
        }
        process.waitFor();
    }

    private void randomSimulation(ProblemInstance instance, Scanner inputScanner, PrintWriter writer, int traceIndex) {
        boolean reading = false;
        //final int valuesToRead = instance.inputVariables().size() + instance.outputVariables().size();
        final List<ValueCombination> inputs = new ArrayList<>();
        final List<ValueCombination> outputs = new ArrayList<>();
        final Map<String, Integer> values = new TreeMap<>();

        final Consumer<String> write = s -> {
            writer.println(s);
            log(s);
        };

        write.accept("pick_state -r");
        write.accept("simulate -r " + (traceLen - 1));
        write.accept("show_traces -v; echo");
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
                    Integer value = null;
                    try {
                        value = ValueCombination.parseValue(tokens[1]);
                    } catch (NumberFormatException ignored) {
                        // enums are not supported, but at least are tolerated
                    }
                    values.put(tokens[0], value);
                }
            }
        }
        final Trace trace = new Trace(inputs, outputs);
        if (aprosDir == null) {
            System.out.println(trace.toString());
        } else {
            final String s = trace.toAprosString();
            try (PrintWriter out = new PrintWriter(aprosDir + "/trace_" + traceIndex + ".txt")) {
                out.println(s);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
