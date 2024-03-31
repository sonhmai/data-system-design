package singlesocketchannel;

public class SocketHandlerThread extends Thread {
  private boolean isRunning;

  @Override
  public void run() {
    isRunning = true;
    try {
      while(isRunning) {
        handleRequest();
      }
    } catch (Exception e) {

    }
  }

  private void handleRequest() {
    RequestOrResponse request = clientConnection.readRequest();
    RequestId requestId = RequestId.valueOf(request.getRequestId());
    server.accept(new Message<>(request, requestId, clientConnection));
  }

  public void closeConnection() {
    clientConnection.close();
  }
}
