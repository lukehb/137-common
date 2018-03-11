package onethreeseven.common.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * An abstract writer which can be extended to write whatever object.
 * It does not implement any of the actual writing functionality though.
 * @author Luke Bermingham.
 */
public abstract class AbstractWriter<T> {

    protected String delimiter = ", ";

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean write(File file, T t) {
        BufferedWriter bw = null;
        try {
            if(file.exists() && file.canWrite()){
                //logger.info("File already exists, going to append to it: " + file.getAbsolutePath());
                bw = new BufferedWriter(new FileWriter(file, true));
            }
            //file does not exist
            else if(file.createNewFile() && file.canWrite()){
                bw = new BufferedWriter(new FileWriter(file));
            }
            if (bw == null) {
                System.err.println("Could not write to file: " + file.getAbsolutePath());
            }else{
                return this.write(bw, t);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Write an entity to a file.
     * @param bw the writer to use.
     * @param t  the entity to write.
     * @return True if writing the entity was successful; otherwise, false.
     * @throws IOException Exception is thrown if writing to the file is not possible.
     */
    protected abstract boolean write(BufferedWriter bw, T t) throws IOException;


}
