package com.chessbukalapak;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import com.chessbukalapak.ServerThread.OnReadListener;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity {

	ServerThread st = null;
	private ImageView gambar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        startMonitoring();
        super.onResume();
    }

    @Override
    public void onPause() {
        stopMonitoring();
        super.onPause();
    }

    private void startMonitoring() {
        st = new ServerThread("xinuc.org", 7387);
        st.setListener(new OnReadListener() {
			@Override
			public void onRead(ServerThread serverThread, final String response) {
				runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		            	// mengatur ulang gambar papan catur
		            	aturUlangGambar();
		            	
		            	String[] posisi = response.split("\\s+");
		            	for(int i=0;i<posisi.length;i++){
		            		char bidak = posisi[i].charAt(0);
		            		char horizontal = posisi[i].charAt(1);
		            		char vertikal = posisi[i].charAt(2);
		            		String cell = posisi[i].substring(1);
		            		int nilai = ((int)horizontal)-96;
		            		
		            		//Mendapatkan warna dasar papan
		            		int warnaDasar = getStatus(vertikal, nilai);
		            		
		            		//getImageViewID
		            		String namaFile = getNamaFile(warnaDasar, bidak);
		            		int imageViewID = getResources().getIdentifier(cell, "id", getPackageName());
		            		gambar = (ImageView) findViewById(imageViewID);
		            		
		            		//ubahGambar
		            		int file = getResources().getIdentifier(namaFile, "drawable",  getPackageName());
		            		gambar.setImageResource(file);
		            	}
		            }
		        });
			}
		});
        st.start();
    }
    
    private int getStatus(int vertikal, int horizontal){
    	int status = 0;
    	if(Character.getNumericValue(vertikal)%2==0){
			if(horizontal%2==1)
				status=1;
			else
				status=0;
		}
		else{
			if(horizontal%2==1)
				status=0;
			else
				status=1;
		}
    	return status;
    }
    
    private void aturUlangGambar(){
    	for(int i=1;i<=8;i++){
    		for(int j=0;j<8;j++){
    			String posisi = Character.toString((char)(j+97))+String.valueOf(i);
    			int resID = getResources().getIdentifier(posisi, "id", "com.chessbukalapak");
        		gambar = (ImageView) findViewById(resID);
    			if(i%2==0){
    				if(j%2==0)
                		gambar.setImageResource(R.drawable.white_square);
    				else
                		gambar.setImageResource(R.drawable.black_square);
    			}
    			else{
    				if(j%2==0)
                		gambar.setImageResource(R.drawable.black_square);
    				else
                		gambar.setImageResource(R.drawable.white_square);
    			}
    		}
    	}
    }
    
    private String getNamaFile(int status, char bidak){
		String hasil = null, latar = null;
		
		//background
		if(status==0)
			latar = "_black";
		else
			latar = "_white";
		
		//jenis bidak
		switch(bidak){
			case 'K':
				hasil = "kingwhite";
				break;
			case 'k':
				hasil = "kingblack";
				break;
			case 'Q':
				hasil = "queenwhite";
				break;
			case 'q':
				hasil = "queenblack";
				break;
			case 'N':
				hasil = "knightwhite";
				break;
			case 'n':
				hasil = "knightblack";
				break;
			case 'B':
				hasil = "bishopwhite";
				break;
			case 'b':
				hasil = "bishopblack";
				break;
			case 'R':
				hasil = "rookwhite";
				break;
			case 'r':
				hasil = "rookblack";
				break;
			default:
				break;
		}
		return hasil+latar;
    }
    
    private void stopMonitoring() {
        if (st != null) {
            try {
				st.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}
