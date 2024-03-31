package singlesocketchannel;

import java.io.*;

import static org.apache.commons.lang3.SerializationUtils.deserialize;

public class SingleSocketChannel implements Closeable {

  final InetAddressAndPort address;
  final int heartbeatIntervalMs;
  private final OutputStream socketOutputStream;
  private final InputStream inputStream;

  public SingleSocketChannel(InetAddressAndPort address, int heartbeatInternalMs) throws IOException {
    this.address = address;
    this.heartbeatIntervalMs = heartbeatInternalMs;
    clientSocket = new Socket();

  }

  public synchronized RequestOrResponse blockingSend(RequestOrResponse request) throws IOException {
    writeRequest(request);
    byte[] responseBytes = readResponse();
    return deserialize(responseBytes);
  }

  private void writeRequest(RequestOrResponse request) throws IOException {
    var dataStream = new DataOutputStream(socketOutputStream);
    byte[] messageBytes = serialize(request);
    dataStream.writeInt(messageBytes.length);
    dataStream.write(messageBytes);
  }

  @Override
  public void close() throws IOException {

  }
}
