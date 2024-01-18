package zookeeper;

import org.apache.jute.BinaryOutputArchive;
import org.apache.jute.OutputArchive;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

/**
 * Provides apis to access transaction log and add entries to it.
 * <p>
 * Format of a Transactional Log:
 * <blockquote><pre>
 *
 * LogFile: FileHeader TxnList ZeroPad
 *
 * Txn: checksum TxnLen TxnHeader Record 0x42
 *  - checksum: 8bytes Adler32 calculated across payload (TxnLen, TxnHeader, Record, 0x42)
 *  - Txnlen: len 4bytes
 *  - TxnHeader
 *  - Record: See Jute definition file for details on the various record types
 *  - ZeroPad: 0 padded to EOF (filled during pre-allocation stage)
 *
 * </pre></blockquote>
 */
public class FileTxnLog {

    // By setting up such an output stream, an application can write bytes
    // to the underlying output stream without necessarily causing
    // a call to the underlying system for each byte written.
    volatile BufferedOutputStream logStream = null;
    volatile OutputArchive outputArchive;
    // Used in commit().
    // Keep track of all FOS instances that need to be committed/ flushed to disk.
    // Why many FOS?
    //  - a new FOS is created each time a new txn log file is created (when a new txn is appended
    //    and current log file is null or reached its size limit).
    // Why Queue? to handle queue in FIFO order when commit().
    private final Queue<FileOutputStream> streamsToFlush = new ArrayDeque<>();

    // used in commit(), max time allowed for fsync before warning and recording server stats count
    private static final long fsyncWarningThresholdMS = 1;

    /**
     * Constructor.
     * @param logDir the dir where the transaction log files are stored.
     */
    public FileTxnLog(File logDir) {
    }

    /**
     * Append a request to a transaction log.
     */
    public synchronized boolean append(Request request) throws IOException {


        outputArchive = BinaryOutputArchive.getArchive(logStream);

        byte[] buf = request.getSerializedData();
        Checksum crc = makeChecksumAlgorithm();
        crc.update(buf, 0, buf.length);
        outputArchive.writeLong(crc.getValue(), "txnEntryCRC");

        return true;
    }

    /**
     *  Commit the log, make sure changes are persisted.
     *  - flush in-memory buffered output stream if it is not null.
     *  - flush and sync the FileOutputStream's (1 per file opened) in streamsToFlush queue.
     *  - record the time taken to fsync. Recorded count and log warning if fsync time longer than threshold.
     *  - close all the FOS except the last (newest) one in queue to new txn writes.
     *  - check and roll the log file if it is over limit.
     *  - done
     *  <p></p>
     *  Why synchronized? for thread-safety.
     *  - ask JVM to ensure that only 1 thread can execute this method on 1 class instance
     *    at a time (it modifies shared resources: logStream, filePosition, etc.)
     *  FileOutputStream log.getChannel()
     *      FileChannel channel : A channel for reading, writing, mapping, and manipulating a file.
     *      channel.force(metadata=false); // force any update to be persisted
     */
    public synchronized void commit() throws IOException {

    }

    /**
     * Adler-32 is a checksum algorithm which was invented by Mark Adler.
     * Used here for a good balance between speed (faster than CRD32 and MD5) and error detection for this use case.
     * <p></p>
     * Widely used in network protocols and file formats, especially in situations
     * where a simple yet effective checksum is required.
     * - fast (much faster to compute than other checksums such as CRC32 or MD5, especially on long data sequences)
     * - simple to implement in code
     * - not as reliable as some other checksums for detecting errors, it is still quite effective
     * for many common types of errors that occur in real-world data.
     * <p></p>
     * Cons
     * - less reliable than other checksums.
     *      - weak for short messages.
     *      - limited ability to detect bit flips.
     * <p></p>
     * Compare to others
     * - SHA-256: slower to compute, is cryptographic hash, used where security is important because it
     *   is secure against deliberate attack. Has very high level of error detection.
     * - MD5:
     *      - provides a much higher level of error detection than either Adler-32 or CRC32
     *      - slower to compute
     *      - no longer considered secured against deliberate attack.
     * - CRC32:
     *      - more reliable than Adler-32 at detecting errors, but it is also slower to compute.
     *      - often used in network protocols and file formats where error detection is critical.
     * <p></p>
     * Security sort descending: SHA-256, SHA-1, MD5, CRC32, Adler32
     * Error detection descending: SHA-256, SHA-1, MD5, CRC32, Adler32
     * Compute fast descending: Adler32, CDC32, MD5, SHA1, SHA256
     */
    private Checksum makeChecksumAlgorithm() {
        return new Adler32();
    }
}
