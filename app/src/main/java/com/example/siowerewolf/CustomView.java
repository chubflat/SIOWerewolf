package com.example.siowerewolf;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.StringTokenizer;

/**
 * Created by Kazuaki on 2016/02/17.
 */
public class CustomView extends View {
    //TODO Bitmap宣言
    private Bitmap backgroundImg = null;
    private Bitmap roleImg = null;
    private Bitmap frameImg = null;
    private Bitmap timerFrameImg = null;
    private Bitmap buttonImg = null;
    private Bitmap backCard = null;

    //TODO サイズ取得用
    public static int width;
    public static int height;

    public static int bitmapWidth;
    public static int bitmapHeight;

    //SettingScene用
    public static Rect backgroundRect;
    public static Rect buttonRect3;
    public static Rect buttonRect2;
    // TODO ユーザー設定中身

    //gameScene用
    public static Rect buttonRect1;
    public static Rect actionButtonRect;
    public static Rect topTextRect;
    public static Rect roleCardRect;
    public static Rect timerRect;
    public static Rect historyRect;

    // TODO GameSceneと共通の変数
    public static int day = 0;
    public static int selectedPlayerId;
    public static int mediumId;
//    public static boolean isFirstNight;
    //    public static String scene;
    public static String settingPhase;
    public static String gamePhase;
    public static Boolean isSettingScene;
    public static Boolean isGameScene;
//    public static Boolean isWaiting;
    public static int myPlayerId;

    //TODO Canvasに新要素追加時

