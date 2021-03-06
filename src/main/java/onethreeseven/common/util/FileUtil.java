package onethreeseven.common.util;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Some useful utilities for dealing with files.
 * @author Luke Bermingham.
 */
public final class FileUtil {

    private FileUtil() {
    }

    public static char[][] getLineBreaks() {
        return new char[][]{
                new char[]{'\n', '\r'},
                new char[]{'\r', '\n'},
                new char[]{'\n'}
        };
    }


    public static String readUntil(BufferedReader br, char[][] terminators, StringBuilder sb, boolean keepTerminators) throws IOException {

        boolean firstRead = true;
        boolean keepReading = true;

        while (keepReading) {
            int c = br.read();
            if (c == -1) {
                //if it is the first loop through the current readUntil and we get EOF, then return
                if (firstRead) {
                    return null;
                }
                //otherwise we got EOF whilst we have some string in the sb, so return the string
                break;
            }
            if (firstRead) {
                firstRead = false;
            }
            //append
            sb.append((char) c);
            //check for the terminating characters
            for (char[] terminator : terminators) {
                //the buffer has to have at least that same number of characters
                if (sb.length() >= terminator.length) {
                    //check the last characters of the buffer for containing the terminators
                    int srcEnd = sb.length();
                    if (srcEnd > 0) {
                        int srcBegin = srcEnd - terminator.length;
                        char[] lastChars = new char[terminator.length];
                        sb.getChars(srcBegin, srcEnd, lastChars, 0);
                        //check if we have terminator
                        if (Arrays.equals(lastChars, terminator)) {
                            if (!keepTerminators) {
                                //change the output length to not include the terminators
                                sb.setLength(sb.length() - terminator.length);
                            }
                            //stop reading if we found a terminator
                            keepReading = false;
                            //don't look for anymore terminators
                            break;
                        }
                    }
                }
            }
        }
        String out = sb.toString();
        sb.setLength(0);
        return out;
    }

    /**
     * Reads a file line by line using some supplied terminators to define when a line ends.
     *
     * @param file  the file to read
     * @param terminators the terminators used to determine a line end
     * @param keepTerminators whether to keep the terminators in the output line string
     * @param readLineListener what to do with each line
     */
    public static void readLineByLine(File file, char[][] terminators, boolean keepTerminators, Consumer<String> readLineListener) {
        try {
            StringBuilder sb = new StringBuilder();
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = readUntil(br, terminators, sb, keepTerminators)) != null) {
                readLineListener.accept(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes a temp file in a temp direction
     *
     * @return The temp file that was made
     */
    public static File makeTempFile() {
        String path = System.getProperty("java.io.tmpdir");
        String filename = path + File.separator + System.currentTimeMillis() + ".tmp";
        File tmpFile = new File(filename);
        if (tmpFile.exists()) {
            if (!tmpFile.delete()) {
                tmpFile.deleteOnExit();
            }
        }
        try {
            if (!tmpFile.createNewFile()) {
                System.err.println("Could not create temporary file at: " + tmpFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpFile;
    }

    /**
     * Get a directory under our application directory
     *
     * @param dirName the directory to use under the application directory
     * @return the directory as a file
     */
    public static File makeAppDir(String dirName) {
        String path = System.getProperty("java.io.tmpdir");
        File file = Paths.get(path, dirName).toFile();
        if(!file.exists() && !file.mkdir()){
            System.err.println("Could not create directory: " + file.getAbsolutePath());
        }
        return file;
    }

    /**
     * Removes a directory and all its children...be careful
     * @param toDelete the directory to delete
     * @return whether or not the delete was 100% successful
     */
    public static boolean removeRecursive(Path toDelete) {
        try {
            Files.walkFileTree(toDelete, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // try to delete the file anyway, even if its attributes
                    // could not be read, since delete-only access is
                    // theoretically possible
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc == null) {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    } else {
                        // directory iteration failed; propagate exception
                        throw exc;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Checks for null, that it is a file (not a directory), that it exists, and that we can read it.
     * @param file the file to check
     * @return true if okay
     */
    public static boolean fileOkayToRead(File file){
        return file != null && file.exists() && file.canRead() && !file.isDirectory();
    }

    /**
     * Get the extension of a file.
     * @param file The whose extension we want.
     * @return The extension without the dot.
     */
    public static String getExtension(File file){
        String fileName = file.getName();
        char ch;
        int len;
        if((len = fileName.length())==0 ||
                (ch = fileName.charAt(len-1))=='/' || ch=='\\' || //in the case of a directory
                ch=='.' ) //in the case of . or ..
            return "";
        int dotInd = fileName.lastIndexOf('.'),
                sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if( dotInd<=sepInd )
            return "";
        else
            return fileName.substring(dotInd+1).toLowerCase();


    }

    /**
     * Get the file name with the dot and extension stripped off.
     * @param file The input file to get the name of.
     * @return The file name.
     */
    public static String getFilenameOnly(File file){
        String filename = file.getName();
        if(filename.contains(".")){
            return filename.substring(0, filename.lastIndexOf("."));
        }
        return null;
    }

}
