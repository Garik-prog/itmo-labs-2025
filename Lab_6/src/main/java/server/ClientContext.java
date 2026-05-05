package server;

import java.nio.ByteBuffer;

public class ClientContext {
    public enum State { READ_LENGTH, READ_DATA }
    public State state = State.READ_LENGTH;
    public ByteBuffer buffer = ByteBuffer.allocate(4096);
    public int dataLength;
    public ByteBuffer pendingResponse;
}