    public CustomView(Context context) {
        super(context);
        setFocusable(true);

        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
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
    protected void onDraw(Canvas canvas){

        Paint paint = new Paint();
        paint.setTextSize(width * 6 / 100);
        paint.setColor(Color.BLACK);

        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // Bitmap初期化
        bitmapWidth = width;
        bitmapHeight = height;
        backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.afternoon,bitmapWidth,bitmapHeight);
        frameImg = decodeSampledBitmapFromResource(getResources(),R.drawable.frame,bitmapWidth,bitmapHeight);
        //TODO 画像サイズ修正時になおす
        timerFrameImg = decodeSampledBitmapFromResource(getResources(),R.drawable.frame,bitmapWidth,bitmapHeight);
        buttonImg = decodeSampledBitmapFromResource(getResources(),R.drawable.button,bitmapWidth,bitmapHeight);
        roleImg = decodeSampledBitmapFromResource(getResources(),R.drawable.card0,bitmapWidth,bitmapHeight);
        backCard = decodeSampledBitmapFromResource(getResources(),R.drawable.back_card,bitmapWidth,bitmapHeight);

        //SettingScene用Rect初期化
        backgroundRect = new Rect(0,0,width,height);
        buttonRect3 = new Rect(width * 10 / 100 ,height * 50 / 100,width * 90 / 100 ,height * 60 / 100);
        buttonRect2 = new Rect(width * 10 / 100 ,height * 65 / 100,width * 90 / 100 ,height * 75 / 100);

        //GameScene用Rect初期化
        buttonRect1 = new Rect(width * 25 / 100 ,height * 80 / 100,width * 75 / 100 ,height * 90 / 100);
        actionButtonRect = new Rect (width * 75 / 100 ,height * 87 / 100,width * 95 / 100 ,height * 95 / 100);
        topTextRect = new Rect(width * 20 / 100 ,height * 5 / 100,width * 80 / 100 ,height * 15 / 100);
        roleCardRect = new Rect(width * 5 / 100, height * 5/100 ,width * 20 / 100 ,height *5 /100 + width * 15 / 100  * 1125 /938 );
        timerRect = new Rect(width * 22 / 100, height * 5/100 ,width * 70 / 100 ,height * 15 / 100);

        //TODO GameSceneとの共有変数の初期化
        setSameVariable();

        // default List非表示
        MainActivity.drawListView(false);
        MainActivity.drawChat(false);
        MainActivity.companionListView.setVisibility(View.INVISIBLE);

        /**listview表示**/


        if(isSettingScene){
            backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.afternoon,bitmapWidth,bitmapHeight);
            canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);
            switch (settingPhase){
                case "setting_menu":
                    canvas.drawBitmap(frameImg,null,topTextRect,paint);
                    canvas.drawText("通信設定", width * 30 / 100, height * 10 / 100, paint);

                    /**room select**/
                    canvas.drawBitmap(buttonImg,null,buttonRect3,paint);
                    canvas.drawText("ルーム設定", width * 25 / 100, height * 55 / 100, paint);
                    /**user_setting**/
                    canvas.drawBitmap(buttonImg,null,buttonRect2,paint);
                    canvas.drawText("ユーザー設定", width * 25 / 100, height * 70 / 100, paint);

                    break;
                case "user_setting":
                    // TODO User設定画面描画
                    paint.setTextSize(width * 5 / 100);

                    /**user ID**/
                    canvas.drawText("ID   :\n" + MainActivity.myId,width * 5 / 100,height * 5/100,paint);
                    canvas.drawText("Name :\n" + MainActivity.myName,width * 5 / 100,height * 15/100,paint);
                    canvas.drawText("接続先:\n" + MainActivity.ipAddress,width * 5 / 100,height * 25/100,paint);

                    /**接続先変更**/
                    canvas.drawBitmap(buttonImg, null, buttonRect3, paint);
                    canvas.drawText("接続先変更", width * 25 / 100, height * 55 / 100, paint);
                    /**user name**/
                    canvas.drawBitmap(buttonImg,null,buttonRect2,paint);
                    canvas.drawText("名前変更", width * 25 / 100, height * 70 / 100, paint);
                    /**user_setting**/
                    canvas.drawBitmap(buttonImg,null,buttonRect1,paint);
                    canvas.drawText("戻る",width * 25/100,height * 85/100,paint);

                    break;
                case "room_select":
                    //TODO Client設定 部屋探索
                        /**
                        * bluetoothで部屋のIDを受信
                        * リストに表示
                        * リストの中身を選択したら待機画面に**/

                    buttonRect1 = new Rect(width * 10 / 100 ,height * 80 / 100,width * 90 / 100 ,height * 90 / 100);
//                    backgroundImg = BitmapFactory.decodeResource(getResources(),R.drawable.night);
                    canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
                    canvas.drawText("戻る", width * 25 / 100, height * 85 / 100, paint);
                    paint.setTextSize(width * 10 / 100);
                    canvas.drawText("待機中", width * 30 / 100, height * 50 / 100, paint);
                    canvas.drawBitmap(frameImg, null, topTextRect, paint);
                    paint.setTextSize(width * 5 /100);
                    canvas.drawText("ゲーム部屋一覧", width * 30 / 100, height * 10 / 100, paint);

                    MainActivity.drawListView(true);

                    break;
                case "info_check":
                    String playerInfoReceive = "";
                    if(!(MainActivity.isWaiting)){
                        playerInfoReceive = "プレイヤー情報受信中";
                    }else{
                        playerInfoReceive = "ルール設定待ち";
                    }

                    canvas.drawText(playerInfoReceive, width * 10 / 100, height * 50 / 100, paint);

//                    canvas.drawText("test1", width * 20 / 100, height * 70 / 100, paint);
//                    canvas.drawText("test2", width * 20 / 100, height * 80 / 100, paint);
//                    canvas.drawText("test3", width * 20 / 100, height * 90 / 100, paint);

                    break;
                case "rule_confirm":
                    MainActivity.drawListView(true);
                    // background
//                    canvas.drawBitmap(backgroundImg, null, backgroundRect, paint);
                    //topText
                    canvas.drawBitmap(timerFrameImg,null,topTextRect,paint);
                    canvas.drawText("ルール確認", width * 30 / 100, height * 10 / 100, paint);

                    //confirmButton

                    String text1 = "";
                    if(!(MainActivity.isWaiting)){
                        canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
                        text1 = "確認";
                    }else{
                        text1 = "全員の確認待ち";
                    }
                    canvas.drawText(text1,width * 25/100,height * 85/100,paint);
                    break;
                default:
                    break;
            }
        }else if(!isSettingScene && isGameScene){
            // background
            backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.night,bitmapWidth,bitmapHeight);

            switch (gamePhase){
                case "roleRotate":
                    // rotateImg 表示
                    canvas.drawBitmap(backgroundImg, null, backgroundRect, paint);

                    Rect rotateCardRect = new Rect(width * 15 /100,height * 20 / 100 ,width * 85 / 100 ,height *20 /100 + width * 70 / 100  * 1125 /938 );
                    //TODO cardRotate
                    //TODO roleImgを取ってくる:デフォルトで村人
//                    roleImg = decodeSampledBitmapFromResource(getResources(),MainActivity.roleImg,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(MainActivity.roleBitmap, null, rotateCardRect, paint);
//                    canvas.drawText((String)MainActivity.getPlayerInfo(myPlayerId, "roleId", "name"), width * 25 / 100, height * 5 / 100, paint);

                    // confirm button
                    if(!MainActivity.isWaiting){
                        canvas.drawBitmap(buttonImg,null,buttonRect1,paint);
                        canvas.drawText("詳細確認", width * 25 / 100, height * 85 / 100, paint);
                    }


                    break;

                case "roleCheck":
                    roleImg = MainActivity.roleBitmap;
                    canvas.drawBitmap(backgroundImg, null, backgroundRect, paint);

                    //Rect宣言
                    Rect topFrameRect = new Rect(width * 5 /100,height * 5 / 100 ,width * 95 / 100 ,height * 45 /100);
//                    Rect belowFrameRect = new Rect(width * 20 /100,height * 60 / 100 ,width * 80 / 100 ,height * 75 /100);
                    Rect roleCheckCardRect = new Rect(width * 42 /100,height * 10 / 100 ,width * 58 / 100 ,height * 10 /100 + width * 16/100 * 1125/938);

                    // canvasDraw
                    // 画面上部のテキスト情報
                    canvas.drawBitmap(frameImg, null, topFrameRect, paint);

//                    roleImg = decodeSampledBitmapFromResource(getResources(),(int)MainActivity.getPlayerInfo(myPlayerId,"roleId","cardId"),bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(MainActivity.roleBitmap, null, roleCheckCardRect, paint);


                    // 仲間の表示

//                    canvas.drawBitmap(frameImg,null,belowFrameRect,paint);
                    if((Boolean)MainActivity.getPlayerInfo(myPlayerId,"roleId","hasTableFirst")){
                        MainActivity.companionListView.setVisibility(View.VISIBLE);
                    }
                    // confirm button

                    String text2 = "";
                    if(!(MainActivity.isWaiting)){
                        canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
                        text2 = "初日夜へ";
                    }else{
                        text2 = "全員の確認待ち";
                        paint.setColor(Color.WHITE);

                    }
                    canvas.drawText(text2,width * 25/100,height * 85/100,paint);

                    String roleText = String.format("あなたの役職は「%s」です。%s",(String)MainActivity.getPlayerInfo(myPlayerId, "roleId", "name"),(String)MainActivity.getPlayerInfo(myPlayerId, "roleId", "explain"));
                    TextPaint mTextPaint = new TextPaint();
                    mTextPaint.setTextSize(width * 4/100);
                    StaticLayout mTextLayout = new StaticLayout(roleText,mTextPaint,width*3/5, Layout.Alignment.ALIGN_NORMAL,1.0f, 0.0f, false);
                    canvas.translate(width * 2 / 10, height * 25 / 100);//text の左上座標の指定

                    mTextLayout.draw(canvas);
                    canvas.restore();

                    break;

                case "night_chat":
                    canvas.drawBitmap(backgroundImg, null, backgroundRect, paint);


                    MainActivity.drawChat(true);
                    roleImg = decodeSampledBitmapFromResource(getResources(),(int)MainActivity.getPlayerInfo(myPlayerId,"roleId","cardId"),bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(roleImg, null, roleCardRect, paint);
                    canvas.drawBitmap(timerFrameImg, null, timerRect, paint);
                    canvas.drawText(MainActivity.timer, width * 30 / 100, height * 10 / 100, paint);

                    historyRect = new Rect(width * 5 /100,height * 87 / 100 ,width * 25 / 100 ,height *95 /100);
                    canvas.drawBitmap(buttonImg,null,historyRect,paint);
                    canvas.drawText("履歴",width * 7/100,height * 92 / 100,paint);

//                    canvas.drawBitmap(buttonImg,null,actionButtonRect,paint);

                    String action = "";
                    if(MainActivity.day == 1){
                        if((Boolean)MainActivity.getPlayerInfo(myPlayerId,"roleId","hasActionFirst")&& (int)MainActivity.ruleDic.get("seerMode") == 1){
                                if(!MainActivity.actionDone){
                                    action = (String)MainActivity.getPlayerInfo(myPlayerId,"roleId","actionButtonText");
                                    canvas.drawBitmap(buttonImg,null,actionButtonRect,paint);
                                    canvas.drawText(action,width * 75 / 100 ,height * 92 / 100,paint);
                                }

                        }
                    }else{
                        if((Boolean)MainActivity.getPlayerInfo(myPlayerId,"roleId","hasAction")){
                            if(!MainActivity.actionDone){
                                action = (String)MainActivity.getPlayerInfo(myPlayerId,"roleId","actionButtonText");
                                canvas.drawBitmap(buttonImg,null,actionButtonRect,paint);
                                canvas.drawText(action,width * 75 / 100 ,height * 92 / 100,paint);
                            }

                        }
                    }
                    if(MainActivity.timer.equals("00:00")){
                        MainActivity.drawChat(false);
                    }
                    break;

//                case "night_action":
////                    paint.setColor(Color.BLACK);
////                    paint.setAlpha(56);
////                    canvas.drawRect(backgroundRect,paint);
//                    MainActivity.setListAdapter("night_action");
//                    MainActivity.drawListView(true);
//
//                    String actionString = (String)MainActivity.getPlayerInfo(myPlayerId,"roleId","actionButtonText");
//                    canvas.drawText(actionString + "先を指定してください", width * 10 / 100, height * 10 / 100, paint);
//
//                    canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
//                    canvas.drawText("戻る",width * 25/100,height * 85/100,paint);
//
//                    break;
//
//                case "history":
////                    paint.setColor(Color.BLACK);
////                    paint.setAlpha(56);
////                    canvas.drawRect(backgroundRect,paint);
//                    MainActivity.historyListView.setVisibility(View.VISIBLE);
//                    canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
//                    canvas.drawText("戻る", width * 25 / 100, height * 85 / 100, paint);
//
//                    break;

                case "morning":
                    // background
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.morning,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);

//                    roleImg = decodeSampledBitmapFromResource(getResources(),R.drawable.back_card,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backCard,null,roleCardRect,paint);
                    canvas.drawBitmap(timerFrameImg,null,timerRect,paint);
                    Rect morningFrameRect = new Rect(width * 15 / 100,height * 40 / 100,width * 85 / 100 ,height * 60 / 100);
                    canvas.drawBitmap(frameImg, null, morningFrameRect, paint);

                    //TODO Text表示

                    String text3 = "";
                    if(!(MainActivity.isWaiting)){
                        canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
                        text3 = "確認";
                    }else{
                        text3 = "全員の確認待ち";
                    }
                    canvas.drawText(text3, width * 25 / 100, height * 85 / 100, paint);

                    String morningText = String.format("%d日目の朝になりました。昨日の犠牲者は%sでした。",MainActivity.day,MainActivity.victimString);

                    TextPaint morningTextPaint = new TextPaint();
                    morningTextPaint.setTextSize(width * 5 / 100);
                    StaticLayout morningTextLayout = new StaticLayout(morningText,morningTextPaint,width*3/5, Layout.Alignment.ALIGN_NORMAL,1.0f, 0.0f, false);
                    canvas.translate(width * 2 / 10, height * 40 / 100);//text の左上座標の指定

                    morningTextLayout.draw(canvas);
                    canvas.restore();

                    break;
                case "beforeAfternoon":
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.morning,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);

