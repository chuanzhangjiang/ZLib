package me.zjc.zlib.network.socket;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

import me.zjc.zlib.common.io.StreamManager;
import rx.functions.Action1;


final class SocketClient extends AbstractSocketClient {

    private final Socket client;
    private final PrintWriter out;
    private final BufferedReader in;
    private final StreamManager mStreamManager;
    private boolean hasClose = false;

    SocketClient(Socket socket, Charset serverCharset) throws IOException {
        this.client = socket;
        mStreamManager = new StreamManager();
        this.out = wrapperOutputFromSocket(client, serverCharset, mStreamManager);
        this.in = wrapperInputFromSocket(client, serverCharset, mStreamManager);
    }

    private PrintWriter wrapperOutputFromSocket(Socket socket, Charset charset,
                                                       StreamManager streamManager) throws IOException {
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, charset);
        BufferedWriter bw = new BufferedWriter(osw);
        PrintWriter out = new PrintWriter(bw, true);

        streamManager.addFlushable(os);
        streamManager.addFlushable(osw);
        streamManager.addFlushable(bw);
        streamManager.addFlushable(out);

        streamManager.addCloseable(os);
        streamManager.addCloseable(osw);
        streamManager.addCloseable(bw);
        streamManager.addCloseable(out);
        return out;
    }

    private BufferedReader wrapperInputFromSocket(Socket socket, Charset charset,
                                                         StreamManager streamManager) throws IOException {
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, charset);
        BufferedReader in = new BufferedReader(isr);

        streamManager.addCloseable(is);
        streamManager.addCloseable(isr);
        streamManager.addCloseable(in);
        return in;
    }

    @Override
    public void send(String msg) {
        if (client != null && client.isConnected() && !client.isOutputShutdown()) {
            out.println(msg);
        }
    }

    @Override
    public void accept(@NonNull final Action1<String> action1) {
        while (!hasClosed()) {
            if (!client.isConnected() && client.isInputShutdown())
                continue;
            try {
                final String content;
                if ((content = in.readLine()) != null) {
                    action1.call(content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized boolean hasClosed() {
        return hasClose;
    }

    @Override
    public synchronized void close() throws IOException {
        hasClose = true;
        mStreamManager.release();
        client.close();
    }
}
