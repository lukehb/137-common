package onethreeseven.common.data;

import onethreeseven.common.util.FileUtil;
import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * @author Luke Bermingham
 */

/**
 * A line-based text file parser.
 * @param <T> The output type.
 */
public abstract class AbstractLineBasedParser<T> {

    protected char[][] lineTerminators = null;
    protected int nLinesToSkip = 0;
    protected Consumer<Double> progressListener;

    public AbstractLineBasedParser(){
        progressListener = getDefaultProgressListener();
    }

    /**
     * Parse some input resolve a buffered stream into our output format.
     * @param br to read resolve
     * @param streamLength the length of the stream, or -1 if the size is unknown.
     * @return A collection of trajectories, or null if the stream could not be read.
     */
    public T parse(BufferedReader br, double streamLength){
        try{

            String line;
            final Charset charset = Charset.forName("UTF-8");
            double processed = 0;

            int linesSkipped = 0;
            StringBuilder sb = new StringBuilder();

            //READING
            while ((line = (lineTerminators == null) ?
                    br.readLine() :
                    FileUtil.readUntil(br, lineTerminators, sb, false)) != null) {

                processed += line.getBytes(charset).length;
                //output progress to the listener
                if (progressListener != null) {
                    progressListener.accept(processed / streamLength);
                }
                if (nLinesToSkip != linesSkipped) {
                    linesSkipped++;
                } else {
                    //PARSING
                    parseLine(line);
                }
            }
            //FINALISING
            return done();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getSimpleName())
                    .severe("File IO exception, " + e.getMessage());
        }
        finally {
            //done reading close readers
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ignore) {
            }
        }
        return null;
    }

    public T parse(File dataset){

        if(!isOkayToRead(dataset)){
            throw new IllegalArgumentException("Dataset either doesn't exist or doesn't have read permissions.");
        }

        FileReader fr;
        BufferedReader br;

        //setup readers
        try {
            fr = new FileReader(dataset);
            br = new BufferedReader(fr);
            final long fileLength = dataset.length();
            T output = parse(br, fileLength);

            try {
                fr.close();
            } catch (IOException ignore) {
            }

            return output;
        } catch (FileNotFoundException e) {
            Logger.getLogger(this.getClass().getSimpleName())
                    .severe("File not found, " + e.getMessage());
        }
        return null;
    }

    /**
     * If custom line terminators are set these are used in parsing instead
     * of the default \n \r or \n\r \r\n
     * @param lineTerminators the custom line terminators to check for.
     * @return A reference to this object. Useful for method chaining.
     */
    public AbstractLineBasedParser<T> setLineTerminators(char[][] lineTerminators) {
        this.lineTerminators = lineTerminators;
        return this;
    }

    /**
     * Many data-sets have headers which are not useful for parsing.
     * We can skip those headers by specifying a number of lines to skip.
     * @param nLinesToSkip How many lines to skip.
     * @return A reference to this object. Useful for method chaining.
     */
    public AbstractLineBasedParser<T> setnLinesToSkip(int nLinesToSkip) {
        this.nLinesToSkip = nLinesToSkip;
        return this;
    }

    /**
     * Attach a progress listener to parsing a trajectory data-set.
     * Progress is reported as an int 1-100%
     * @param progressListener the progress listener.
     * @return A reference to this object. Useful for method chaining.
     */
    public AbstractLineBasedParser<T> setProgressListener(Consumer<Double> progressListener) {
        this.progressListener = progressListener;
        return this;
    }

    private boolean isOkayToRead(File dataset){
        return dataset.exists() && dataset.canRead();
    }

    protected Consumer<Double> getDefaultProgressListener(){
        final AtomicInteger progress = new AtomicInteger(0);
        final String fmt = "Parsed file %d%%";
        final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

        return progressValue -> {
            int clippedProgress = (int) (progressValue * 100);
            if (clippedProgress > progress.get()) {
                progress.set(clippedProgress);
                logger.info(String.format(fmt, clippedProgress));
            }
        };
    }

    /**
     * Parse a the line and add to the output.
     * @param line The line to parse
     */
    protected abstract void parseLine(String line);


    /**
     * Called to indicate the file has been parsed fully
     * and the output is ready for returning.
     * @return The output resolve parsing.
     */
    protected abstract T done();



}
