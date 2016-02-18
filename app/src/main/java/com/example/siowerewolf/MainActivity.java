package com.example.siowerewolf;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {


	// socketIO
	private EditText editText;
	private ArrayAdapter<String> adapter;
	private SocketIO socket;
    private Handler handler = new Handler();

    // List
	public static ListView listView;
	public static SimpleAdapter simpleAdapter;
	//    public static Adapter adapter;
	public static CustomView customView = null;
	public static String dialogPattern = "default";

	// 各種List宣言
//	public static List<Map<String,Object>> playerArray;//参加者Array
//	public static List<Map<String,String>> listInfoDicArray;//リストに表示する情報のArray
//	public static ArrayList<Integer> listPlayerIdArray;//listに入っているplayerId Array
//	public static ArrayList<Integer> victimArray;//夜間犠牲者Array

//	public static int selectedPlayerId;//リストで選択されたプレイヤーのID

	// TODO Adapter宣言

	// dialog関連
//	public static boolean onDialog = false;

	// フラグ管理用 変数宣言
	public static Boolean isSettingScene;
	public static Boolean isGameScene;
	public static String settingPhase;
	public static String gamePhase;
	public static boolean isFirstNight;

	// list_item
	public static LinearLayout content;
	public static TextView txtInfo;
	public static LinearLayout contentWithBackground;
	public static TextView txtMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isSettingScene = true;
        settingPhase = "setting_menu";

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);



		// FrameLayout作成
		FrameLayout mFrameLayout = new FrameLayout(this);
		setContentView(mFrameLayout);

		// TODO initBackground

		//TODO FrameLayoutに追加
		customView = new CustomView(this);
		mFrameLayout.addView(customView);
//		setContentView(R.layout.activity_main);

        Log.d("custom","custom=");


//
//		// ListViewの設定
//		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
//		ListView listView = (ListView)findViewById(R.id.listView1);
//		listView.setAdapter(adapter);
//
//		editText = (EditText)findViewById(R.id.editText1);
//
//		try {
//			connect();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//    }
//
//    private void connect() throws MalformedURLException{
//		socket = new SocketIO("http://blewerewolfserver.herokuapp.com/");
//		socket.connect(iocallback);
//    }
//
//	private IOCallback iocallback = new IOCallback() {
//
//		@Override
//		public void onConnect() {
//		    System.out.println("onConnect");
//		}
//
//		@Override
//		public void onDisconnect() {
//		    System.out.println("onDisconnect");
//		}
//
//		@Override
//		public void onMessage(JSONObject json, IOAcknowledge ack) {
//			System.out.println("onMessage");
//		}
//
//		@Override
//		public void onMessage(String data, IOAcknowledge ack) {
//		    System.out.println("onMessage");
//		}
//
//		@Override
//		public void on(String event, IOAcknowledge ack, Object... args) {
//			final JSONObject message = (JSONObject)args[0];
//
//			new Thread(new Runnable() {
//				public void run() {
//				handler.post(new Runnable() {
//					public void run() {
//						try {
//							if(message.getString("message") != null) {
//								// メッセージが空でなければ追加
//								adapter.insert(message.getString("message"), 0);
//							}
//
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//					});
//				}
//			}).start();
//		}
//
//		@Override
//		public void onError(SocketIOException socketIOException) {
//		    System.out.println("onError");
//		    socketIOException.printStackTrace();
//		}
//    };
//
//    public void sendEvent(View view){
//		// 文字が入力されていなければ何もしない
//		if (editText.getText().toString().length() == 0) {
//		    return;
//		}
//
//		try {
//		// イベント送信
//			JSONObject json = new JSONObject();
//			json.put("message", editText.getText().toString());
//			socket.emit("message:send", json);
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//    	// テキストフィールドをリセット
//    	editText.setText("");
    }
}
