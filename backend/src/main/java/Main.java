
import encoding.IOLogics.causal.OUT_1_C;
import encoding.IOLogics.causal.OUT_2_C;
import encoding.IOLogics.causal.OUT_3_C;
import encoding.IOLogics.causal.OUT_4_C;
import encoding.IOLogics.originals.OUT_1;
import encoding.IOLogics.originals.OUT_2;
import encoding.IOLogics.originals.OUT_3;
import encoding.IOLogics.originals.OUT_4;
import util.IOLogic;
import encoding.EntailmentProblem;
import parser.ParseException;
import parser.TokenMgrError;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import parser.NodeVisitor;
import util.DagNode;
import parser.PairParser;
import parser.SimpleNode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class Main {
    private static final String prompt = ">";
    private static final String ioPrompt = "=>";


    public static void main(String[] args) {
        displayProgramInformation();

        try(Terminal terminal = TerminalBuilder.builder().encoding(StandardCharsets.UTF_8).build()){
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();
            String input = "";
            String original = "";

            while (true) {
                try {
                    original = lineReader.readLine(prompt);
                    input = original.strip().toLowerCase();
                    if (input.equals("h") || input.equals("help")) {
                        displayHelp();
                    } else if (input.isEmpty()) {
                        runProgram(lineReader);
                    } else if (input.equals("q") || input.equals("quit")){
                        return;
                    }
                    else {
                        System.out.println("Unknown command " + original + "\n");
                    }
                } catch (UserInterruptException e) {
                    return;
                } catch (EndOfFileException e) {
                    //ignore
                }
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }

    }


    /**
     * Gets all the necessary input from the user and prints whether the deriving pairs entail the goal pair or not.
     * @param in line reader for the terminal
     */
    private static void runProgram(LineReader in){
        EntailmentProblem p = getInput(in);
        if(p == null) return;

        if(p.entails()) {
            System.out.println("\nDeriving Pairs entail the goal pair.\n");
        } else {
            System.out.println("\nDeriving Pairs do not entail the goal pair.\n");
        }
    }

    /**
     * Takes the input from the user and returns the corresponding entailment problem.
     *
     * @param in line reader for the terminal
     * @return instance of the entailment problem with the deriving pairs, goal pair and the I/O Logic
     */
    private static EntailmentProblem getInput(LineReader in){
        try {
            System.out.println("\nPlease enter the deriving pairs:");
            String input = in.readLine(ioPrompt);
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            List<DagNode> derivingPairs = getDerivingPairs(new PairParser(stream));

            System.out.println("\nPlease enter the goal pair:");
            input = in.readLine(ioPrompt);
            stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            DagNode goalPair = getGoalPair(new PairParser(stream));

            IOLogic logic = getSystem(in);
            switch (logic) {
                case OUT1C -> {
                    return new OUT_1_C(goalPair, derivingPairs);
                }
                case OUT2C -> {
                    return new OUT_2_C(goalPair, derivingPairs);
                }
                case OUT3C -> {
                    return new OUT_3_C(goalPair, derivingPairs);
                }
                case OUT4C -> {
                    return new OUT_4_C(goalPair, derivingPairs);
                }
                case OUT1 -> {
                    return new OUT_1(goalPair, derivingPairs);
                }
                case OUT2 -> {
                    return new OUT_2(goalPair, derivingPairs);
                }
                case OUT3 -> {
                    return new OUT_3(goalPair, derivingPairs);
                }
                case OUT4 -> {
                    return new OUT_4(goalPair, derivingPairs);
                }
                default -> throw new RuntimeException("Unexpected I/O Logic " + logic + " encountered.");
            }

        } catch (ParseException e) {
            System.err.println("Parsing failed, please use correct grammar.");
            System.err.println(e.getMessage());
            System.out.println("Enter 'h' or 'help' for help.\n");
        } catch (TokenMgrError e) {
            System.err.println("Unexpected token encountered:");
            System.err.println(e.getMessage());
            System.err.println("Enter 'h' or 'help' for help.\n");
        }
        return null;
    }

    /**
     * Returns the I/O Logic chosen by the user.
     *
     * @param in line reader for the terminal
     * @return I/O Logic
     */
    private static IOLogic getSystem(LineReader in){
        System.out.println("\nChoose logic:");
        System.out.println("1 - OUT1");
        System.out.println("2 - OUT2");
        System.out.println("3 - OUT3");
        System.out.println("4 - OUT4");
        System.out.println("5 - OUT1C");
        System.out.println("6 - OUT2C");
        System.out.println("7 - OUT3C");
        System.out.println("8 - OUT4C\n");

        String input = in.readLine(ioPrompt);
        return switch (input) {
            case "1" -> IOLogic.OUT1;
            case "2" -> IOLogic.OUT2;
            case "3" -> IOLogic.OUT3;
            case "4" -> IOLogic.OUT4;
            case "5" -> IOLogic.OUT1C;
            case "6" -> IOLogic.OUT2C;
            case "7" -> IOLogic.OUT3C;
            case "8" -> IOLogic.OUT4C;
            default -> {
                System.err.println("Please enter a number between 1 and 8.");
                yield getSystem(in);
            }
        };
    }

    /**
     * Parses the user input for the goal pair according to the specified EBNF grammar of the parser and returns a dag of this input.
     *
     * @param parser for the goal- and deriving pairs
     * @return root of the dag
     * @throws ParseException when parsing fails
     * @throws TokenMgrError when an unexpected token is encountered
     */
    private static DagNode getGoalPair(PairParser parser) throws ParseException, TokenMgrError {
        DagNode root = new DagNode();
        NodeVisitor visitor = new NodeVisitor();
        SimpleNode start = parser.GetGoalPair();
        root = (DagNode) start.jjtAccept(visitor, null);
        return root.getChild(0);
    }

    /**
     * Parses the user input for the deriving pairs according to the specified EBNF grammar of the parser and returns a dag of this input.
     *
     * @param parser for the goal- and deriving pairs
     * @return root of the dag
     * @throws ParseException when parsing fails
     * @throws TokenMgrError when an unexpected token is encountered
     */
    private static List<DagNode> getDerivingPairs(PairParser parser) throws ParseException, TokenMgrError {
        DagNode root = new DagNode();
        NodeVisitor visitor = new NodeVisitor();
        SimpleNode start = parser.GetDerivingPairs();
        root = (DagNode) start.jjtAccept(visitor, null);
        List<DagNode> pairs = new ArrayList<>();

        for (int i = 0; i < root.numberOfChildren(); i++) {
            pairs.add(root.getChild(i));
        }
        return pairs;
    }

    /**
     * Displays the initial program information and usage.
     */
    private static void displayProgramInformation() {
        System.out.println("\nWelcome to the I/O-Entailment-solver!\n");
        System.out.println("Using a SAT-Solver this program efficiently solves the entailment problem for the Input/Output Logics " +
                "OUT1, OUT2, OUT3, OUT4 and their causal counterparts OUT1C, OUT2C, OUT3C, OUT4C.\n" +
                "For more information on the theoretical background see \"Streamlining Input/Output Logics with Sequent Calculi\" by " +
                "Ciabattoni and Rozplokhas." );
        System.out.println("\n=====================================\n");
        System.out.println("Instructions:");
        System.out.println("- enter 'h' or 'help' for help");
        System.out.println("- enter 'q' or 'quit', or press CTRL-C to quit");
        System.out.println("1. start the process by pressing enter");
        System.out.println("2. enter the deriving pairs (see help for grammar)");
        System.out.println("3. enter the goal pair (see help for grammar)");
        System.out.println("4. choose the I/O Logic");
        System.out.println("\n=====================================");
        System.out.println();
    }

    /**
     * Displays help for the user including the EBNF grammar of the pairs.
     */
    private static void displayHelp(){
        System.out.println("\n=====================================\n");
        System.out.println("The goal and the deriving pairs have to be in the following EBNF-grammar, whitespace is ignored:\n");
        System.out.println("<GoalPair> ::= <Pair>");
        System.out.println("<DerivingPairs> ::= <Pair> (\",\" <Pair>)*" );
        System.out.println("<Pair> ::= \"(\" <Formula> \",\" <Formula> \")\"");
        System.out.println("<Formula> ::= <Var> | <TruthValue> | <UnaryOperation> | \"(\" <BinaryOperation> \")\"  ");
        System.out.println("<BinaryOperation> ::= <Conjunction> | <Disjunction> | <Implication> | <Equivalence>  ");
        System.out.println("<Conjunction> ::= <Formula> \" & \" <Formula>");
        System.out.println("<Disjunction> ::= <Formula> \" | \" <Formula>");
        System.out.println("<Implication> ::= <Formula> \" -> \" <Formula>");
        System.out.println("<Equivalence> ::= <Formula> \" <-> \" <Formula>");
        System.out.println("<UnaryOperator> ::= <Negation>");
        System.out.println("<Negation> ::= \"~\" <Formula>");
        System.out.println("<Var> ::= ([\"a\"-\"z\",\"A\"-\"E\", \"G\"-\"S\", \"U\"-\"Z\", \"_\", \"0\" - \"9\"])+");
        System.out.println("<TruthValue> ::= <True> | <False>");
        System.out.println("<True> ::= \"T\"");
        System.out.println("<False> ::= \"F\"");

        System.out.println("\nExample for a goal pair: ((w1 & w2), (~e -> (a <-> b)))");
        System.out.println("Example for deriving pairs: ((w1 & w2), (~e -> (a <-> b))), (A, B), (~C, (e | (w1 <-> w2)))");
        System.out.println("\nTo exit the program enter 'q' or 'quit' or press CTRL-C.");
        System.out.println("\n=====================================\n");

    }
}