//                    roleImg = decodeSampledBitmapFromResource(getResources(),R.drawable.back_card,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backCard,null,roleCardRect,paint);
                    canvas.drawBitmap(timerFrameImg,null,timerRect,paint);
                    Rect beforeAfternoonFrameRect = new Rect(width * 15 / 100,height * 40 / 100,width * 85 / 100 ,height * 60 / 100);
                    canvas.drawBitmap(frameImg, null, beforeAfternoonFrameRect, paint);

                    //TODO Text表示

//                    String beforeAfternoon = "";
                    if(!(MainActivity.isWaiting)){
                        canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
                        text3 = "確認";
                    }else{
                        text3 = "全員の確認待ち";
                    }
                    canvas.drawText(text3, width * 25 / 100, height * 85 / 100, paint);

//                    String morningText = String.format("%d日目の朝になりました。昨日の犠牲者は%sでした。",MainActivity.day,MainActivity.victimString);

                    TextPaint beforeAfternoonTextPaint = new TextPaint();
                    beforeAfternoonTextPaint.setTextSize(width * 5 / 100);
                    StaticLayout beforeAfternoonTextLayout = new StaticLayout(MainActivity.victimString,beforeAfternoonTextPaint,width*3/5, Layout.Alignment.ALIGN_NORMAL,1.0f, 0.0f, false);
                    canvas.translate(width * 2 / 10, height * 40 / 100);//text の左上座標の指定

                    beforeAfternoonTextLayout.draw(canvas);
                    canvas.restore();

                    break;
                case "afternoon_meeting":
                    // background
                    backgroundImg = decodeSampledBitmapFromResource(getResources(), R.drawable.afternoon, width, height);
                    canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);

                    backCard = decodeSampledBitmapFromResource(getResources(),R.drawable.back_card,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backCard,null,roleCardRect,paint);
                    canvas.drawBitmap(timerFrameImg, null, timerRect, paint);
                    canvas.drawText(MainActivity.timer, width * 30 / 100, height * 10 / 100, paint);

                    historyRect = new Rect(width * 5 /100,height * 87 / 100 ,width * 25 / 100 ,height *95 /100);
                    canvas.drawBitmap(buttonImg, null, historyRect, paint);
                    canvas.drawText("履歴", width * 7 / 100, height * 92 / 100, paint);

                    if(!(MainActivity.isWaiting)){
                        canvas.drawBitmap(buttonImg,null,actionButtonRect,paint);
                        action = "議論終了";
                    }else{
                        action = "終了待ち";
                    }

                    canvas.drawText(action,width * 75 / 100 ,height * 92 / 100,paint);

                    break;
                case "evening_voting":
                    // background
                    MainActivity.drawListView(true);
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.evening,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg, null, backgroundRect, paint);
                    canvas.drawBitmap(timerFrameImg, null, timerRect, paint);
                    canvas.drawText(MainActivity.timer, width * 30 / 100, height * 10 / 100, paint);

                    canvas.drawText("全員の投票待ち", width * 20 / 100, height * 50 / 100, paint);

