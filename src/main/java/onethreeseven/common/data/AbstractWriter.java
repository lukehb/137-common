package onethreeseven.common.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * An abstract writer which can be extended to write whatever object.
 * It does not implement any of the actual writing functionality though.
 * @author Luke Bermingham.
 */
public abstract class AbstractWriter<T> {

    protected final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    protected String delimiter = ", ";

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void write(File file, T t) {
        BufferedWriter bw = null;
        try {
            if (file.createNewFile() && file.canWrite()) {
                bw = new BufferedWriter(new FileWriter(file));
                write(bw, t);
            }
            else{
                logger.warning("A file already exists there, delete this first: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            logger.severe("Could not create writer: " + e.getMessage());
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                logger.severe("Could not close writer: " + e.getMessage());
            }
        }
    }

    /**
     * Write the entity to the file
     *
     * @param bw the writer to use
     * @param t  the entity to write
     * @throws IOException Exception is thrown if writing to the file is not possible
     */
    protected abstract void write(BufferedWriter bw, T t) throws IOException;


}
