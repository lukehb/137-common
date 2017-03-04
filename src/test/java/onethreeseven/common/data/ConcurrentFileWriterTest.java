package onethreeseven.common.data;


import onethreeseven.common.util.FileUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Testing writing to a file with multiple threads.
 * @see ConcurrentFileWriter
 * @author Luke Bermingham
 */
public class ConcurrentFileWriterTest {

    private static final int nThreads = 10;
    private static File someFile;
    private static ConcurrentFileWriter writer;

    @BeforeClass
    public static void setup() throws Exception {
        URL path = ConcurrentFileWriterTest.class.getProtectionDomain().getCodeSource().getLocation();
        someFile = new File(path.toURI().resolve("test.txt"));
        if (someFile.exists()) {
            if (someFile.delete()) {
                System.out.println("Deleted test file.");
            }
        }
        if (someFile.createNewFile()) {
            System.out.println("Created empty file for testing.");
        }
        writer = new ConcurrentFileWriter(someFile, nThreads);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (someFile.delete()) {
            System.out.println("Deleted test file.");
        }
    }

    @Test
    public void testAppend() throws Exception {
        ExecutorService exec = Executors.newFixedThreadPool(nThreads);

        final String testString = "test test tets \n";

        for (int i = 0; i < nThreads; i++) {
            exec.submit(() -> writer.append(testString));
        }

        //wait for threads to write
        exec.shutdown();
        if (exec.awaitTermination(60, TimeUnit.SECONDS)) {
            System.out.println("Shut down all appender threads.");
        }
        writer.close();

        FileUtil.readLineByLine(someFile, FileUtil.getLineBreaks(), true, s -> {
            System.out.println("Read line: " + s);
            Assert.assertTrue(s.equals(testString));
        });

    }
}