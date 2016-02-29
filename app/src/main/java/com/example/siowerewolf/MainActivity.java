package com.example.siowerewolf;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;
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
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import static android.content.SharedPreferences.*;

public class MainActivity extends Activity {

    /**preferences**/
    private SharedPreferences preference;
    private Editor editor;
    public static String myId = "noId";
    public static String myName = "guest";
    public static int myPlayerId = 0;


	// socketIO
//	private EditText editText;
	public static ArrayAdapter<String> adapter;
	public static SocketIO socket;
    private Handler handler = new Handler();
    public static String receivedmsg = "";
    public static String sendmsg = "";
    public static int signalId;

    // List
	public static ListView listView;
//	public static SimpleAdapter simpleAdapter;
	//    public static Adapter adapter;
	public static CustomView customView = null;
	public static String dialogPattern = "default";
    public static EditText editText;

	// 各種List宣言
	public static List<Map<String,Object>> playerInfoDicArray;//参加者Array
	public static List<Map<String,String>> listInfoDicArray;//リストに表示する情報のArray
	public static ArrayList<Integer> listPlayerIdArray;//listに入っているplayerId Array
	public static ArrayList<Integer> victimArray;//夜間犠牲者Array
    public static List<Map<String,String>> roomInfoDicArray;
    public static Map<String,String> fixedGameInfo;
    public static Map<String,Object> ruleDic;
    public static ArrayList<Integer> roleArray;
    public static Map<String,Object> infoDic;
    public static Map<String,String> userDic;//chat表示用

	public static int selectedPlayerId;//リストで選択されたプレイヤーのId
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
    public static boolean isWaiting;
    public static int day;


    //Chat用
    public static View chat;
    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter chatadapter;
    private ArrayList<ChatMessage> chatHistory;

    // list_item
    public static LinearLayout content;
    public static TextView txtInfo;
    public static LinearLayout contentWithBackground;
    public static TextView txtMessage;


    //Timer
    public static LoopEngine loopEngine;
    public static long startDate;
    public static long stopDate;
    public static String timer;
    public static int roleImg;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setUserId();
        initBackground();
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
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(CustomView.width *90/100, CustomView.height *6/10);
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        selectedPlayerId = -2;

