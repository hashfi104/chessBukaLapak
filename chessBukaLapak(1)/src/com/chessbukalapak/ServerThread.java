package com.chessbukalapak;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

public class ServerThread extends Thread {
    public interface OnReadListener {
        public void onRead(ServerThread serverThread, String response);
    }

    Socket socket;
    String address;
    int port;
    OnReadListener listener = null;

    public ServerThread(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(InetAddress.getByName(address), port);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            PrintWriter writer = new PrintWriter(bw, true);
            writer.print("*99*1##");
            writer.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            while ((line = br.readLine()) != null) {
                if (line != null) {
                    Log.i("Dev", "Line ");

                    if (listener != null) {
                        listener.onRead(this, line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListener(OnReadListener listener) {
        this.listener = listener;
    }
}