//                    // confirm button
//                    canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
//                    canvas.drawText("次へ", width * 25 / 100, height * 85 / 100, paint);

                    break;
                case "voteFinish":
                    // background
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.evening,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg, null, backgroundRect, paint);
                    canvas.drawBitmap(backCard, null, roleCardRect, paint);
//                    canvas.drawBitmap(timerFrameImg, null, timerRect, paint);
//                    canvas.drawText(MainActivity.timer, width * 30 / 100, height * 10 / 100, paint);

//                    Rect voteFinishFrameRect = new Rect(width * 15 / 100,height * 10 / 100,width * 85 / 100 ,height * 80 / 100);
//                    canvas.drawBitmap(frameImg, null, voteFinishFrameRect, paint);

//                    String result = String.format("%d日目%s回目の投票の結果、\n %sが追放されました。",MainActivity.day,MainActivity.receivedCommandMessageArray[0],"aaa");

                    MainActivity.drawListView(true);

                    String text4 = "";
                    if(!(MainActivity.isWaiting)){
                        canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
                        text4 = "確認";
                    }else{
                        text4 = "全員の確認待ち";
                        paint.setColor(Color.WHITE);
                    }
                    canvas.drawText(text4, width * 25 / 100, height * 85 / 100, paint);

                    paint.setColor(Color.BLACK);
                    String afternoonVictim = "";
                    if(MainActivity.afternoonVictimId !=-1){
                        afternoonVictim =String.format("%sさんが追放されました。", (String) MainActivity.playerInfoDicArray.get(MainActivity.afternoonVictimId).get("userName"));
                    }else{
                        afternoonVictim = "処刑者が決まりませんでした。再投票を行います。";
                    }
                    String result = String.format("%d日目%d回目の投票の結果、\n %s",MainActivity.day,MainActivity.voteTime,afternoonVictim);
