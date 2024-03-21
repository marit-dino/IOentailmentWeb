package parser;

import org.junit.Test;
import util.Type;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class PairParserTest {

    @Test
    public void getGoalPair_rejects_twoPairs() {
        String twoPairs = "(A,B), (B, C)";
        PairParser p = new PairParser(new ByteArrayInputStream(twoPairs.getBytes(StandardCharsets.UTF_8)));

        Throwable thrown = assertThrows(
                ParseException.class,
                p::GetGoalPair);
        assertTrue(thrown.getMessage().contains("Was expecting"));
    }

    @Test
    public void getGoalPair_rejects_zeroPairs() {
        String zeroPairs = "";
        PairParser p = new PairParser(new ByteArrayInputStream(zeroPairs.getBytes(StandardCharsets.UTF_8)));

        Throwable thrown = assertThrows(
                ParseException.class,
                p::GetGoalPair);
        assertTrue(thrown.getMessage().contains("Was expecting"));
    }

    @Test
    public void getGoalPair_accepts_onePair() throws ParseException {
        String onePair = "((A & ~E), ((A -> (B -> C)) <-> F))";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetGoalPair();
        assertNotNull(node);
    }

    @Test
    public void getGoalPair_buildsCorrectTree_simple() throws ParseException {
        String onePair = "(A, B)";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetGoalPair();
        assertEquals(Type.ROOT, node.jjtGetValue());
        node = (SimpleNode) node.jjtGetChild(0);
        assertEquals(Type.PAIR, node.jjtGetValue());
        assertEquals("A", ((SimpleNode) node.jjtGetChild(0)).jjtGetValue());
        assertEquals("B", ((SimpleNode) node.jjtGetChild(1)).jjtGetValue());
    }

    @Test
    public void getGoalPair_buildsCorrectTree_complex() throws ParseException {
        String onePair = "((A& (B|C)), ((E <-> (F & A)) -> ~B))";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetGoalPair();
        assertEquals(Type.ROOT, node.jjtGetValue());
        node = (SimpleNode) node.jjtGetChild(0);
        assertEquals(Type.PAIR, node.jjtGetValue());

        SimpleNode in = (SimpleNode) node.jjtGetChild(0);
        SimpleNode out = (SimpleNode) node.jjtGetChild(1);
        assertEquals(Type.CON, in.jjtGetValue());
        assertEquals(Type.IMP, out.jjtGetValue());

        SimpleNode in1 = (SimpleNode) in.jjtGetChild(0);
        SimpleNode in2 = (SimpleNode) in.jjtGetChild(1);
        assertEquals("A", in1.jjtGetValue());
        assertEquals(Type.DIS, in2.jjtGetValue());

        SimpleNode in21 = (SimpleNode) in2.jjtGetChild(0);
        SimpleNode in22 = (SimpleNode) in2.jjtGetChild(1);
        assertEquals("B", in21.jjtGetValue());
        assertEquals("C", in22.jjtGetValue());

        SimpleNode out1 = (SimpleNode) out.jjtGetChild(0);
        SimpleNode out2 = (SimpleNode) out.jjtGetChild(1);
        assertEquals(Type.EQ, out1.jjtGetValue());
        assertEquals(Type.NEG, out2.jjtGetValue());

        SimpleNode out21 = (SimpleNode) out2.jjtGetChild(0);
        assertEquals("B", out21.jjtGetValue());

        SimpleNode out11 = (SimpleNode) out1.jjtGetChild(0);
        SimpleNode out12 = (SimpleNode) out1.jjtGetChild(1);
        assertEquals("E", out11.jjtGetValue());
        assertEquals(Type.CON, out12.jjtGetValue());

        SimpleNode out121 = (SimpleNode) out12.jjtGetChild(0);
        SimpleNode out122 = (SimpleNode) out12.jjtGetChild(1);
        assertEquals(Type.FALSE, out121.jjtGetValue());
        assertEquals("A", out122.jjtGetValue());
    }

    @Test
    public void getDerivingPairs_buildsCorrectTree_complex() throws ParseException {
        String onePair = "(~a, ((b & c) | d)), ((b & c), (d <-> (a | c))), ((~a | a), (a <-> (a -> a)))";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode root = p.GetDerivingPairs();

        assertEquals(Type.ROOT, root.jjtGetValue());
        SimpleNode node1 = (SimpleNode) root.jjtGetChild(0);
        assertEquals(Type.PAIR, node1.jjtGetValue());
        SimpleNode node2 = (SimpleNode) root.jjtGetChild(1);
        assertEquals(Type.PAIR, node2.jjtGetValue());
        SimpleNode node3 = (SimpleNode) root.jjtGetChild(2);
        assertEquals(Type.PAIR, node3.jjtGetValue());

        //first pair
        SimpleNode in1 = (SimpleNode) node1.jjtGetChild(0);
        SimpleNode out1 = (SimpleNode) node1.jjtGetChild(1);
        assertEquals(Type.NEG, in1.jjtGetValue());
        assertEquals(Type.DIS, out1.jjtGetValue());

        SimpleNode in11 = (SimpleNode) in1.jjtGetChild(0);
        assertEquals("a", in11.jjtGetValue());

        SimpleNode out11 = (SimpleNode) out1.jjtGetChild(0);
        SimpleNode out12 = (SimpleNode) out1.jjtGetChild(1);
        assertEquals(Type.CON, out11.jjtGetValue());
        assertEquals("d", out12.jjtGetValue());

        SimpleNode out111 = (SimpleNode) out11.jjtGetChild(0);
        SimpleNode out112 = (SimpleNode) out11.jjtGetChild(1);
        assertEquals("b", out111.jjtGetValue());
        assertEquals("c", out112.jjtGetValue());

        //second pair
        in1 = (SimpleNode) node2.jjtGetChild(0);
        out1 = (SimpleNode) node2.jjtGetChild(1);
        assertEquals(Type.CON, in1.jjtGetValue());
        assertEquals(Type.EQ, out1.jjtGetValue());

        in11 = (SimpleNode) in1.jjtGetChild(0);
        SimpleNode in12 = (SimpleNode) in1.jjtGetChild(1);
        assertEquals("b", in11.jjtGetValue());
        assertEquals("c", in12.jjtGetValue());

        out11 = (SimpleNode) out1.jjtGetChild(0);
        out12 = (SimpleNode) out1.jjtGetChild(1);
        assertEquals("d", out11.jjtGetValue());
        assertEquals(Type.DIS, out12.jjtGetValue());

        SimpleNode out121 = (SimpleNode) out12.jjtGetChild(0);
        SimpleNode out122 = (SimpleNode) out12.jjtGetChild(1);
        assertEquals("a", out121.jjtGetValue());
        assertEquals("c", out122.jjtGetValue());

        //third pair
        in1 = (SimpleNode) node3.jjtGetChild(0);
        out1 = (SimpleNode) node3.jjtGetChild(1);
        assertEquals(Type.DIS, in1.jjtGetValue());
        assertEquals(Type.EQ, out1.jjtGetValue());

        in11 = (SimpleNode) in1.jjtGetChild(0);
        in12 = (SimpleNode) in1.jjtGetChild(1);
        assertEquals(Type.NEG, in11.jjtGetValue());
        assertEquals("a", in12.jjtGetValue());

        SimpleNode in111 = (SimpleNode) in11.jjtGetChild(0);
        assertEquals("a", in111.jjtGetValue());

        out11 = (SimpleNode) out1.jjtGetChild(0);
        out12 = (SimpleNode) out1.jjtGetChild(1);
        assertEquals("a", out11.jjtGetValue());
        assertEquals(Type.IMP, out12.jjtGetValue());

        out121 = (SimpleNode) out12.jjtGetChild(0);
        assertEquals("a", out121.jjtGetValue());
        out122 = (SimpleNode) out12.jjtGetChild(1);
        assertEquals("a", out122.jjtGetValue());

    }


    @Test
    public void getDerivingPairs_buildsCorrectTree_simple() throws ParseException {
        String onePair = "(A, B), (B, C)";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));
        SimpleNode node = p.GetDerivingPairs();
        assertEquals(Type.ROOT, node.jjtGetValue());

        SimpleNode node1 = (SimpleNode) node.jjtGetChild(0);
        assertEquals(Type.PAIR, node1.jjtGetValue());
        SimpleNode node2 = (SimpleNode) node.jjtGetChild(1);
        assertEquals(Type.PAIR, node2.jjtGetValue());

        assertEquals("A", ((SimpleNode) node1.jjtGetChild(0)).jjtGetValue());
        assertEquals("B", ((SimpleNode) node1.jjtGetChild(1)).jjtGetValue());

        assertEquals("B", ((SimpleNode) node2.jjtGetChild(0)).jjtGetValue());
        assertEquals("C", ((SimpleNode) node2.jjtGetChild(1)).jjtGetValue());
    }



    @Test
    public void unexpectedToken_throwsException1(){
        String str = "(A + B)";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));

        Throwable thrown = assertThrows(
                TokenMgrError.class,
                p::GetDerivingPairs);
        assertTrue(thrown.getMessage().contains("Lexical error"));
    }

    @Test
    public void unexpectedToken_throwsException2(){
        String str = "(Ä + B)";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));

        Throwable thrown = assertThrows(
                TokenMgrError.class,
                p::GetDerivingPairs);
        assertTrue(thrown.getMessage().contains("Lexical error"));
    }

    @Test
    public void whiteSpace_ignored() throws ParseException {
        String str = "   (      A      ,  B   )     ";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));

        SimpleNode node = p.GetGoalPair();
        assertNotNull(node);
        assertEquals(Type.ROOT, node.jjtGetValue());
        node = (SimpleNode) node.jjtGetChild(0);
        assertEquals(Type.PAIR, node.jjtGetValue());
        assertEquals("A", ((SimpleNode) node.jjtGetChild(0)).jjtGetValue());
        assertEquals("B", ((SimpleNode) node.jjtGetChild(1)).jjtGetValue());
    }

    @Test
    public void getGoalPair_emptyInput_rejected() {
        String str = "";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        Throwable thrown = assertThrows(
                ParseException.class,
                p::GetGoalPair);
        assertTrue(thrown.getMessage().contains("Encountered"));
    }

    @Test
    public void getDerivingPairs_emptyInput_accepted() {
        String str = "";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.GetDerivingPairs();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void getDerivingPairs_accepts_onePair() throws ParseException {
        String onePair = "(A, B)";
        PairParser p = new PairParser(new ByteArrayInputStream(onePair.getBytes(StandardCharsets.UTF_8)));

        SimpleNode node = p.GetDerivingPairs();
        assertNotNull(node);
    }


    @Test
    public void getDerivingPairs_accepts_twoPairs() throws ParseException {
        String twoPairs = "(A, B), (C, D)";
        PairParser p = new PairParser(new ByteArrayInputStream(twoPairs.getBytes(StandardCharsets.UTF_8)));

        SimpleNode node = p.GetDerivingPairs();
        assertNotNull(node);
    }

    @Test
    public void pair_accepts_twoElements() {
        String str = "(A, B)";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Pair();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void pair_rejects_oneElement() {
        String str = "(AB)";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        Throwable thrown = assertThrows(
                ParseException.class,
                p::Pair);
        assertTrue(thrown.getMessage().contains("Was expecting"));
    }

    @Test
    public void pair_rejects_threeElement() {
        String str = "(AB, v, c)";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        Throwable thrown = assertThrows(
                ParseException.class,
                p::Pair);
        assertTrue(thrown.getMessage().contains("Was expecting"));
    }

    @Test
    public void formula_accepts_identifier() {
        String str = "hu1";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Formula();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void formula_rejects_wrongNegation() {
        String str = "~(A)";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        Throwable thrown = assertThrows(
                ParseException.class,
                p::Formula);
        assertTrue(thrown.getMessage().contains("Encountered"));
    }

    @Test
    public void formula_accepts_correctNegation() {
        String str = "~A";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Formula();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void formula_rejects_missingBracket() {
        String str = "(A -> B";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        Throwable thrown = assertThrows(
                ParseException.class,
                p::Formula);
        assertTrue(thrown.getMessage().contains("Encountered"));
    }



    @Test
    public void unaryOperation_acceptsNegation() {
        String str = "~A";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.UnaryOperation();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }


    @Test
    public void unaryOperation_rejectsBinaryOperator() {
        String str = "&A";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        Throwable thrown = assertThrows(
                ParseException.class,
                p::Negation);
        assertTrue(thrown.getMessage().contains("Encountered"));
    }

    @Test
    public void binaryOperation_acceptsTwoParameters() {
        String str = "A <-> B";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.BinaryOperation();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void binaryOperation_rejectsThreeParameters() {
        String str = "A -> B -> C";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.BinaryOperation();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void binaryOperation_rejectsOneParameter() {
        String str = "A | ";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        Throwable thrown = assertThrows(
                ParseException.class,
                p::BinaryOperation);
        assertTrue(thrown.getMessage().contains("Encountered"));
    }

    @Test
    public void identifier_acceptsOneSymbol1() {
        String str = "m";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Identifier();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void identifier_acceptsOneSymbol2() {
        String str = "9";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Identifier();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void identifier_acceptsOneSymbol3() {
        String str = "_";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Identifier();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void identifier_acceptsCombination() {
        String str = "Cat_42";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Identifier();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void identifier_rejectsNotSpecifiedCharacter1() {
        String str = "ß";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        Throwable thrown = assertThrows(
                TokenMgrError.class,
                p::Identifier);
        assertTrue(thrown.getMessage().contains("Encountered"));
    }

    @Test
    public void identifier_rejectsNotSpecifiedCharacter2() {
        String str = ".#";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        Throwable thrown = assertThrows(
                TokenMgrError.class,
                p::Identifier);
        assertTrue(thrown.getMessage().contains("Encountered"));
    }


    @Test
    public void conjunction_acceptsConjunction_simple() {
        String str = "W1 & W2";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Conjunction();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void conjunction_acceptsConjunction_complex1() {
        String str = "((W1 & r) & W2) & (A & (B & ((C & V) & D)))";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Conjunction();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void conjunction_acceptsConjunction_complex2() {
        String str = "((A <-> ~e) -> (F | T)) & (L | (K | (M & ~q)))";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Conjunction();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void disjunction_acceptsTwoParameters() {
        String str = "A | B";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Disjunction();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void disjunction_rejectsThreeParameters() {
        String str = "A | B | C";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Disjunction();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void implication_acceptsImplication_simple() {
        String str = "W1 -> W2";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Implication();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void implication_acceptsImplication_complex1() {
        String str = "((1 -> (r -> e)) -> (W2 -> W1)) -> ((A -> B) -> (B ->D))";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Implication();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void implication_acceptsImplication_complex2() {
        String str = "((A <-> ~(e -> f )) & ((4 | 5) | F)) -> ((L -> ~e) | (K | (M & ~q)))";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Implication();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }
    @Test
    public void negation_rejectsWrongBrackets() {
        String str = "~(A)";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        Throwable thrown = assertThrows(
                ParseException.class,
                p::Negation);
        assertTrue(thrown.getMessage().contains("Encountered"));
    }

    @Test
    public void negation_acceptsNegation_simple() {
        String str = "~A";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Negation();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void negation_acceptsNegation_complex() {
        String str = "~(L | (K | (M & ~q))))";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Negation();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }


    @Test
    public void equivalence_acceptsEquivalence_simple() {
        String str = "A <-> B";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Equivalence();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void equivalence_acceptsEquivalence_complex1() {
        String str = "((r <-> e) <-> (W2 <-> W1)) <-> ((A <-> B) <-> D)";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Equivalence();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }

    @Test
    public void equivalence_acceptsEquivalence_complex2() {
        String str = "((A <-> ~(e -> f )) & ((4 | 5) | F)) <-> ((L -> ~e) | (K | (M & ~q)))";
        PairParser p = new PairParser(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        try {
            p.Equivalence();
        } catch (ParseException e) {
            throw new RuntimeException("Expected action not to throw, but it did", e);
        }
    }
}