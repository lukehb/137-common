package onethreeseven.common.util;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Testing the file utility.
 * @see FileUtil
 * @author Luke Bermingham
 */
public class FileUtilTest {

    @Test
    public void testReadLineByLine() throws Exception {

        //write a file with four lines
        File tempFile = FileUtil.makeTempFile();
        FileWriter fw = new FileWriter(tempFile);
        fw.write("one\n");
        fw.write("two\n");
        fw.write("three\n");
        fw.write("four");
        fw.close();

        AtomicInteger linesProcessed = new AtomicInteger(0);
        FileUtil.readLineByLine(tempFile, FileUtil.getLineBreaks(), false, s -> {
            linesProcessed.incrementAndGet();
            System.out.println(s);
        });
        Assert.assertTrue(linesProcessed.get() == 4);
        tempFile.deleteOnExit();
    }

    @Test
    public void testMakeTmpFile() {
        File tmpFile = FileUtil.makeTempFile();
        Assert.assertTrue(tmpFile.exists());
        Assert.assertTrue(FileUtil.fileOkayToRead(tmpFile));
        if (tmpFile.delete()) {
            System.out.println("Deleted tmp file: " + tmpFile.getAbsolutePath());
            Assert.assertTrue(!tmpFile.exists());
        }
    }

    @Test
    public void testMakeDir(){
        File dir = FileUtil.makeAppDir("testing_" + System.currentTimeMillis());
        System.out.println("Made: " + dir.getAbsolutePath());
        File testFile = new File(dir, "testfile.test");
        System.out.println("Made: " + testFile.getAbsolutePath());
        FileUtil.removeRecursive(dir.toPath());
        Assert.assertTrue(!dir.exists());
        Assert.assertTrue(!testFile.exists());
        System.out.println("Now they are gone.");
    }

    @Test
    public void testOkayToRead(){
        File tmpFile = FileUtil.makeTempFile();
        Assert.assertTrue(FileUtil.fileOkayToRead(tmpFile) && tmpFile.delete());
    }

}