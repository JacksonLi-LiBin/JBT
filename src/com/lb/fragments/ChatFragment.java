package com.lb.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.lb.constants.DateConvert;
import com.lb.entities.ChatMessage;
import com.lb.jbt.R;
import com.lb.tools.ReadProperties;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ChatFragment extends Fragment implements View.OnClickListener {
	private SharedPreferences spf = null;
	private SharedPreferences.Editor editor = null;
	private String userId = "";
	private String fullName = "";
	private ListView msg_list_view;
	private EditText msg_type;
	private Button sendBtn;
	private Socket socket;
	private MessageListAdapter adapter = null;
	private List<ChatMessage> msgList = null;
	private Handler updateChatListHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			ChatMessage message = (ChatMessage) msg.obj;
			msgList.add(message);
			adapter.notifyDataSetChanged();
			return false;
		}
	});

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		spf = ChatFragment.this.getActivity().getSharedPreferences("jbt", ChatFragment.this.getActivity().MODE_PRIVATE);
		userId = spf.getString("userId", "");
		fullName = spf.getString("fullName", "");
		msgList = new ArrayList<ChatMessage>();
		adapter = new MessageListAdapter(msgList);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (socket == null) {
						socket = new Socket(ReadProperties.read("url", "common_url"),
								Integer.valueOf(ReadProperties.read("url", "socket_port")));
						while (socket.isConnected() && !socket.isClosed()) {
							BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							System.out.println("-->" + in.readLine());
							ChatMessage msg = (ChatMessage) JSON.parse(in.readLine());
							if (!msg.getUserId().equals(userId)) {
								Message message = new Message();
								message.obj = msg;
								updateChatListHandler.sendMessage(message);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.chat_fragment, container, false);
		msg_list_view = (ListView) view.findViewById(R.id.msg_list_view);
		msg_list_view.setAdapter(adapter);
		msg_type = (EditText) view.findViewById(R.id.msg_type);
		sendBtn = (Button) view.findViewById(R.id.msg_send);
		sendBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msg_send:
			if (msg_type.getText().toString().length() > 0) {
				try {
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								ChatMessage message = new ChatMessage();
								if (socket != null && !socket.isClosed()) {
									PrintWriter out = new PrintWriter(socket.getOutputStream());
									message.setUserId(userId);
									message.setUserName(fullName);
									message.setSendTime(DateConvert.sdf.format(new Date()));
									message.setMsg(msg_type.getText().toString());
									out.println(JSON.toJSONString(message));
									out.flush();
								}
								Message msg = new Message();
								msg.obj = message;
								updateChatListHandler.sendMessage(msg);
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
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
		} catch (Exception e) {
		}
	}

	class MessageListAdapter extends BaseAdapter {
		private List<ChatMessage> msgs = null;

		public MessageListAdapter(List<ChatMessage> msgs) {
			super();
			this.msgs = msgs;
		}

		@Override
		public int getCount() {
			return msgs.size();
		}

		@Override
		public Object getItem(int arg0) {
			return msgs.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = null;
			ChatMessage message = msgs.get(arg0);
			if (message.getUserId().equals(userId)) {
				view = LayoutInflater.from(ChatFragment.this.getActivity()).inflate(R.layout.chat_my_msg, null);
			} else {
				view = LayoutInflater.from(ChatFragment.this.getActivity()).inflate(R.layout.chat_others_msg, null);
			}
			TextView msg = (TextView) view.findViewById(R.id.msg);
			msg.setText(message.getMsg());
			return view;
		}

	}
}
