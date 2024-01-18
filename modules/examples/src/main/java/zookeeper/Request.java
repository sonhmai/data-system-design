package zookeeper;

/**
 * This is the structure that represents a request moving through a chain of
 * RequestProcessors. There are various pieces of information that is tacked
 * onto the request as it is processed.
 */
public class Request {

    private transient byte[] serializedData;

    public Request() {

    }

    public byte[] getSerializedData() {
        return this.serializedData;
    }
}