//                    canvas.drawText(result,width * 15/100,height * 10/100,paint);

                    TextPaint voteResultPaint = new TextPaint();
                    voteResultPaint.setTextSize(width * 5 / 100);
                    StaticLayout voteResultLayout = new StaticLayout(result,voteResultPaint,width*70/100, Layout.Alignment.ALIGN_NORMAL,1.0f, 0.0f, false);
                    canvas.translate(width * 20 / 100, height * 5 / 100);//text の左上座標の指定

                    voteResultLayout.draw(canvas);
                    canvas.restore();

                    break;
                case "beforeNight":
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.evening,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);

    //                    roleImg = decodeSampledBitmapFromResource(getResources(),R.drawable.back_card,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backCard,null,roleCardRect,paint);
                    canvas.drawBitmap(timerFrameImg,null,timerRect,paint);
                    Rect beforeNightFrameRect = new Rect(width * 15 / 100,height * 40 / 100,width * 85 / 100 ,height * 60 / 100);
                    canvas.drawBitmap(frameImg, null, beforeNightFrameRect, paint);

                    //TODO Text表示

    //                    String beforeAfternoon = "";
                    if(!(MainActivity.isWaiting)){
                        canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
                        text3 = "確認";
                    }else{
                        text3 = "全員の確認待ち";
                    }
                    canvas.drawText(text3, width * 25 / 100, height * 85 / 100, paint);

    //                    String morningText = String.format("%d日目の朝になりました。昨日の犠牲者は%sでした。",MainActivity.day,MainActivity.victimString);

                    TextPaint beforeNightTextPaint = new TextPaint();
                    beforeNightTextPaint.setTextSize(width * 5 / 100);
                    StaticLayout beforeNightTextLayout = new StaticLayout(MainActivity.victimString,beforeNightTextPaint,width*3/5, Layout.Alignment.ALIGN_NORMAL,1.0f, 0.0f, false);
                    canvas.translate(width * 2 / 10, height * 40 / 100);//text の左上座標の指定

                    beforeNightTextLayout.draw(canvas);
                    canvas.restore();

                    break;
                case "heaven":
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.bg_heaven,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg, null, backgroundRect, paint);
                    Rect heavenFrameRect = new Rect(width * 15 / 100,height * 40 / 100,width * 85 / 100 ,height * 60 / 100);
                    canvas.drawBitmap(frameImg, null, heavenFrameRect, paint);

                    String heavenText = "あなたは死亡しました。以後ゲームが終了するまで話をすることができません。";

                    TextPaint heavenPaint = new TextPaint();
                    heavenPaint.setTextSize(width * 5 / 100);
                    StaticLayout heavenLayout = new StaticLayout(heavenText,heavenPaint,width*3/5, Layout.Alignment.ALIGN_NORMAL,1.0f, 0.0f, false);
                    canvas.translate(width * 25 / 100, height * 45 / 100);//text の左上座標の指定

                    heavenLayout.draw(canvas);
                    canvas.restore();


                    break;
                case "gameOver":
                    // confirm button
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.evening,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);
                    canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
                    canvas.drawText("終了する", width * 25 / 100, height * 85 / 100, paint);

                    MainActivity.drawListView(true);
                    String gameOverText = "";
                    if(MainActivity.winner == 0){
//                        gameOverText = "村に潜んだすべての人狼を追放しました。村人チームの勝利です。";
                        gameOverText = "村人チームの勝利です";
                    }else if(MainActivity.winner == 1){
//                        gameOverText = "人狼達は最後の獲物を捕らえた後、次の村へと去って行きました。人狼チームの勝利です。";
                        gameOverText = "人狼チームの勝利です";
                    }else if(MainActivity.winner == 2){
//                        gameOverText = "妖狐は村人と人狼を欺き、この村を支配しました。妖狐チームの勝利です。";
                        gameOverText = "妖狐チームの勝利です";
                    }
                    canvas.drawText(gameOverText,width * 10 / 100, height * 10 / 100, paint);

