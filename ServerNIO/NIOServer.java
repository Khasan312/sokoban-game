    package ServerNIO;

    import java.io.IOException;
    import java.net.InetSocketAddress;
    import java.nio.ByteBuffer;
    import java.nio.channels.*;
    import java.util.HashMap;
    import java.util.Iterator;
    import java.util.Map;
    import java.util.Set;

    public class NIOServer {
        private final int port;
        private final Selector selector;
        private final Map<SocketChannel, ByteBuffer> dataMap;

        public NIOServer(int port) {
            this.port = port;
            ServerSocketChannel serverSocketChannel = null;
            try {
                serverSocketChannel = ServerSocketChannel.open();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                serverSocketChannel.bind(new InetSocketAddress(port));
                serverSocketChannel.configureBlocking(false);
                this.selector = Selector.open();
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.dataMap = new HashMap<>();
        }

        public void start() {
            System.out.println("Server is running on port " + port);
            while (true) {
                try {
                    selector.select();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()) {
                        handleAcceptable(key);
                    } else if (key.isReadable()) {
                        handleReadable(key);
                    }

                    keyIterator.remove();
                }
            }
        }

        private void handleAcceptable(SelectionKey key) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = null;
            try {
                socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                socketChannel.register(selector, SelectionKey.OP_READ);
            } catch (ClosedChannelException e) {
                throw new RuntimeException(e);
            }

            dataMap.put(socketChannel, ByteBuffer.allocate(1024));
            try {
                System.out.println("Accepted connection from: " + socketChannel.getRemoteAddress());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        private void handleReadable(SelectionKey key) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = dataMap.get(socketChannel);
            int bytesRead = 0;
            try {
                bytesRead = socketChannel.read(buffer);
            } catch (IOException e) {
                System.out.println(e);
            }

            StringBuilder requestData = new StringBuilder();
            if (bytesRead > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    char symbol = (char) buffer.get();
                    requestData.append(symbol);
                }
                System.out.println(requestData.toString());


                System.out.println(dataMap);
                RequestHandler handler = RequestHandlerFactory.getHandler(socketChannel, requestData.toString());
                if (handler != null) {
                    try {
                        handler.handleRequest();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
                buffer.compact();
            }
        }
    }

