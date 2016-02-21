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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
    public static String myId = "noID";
    public static String myName = "guest";


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
	public static boolean onDialog = false;

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
        setUserID();
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
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        selectedPlayerId = -2;

        listPlayerIdArray = new ArrayList<>();

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



//        preference = getSharedPreferences("user_setting", MODE_PRIVATE);
//        editor = preference.edit();
//
//        if(preference.getBoolean("Launched",false)){
//            //初回起動時処理
////            int id = (int)Math.random()*999999;
////            myId = String.format("%1$06d",id);
//            myName = "はせべ";
//            /**preferenceの書き換え**/
//            editor.putBoolean("Launched",true);
//            editor.commit();
//
//        }else{
//            /**2回目以降の処理**/
//        }
    }
    /**onCreateここまで**/

    public void setUserID(){

        preference = getPreferences(MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
        editor = preference.edit();

        if(!(preference.getBoolean("launched",false))){
//            初回起動時処理
            int id = (int)(Math.random()*999999);
            myId = String.format("%1$06d", id);
            editor.putString("userID",myId);
            /**preferenceの書き換え**/
            editor.putBoolean("launched",true);
            editor.commit();

        }else{
            /**2回目以降の処理**/
        myId = preference.getString("userID","noID");
        myName = preference.getString("userName","guest");
        }
    }

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
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {    //戻るボタンの反応なくす
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int actionId = event.getAction();


        String dialogText = "dialogText";

        if(event.getAction() == MotionEvent.ACTION_DOWN && onDialog == true ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            if(dialogPattern.equals("editUserName")){
                final EditText editUserName = new EditText(this);
                builder.setTitle("プレイヤー名変更")
                        //setViewにてビューを設定
                        .setView(editUserName)
                        .setPositiveButton("変更", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                       Toast.makeText(SettingScene.this, addPlayerView.getText().toString(), Toast.LENGTH_LONG).show();

                                String text = editUserName.getText().toString();
                                if(!(text.equals(""))){
                                    editor.putString("userName",text);
                                    /**preferenceの書き換え**/
                                    editor.commit();

                                }
                            }
                        })
                        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                dialogPattern = "";

            }else if(dialogPattern.equals("start")){

                switch (dialogPattern){
                    case "start":
                        dialogText = "ゲームを開始します";
                        break;
                    default:
                        break;
                }
//                builder.setMessage(dialogText)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // ボタンをクリックしたときの動作
//                                // dialog 表示しない
//                                onDialog = false;
//                                SettingScene.isSettingScene = false;
//                                SettingScene.isGameScene = true;
//                                // Activity遷移
//                                Intent intent = new Intent(SettingScene.this,GameScene.class);
//                                startActivity(intent);
////                                                   customView.invalidate();
//
//                            }
//                        });
//                builder.setMessage(dialogText)
//                        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // ボタンをクリックしたときの動作
//                            }
//                        });
//                builder.show();
            }
//                   else if(dialogPattern.equals("roleVolume")){
//                       ArrayList<Integer> roleVolume = new ArrayList<>();
//                       for(int i = 0;i<playerNameArray.size();i++){
//                           roleVolume.add(i);
//                       }
//                       builder.setTitle("人数を選択してください")
//                               //setViewにてビューを設定
//                               .setSingleChoiceItems(roleVolume,0,mClickListerner)
//                               .setPositiveButton("追加", new DialogInterface.OnClickListener() {
//                                   @Override
//                                   public void onClick(DialogInterface dialog, int which) {
////                                       Toast.makeText(SettingScene.this, addPlayerView.getText().toString(), Toast.LENGTH_LONG).show();
//
//                                       String text = addPlayerView.getText().toString();
//                                       if (!(text.equals(""))) {
//                                           playerNameArray.add(text);
//                                       }
//
//                                       listInfoDicArray.clear();
//
//                                       for (int i = 0; i < playerNameArray.size(); i++) {
//
//                                           Map<String, String> conMap = new HashMap<>();
//                                           conMap.put("name", playerNameArray.get(i));
//                                           conMap.put("listSecondInfo", "");
//                                           listInfoDicArray.add(conMap);
//                                       }
//
//                                       playerListView.invalidateViews();
////                                       // 中身クリア
////                                       GameScene.editText.getEditableText().clear();
//                                   }
//                               })
//                               .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
//                                   @Override
//                                   public void onClick(DialogInterface dialog, int which) {
//
//                                   }
//                               })
//                               .show();
//
//                       dialogPattern = "";
//
//                   }
        }
        return true;
    }

}
