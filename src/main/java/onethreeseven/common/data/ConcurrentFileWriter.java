package onethreeseven.common.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Allows concurrently appending to a file.
 * It achieves this by using a blocking queue.
 * @author Luke Bermingham
 */
public class ConcurrentFileWriter {

    private final Byte mon = 0;

    private final FileWriter writer;
    private final ArrayBlockingQueue<String> toProcess;
    private final AtomicBoolean stillWriting = new AtomicBoolean(true);
    private final Logger logger = Logger.getLogger(ConcurrentFileWriter.class.getSimpleName());
    private final Thread writerThread;

    public ConcurrentFileWriter(File toWriteTo, int maxConcurrentWrites) throws IOException {
        this.writer = new FileWriter(toWriteTo, true);
        this.toProcess = new ArrayBlockingQueue<>(maxConcurrentWrites, false);

        writerThread = new Thread(() -> {
            synchronized (mon) {
                try {
                    //while we are still writing
                    while (!toProcess.isEmpty() || stillWriting.get()) {
                        //write anything that we have to
                        while (!toProcess.isEmpty()) {
                            String toWrite = toProcess.poll();
                            if (toWrite != null) {
                                writer.append(toWrite, 0, toWrite.length());
                            }
                        }
                        //wait to be woken up again
                        mon.wait();
                    }
                } catch (IOException e) {
                    logger.severe("IO exception writing to file: " + e.getMessage());
                } catch (InterruptedException e) {
                    logger.severe("Writer thread interrupted: " + e.getMessage());
                }
            }
        }, "Concurrent-File-Writer-" + System.currentTimeMillis());
        writerThread.start();
    }

    public void append(String toAppend) {
        try {
            toProcess.put(toAppend);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //wake up the writer thread so it can begin writing again
        synchronized (mon) {
            mon.notifyAll();
        }
    }

    /**
     * Closes resources associated with the writer, wait for the writer to finish what it is doing.
     */
    public void close() {
        stillWriting.set(false);
        synchronized (mon) {
            mon.notifyAll();
        }
        try {
            writerThread.join();
        } catch (InterruptedException e) {
            logger.severe("IO exception waiting for writer thread to finish: " + e.getMessage());
        }

        toProcess.clear();
        try {
            writer.close();
        } catch (IOException e) {
            logger.severe("IO exception closing writer: " + e.getMessage());
        }
    }

}
