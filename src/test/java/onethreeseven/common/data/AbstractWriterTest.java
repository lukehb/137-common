package onethreeseven.common.data;

import onethreeseven.common.util.FileUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import java.io.*;

/**
 * Test the {@link AbstractWriter}
 * @author Luke Bermingham
 */
public class AbstractWriterTest {

    private static final AbstractWriter<String> mockWriter = new AbstractWriter<String>() {
        @Override
        protected boolean write(BufferedWriter bw, String s) throws IOException {
            bw.write(s);
            return true;
        }
    };

    private static final File testFile = new File(FileUtil.makeAppDir("tests"), "writertest.txt");
    private static final String testStr = "test";

    @Test
    public void testWrite() throws Exception {
        if(testFile.exists() && testFile.delete()){
            System.out.println("Let over test file, deleting now.");
        }

        System.out.println("Writing to: " + testFile.getAbsolutePath());
        Assert.assertTrue(mockWriter.write(testFile, testStr));
        Assert.assertTrue(testFile.exists());
        BufferedReader br = new BufferedReader(new FileReader(testFile));
        Assert.assertTrue(br.readLine().equals(testStr));
        br.close();
    }

    @AfterClass
    public static void tearDown(){
        System.out.println("Deleting: " + testFile.getAbsolutePath() +
                ((testFile.delete()) ? " deleted" : " not deleted")  );
    }

}