        listView.setAdapter(adapter);
        listView.setLayoutParams(lp);
//        listView.setBackgroundColor(Color.WHITE);
        listView.setBackgroundResource(R.drawable.frame);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (settingPhase.equals("player_setting")) {
//                    selectedPlayerId = -2;
//                } else {
//                    selectedPlayerId = listPlayerIdArray.get(position);
//                }
                if (settingPhase.equals("room_select")) {
                    selectedRoomId = position;
                    fixedGameInfo.put("gameId", roomInfoDicArray.get(selectedRoomId).get("gameId"));
                    fixedGameInfo.put("periId", roomInfoDicArray.get(selectedRoomId).get("periId"));
                    fixedGameInfo.put("periName", roomInfoDicArray.get(selectedRoomId).get("periName"));
                    String participateRequest = String.format("participateRequest:%s/%s/%s/%s/0", fixedGameInfo.get("gameId"), myId, myName, fixedGameInfo.get("periId"));
                    sendEvent(fixedGameInfo.get("periId"), participateRequest);
                    drawListView(false);
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

        chat = getLayoutInflater().inflate(R.layout.activity_chat,null);
        FrameLayout.LayoutParams chatLp = new FrameLayout.LayoutParams(CustomView.width * 90 /100, CustomView.height * 70 / 100);
        chatLp.gravity = Gravity.BOTTOM | Gravity.CENTER;
        chatLp.bottomMargin = 100;

        addContentView(chat, chatLp);
        drawChat(false);
        initControls();

        editText = new EditText(this);
        FrameLayout.LayoutParams editLP = new FrameLayout.LayoutParams(CustomView.width, CustomView.height /10);
        editLP.gravity = Gravity.BOTTOM;
        editLP.bottomMargin = CustomView.height *10/100;

        editText.setLayoutParams(editLP);
        editText.setBackgroundColor(Color.WHITE);

        mFrameLayout.addView(editText);
        drawEditText(false);
		try {
			connect();
		} catch(Exception e) {
			e.printStackTrace();
		}

    }
    /**onCreateここまで**/

    public static void initBackground(){
        isSettingScene = true;
        settingPhase = "setting_menu";
        day = 1;

        listPlayerIdArray = new ArrayList<>();
        roomInfoDicArray = new ArrayList<>();
        fixedGameInfo = new HashMap<>();
        playerInfoDicArray = new ArrayList<>();
        roleArray = new ArrayList<>();
        infoDic = new HashMap<>();

        userDic = new HashMap<>();

        signalId = (int)(Math.random()*999999);

        isWaiting = false;
        receivedCommand = "";

        loopEngine = new LoopEngine();
        timer = "";

    }
    public static void setListAdapter(String type){
        listInfoDicArray.clear();
        listPlayerIdArray.clear();
        adapter.clear();

        switch (type){
            case "ruleCheck":
                adapter.add("");
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

    public static Object getPlayerInfo(int arrayId,String playerInfoKey,String roleInfoKey){
        String playerInfo = (String) Utility.getRoleInfo(MainActivity.getRole((int) MainActivity.playerInfoDicArray.get(arrayId).get(playerInfoKey))).get(roleInfoKey);

        return playerInfo;
    }

    public void setUserId(){

        preference = getPreferences(MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
        editor = preference.edit();

        if(!(preference.getBoolean("launched",false))){
//            初回起動時処理
            int id = (int)(Math.random()*999999);
            myId = String.format("%1$06d", id);
            editor.putString("userId",myId);
            /**preferenceの書き換え**/
            editor.putBoolean("launched",true);
            editor.commit();

        }else{
            /**2回目以降の処理**/
            myId = preference.getString("userId","noId");
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
                            getCommand(msgInfo);


                            // command,receivedCommandMessage,receivedCommandMessageArray

                            switch (msgInfo[0]){
                                case "advertiseMyDevice":
//                                    loopEngine.start();
                                    Boolean isNew = true;

                                    for(int i=0;i<roomInfoDicArray.size();i++){
                                        if(roomInfoDicArray.get(i).get("gameId").equals(msgInfo[1])){
                                            isNew = false;
                                        }
                                    }
                                    if(isNew){
                                        /**文章の解読**/
                                        Map<String,String> roomInfoDic = new HashMap<String,String>();
                                        roomInfoDic.put("header",msgInfo[0]);
                                        roomInfoDic.put("gameId",msgInfo[1]);
                                        roomInfoDic.put("periId",msgInfo[2]);
                                        roomInfoDic.put("periName", msgInfo[3]);

                                        roomInfoDicArray.add(roomInfoDic);
                                        // メッセージが空でなければ追加
                                        adapter.add(roomInfoDic.get("periName") + "(" + roomInfoDic.get("gameId") + ")");
                                        /**受信メッセージを格納**/

                                        customView.invalidate();
                                    }

                                    break;
                                case "closeMyDevice":
                                    for(int i=0;i<roomInfoDicArray.size();i++){
                                        if(roomInfoDicArray.get(i).get("gameId").equals(msgInfo[1])){
                                            adapter.remove(roomInfoDicArray.get(i).get("periName") + "(" + roomInfoDicArray.get(i).get("gameId") + ")");
                                            roomInfoDicArray.remove(i);
                                        }

                                    }
                                    customView.invalidate();
                                    // 参加登録しめきり
                                    break;
                                case "mes":
                                    isWaiting = false;
                                    if((msgInfo[2].equals(myId) || msgInfo[2].equals("centrals")) && msgInfo[3].equals(fixedGameInfo.get("periId"))){
                                        if(msgInfo[4].equals("participateAllow")){
                                            settingPhase = "info_check";
                                            customView.invalidate();
                                        }
                                        switch (receivedCommand){
                                            case "member":
                                                Map<String,Object> member = new HashMap<String, Object>();
                                                int playerId = Integer.valueOf(receivedCommandMessageArray[0]);
                                                String userId = receivedCommandMessageArray[1];
                                                String userName = receivedCommandMessageArray[2];
                                                member.put("playerId",playerId);
                                                member.put("userId",userId);
                                                member.put("userName",userName);
                                                playerInfoDicArray.add(member);
                                                if(userId.equals(myId)){
                                                    myPlayerId = playerId;
                                                }
                                                userDic.put(userId,receivedCommandMessageArray[2]);

//                                            Collections.sort(playerInfoDicArray, new Comparator<Map<String, Object>>() {
//                                                public int compare(Map<String, Object> member1, Map<String, Object> member2) {
//                                                    // 引数１と引数２はそれぞれ商品データ。
//
//                                                    // 商品データのproduct_idを取得する。
//                                                    String player_id1 = member1.get("playerId").toString();
//                                                    String player_id2 = member2.get("playerId").toString();
//
//                                                    return player_id1.compareTo(player_id2);
//                                                }
//                                            });
                                                if(playerInfoDicArray.size() == Integer.valueOf(receivedCommandMessageArray[3])){
                                                    // 参加人数分の配列取得
                                                    sendEvent(fixedGameInfo.get("periId"),"memberCheck:"+ myId);
//                                                    infoDic.put("playerInfoDicArray",(ArrayList<String,Map<String,Object>>)playerInfoDicArray);
                                                }

                                                break;
                                            case "setting":
                                                adapter.clear();

                                                /**role_setting**/
                                                String[] roleSetting = receivedCommandMessageArray[0].split(",",0);
                                                String roleString = "";
                                                for(int i= 0;i<roleSetting.length;i++){
                                                    int j = Integer.valueOf(roleSetting[i]);
                                                    roleArray.add(j);
                                                    Utility.Role role = getRole(i);
                                                    if(j!=0){
                                                        roleString = roleString + Utility.getRoleInfo(role).get("token") + j ;
                                                    }
                                                }
                                                adapter.add(roleString);
                                                /**rule_setting**/
                                                String [] ruleSetting = receivedCommandMessageArray[1].split(",",0);
                                                ruleDic = new HashMap<String, Object>();
                                                ruleDic.put("timer", ruleSetting[0]); // 昼時間
                                                ruleDic.put("night_timer",ruleSetting[1]);// 夜時間
                                                ruleDic.put("seerMode",ruleSetting[2]);// 初日占い
                                                ruleDic.put("canGuard",ruleSetting[3]);// 連続ガード
                                                ruleDic.put("isLack", ruleSetting[4]);// 役欠け

                                                infoDic.put("ruleDic",ruleDic);

                                                for(int i =0;i<ruleSetting.length;i++){
                                                    String ruleString = "";
                                                    int ruleNum = Integer.valueOf(ruleSetting[i]);
                                                    String text = "";
                                                    switch (i){
                                                        case 0:
                                                            ruleDic.put("timer", ruleNum); // 昼時間
                                                            ruleString = String.format("昼時間:  %d分",ruleNum);
                                                            break;
                                                        case 1:
                                                            ruleDic.put("night_timer", ruleNum); // 夜時間
                                                            ruleString = String.format("夜時間:  %d分",ruleNum);
                                                            break;
                                                        case 2:
                                                            ruleDic.put("seerMode", ruleNum); // 初日占い

                                                            if(ruleNum == 0){
                                                                text = "なし";
                                                            }else if(ruleNum == 1){
                                                                text = "あり";
                                                            }else if(ruleNum == 2){
                                                                text = "お告げ";
                                                            }
                                                            ruleString = String.format("初日占い:  %s",text);
                                                            break;
                                                        case 3:
                                                            ruleDic.put("canContinuousGuard", ruleNum); // 連続ガード
                                                            if(ruleNum == 0){
                                                                text = "なし";
                                                            }else if(ruleNum == 1){
                                                                text = "あり";
                                                            }
                                                            ruleString = String.format("連続ガード:  %s",text);
                                                            break;
                                                        case 4:
                                                            ruleDic.put("isLacking", ruleNum); // 役欠け
                                                            if(ruleNum == 0){
                                                                text = "なし";
                                                            }else if(ruleNum == 1){
                                                                text = "あり";
                                                            }
                                                            ruleString = String.format("役かけ:  %s",text);
                                                            break;
                                                    }
                                                    adapter.add(ruleString);
                                                }
                                                settingPhase = "rule_confirm";

                                                break;
                                            case "gamestart":
                                                goGameScene();
                                                isWaiting = false;
                                                //receivedCommandMessageArray  = 0,2/1,1/2,0/3,1
                                                //receivedCommandMessageArray[0] = 0,2
                                                for(int i= 0;i<receivedCommandMessageArray.length;i++){
                                                    String[] playerRole = receivedCommandMessageArray[i].split(",",0);
                                                    int j = Integer.valueOf(playerRole[0]);
//                                                    infoDic.get("ruleDic").get;
//                                                    infoDic.get("playerInfoDicArray").get(j).put("roleId", Integer.valueOf(playerRole[1]));
                                                    playerInfoDicArray.get(j).put("roleId",Integer.valueOf(playerRole[1]));
                                                    playerInfoDicArray.get(j).put("isLive", true);
                                                }
                                                startDate =System.currentTimeMillis()/1000;
                                                stopDate = startDate + 3;
                                                loopEngine.rotateStart();

                                                break;

                                            case "firstNight":
                                                gamePhase = "night_action";
                                                drawChat(true);
                                                startDate =System.currentTimeMillis()/1000;
                                                stopDate = startDate + (int)ruleDic.get("night_timer")*20;
                                                loopEngine.start();
                                                break;

                                            case "chatreceive":

                                                if(msgInfo[2].equals(myId)){

                                                }

                                                ChatMessage chatMessage = new ChatMessage();
                                                String receivedMessage = "";
                                                if(receivedCommandMessageArray[0].equals(myId)){

                                                }else if(receivedCommandMessageArray[0].equals("aaaaaa")){
                                                    // centID  == receivedCommandMessageArray[0]

                                                    receivedMessage = receivedCommandMessageArray[2];
                                                    chatMessage.setId(1);//dummy
                                                    chatMessage.setMessage(receivedMessage);
                                                    chatMessage.setName("GM");
                                                    chatMessage.setMe(false);
                                                    displayMessage(chatMessage);

                                                }else{
                                                    receivedMessage = receivedCommandMessageArray[1];//userId
                                                    String name = userDic.get(receivedCommandMessageArray[0]);
                                                    chatMessage.setId(1);//dummy
                                                    chatMessage.setMessage(receivedMessage);
                                                    chatMessage.setName(name);
                                                    chatMessage.setMe(false);
                                                    displayMessage(chatMessage);
                                                }


                                                break;
                                            case "afternoonStart":
                                                gamePhase = "morning";
                                                day++;
                                                startDate =System.currentTimeMillis()/1000;
                                                stopDate = startDate + (int)ruleDic.get("timer")*20;
                                                loopEngine.start();

                                                break;
                                            default:
                                                break;
                                        }


                                        customView.invalidate();

                                    }else{
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

    private void initControls() {

        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        TextView meLabel = (TextView) findViewById(R.id.meLbl);
        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        companionLabel.setText("");// Hard Coded
//        meLabel.setText("melbl");
        loadHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setName(myName);
                chatMessage.setMe(true);

                // send to Periferal
                String sendMessage = String.format("chatsend:%s/%s",myId,messageText);
                sendEvent(fixedGameInfo.get("periId"),sendMessage);

                messageET.setText("");
                displayMessage(chatMessage);

            }
        });
    }

    public void displayMessage(ChatMessage message) {
        chatadapter.add(message);
        chatadapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadHistory(){

        chatHistory = new ArrayList<ChatMessage>();

        chatadapter = new ChatAdapter(MainActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(chatadapter);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }
    }

    public static void drawChat(boolean visible){
        if(visible) {
            chat.setVisibility(View.VISIBLE);
        }else if(!visible){
            chat.setVisibility(View.INVISIBLE);
        }
    }

    public static Utility.Role getRole(int i){
        Utility.Role[] values = Utility.Role.values();
        Utility.Role role = values[i];
        return role;
    }

    public static void goGameScene(){
        isSettingScene = false;
        isGameScene = true;
        gamePhase = "roleRotate";
        isFirstNight = true;
    }

    public static String receivedCommand;
    public static String[] receivedCommandMessageArray;

    public static void getCommand(String[] message){
        if(message[0].equals("mes")){
            receivedCommand = message[4];
            if(message.length<6){
            }else{
                String receivedCommandMessage = message[5];
                receivedCommandMessageArray = receivedCommandMessage.split("/",0);
            }
        }else{
            receivedCommand = "";
        }
    }

    public static void sendEvent(String yourId,String message){

        String sendMessage = String.format("mes:%d:%s:%s:%s",signalId,yourId,myId,message);

		try {
		// イベント送信
			JSONObject json = new JSONObject();
//			json.put("message", editText.getText().toString());
			json.put("message", sendMessage);
			socket.emit("message:send", json);

		} catch (JSONException e) {
			e.printStackTrace();
		}

    	// テキストフィールドをリセット
        signalId++;
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

    public static void update(){
        int timerSecond = (int)(stopDate - System.currentTimeMillis()/1000);
        String minute = String.format("%1$02d",timerSecond/60);
        String second = String.format("%1$02d",timerSecond%60);
        timer = minute + ":" + second;
        if(timer.equals("00:00")){
            loopEngine.stop();
            if(gamePhase.equals("night_action")){
                sendEvent(fixedGameInfo.get("periId"),"nightFinish:"+myId);
                drawChat(false);
            }else if(gamePhase.equals("afternoon_meeting")){
                gamePhase = "evening_voting";

            }

        }
        customView.invalidate();
    }
    public static void rotate(){
        int i = (int)(Math.random()*6);
        roleImg = (int)Utility.getRoleInfo(getRole(i)).get("cardId");
        if(stopDate == System.currentTimeMillis() / 1000){
            loopEngine.rotateStop();
//            roleImg = (int)Utility.getRoleInfo(getRole(myPlayerId)).get("cardId");
            roleImg = R.drawable.card11;
        }
    }

}

//一定時間後にupdateを呼ぶためのオブジェクト
class LoopEngine extends Handler {
    private boolean isUpdate;
    private boolean isRotate;
    public void start(){
        this.isUpdate = true;
        handleMessage(new Message());
    }
    public void stop(){
        this.isUpdate = false;
    }
    public void rotateStart(){
        this.isRotate = true;
        handleMessage(new Message());
    }
    public void rotateStop(){
        this.isRotate = false;
    }
    @Override
    public void handleMessage(Message msg) {
        this.removeMessages(0);//既存のメッセージは削除
        if(this.isUpdate){
            MainActivity.update();//自信が発したメッセージを取得してupdateを実行
            sendMessageDelayed(obtainMessage(0),100);//100ミリ秒後にメッセージを出力
        }else if(isRotate){
            MainActivity.rotate();
            sendMessageDelayed(obtainMessage(0),100);
        }
    }
}
