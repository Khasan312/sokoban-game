package ServerNIO;

import java.io.IOException;

public interface RequestHandler {
    void handleRequest() throws IOException;
}
