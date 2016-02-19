package com.lb.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.lb.jbt.R;
import com.lb.tools.ReadProperties;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ChatFragment extends Fragment implements View.OnClickListener {
	private Button sendBtn;
	private Socket socket;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					socket = new Socket(ReadProperties.read("url", "common_url"),
							Integer.valueOf(ReadProperties.read("url", "socket_port")));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.chat_fragment, container, false);
		sendBtn = (Button) view.findViewById(R.id.msg_send);
		sendBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msg_send:
			try {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							PrintWriter out = new PrintWriter(socket.getOutputStream());
							out.println("来自客户端：你好");
							out.flush();
							System.out.println("------------->" + in.readLine());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}
}
