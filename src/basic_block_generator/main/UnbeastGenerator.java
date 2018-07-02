package basic_block_generator.main;

import basic_block_generator.MainBase;
import basic_block_generator.MealyMachine;
import basic_block_generator.ProblemInstance;
import basic_block_generator.Util;
import basic_block_generator.formula.LTLFormula;
import basic_block_generator.variable.ValueCombination;
import basic_block_generator.variable.Variable;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import static basic_block_generator.formula.LTLFormula.tag;

/**
 * Created by buzhinsky on 4/13/17.
 */
public class UnbeastGenerator extends MainBase {
    private final static String OUTPUT_PATH = "unbeast.xml";

    @Argument(usage = "input specification file", metaVar = "<file>", required = true)
    private String input;

    @Option(name = "--path", usage = "unbeast directory path", metaVar = "<dir>", required = true)
    private String path;

    @Option(name = "--minimize", handler = BooleanOptionHandler.class,
            usage = "greedily minimize the generated state machine")
    private boolean minimize;

    public static void main(String[] args) {
        new UnbeastGenerator().run(args);
    }

    @Override
    public void launcher() throws IOException, InterruptedException {
        final ProblemInstance instance = ProblemInstance.load(input);
        System.out.println(instance);

        final List<String> formulas = new ArrayList<>();
        for (LTLFormula spec : instance.synthesisSpecs()) {
            formulas.add(spec.toUnbeastString(instance.inputVariables(), instance.outputVariables()));
        }
        formulas.addAll(traceSpecification(instance).stream().map(f -> f.toUnbeastString(instance.inputVariables(),
                instance.outputVariables())).collect(Collectors.toList()));

        final String problemDescription = problemDescription(instance, formulas);
        try (PrintWriter pw = new PrintWriter(OUTPUT_PATH)) {
            pw.println(problemDescription);
        }

        final long unbeastStartTime = System.currentTimeMillis();

        final Process process = Runtime.getRuntime().exec(
                new String[] { path + "unbeast", OUTPUT_PATH, "--runSimulator" },
                new String[0]);
        try (
                final Scanner inputScanner = new Scanner(process.getInputStream());
                final PrintWriter writer = new PrintWriter(process.getOutputStream(), true);
        ) {
            while (true) {
                final String line = inputScanner.nextLine();

                System.out.println(line);
                if (line.equals("Do you want the game position to be printed? (y/n)")) {
                    writer.println("y");
                    break;
                }
            }
            System.out.println("Unbeast execution time: " + (System.currentTimeMillis() - unbeastStartTime) + " ms");
            while (true) {
                final String line = inputScanner.nextLine();
                if (line.startsWith("+-+")) {
                    break;
                }
            }
            final Game game = new Game(inputScanner, writer, instance);
            MealyMachine sm = game.reconstructAutomaton();
            process.destroy();
            System.out.println("Total execution time: " + (System.currentTimeMillis() - unbeastStartTime) + " ms");

            if (minimize) {
                sm = minimize(sm, instance, false);
            }
            output(sm, instance);
            checkSolution(sm, instance);
        }
    }

