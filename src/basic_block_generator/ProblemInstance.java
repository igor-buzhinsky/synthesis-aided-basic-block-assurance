package basic_block_generator;

import basic_block_generator.formula.LTLFormula;
import basic_block_generator.generated.ltlLexer;
import basic_block_generator.generated.ltlParser;
import basic_block_generator.variable.ValueCombination;
import basic_block_generator.variable.Variable;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by buzhinsky on 4/14/17.
 */
public class ProblemInstance {
    private final List<Trace> traces;
    private final List<Variable> inputVariables;
    private final List<Variable> outputVariables;

    private final List<LTLFormula> synthesisSpecs;
    private final List<String> nuSMVSpecs;
    private final List<String> spinSpecs;

    public List<Trace> traces() {
        return Collections.unmodifiableList(traces);
    }

    public List<LTLFormula> synthesisSpecs() {
        return Collections.unmodifiableList(synthesisSpecs);
    }

    List<String> nuSMVSpecs() {
        return Collections.unmodifiableList(nuSMVSpecs);
    }

    List<String> spinSpecs() {
        return Collections.unmodifiableList(spinSpecs);
    }

    private ProblemInstance(List<Trace> traces, List<Variable> inputVariables, List<Variable> outputVariables,
                            List<String> synthesisSpecs) throws IOException {
        this.traces = traces;
        this.inputVariables = inputVariables;
        this.outputVariables = outputVariables;
        this.synthesisSpecs = new ArrayList<>();
        this.nuSMVSpecs = new ArrayList<>();
        this.spinSpecs = new ArrayList<>();
        for (String spec : synthesisSpecs) {
            try (InputStream in = new ByteArrayInputStream(spec.getBytes(StandardCharsets.UTF_8))) {
                final ltlLexer lexer = new ltlLexer(new ANTLRInputStream(in));
                final CommonTokenStream tokens = new CommonTokenStream(lexer);
                final ltlParser parser = new ltlParser(tokens);
                final LTLFormula f = parser.formula().f;
                this.synthesisSpecs.add(f);
                this.nuSMVSpecs.add("LTLSPEC " + f.toNuSMVString(inputVariables, outputVariables));
                this.spinSpecs.add(f.toSpinString(inputVariables, outputVariables));
            }
        }
    }

    public List<Variable> inputVariables() {
        return Collections.unmodifiableList(inputVariables);
    }

    public List<Variable> outputVariables() {
        return Collections.unmodifiableList(outputVariables);
    }

    public List<Variable> allVariables() {
        final List<Variable> list = new ArrayList<>(inputVariables);
        list.addAll(outputVariables);
        return Collections.unmodifiableList(list);
    }

    public static ProblemInstance load(String filename) throws IOException {
        final List<Trace> traces = new ArrayList<>();
        final List<Variable> inputVariables = new ArrayList<>();
        final List<Variable> outputVariables = new ArrayList<>();
        final List<String> specs = new ArrayList<>();

        List<ValueCombination> currentInputs = null;
        List<ValueCombination> currentOutputs = null;
        try (final Scanner sc = new Scanner(new File(filename))) {
            while (sc.hasNextLine()) {
                final String line = sc.nextLine().trim();
                final String[] tokens = line.split(" +");
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                } else if (line.startsWith("SPEC_SYNTHESIS ")) {
                    specs.add(line.substring("SPEC_SYNTHESIS ".length()));
                } else if (tokens[0].equals("INPUT") || tokens[0].equals("OUTPUT")) {
                    final List<Variable> list = tokens[0].equals("INPUT") ? inputVariables : outputVariables;
                    for (int i = 1; i < tokens.length; i++) {
                        list.add(Variable.read(tokens[i]));
                    }
                } else if (tokens[0].equals("TRACE")) {
                    if (currentInputs != null) {
                        traces.add(new Trace(currentInputs, currentOutputs));
                    }
                    currentInputs = new ArrayList<>();
                    currentOutputs = new ArrayList<>();
                } else {
                    // assumes that inputs and outputs are separated with "|"
                    currentInputs.add(ValueCombination.fromList(inputVariables,
                            Arrays.asList(tokens).subList(0, inputVariables.size())));
                    currentOutputs.add(ValueCombination.fromList(outputVariables,
                            Arrays.asList(tokens).subList(inputVariables.size() + 1, tokens.length)));
                }
            }
        }
        if (currentInputs != null) {
            traces.add(new Trace(currentInputs, currentOutputs));
        }
        return new ProblemInstance(traces, inputVariables, outputVariables, specs);
    }

    public Pair<String, String> toEFSMTools() {
        final StringBuilder sb = new StringBuilder();
        for (Trace trace : traces) {
            final List<String> inputTokens = new ArrayList<>();
            trace.inputValues().forEach(v -> inputTokens.add("A" + v.denseEncode()));
            final List<String> outputTokens = new ArrayList<>();

            trace.outputValues().forEach(v -> {
                final List<String> bits = new ArrayList<>();
                for (int i = 0; i < outputVariables.size(); i++) {
                    final Variable variable = outputVariables.get(i);
                    final int value = v.getValue(i);
                    final int x = variable.encode(value);
                    final List<Boolean> intBits = Util.toBits(x, variable.maxBits());
                    for (int j = 0; j < intBits.size(); j++) {
                        if (intBits.get(j)) {
                            bits.add("v" + i + "b" + j);
                        }
                    }
                }
                outputTokens.add(String.join(", ", bits));
            });
            sb.append(String.join("; ", inputTokens)).append("\n").append(String.join("; ", outputTokens))
                    .append("\n\n");
        }
        final String command =
                "java -Xmx4G -jar <JAR> <TRACE_FILENAME> <LTL> --size <SIZE> --eventNames " +
                        String.join(",", allEvents()) + " --actionNames " + String.join(",", allActions()) +
                        " --result <RESULT> --bfsConstraints --globalTree --complete";
        return Pair.of(sb.toString(), command);
    }

    private List<String> allEvents() {
        final int possibleInputs = Util.denseCardinality(inputVariables);
        final List<String> result = new ArrayList<>();
        for (int i = 0; i < possibleInputs; i++) {
            result.add("A" + i);
        }
        return result;
    }

    private List<String> allActions() {
        final List<String> result = new ArrayList<>();
        for (int i = 0; i < outputVariables.size(); i++) {
            final int maxBits = outputVariables.get(i).maxBits();
            for (int j = 0; j < maxBits; j++) {
                result.add("v" + i + "b" + j);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("INPUT ").append(inputVariables).append("\n");
        sb.append("OUTPUT ").append(outputVariables).append("\n\n");
        traces.forEach(t -> sb.append(t).append("\n"));
        sb.append("SYNTHESIS SPECS \n");
        synthesisSpecs.forEach(s -> sb.append(s).append("\n"));
        sb.append("\nNUSMV SPECS \n");
        nuSMVSpecs.forEach(s -> sb.append(s).append("\n"));
        sb.append("\nSPIN SPECS \n");
        spinSpecs.forEach(s -> sb.append(s).append("\n"));
        return sb.toString();
    }
}
