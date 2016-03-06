package com.lb.jbt.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import com.lb.tools.ReadProperties;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ChatService extends Service {
	private Socket socket;
	private MyBinder myBinder = new MyBinder();
	private String fromInterfaceStr = "";

	@Override
	public IBinder onBind(Intent arg0) {
		return myBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		System.out.println("========================>onStart");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (socket == null) {
						socket = new Socket(ReadProperties.read("url", "common_url"),
								Integer.valueOf(ReadProperties.read("url", "socket_port")));
					}
					BufferedReader in = null;
					System.out.println("service start========================>" + fromInterfaceStr);
					while (!socket.isClosed()) {
						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						Intent intent = new Intent("com.lb.jbt.broadcast.MSG_Broadcast");
						intent.putExtra("msg", in.readLine());
						sendBroadcast(intent);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("========================>onDestroy");
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("========================>onUnbind");
		return super.onUnbind(intent);
	}

	public class MyBinder extends Binder {
		public void handleMessage(String fromInterface) {
			fromInterfaceStr = fromInterface;
		}

		public Socket getSocket() {
			return socket;
		}
	}
}