    private static String problemDescription(ProblemInstance instance, List<String> formulas) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n");
        sb.append("<!DOCTYPE SynthesisProblem SYSTEM \"SynSpec.dtd\">\n");
        sb.append("<SynthesisProblem>\n");
        sb.append("<Title>Generated problem</Title>\n");
        sb.append("<Description>Generated description</Description>\n");
        sb.append("<PathToLTLCompiler>./ltl2tgba-wrapper</PathToLTLCompiler>\n");
        sb.append("<GlobalInputs>\n");
        for (Variable var : instance.inputVariables()) {
            final int bits = var.maxBits();
            for (int i = 0; i < bits; i++) {
                sb.append("  <Bit>b").append(i).append("_").append(var.name).append("</Bit>\n");
            }
        }
        sb.append("</GlobalInputs>\n");
        sb.append("<GlobalOutputs>\n");
        for (Variable var : instance.outputVariables()) {
            final int bits = var.maxBits();
            for (int i = 0; i < bits; i++) {
                sb.append("  <Bit>b").append(i).append("_").append(var.name).append("</Bit>\n");
            }
        }
        sb.append("</GlobalOutputs>\n");
        sb.append("<Assumptions>\n");
        rangeConstraints(instance, true).forEach(f -> sb.append("  ").append(tag("LTL", f)).append("\n"));
        sb.append("</Assumptions>\n");
        sb.append("<Specification>\n");
        rangeConstraints(instance, false).forEach(f -> sb.append("  ").append(tag("LTL", f)).append("\n"));
        formulas.forEach(f -> sb.append("  ").append(tag("LTL", f)).append("\n"));
        sb.append("</Specification>\n");
        sb.append("</SynthesisProblem>\n");
        return sb.toString();
    }

    private static List<String> rangeConstraints(ProblemInstance instance, boolean input) {
        return rangeConstraints(input ? instance.inputVariables() : instance.outputVariables(),
                (v, bit) -> tag("Var", "b" + bit + "_" + v.name), s -> tag("Not", s), s -> tag("G", s),
                (x, y) -> tag("And", x + y), (x, y) -> tag("Or", x + y));
    }

    private static class GameState {
        final int number;
        final String description;
        final List<Integer> inputPath;
        final List<Integer> inputCodes = new ArrayList<>();
        final List<Integer> outputCodes = new ArrayList<>();
        final List<GameState> transitions = new ArrayList<>();

        GameState(int actualNumber, String description, List<Integer> inputPath) {
            this.number = actualNumber;
            this.description = description;
            this.inputPath = inputPath;
        }
    }

    private static class Game {
        private final Scanner input;
        private final PrintWriter output;
        private final ProblemInstance instance;

        private final int sparseInputBits;
        private final int denseInputCodes;

        Game(Scanner input, PrintWriter output, ProblemInstance instance) {
            this.input = input;
            this.output = output;
            this.instance = instance;
            sparseInputBits = Util.sparseCardinality(instance.inputVariables());
            denseInputCodes = Util.denseCardinality(instance.inputVariables());
        }

        private void command(String command) {
            //System.out.println("command : " + command);
            output.println(command);
        }

        private String read() {
            //final String line = input.nextLine();
            //System.out.println("response: " + line);
            //return line;
            return input.nextLine();
        }

        private void reachState(GameState state) {
            //System.out.println("reaching " + state.description + "...");
            command("r");

            for (int element : state.inputPath) {
                command(Util.stringFromCode(element, sparseInputBits));
                command("c");
                read();
            }
            //System.out.println("reached  " + state.description);
        }

        private List<String> step(int num) {
            final String str = Util.stringFromCode(num, sparseInputBits);
            command(str);
            final String line1 = read();
            final String[] tokens1 = line1.split("\\|");
            command("c");
            command(str);
            final String line2 = read();
            final String[] tokens2 = line2.split("\\|");
            return Arrays.asList(
                    tokens1[tokens1.length - 3].trim(),
                    tokens1[tokens1.length - 2].trim(),
                    tokens1[tokens1.length - 1].trim(),
                    tokens2[tokens2.length - 3].trim()
            );
        }

        MealyMachine reconstructAutomaton() {
            final List<String> firstStateData = step(0);
            final Map<String, GameState> states = new LinkedHashMap<>();
            final Deque<GameState> unprocessedStates = new LinkedList<>();
            int statesNum = 0;
            final GameState initialState = new GameState(statesNum++, firstStateData.get(0), Arrays.asList());
            unprocessedStates.add(initialState);
            states.put(initialState.description, initialState);

            while (!unprocessedStates.isEmpty()) {
                final GameState s = unprocessedStates.removeFirst();
                for (int denseCode = 0; denseCode < denseInputCodes; denseCode++) {
                    final ValueCombination vc = ValueCombination.denseDecode(instance.inputVariables(), denseCode);
                    final int sparseCode = vc.sparseEncode();
                    reachState(s);
                    final List<String> reply = step(sparseCode);
                    //System.out.println("transition: " + reply);
                    final int input = Util.codeFromString(reply.get(1));
                    final int output = Util.codeFromString(reply.get(2));
                    final String newDescription = reply.get(3);
                    GameState dst = states.get(newDescription);
                    if (dst == null) {
                        final List<Integer> newPath = new ArrayList<>(s.inputPath);
                        newPath.add(input);
                        dst = new GameState(statesNum++, newDescription, newPath);
                        states.put(newDescription, dst);
                        unprocessedStates.add(dst);
                        System.out.println("New state, current #=" + states.size());
                    }
                    s.inputCodes.add(input);
                    s.outputCodes.add(output);
                    s.transitions.add(dst);
                }
            }

            final Integer[][] transitions = new Integer[states.size()][denseInputCodes];
            final ValueCombination[][] outputs = new ValueCombination[states.size()][denseInputCodes];

            for (GameState s : states.values()) {
                for (int i = 0; i < s.transitions.size(); i++) {
                    final int src = s.number;
                    final int dst = s.transitions.get(i).number;
                    final ValueCombination vcIn = ValueCombination.sparseDecode(instance.inputVariables(),
                            s.inputCodes.get(i));
                    final int denseCodeIn = vcIn.denseEncode();
                    final ValueCombination vcOut = ValueCombination.sparseDecode(instance.outputVariables(),
                            s.outputCodes.get(i));
                    transitions[src][denseCodeIn] = dst;
                    outputs[src][denseCodeIn] = vcOut;
                }
            }
            return new MealyMachine("BasicBlock", states.size(), 0, transitions, outputs, instance.inputVariables(),
                    instance.outputVariables());
        }
    }
}
