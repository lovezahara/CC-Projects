import org.junit.jupiter.api.TestReporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GenericTests {

    @org.junit.jupiter.api.Test
    void stateConstructor(TestReporter r) {
        StateI<Character> s0 = new State<Character>("bob", false);
        StateI<String> s1 = new State<String>("marci", true);
        StateI<InvalidStateMachineException> s2 = new State<InvalidStateMachineException>(null, true);
        StateI<List<Set<String>>> s3 = new State<List<Set<String>>>(null, false);

        assertFalse(s0.isAccept());
        assertTrue(s1.isAccept());
        assertTrue(s2.isAccept());
        assertFalse(s3.isAccept());

        assertNull(s2.getLabel());
        assertNull(s3.getLabel());
        assertEquals("bob", s0.getLabel());
        assertEquals("marci", s1.getLabel());

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @org.junit.jupiter.api.Test
    void transitionConstructor(TestReporter r) {
        StateI<Character> s0 = new State<Character>("bob", false);
        StateI<Character> s1 = new State<Character>("marci", true);

        assertFalse(s0.isAccept());
        assertTrue(s1.isAccept());
        assertEquals("bob", s0.getLabel());
        assertEquals("marci", s1.getLabel());

        TransitionI<Character> t = new Transition<>('.', s1);
        assertSame(s1, t.nextState());
        assertEquals('.', t.getSymbol());

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @org.junit.jupiter.api.Test
    void addTransition(TestReporter r) {
        StateI<Character> s0 = new State<Character>("bob", false);
        StateI<Character> s1 = new State<Character>("marci", true);

        assertFalse(s0.isAccept());
        assertTrue(s1.isAccept());
        assertEquals("bob", s0.getLabel());
        assertEquals("marci", s1.getLabel());

        s0.addTransition('.', s1);
        s0.addTransition('-', s0);

        List<TransitionI<Character>> txs = s0.getTransitions();
        assertEquals(2, txs.size());
        for(TransitionI<Character> t:txs) {
            if(t.getSymbol().equals('.')) {
                assertSame(s1, t.nextState());
            } else if(t.getSymbol().equals('-')) {
                assertSame(s0, t.nextState());
            } else {
                fail("A transition on "+t.getSymbol()+" should not be present in this test");
            }
        }

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @org.junit.jupiter.api.Test
    void stateFreezes(TestReporter r) {
        StateI<Character> s0 = new State<Character>("bob", false);
        StateI<Character> s1 = new State<Character>("marci", true);

        assertFalse(s0.isAccept());
        assertTrue(s1.isAccept());
        assertEquals("bob", s0.getLabel());
        assertEquals("marci", s1.getLabel());

        s0.addTransition('.', s1);
        s0.addTransition('-', s0);
        s0.freeze();

        List<TransitionI<Character>> txs = s0.getTransitions();
        assertEquals(2, txs.size());
        for(TransitionI<Character> t:txs) {
            if(t.getSymbol().equals('.')) {
                assertSame(s1, t.nextState());
            } else if(t.getSymbol().equals('-')) {
                assertSame(s0, t.nextState());
            } else {
                fail("A transition on "+t.getSymbol()+" should not be present in this test");
            }
        }

        try {
            s1.addTransition('.', s0);
        } catch(UnsupportedOperationException e) {
            fail("addTransition should not throw UnsupportedOpertationException if the state is not frozen");
        }

        try {
            s0.addTransition('+', s1);
            fail("addTransition should throw UnsupportedOperationException if the state has been frozen");
        } catch(UnsupportedOperationException e) {
            // this should be thrown... check that no new transitions have been added
            txs = s0.getTransitions();
            assertEquals(2, txs.size());
            for(TransitionI<Character> t:txs) {
                if(t.getSymbol().equals('.')) {
                    assertSame(s1, t.nextState());
                } else if(t.getSymbol().equals('-')) {
                    assertSame(s0, t.nextState());
                } else {
                    fail("A transition on "+t.getSymbol()+" should not be present in this test");
                }
            }
        }

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @org.junit.jupiter.api.Test
    void DFAGenGoodFileReads(TestReporter r) {
        StateMachineGenI<Character> smg = new DFAGen();
        String filename;

        filename = "data/goodSample00.dfa";
        try {
            StateI<Character> ss = smg.read(new File(filename));
            assertEquals("s0", ss.getLabel());
            assertFalse(ss.isAccept());
            DFAMatcher m = new DFAMatcher(ss);
            assertTrue(m.doesMatch("cat"));
            assertTrue(m.doesMatch("dog"));
            assertFalse(m.doesMatch("catdog"));
            assertFalse(m.doesMatch("dragon"));
            assertEquals("s3", m.endState("cat").getLabel());
            assertEquals("s3", m.endState("dog").getLabel());
        } catch(InvalidStateMachineException e) {
            fail(filename+" should be a valid .dfa file");
        } catch (FileNotFoundException e) {
            fail(filename+" should exist and be readable");
        } catch (IOException e) {
            fail("IOExceptions should not happen during testing");
        } catch (UnmatchedException e) {
            fail("Accept state should have been reached but wasn't");
        }

        filename = "data/goodSample01.dfa";
        try {
            StateI<Character> ss = smg.read(new File(filename));
            assertEquals("s0", ss.getLabel());
            assertFalse(ss.isAccept());
            DFAMatcher m = new DFAMatcher(ss);
            assertTrue(m.doesMatch("cat"));
            assertTrue(m.doesMatch("dog"));
            assertFalse(m.doesMatch("catdog"));
            assertFalse(m.doesMatch("dragon"));
            assertEquals("s3", m.endState("cat").getLabel());
            assertEquals("s6", m.endState("dog").getLabel());
        } catch(InvalidStateMachineException e) {
            fail(filename+" should be a valid .dfa file");
        } catch (FileNotFoundException e) {
            fail(filename+" should exist and be readable");
        } catch (IOException e) {
            fail("IOExceptions should not happen during testing");
        } catch (UnmatchedException e) {
            fail("Accept state should have been reached but wasn't");
        }

        filename = "data/goodSample02.dfa";
        try {
            StateI<Character> ss = smg.read(new File(filename));
            assertEquals("reject", ss.getLabel());
            assertTrue(ss.isAccept());
            DFAMatcher m = new DFAMatcher(ss);
            assertTrue(m.doesMatch(""));
            assertTrue(m.doesMatch("010%5"));
            assertTrue(m.doesMatch("1001%5%010%5"));
            assertTrue(m.doesMatch("1001%5%"));
            assertFalse(m.doesMatch("1"));
            assertTrue(m.doesMatch("010%5"));
            assertFalse(m.doesMatch("1001%5%%010%5%"));
            assertFalse(m.doesMatch("1001%5%5"));
            assertEquals("reject", m.endState("").getLabel());
            assertEquals("boo", m.endState("1001%5%010%5").getLabel());
        } catch(InvalidStateMachineException e) {
            fail(filename+" should be a valid .dfa file");
        } catch (FileNotFoundException e) {
            fail(filename+" should exist and be readable");
        } catch (IOException e) {
            fail("IOExceptions should not happen during testing");
        } catch (UnmatchedException e) {
            fail("Accept state should have been reached but wasn't");
        }

        filename = "data/goodSample03.dfa";
        try {
            StateI<Character> ss = smg.read(new File(filename));
            assertEquals("s0", ss.getLabel());
            assertFalse(ss.isAccept());
            DFAMatcher m = new DFAMatcher(ss);
            assertTrue(m.doesMatch("001"));
            assertFalse(m.doesMatch("110"));
            assertEquals("s3", m.endState("001").getLabel());
        } catch(InvalidStateMachineException e) {
            fail(filename+" should be a valid .dfa file");
        } catch (FileNotFoundException e) {
            fail(filename+" should exist and be readable");
        } catch (IOException e) {
            fail("IOExceptions should not happen during testing");
        } catch (UnmatchedException e) {
            fail("Accept state should have been reached but wasn't");
        }

        filename = "data/goodSample04.dfa";
        try {
            StateI<Character> ss = smg.read(new File(filename));
            assertEquals("@!#", ss.getLabel());
            assertFalse(ss.isAccept());
            DFAMatcher m = new DFAMatcher(ss);
            assertTrue(m.doesMatch("Y"));
            assertTrue(m.doesMatch("NN"));
            assertTrue(m.doesMatch("NYY"));
            assertTrue(m.doesMatch("NYNYY"));
            assertFalse(m.doesMatch("YY"));
            assertFalse(m.doesMatch("NYNYN"));
            assertEquals("bob()", m.endState("Y").getLabel());
            assertEquals("bob()", m.endState("NYNN").getLabel());
        } catch(InvalidStateMachineException e) {
            fail(filename+" should be a valid .dfa file");
        } catch (FileNotFoundException e) {
            fail(filename+" should exist and be readable");
        } catch (IOException e) {
            fail("IOExceptions should not happen during testing");
        } catch (UnmatchedException e) {
            fail("Accept state should have been reached but wasn't");
        }

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }


    @org.junit.jupiter.api.Test
    void DFAGenGoodStreamReads(TestReporter r) {
        StateMachineGenI<Character> smg = new DFAGen();
        String filename;

        filename = "data/goodSample00.dfa";
        try {
            StateI<Character> ss = smg.read(new FileInputStream(filename));
            assertEquals("s0", ss.getLabel());
            assertFalse(ss.isAccept());
            DFAMatcher m = new DFAMatcher(ss);
            assertTrue(m.doesMatch("cat"));
            assertTrue(m.doesMatch("dog"));
            assertFalse(m.doesMatch("catdog"));
            assertFalse(m.doesMatch("dragon"));
            assertEquals("s3", m.endState("cat").getLabel());
            assertEquals("s3", m.endState("dog").getLabel());
        } catch(InvalidStateMachineException e) {
            fail(filename+" should be a valid .dfa file");
        } catch (FileNotFoundException e) {
            fail(filename+" should exist and be readable");
        } catch (IOException e) {
            fail("IOExceptions should not happen during testing");
        } catch (UnmatchedException e) {
            fail("Accept state should have been reached but wasn't");
        }

        filename = "data/goodSample01.dfa";
        try {
            StateI<Character> ss = smg.read(new FileInputStream(filename));
            assertEquals("s0", ss.getLabel());
            assertFalse(ss.isAccept());
            DFAMatcher m = new DFAMatcher(ss);
            assertTrue(m.doesMatch("cat"));
            assertTrue(m.doesMatch("dog"));
            assertFalse(m.doesMatch("catdog"));
            assertFalse(m.doesMatch("dragon"));
            assertEquals("s3", m.endState("cat").getLabel());
            assertEquals("s6", m.endState("dog").getLabel());
        } catch(InvalidStateMachineException e) {
            fail(filename+" should be a valid .dfa file");
        } catch (FileNotFoundException e) {
            fail(filename+" should exist and be readable");
        } catch (IOException e) {
            fail("IOExceptions should not happen during testing");
        } catch (UnmatchedException e) {
            fail("Accept state should have been reached but wasn't");
        }

        filename = "data/goodSample02.dfa";
        try {
            StateI<Character> ss = smg.read(new FileInputStream(filename));
            assertEquals("reject", ss.getLabel());
            assertTrue(ss.isAccept());
            DFAMatcher m = new DFAMatcher(ss);
            assertTrue(m.doesMatch(""));
            assertTrue(m.doesMatch("010%5"));
            assertTrue(m.doesMatch("1001%5%010%5"));
            assertTrue(m.doesMatch("1001%5%"));
            assertFalse(m.doesMatch("1"));
            assertTrue(m.doesMatch("010%5"));
            assertFalse(m.doesMatch("1001%5%%010%5%"));
            assertFalse(m.doesMatch("1001%5%5"));
            assertEquals("reject", m.endState("").getLabel());
            assertEquals("boo", m.endState("1001%5%010%5").getLabel());
        } catch(InvalidStateMachineException e) {
            fail(filename+" should be a valid .dfa file");
        } catch (FileNotFoundException e) {
            fail(filename+" should exist and be readable");
        } catch (IOException e) {
            fail("IOExceptions should not happen during testing");
        } catch (UnmatchedException e) {
            fail("Accept state should have been reached but wasn't");
        }

        filename = "data/goodSample03.dfa";
        try {
            StateI<Character> ss = smg.read(new FileInputStream(filename));
            assertEquals("s0", ss.getLabel());
            assertFalse(ss.isAccept());
            DFAMatcher m = new DFAMatcher(ss);
            assertTrue(m.doesMatch("001"));
            assertFalse(m.doesMatch("110"));
            assertEquals("s3", m.endState("001").getLabel());
        } catch(InvalidStateMachineException e) {
            fail(filename+" should be a valid .dfa file");
        } catch (FileNotFoundException e) {
            fail(filename+" should exist and be readable");
        } catch (IOException e) {
            fail("IOExceptions should not happen during testing");
        } catch (UnmatchedException e) {
            fail("Accept state should have been reached but wasn't");
        }

        filename = "data/goodSample04.dfa";
        try {
            StateI<Character> ss = smg.read(new FileInputStream(filename));
            assertEquals("@!#", ss.getLabel());
            assertFalse(ss.isAccept());
            DFAMatcher m = new DFAMatcher(ss);
            assertTrue(m.doesMatch("Y"));
            assertTrue(m.doesMatch("NN"));
            assertTrue(m.doesMatch("NYY"));
            assertTrue(m.doesMatch("NYNYY"));
            assertFalse(m.doesMatch("YY"));
            assertFalse(m.doesMatch("NYNYN"));
            assertEquals("bob()", m.endState("Y").getLabel());
            assertEquals("bob()", m.endState("NYNN").getLabel());
        } catch(InvalidStateMachineException e) {
            fail(filename+" should be a valid .dfa file");
        } catch (FileNotFoundException e) {
            fail(filename+" should exist and be readable");
        } catch (IOException e) {
            fail("IOExceptions should not happen during testing");
        } catch (UnmatchedException e) {
            fail("Accept state should have been reached but wasn't");
        }

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }
}