//                    TextPaint gameOverTextPaint = new TextPaint();
//                    gameOverTextPaint.setTextSize(width * 5 / 100);
//                    StaticLayout gameOverTextLayout = new StaticLayout(gameOverText,gameOverTextPaint,width*3/5, Layout.Alignment.ALIGN_NORMAL,1.0f, 0.0f, false);
//                    canvas.translate(width * 2 / 10, height * 40 / 100);//text の左上座標の指定
//
//                    gameOverTextLayout.draw(canvas);
//                    canvas.restore();
                    break;
                default:
                    break;
            }

            Paint surfaceView = new Paint();
            surfaceView.setColor(Color.BLACK);
            surfaceView.setAlpha(200);


            switch (MainActivity.surfaceView){
                case "night_action":
//                    MainActivity.setListAdapter("night_action");
                    MainActivity.drawListView(true);
                    canvas.drawRect(0,0,width,height,surfaceView);
                    canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
                    canvas.drawText("戻る",width * 25/100,height * 85/100,paint);
                    break;
                case "history":
                    canvas.drawRect(0,0,width,height,surfaceView);
                    MainActivity.historyListView.setVisibility(View.VISIBLE);
                    canvas.drawBitmap(buttonImg, null, buttonRect1, paint);
                    canvas.drawText("戻る", width * 25 / 100, height * 85 / 100, paint);
//
                    break;
                case "invisible":
                    MainActivity.drawListView(false);
                    MainActivity.historyListView.setVisibility(View.INVISIBLE);
                    if(gamePhase.equals("evening_voting") || gamePhase.equals("voteFinish") ||gamePhase.equals("gameOver")){
                        MainActivity.drawListView(true);
                    }

                    break;
                default:
                    break;
            }
        }
    }

    public static float pointX;
    public static float pointY;

    public boolean getTouchButton(Rect r){
        if(r.contains((int)pointX,(int)pointY)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        pointX = event.getX();
        pointY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(isSettingScene){
                    switch (settingPhase){
                        case "setting_menu":
                            if(getTouchButton(buttonRect3)){
                                MainActivity.settingPhase = "room_select";
                                MainActivity.drawListView(true);
//                                MainActivity.sendEvent(this);
                            }else if(getTouchButton(buttonRect2)){
                                MainActivity.settingPhase = "user_setting";
                            }
                            break;
                        case "room_select":
                            if(getTouchButton(buttonRect1)){
                                MainActivity.settingPhase = "setting_menu";
                            }
                            break;
                        case "user_setting":
                            if(getTouchButton(buttonRect1)){
                                MainActivity.settingPhase = "setting_menu";
                            }else if(getTouchButton(buttonRect3)){
                                setDialog("editIpAddress");

                            }else if(getTouchButton(buttonRect2)){
                                setDialog("editUserName");
                            }
                            break;
                        case "rule_confirm":
                            if(getTouchButton(buttonRect1)&& !(MainActivity.isWaiting)){
                                MainActivity.sendEvent(MainActivity.fixedGameInfo.get("periId"),"settingCheck:" + MainActivity.myId);
                                MainActivity.isWaiting = true;
                            }

                            break;

                        default:
                            break;
                    }

                }else if(!isSettingScene && isGameScene){
                    if(getTouchButton(buttonRect1)){
                        MainActivity.surfaceView = "invisible";
                        switch (gamePhase){
                            case "roleRotate":
                                    MainActivity.gamePhase = "roleCheck";
                                    MainActivity.isWaiting = false;
                                break;
                            case "roleCheck":
                                if(!MainActivity.isWaiting){
                                    MainActivity.sendEvent(MainActivity.fixedGameInfo.get("periId"),"roleCheck:" + MainActivity.myId);
                                    MainActivity.isWaiting = true;
                                }
                                break;
                            case "night_chat":
                                if(MainActivity.surfaceView.equals("night_action")||MainActivity.surfaceView.equals("history")){
                                    MainActivity.surfaceView = "invisible";
                                    MainActivity.historyListView.setVisibility(View.INVISIBLE);
                                }
                                break;
//                            case "night_action":
//                                    MainActivity.gamePhase = "night_chat";
//                                break;
//                            case "history":
//                                    MainActivity.gamePhase = "night_chat";
//                                    MainActivity.historyListView.setVisibility(View.INVISIBLE);
//                                break;
                            case "morning":
                                if(!MainActivity.isWaiting){
                                    MainActivity.sendEvent(MainActivity.fixedGameInfo.get("periId"),"checkVictim:" + MainActivity.myId);
                                    MainActivity.isWaiting = true;
                                }
                                break;
                            case "beforeAfternoon":
                                if(!MainActivity.isWaiting){
                                    MainActivity.sendEvent(MainActivity.fixedGameInfo.get("periId"),"morningVictimCheck:" + MainActivity.myId);
                                    MainActivity.isWaiting = true;
                                }
                                break;
                            case "afternoon_meeting":
                                MainActivity.surfaceView = "invisible";
//                                    MainActivity.gamePhase = "history";
                                break;
                            case "voteFinish":
                                if(!(MainActivity.isWaiting)){
                                    MainActivity.sendEvent(MainActivity.fixedGameInfo.get("periId"),"checkVoting:" + MainActivity.myId);
                                    MainActivity.isWaiting = true;
                                }
                                break;
                            case "beforeNight":
                                if(!MainActivity.isWaiting){
                                    MainActivity.sendEvent(MainActivity.fixedGameInfo.get("periId"),"afternoonVictimCheck:" + MainActivity.myId);
                                    MainActivity.isWaiting = true;
                                }
                                break;
                            case "gameOver":
                                if(!(MainActivity.isWaiting)){
                                    MainActivity.initBackground();
                                    MainActivity.adapter.clear();
                                    MainActivity.historyAdapter.clear();
                                    MainActivity.companionListAdapter.clear();
                                }
                                break;
                            default:
                                break;
                        }
                    }else if(getTouchButton(historyRect) && MainActivity.surfaceView.equals("invisible")){
                        if(MainActivity.timer.equals("00:00")){
                        }else{
                            switch (gamePhase){
                                case "night_chat":
                                case "afternoon_meeting":
                                    MainActivity.surfaceView = "history";
                                    break;
                                default:
                                    break;
                            }
                        }
                    }else if(getTouchButton(actionButtonRect) && !(MainActivity.actionDone)){
                        if(MainActivity.timer.equals("00:00")){
                        }else{
                            if(gamePhase.equals("night_chat")&&MainActivity.surfaceView.equals("invisible")){
                                MainActivity.surfaceView = "night_action";
                                MainActivity.setListAdapter("night_action");
                            }else if(gamePhase.equals("afternoon_meeting") && !(MainActivity.isWaiting)){
                                MainActivity.sendEvent(MainActivity.fixedGameInfo.get("periId"),"finishAfternoonRequest:" + MainActivity.myId);
                                MainActivity.isWaiting = true;
                            }else{

                            }
                        }

                    }
                }
                break;

            default:
                return true;
        }
        invalidate();
        return false;
    }


    private void setDialog(String dialogPattern){
        MainActivity.onDialog = true;
        MainActivity.dialogPattern = dialogPattern;
    }

    public static void setSameVariable(){
        isSettingScene = MainActivity.isSettingScene;
        isGameScene = MainActivity.isGameScene;
//        selectedPlayerId = MainActivity.selectedPlayerId;
//        isFirstNight = MainActivity.isFirstNight;
        settingPhase = MainActivity.settingPhase;
        gamePhase = MainActivity.gamePhase;
        myPlayerId = MainActivity.myPlayerId;

    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,int reqWidth, int reqHeight) {

// First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

// Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

// Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

}
