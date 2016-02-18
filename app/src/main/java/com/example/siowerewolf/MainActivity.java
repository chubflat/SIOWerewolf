package com.example.siowerewolf;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
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

import static android.content.SharedPreferences.*;

public class MainActivity extends Activity {

    /**preferences**/
    private SharedPreferences preference;
    private Editor editor;
    public static String myId = "test";
    public static String myName = "hasebe";


	// socketIO
//	private EditText editText;
	private ArrayAdapter<String> adapter;
	public static SocketIO socket;
    private Handler handler = new Handler();
    public static String receivedmsg = "";
    public static String sendmsg = "";

    // List
	public static ListView listView;
	public static SimpleAdapter simpleAdapter;
	//    public static Adapter adapter;
	public static CustomView customView = null;
	public static String dialogPattern = "default";
    public static EditText editText;

	// 各種List宣言
	public static List<Map<String,Object>> playerArray;//参加者Array
	public static List<Map<String,String>> listInfoDicArray;//リストに表示する情報のArray
	public static ArrayList<Integer> listPlayerIdArray;//listに入っているplayerId Array
	public static ArrayList<Integer> victimArray;//夜間犠牲者Array

	public static int selectedPlayerId;//リストで選択されたプレイヤーのID

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


//
        /**ListViewの追加**/
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
//		ListView listView = (ListView)findViewById(R.id.listView1);
//		listView.setAdapter(adapter);
        listView = new ListView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(customView.width*90/100,customView.height*4/10);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        selectedPlayerId = -2;

        listPlayerIdArray = new ArrayList<>();
        Log.d("array", "array=");

//        listInfoDicArray = new ArrayList<Map<String,String>>();
//        simpleAdapter = new SimpleAdapter(this,listInfoDicArray,android.R.layout.simple_list_item_2,new String[]{"name","listSecondInfo"},new int[]{android.R.id.text1,android.R.id.text2});

        listView.setAdapter(adapter);
        listView.setLayoutParams(lp);
        listView.setBackgroundColor(Color.WHITE);

        mFrameLayout.addView(listView);
        drawListView(false);

        /** ListViewの追加終了 **/

        editText = new EditText(this);
        FrameLayout.LayoutParams editLP = new FrameLayout.LayoutParams(customView.width,customView.height/10);
        editLP.gravity = Gravity.BOTTOM;
        editLP.bottomMargin = customView.height*10/100;

        editText.setLayoutParams(editLP);
        editText.setBackgroundColor(Color.WHITE);

        mFrameLayout.addView(editText);
        draweditText(false);
		try {
			connect();
		} catch(Exception e) {
			e.printStackTrace();
		}

        preference = getSharedPreferences("user_setting", MODE_PRIVATE);
        editor = preference.edit();

        if(preference.getBoolean("Launched",false)){
            //初回起動時処理
            int id = (int)Math.random()*999999;
            myId = String.format("%1$06d",id);
            myName = "はせべ";
            /**preferenceの書き換え**/
            editor.putBoolean("Launched",true);
            editor.commit();

        }else{
            /**2回目以降の処理**/
        }
    }
    /**onCreateここまで**/

    private void connect() throws MalformedURLException{
		socket = new SocketIO("http://blewerewolfserver.herokuapp.com/");
		socket.connect(iocallback);
    }

	private IOCallback iocallback = new IOCallback() {

		@Override
		public void onConnect() {
		    System.out.println("onConnect");
		}

		@Override
		public void onDisconnect() {
		    System.out.println("onDisconnect");
		}

		@Override
		public void onMessage(JSONObject json, IOAcknowledge ack) {
			System.out.println("onMessage");
		}

		@Override
		public void onMessage(String data, IOAcknowledge ack) {
		    System.out.println("onMessage");
		}

		@Override
		public void on(String event, IOAcknowledge ack, Object... args) {
			final JSONObject message = (JSONObject)args[0];

			new Thread(new Runnable() {
				public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							if(message.getString("message") != null) {
                                /**文章の解読**/
                                receivedmsg = message.getString("message");
                                String[] roomInfo = receivedmsg.split(":",0);
                                Map<String,String> roomInfoDic = new HashMap<String,String>();
                                roomInfoDic.put("header",roomInfo[0]);
                                roomInfoDic.put("gameID",roomInfo[1]);
                                roomInfoDic.put("periID",roomInfo[2]);
                                roomInfoDic.put("periName",roomInfo[3]);
								// メッセージが空でなければ追加
								adapter.insert(roomInfo[3] + "(" + roomInfo[1] + ")", 0);
                                /**受信メッセージを格納**/
                            // TODO 配列に辞書追加

                                customView.invalidate();
							}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}).start();
		}

		@Override
		public void onError(SocketIOException socketIOException) {
		    System.out.println("onError");
		    socketIOException.printStackTrace();
		}
    };

    public static void sendEvent(View view){
		// 文字が入力されていなければ何もしない
//		if (editText.getText().toString().length() == 0) {
//		    return;
//		}

        String sendmsd = "";
        sendmsg = "mes:" + 0 + ":" + "periID" + ":"+
                "centID" + ":" + "participateRequest" + ":"+
                "012345/centID/はせべ/" +
                "periID/0";
		try {
		// イベント送信
			JSONObject json = new JSONObject();
//			json.put("message", editText.getText().toString());
			json.put("message", sendmsg);
			socket.emit("message:send", json);

		} catch (JSONException e) {
			e.printStackTrace();
		}

    	// テキストフィールドをリセット
//    	editText.setText("");
    }

    public static void drawListView(boolean visible){
        if(visible == true) {
            listView.setVisibility(View.VISIBLE);
        }else if(visible == false){
            listView.setVisibility(View.INVISIBLE);
        }
    }

    public static void draweditText(boolean visible){
        if(visible == true) {
            editText.setVisibility(View.VISIBLE);
        }else if(visible == false){
            editText.setVisibility(View.INVISIBLE);
        }
    }
}
