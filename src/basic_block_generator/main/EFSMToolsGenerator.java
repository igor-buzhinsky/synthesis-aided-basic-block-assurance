package basic_block_generator.main;

import basic_block_generator.MainBase;
import basic_block_generator.MealyMachine;
import basic_block_generator.ProblemInstance;
import basic_block_generator.formula.LTLFormula;
import basic_block_generator.variable.ValueCombination;
import basic_block_generator.variable.Variable;
import org.apache.commons.lang3.tuple.Pair;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by buzhinsky on 4/13/17.
 */
public class EFSMToolsGenerator extends MainBase {
    private final static String RESULT_FILENAME = "result.gv";

    @Argument(usage = "input specification file", metaVar = "<file>", required = true)
    private String input;

    @Option(name = "--path", usage = "EFSM-Tools jars directory path", metaVar = "<dir>", required = true)
    private String path;

    @Option(name = "--states", aliases = {"-s"}, usage = "number of states", metaVar = "<k>", required = true)
    private int states;

    @Option(name = "--minimize", handler = BooleanOptionHandler.class,
            usage = "greedily minimize the generated state machine")
    private boolean minimize;

    public static void main(String[] args) {
        new EFSMToolsGenerator().run(args);
    }

    @Override
    public void launcher() throws IOException, InterruptedException {
        final ProblemInstance instance = ProblemInstance.load(input);
        //System.out.println(instance);
        final Pair<String, String> p = instance.toEFSMTools();

        try (PrintWriter pw = new PrintWriter(SC_FILENAME)) {
            pw.println(p.getLeft());
        }

        final boolean enableLTL = !instance.synthesisSpecs().isEmpty();
        try (PrintWriter pw = new PrintWriter(LTL_FILENAME)) {
            rangeConstraints(instance).forEach(pw::println);
            for (LTLFormula spec : instance.synthesisSpecs()) {
                pw.println(spec.toEFSMToolsString(instance.inputVariables(), instance.outputVariables()));
            }
        }

        final String command = p.getRight()
                .replace("<JAR>", "fast-automaton-generator.jar")
                .replace("<TRACE_FILENAME>", SC_FILENAME)
                .replace("<LTL>", enableLTL ? ("--ltl " + LTL_FILENAME) : "")
                .replace("<SIZE>", String.valueOf(states))
                .replace("<RESULT>", RESULT_FILENAME);
        System.out.println(command);
        final ProcessBuilder pb = new ProcessBuilder(command.split(" +"));
        pb.directory(new File(path));
        pb.redirectErrorStream(true);
        final Process process = pb.start();
        boolean found = false;
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                if (line.contains(" states WAS FOUND!")) {
                    found = true;
                }
                System.out.println(decodeEvents(line, instance.inputVariables()));
            }
        }
        process.waitFor();

        if (found) {
            MealyMachine sm = MealyMachine.fromEFSMTools(RESULT_FILENAME, "BasicBlock", instance.inputVariables(),
                    instance.outputVariables());
            if (minimize) {
                sm = minimize(sm, instance, false);
            }
            output(sm, instance);
            checkSolution(sm, instance);
        }
    }

    private static List<String> rangeConstraints(ProblemInstance instance) {
        final List<Variable> outputVariables = instance.outputVariables();
        final Map<Variable, Integer> varIndices = new HashMap<>();
        for (int i = 0; i < outputVariables.size(); i++) {
            varIndices.put(outputVariables.get(i), i);
        }
        return rangeConstraints(outputVariables,
                (v, bit) -> "action(v" + varIndices.get(v) + "b" + bit + ")", s -> "!" + s, s -> "G(" + s + ")",
                (x, y) -> "(" + x + " && " + y + ")", (x, y) -> "(" + x + " || " + y + ")");
    }

    private static String decodeEvents(String input, List<Variable> inputVariables) {
        final Pattern p = Pattern.compile("A([0-9]+)/");
        final StringBuilder sb = new StringBuilder();
        final Matcher m = p.matcher(input);
        int lastPos = 0;
        while (m.find()) {
            final int num = Integer.parseInt(m.group(1));
            final String decoded = ValueCombination.denseDecode(inputVariables, num).toNuSMVString();
            sb.append(input.substring(lastPos, m.start()));
            sb.append("[").append(decoded).append("]/");
            lastPos = m.end();
        }
        sb.append(input.substring(lastPos, input.length()));
        return sb.toString();
    }
}
