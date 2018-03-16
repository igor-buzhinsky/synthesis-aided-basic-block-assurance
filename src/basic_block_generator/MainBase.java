package basic_block_generator;

import basic_block_generator.formula.BinaryOperator;
import basic_block_generator.formula.LTLFormula;
import basic_block_generator.formula.Proposition;
import basic_block_generator.formula.UnaryOperator;
import basic_block_generator.variable.ValueCombination;
import basic_block_generator.variable.Variable;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by buzhinsky on 7/1/16.
 */
public abstract class MainBase {
    protected final static String SC_FILENAME = "sc.sc";
    protected final static String LTL_FILENAME = "ltl.ltl";

    protected final static String SMV_FILENAME = "result.smv";
    protected final static String SPIN_FILENAME = "result.pml";

    protected abstract void launcher() throws IOException, InterruptedException;

    protected void run(String[] args) {
        if (!parseArgs(args)) {
            return;
        }
        try {
            launcher();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private boolean parseArgs(String[] args) {
        final CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            return true;
        } catch (CmdLineException e) {
            System.out.print("Usage: java -jar <jar filename> ");
            parser.printSingleLineUsage(System.out);
            System.out.println();
            parser.printUsage(System.out);
            return false;
        }
    }

    private static LTLFormula elementFormula(ValueCombination vc) {
        LTLFormula f = null;
        for (int i = 0; i < vc.size(); i++) {
            final LTLFormula part = new Proposition(vc.getVariable(i).name, String.valueOf(vc.getValue(i)));
            f = f == null ? part : new BinaryOperator("&", f, part);
        }
        return f;
    }

    protected static List<LTLFormula> traceSpecification(ProblemInstance instance) {
        final List<LTLFormula> result = new ArrayList<>();
        for (Trace trace : instance.traces()) {
            LTLFormula f = new BinaryOperator("->",
                    elementFormula(trace.inputValues().get(trace.size() - 1)),
                    elementFormula(trace.outputValues().get(trace.size() - 1)));
            for (int i = trace.size() - 2; i >= 0; i--) {
                f = new BinaryOperator("->", elementFormula(trace.inputValues().get(i)),
                        new BinaryOperator("&", elementFormula(trace.outputValues().get(i)),
                                new UnaryOperator("X", f)));
            }
            result.add(f);
        }
        return result;
    }

    /*
     * Each variable is within its allowed range.
     */
    protected static List<String> rangeConstraints(List<Variable> variables,
                                                   BiFunction<Variable, Integer, String> bitEncoder,
                                                   Function<String, String> not, Function<String, String> g,
                                                   BiFunction<String, String, String> and,
                                                   BiFunction<String, String, String> or) {
        final List<String> result = new ArrayList<>();
        for (final Variable var : variables) {
            final int cardinality = var.cardinality();
            final int maxBits = var.maxBits();
            final int sparseValues = 1 << maxBits;
            if (cardinality == sparseValues) {
                continue;
            }
            // encode the constraint "value <= cardinality - 1"
            final boolean x0b = Util.getBit(cardinality - 1, 0);
            final String x0a = bitEncoder.apply(var, 0);
            //x0a <= x0b
            String formula = x0b ? null : not.apply(x0a);
            for (int j = 1; j < maxBits; j++) {
                final boolean xjb = Util.getBit(cardinality - 1, j);
                final String xja = bitEncoder.apply(var, j);
                // xja = xjb
                final String eqPart = xjb ? xja : not.apply(xja);
                final String conjunction = formula == null ? eqPart : and.apply(eqPart, formula);
                // xja < xjb
                final String lessPart = xjb ? not.apply(xja) : null;
                // (xja < xjb) or (xja = xjb) and (previous part)
                formula = lessPart == null ? conjunction : or.apply(lessPart, conjunction);
            }
            result.add(g.apply(formula));
        }
        return result;
    }

    protected static void output(MealyMachine sm, ProblemInstance instance) throws FileNotFoundException {
        //System.out.println(sm.toNuSMVString());
        try (PrintWriter pw = new PrintWriter(SMV_FILENAME)) {
            pw.println(sm.toNuSMVString());
            instance.nuSMVSpecs().forEach(pw::println);
        }
        //System.out.println(sm.toSPINString());
        try (PrintWriter pw = new PrintWriter(SPIN_FILENAME)) {
            pw.println(sm.toSPINString());
            for (int i = 0; i < instance.spinSpecs().size(); i++) {
                pw.println("ltl p" + i + " { X(" + instance.spinSpecs().get(i) + ") }");
            }
        }
    }

    private static void checkTraces(MealyMachine sm, ProblemInstance instance) {
        final boolean traceCompliance = sm.checkTraceCompliance(instance);
        if (traceCompliance) {
            System.err.println("TRACES: OK");
        } else {
            System.err.println("TRACES: NON-COMPLIANT");
        }
    }

    protected static void checkNuSMV() throws IOException, InterruptedException {
        final ProcessBuilder pb = new ProcessBuilder("NuSMV", "-dcx", SMV_FILENAME);
        pb.redirectErrorStream(true);
        final Process process = pb.start();
        boolean ok = true;
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                if (line.startsWith("-- specification ") && line.endsWith("is false")) {
                    ok = false;
                    System.err.println("NuSMV: " + line);
                }
            }
        }
        process.waitFor();
        if (ok) {
            System.err.println("NuSMV: OK");
        }
    }

    private static boolean checkNuSMVFast(MealyMachine sm, ProblemInstance instance) throws IOException,
            InterruptedException {
        final String filename = ".tmp.smv";
        try (PrintWriter pw = new PrintWriter(filename)) {
            pw.println(sm.toNuSMVString());
            instance.nuSMVSpecs().forEach(pw::println);
        }
        final Process process = new ProcessBuilder("NuSMV", "-dcx", filename).start();
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                if (line.startsWith("-- specification ") && line.endsWith("is false")) {
                    process.destroy();
                    return false;
                }
            }
        }
        process.waitFor();
        return true;
    }

    private static void checkSpin() throws IOException, InterruptedException {
        final ProcessBuilder pb = new ProcessBuilder("./spin.sh", "notrail", SPIN_FILENAME);
        pb.redirectErrorStream(true);
        final Process process = pb.start();
        boolean ok = true;
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                if (line.startsWith("*** p") && line.endsWith(" = FALSE ***")) {
                    ok = false;
                    System.err.println("SPIN: " + line);
                }
            }
        }
        process.waitFor();
        if (ok) {
            System.err.println("SPIN: OK");
        }
    }

    protected static void checkSolution(MealyMachine sm, ProblemInstance instance) throws IOException,
            InterruptedException {
        checkTraces(sm, instance);
        checkNuSMV();
        if (false) {
            checkSpin();
        }
    }

    private static boolean checkSolutionBool(MealyMachine sm, ProblemInstance instance, boolean onlyTraces)
            throws IOException, InterruptedException {
        return sm.checkTraceCompliance(instance) && (onlyTraces || checkNuSMVFast(sm, instance));
    }

    private static final Random RND = new Random(31341);

    private static List<Integer> randomPermutation(int size) {
        final List<Integer> ii = Arrays.asList(new Integer[size]);
        for (int i = 0; i < size; i++) {
            ii.set(i, i);
        }
        Collections.shuffle(ii, RND);
        return ii;
    }

    protected static MealyMachine minimize(MealyMachine sm, ProblemInstance instance, boolean onlyTraces)
            throws IOException, InterruptedException {
        System.out.println("Started minimization...");
        System.out.println("Current number of states: " + sm.size);
        l: while (true) {
            final List<Integer> iList = randomPermutation(sm.size);
            final List<Integer> jList = randomPermutation(sm.size);
            for (int i = 0; i < sm.size; i++) {
                final int ii = iList.get(i);
                for (int j = 0; j < sm.size; j++) {
                    final int jj = jList.get(j);
                    if (ii == jj) {
                        continue;
                    }
                    System.out.print(".");
                    final MealyMachine attempt = sm.mergeNodes(ii, jj);
                    if (checkSolutionBool(attempt, instance, onlyTraces)) {
                        sm = attempt;
                        System.out.println("\nCurrent number of states: " + sm.size);
                        continue l;
                    }
                }
            }
            break;
        }
        System.out.println("\nFinished minimization.");
        return sm;
    }
}
