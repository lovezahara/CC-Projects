import fileaccess.*;
import lines.DuplicateLabelException;
import lines.Line;
import lines.LineFactory;
import lines.LineI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class LineTests {
    PrintStream origStdout;
    PrintStream origStderr;
    PrintStream testStdout;
    PipedOutputStream outpipe;
    PipedInputStream inpipe;

    @BeforeEach
    void grabStdout() {
        origStdout = System.out;
        origStderr = System.err;
        outpipe = new PipedOutputStream();
        try {
            inpipe = new PipedInputStream(outpipe);
        } catch (IOException e) {
            // This should never happen...
            e.printStackTrace();
            fail("The impossible appears to have occurred...");
        }
        testStdout = new PrintStream(outpipe);
        System.setOut(testStdout);
        System.setErr(testStdout);
    }

    @AfterEach
    void putBack() {
        try {
            System.out.flush();
            System.err.flush();
            int a = inpipe.available();
            if(inpipe.available() != 0) {
                fail("Unexpectedly found output... Did you remove all your temporary print statements?");
            }
        } catch (IOException e) {
            // This should never happen...
            e.printStackTrace();
            fail("The impossible appears to have occurred...");
        } finally {
            System.setOut(origStdout);
            System.setErr(origStderr);
        }
    }

    @Test
    void testBasicCommentLine(TestReporter r) {
        // Generate a CommentLine using the lines.LineFactory
        try {
            LineFactory lf = new LineFactory();
            LineI l = lf.createLine("# I'm a comment!");

            assertEquals("# I'm a comment!", l.sourceText());
            assertEquals(1, l.lineNumber());
            assertEquals("# I'm a comment!", l.outputText(new HashMap<String, LineI>()));
            assertNull(l.address());
        } catch(DuplicateLabelException e) {
            fail("Should not have thrown a DuplicateLabelException");
        }
        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @Test
    void testBasicLabelLine(TestReporter r) {
        // Generate a CommentLine using the lines.LineFactory
        try {
            LineFactory lf = new LineFactory();
            LineI l = lf.createLine(":label:");

            assertEquals(":label:", l.sourceText());
            assertEquals(1, l.lineNumber());
            assertEquals("# :label:", l.outputText(new HashMap<String, LineI>()));
            assertNull(l.address());
        } catch(DuplicateLabelException e) {
            fail("Should not have thrown a DuplicateLabelException");
        }
        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @Test
    void testBasicInstrLine(TestReporter r) {
        // Generate a CommentLine using the lines.LineFactory
        try {
            LineFactory lf = new LineFactory();
            LineI l = lf.createLine("halt");

            assertEquals("halt", l.sourceText());
            assertEquals(1, l.lineNumber());
            assertEquals("0 halt", l.outputText(lf.getLookup()));
            assertEquals("0", l.address());
        } catch(DuplicateLabelException e) {
        fail("Should not have thrown a DuplicateLabelException");
    }

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @Test
    void testLabelInstrLine(TestReporter r) {
        // Generate a CommentLine using the lines.LineFactory
        try {
            LineFactory lf = new LineFactory();
            LineI ll = lf.createLine(":mylabel:");
            LineI l = lf.createLine("jumpn :mylabel:");

            ll.setNextLine(l);

            assertEquals(":mylabel:", ll.sourceText());
            assertEquals("# :mylabel:", ll.outputText(lf.getLookup()));
            assertEquals(1, ll.lineNumber());

            assertEquals("jumpn :mylabel:", l.sourceText());
            assertEquals(2, l.lineNumber());
            assertEquals("0 jumpn 0 # :mylabel:", l.outputText(lf.getLookup()));
            assertEquals("0", l.address());
        } catch(DuplicateLabelException e) {
            fail("Should not have thrown a DuplicateLabelException");
        }

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @Test
    void testIndirectLine(TestReporter r) {
        // Generate a CommentLine using the lines.LineFactory
        try {
            LineFactory lf = new LineFactory();
            LineI ll = lf.createLine(":mylabel:");
            LineI ol = ll;
            assertEquals(":mylabel:", ll.sourceText());
            assertEquals("# :mylabel:", ll.outputText(lf.getLookup()));
            assertEquals(1, ll.lineNumber());

            LineI l;
            for(int i=0; i<100; i++) {
                if(i%2==0) {
                    l = lf.createLine("# Comment" + i);
                } else {
                    l = lf.createLine(":lbl"+i+":");
                }
                ll.setNextLine(l);
                ll = l;
            }
            l = lf.createLine("jumpn :mylabel:");
            ll.setNextLine(l);

            assertEquals("jumpn :mylabel:", l.sourceText());
            assertEquals(102, l.lineNumber());
            assertEquals("0 jumpn 0 # :mylabel:", l.outputText(lf.getLookup()));
            assertEquals("0", l.address());
        } catch(DuplicateLabelException e) {
            fail("Should not have thrown a DuplicateLabelException");
        }

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @Test
    void testDuplicateLabel(TestReporter r) {
        // Generate a CommentLine using the lines.LineFactory
        try {
            LblReaderI lblreader = new LblReader();
            lblreader.read(new File("data/test1.lbl"));
            lblreader.read(new File("data/test2.lbl"));
            fail("DuplicateLabelException should have happened");
        } catch(DuplicateLabelException e) {
            // This should happen, test passed
        } catch(IOException e) {
            fail("IOException should not have happened");
        } catch (BadFileTypeException e) {
            fail("File suffix should be ok");
        }

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @Test
    void testReaderBadFileType(TestReporter r) {
        // Generate a CommentLine using the lines.LineFactory
        try {
            LblReaderI lblreader = new LblReader();
            lblreader.read(new File("data/test1.ref.hmmm"));
            lblreader.read(new File("data/test2.ref.hmmm"));
            fail("File suffix should have caused an exception");
        } catch(DuplicateLabelException e) {
            fail("Should not have thrown a DuplicateLabelException");
        } catch(IOException e) {
            fail("IOException should not have happened");
        } catch (BadFileTypeException e) {
            // Yay! passed
        }

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @Test
    void testWriterBadFileType(TestReporter r) {
        // Generate a CommentLine using the lines.LineFactory
        try {
            LineFactory lf = new LineFactory();
            LineI ll = lf.createLine(":mylabel:");
            LineI l = lf.createLine("jumpn :mylabel:");
            ll.setNextLine(l);
            ArrayList<LineI> lines = new ArrayList<>();
            lines.add(ll);
            lines.add(l);

            HmmmWriterI hmmmwriter = new HmmmWriter(lf.getLookup());
            hmmmwriter.write(new File("data/test1.lbl"), lines);
            fail("File suffix should have caused an exception");
        } catch(DuplicateLabelException e) {
            fail("Should not have thrown a DuplicateLabelException");
        } catch(IOException e) {
            fail("IOException should not have happened");
        } catch (BadFileTypeException e) {
            // Yay! passed
        }

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @Test
    void testReaderFileNotFound(TestReporter r) {
        // Generate a CommentLine using the lines.LineFactory
        try {
            LblReaderI lblreader = new LblReader();
            lblreader.read(new File("data/blahblahblah.lbl"));
            fail("Can't read a file that doesn't exist... there should have been an exception.");
        } catch(DuplicateLabelException e) {
            fail("Should not have thrown a DuplicateLabelException");
        } catch(FileNotFoundException e) {
            // Yay! passed
        } catch(IOException e) {
            fail("IOException should not have happened");
        } catch (BadFileTypeException e) {
            fail("BadFileTypeException should not have happened");
        }

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    @Test
    void testAbstractLine(TestReporter r) {
        // Generate a CommentLine using the lines.LineFactory
        TestLine t1 = new TestLine(42, "test stuff");
        TestLine t2 = new TestLine(16, "blah");
        t1.setNextLine(t2);

        assertEquals(42, t1.lineNumber());
        assertEquals(16,t2.lineNumber());
        assertEquals("test stuff", t1.sourceText());
        assertEquals("blah", t2.sourceText());
        assertEquals(t2, t1.getNextLine());

        // Generate a report line
        String TEST = Thread.currentThread().getStackTrace()[1].getMethodName();
        r.publishEntry(TEST + " -> passed");
    }

    private class TestLine extends Line {
        public TestLine(int linenum, String text) {
            super(linenum, text);
        }
        @Override
        public String outputText(HashMap<String, LineI> labellookup) {
            return "";
        }
        @Override
        public String address() {
            return "";
        }
    }
}