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
import android.widget.AdapterView;
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
    public static List<Map<String,String>> roomInfoDicArray;
    public static Map<String,String> fixedGameInfo;

	public static int selectedPlayerId;//リストで選択されたプレイヤーのID
    public static int selectedRoomId;

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

    public static int signalID;


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
        roomInfoDicArray = new ArrayList<>();
        fixedGameInfo = new HashMap<>();

        signalID = (int)(Math.random()*999999);

//        listInfoDicArray = new ArrayList<Map<String,String>>();
//        simpleAdapter = new SimpleAdapter(this,listInfoDicArray,android.R.layout.simple_list_item_2,new String[]{"name","listSecondInfo"},new int[]{android.R.id.text1,android.R.id.text2});

        listView.setAdapter(adapter);
        listView.setLayoutParams(lp);
        listView.setBackgroundColor(Color.WHITE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (settingPhase.equals("player_setting")) {
//                    selectedPlayerId = -2;
//                } else {
//                    selectedPlayerId = listPlayerIdArray.get(position);
//                }
                if(settingPhase.equals("room_select")){
                    selectedRoomId = position;
                    Log.d("posi","posi=");
                    fixedGameInfo.put("gameID", roomInfoDicArray.get(selectedRoomId).get("gameID"));
                    fixedGameInfo.put("periID", roomInfoDicArray.get(selectedRoomId).get("periID"));
                    fixedGameInfo.put("periName",roomInfoDicArray.get(selectedRoomId).get("periName"));
                    Log.d("posi1", "posi1=");
                    sendEvent();
                    Log.d("posi2", "posi2=");
                }
//                if (phase.equals("player_setting")) {
//
//                } else {
//                    if (nowPlayer < playerArray.size() && playerArray.get(nowPlayer).get("roleId") == Utility.Role.Werewolf) {
//                        if (isFirstNight) {//人狼：初日の夜はタッチできない
//                            if (selectedPlayerId == -1) {
//                                goNextPhase();
//                                customView.invalidate();
//                            }
//
//                        } else {// 人狼：2日目以降タッチされたplayerIdを渡して再描画
//                            wolfkill(selectedPlayerId, 0);
//                            goNextPhase();
//                            customView.invalidate();
//                        }
//                    } else if (nowPlayer < playerArray.size() && playerArray.get(nowPlayer).get("roleId") == Utility.Role.Bodyguard) {
//                        bodyguardId = selectedPlayerId;
//                        goNextPhase();
//                        customView.invalidate();
//                    } else {
//                        goNextPhase();
//                        customView.invalidate();
//                    }
//                }
            }

        });

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
        drawEditText(false);
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

    public static void setListAdapter(String type){
        listInfoDicArray.clear();
        listPlayerIdArray.clear();

        switch (type){
            case "select _room":

                break;
            default:
                break;
        }
//        if(type == -1) { //処刑用
//            for (int i = 0; i < playerArray.size(); i++) {
//                if ((boolean) playerArray.get(i).get("isLive") == true) {
//                    listPlayerIdArray.add(i);
//
//                    Map<String,String> conMap = new HashMap<>();
//                    conMap.put("name",(String)playerArray.get(i).get("name"));
//                    conMap.put("listSecondInfo","");
//                    listInfoDicArray.add(conMap);
//                }
//            }
//        }else if(type == 1){//人狼用
//            if(isFirstNight){//仲間確認用
//                for (int i = 0; i < playerArray.size(); i++) {
//                    if (playerArray.get(i).get("roleId") == Utility.Role.Werewolf && nowPlayer != i) {
//                        listPlayerIdArray.add(i);
//
//                        Map<String,String> conMap = new HashMap<>();
//                        conMap.put("name",(String)playerArray.get(i).get("name"));
//                        conMap.put("listSecondInfo","");
//                        listInfoDicArray.add(conMap);
//                    }
//                }
//                Map<String,String> confirm = new HashMap<>();
//                confirm.put("name","確認したらここをタップ");
//                confirm.put("listSecondInfo", "");
//                listInfoDicArray.add(confirm);
//                listPlayerIdArray.add(-1);
//
//            }else{ // 噛み用
//                for (int i = 0; i < playerArray.size(); i++) {
//                    if ((boolean) playerArray.get(i).get("isLive") == true && playerArray.get(i).get("roleId") != Utility.Role.Werewolf) {
//                        listPlayerIdArray.add(i);
//
//                        Map<String,String> conMap = new HashMap<>();
//                        conMap.put("name", (String) playerArray.get(i).get("name"));
//
//                        String listSecondInfo = String.format("feel: %d ,should: %d ,must: %d ",wolfkillArray.get(i).get(0),wolfkillArray.get(i).get(1),wolfkillArray.get(i).get(2));
//                        if((wolfkillArray.get(i).get(0) + wolfkillArray.get(i).get(1) + wolfkillArray.get(i).get(2) >  0)){
//                            conMap.put("listSecondInfo",listSecondInfo);
//                        }else{
//                            conMap.put("listSecondInfo","");
//                        }
//                        listInfoDicArray.add(conMap);
//                    }
//                }
//            }
//        }else if(type == 2){//予言者用
//            for(int i=0;i < playerArray.size();i++){
//                if((boolean) playerArray.get(i).get("isLive") == true && playerArray.get(i).get("roleId") != Utility.Role.Seer){
//                    listPlayerIdArray.add(i);
//
//                    Map<String,String> conMap = new HashMap<>();
//                    conMap.put("name",(String)playerArray.get(i).get("name"));
//                    conMap.put("listSecondInfo","");
//                    listInfoDicArray.add(conMap);
//                }
//            }
//        }else if(type == 3){
//            for(int i=0;i < playerArray.size();i++){
//                if((boolean) playerArray.get(i).get("isLive") == true && playerArray.get(i).get("roleId") != Utility.Role.Bodyguard){
//                    listPlayerIdArray.add(i);
//
//                    Map<String,String> conMap = new HashMap<>();
//                    conMap.put("name",(String)playerArray.get(i).get("name"));
//                    conMap.put("listSecondInfo","");
//                    listInfoDicArray.add(conMap);
//                }
//            }
//        }
//        listView.invalidateViews();
    }

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
    public static String ipAddress = "接続先を設定してください";

    private void connect() throws MalformedURLException{
        ipAddress = preference.getString("ipAddress","接続先を設定してください");
		socket = new SocketIO(ipAddress);
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
                            receivedmsg = message.getString("message");
                            String [] msgInfo = receivedmsg.split(":",0);

                            switch (msgInfo[0]){
                                case "advertiseMyDevice":
                                    Boolean isNew = true;
                                    for(int i=0;i<roomInfoDicArray.size();i++){
                                        if(roomInfoDicArray.get(i).get("gameID").equals(msgInfo[1])){
                                            isNew = false;
                                        }
                                    }
                                    if(isNew){
                                        /**文章の解読**/
                                        Map<String,String> roomInfoDic = new HashMap<String,String>();
                                        roomInfoDic.put("header",msgInfo[0]);
                                        roomInfoDic.put("gameID",msgInfo[1]);
                                        roomInfoDic.put("periID",msgInfo[2]);
                                        roomInfoDic.put("periName", msgInfo[3]);

                                        roomInfoDicArray.add(roomInfoDic);
                                        // メッセージが空でなければ追加
                                        adapter.add(roomInfoDic.get("periName") + "(" + roomInfoDic.get("gameID") + ")");
                                        /**受信メッセージを格納**/
                                        // TODO 配列に辞書追加

                                        customView.invalidate();
                                    }

                                    break;
                                case "closeMyDevice":
                                    for(int i=0;i<roomInfoDicArray.size();i++){
                                        if(roomInfoDicArray.get(i).get("gameID").equals(msgInfo[1])){
                                            adapter.remove(roomInfoDicArray.get(i).get("periName") + "(" + roomInfoDicArray.get(i).get("gameID") + ")");
                                            roomInfoDicArray.remove(i);
                                        }

                                    }
                                    customView.invalidate();
                                    // 参加登録しめきり
                                    break;
                                case "mes":
                                    if(msgInfo[2].equals(fixedGameInfo.get("periID")) && msgInfo[3].equals(myId)){
                                        /**from peri , to me のもののみ受け取る**/
                                        Map<String,String> c = new HashMap<String,String>();
                                        c.put("header",msgInfo[0]);
                                        c.put("gameID",msgInfo[1]);
                                        c.put("periID",msgInfo[2]);
                                        c.put("periName",msgInfo[3]);

                                        roomInfoDicArray.add(c);
                                        // メッセージが空でなければ追加
                                        adapter.add(c.get("periName") + "(" + c.get("gameID") + ")");
                                        /**受信メッセージを格納**/
                                        // TODO 配列に辞書追加

                                        customView.invalidate();
                                    }
                                    break;
                                default:
                                    break;
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


    public static void sendEvent(){
		// 文字が入力されていなければ何もしない
//		if (editText.getText().toString().length() == 0) {
//		    return;
//		}

        Log.d("sendEvent","sendEvent=");
        String sendmsg = "";
        String periID = roomInfoDicArray.get(selectedRoomId).get("periID");
        String centID = myId;
        String command = "participateRequest";
        String gameID = roomInfoDicArray.get(selectedRoomId).get("gameID");
        String centName = myName;

        sendmsg = "mes:"
                + Integer.toString(signalID) + ":"
                + periID + ":"
                + centID + ":"
                + command + ":"
                + gameID + "/"
                + centID + "/"
                + centName + "/"
                + periID + "/0";
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

    public static void drawEditText(boolean visible){
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

            switch (dialogPattern){
                case "editUserName":
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
                                        myName = text;

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
                    break;
                case "editIpAddress":
                    final EditText editIpAddress = new EditText(this);
                    builder.setTitle("接続先を入力してください")
                            //setViewにてビューを設定
                            .setView(editIpAddress)
                            .setPositiveButton("設定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                       Toast.makeText(SettingScene.this, addPlayerView.getText().toString(), Toast.LENGTH_LONG).show();

                                    String text = editIpAddress.getText().toString();
                                    if(!(text.equals(""))){
                                        editor.putString("ipAddress",text);
                                        /**preferenceの書き換え**/
                                        editor.commit();
                                        ipAddress = text;

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
        return true;
    }

}
