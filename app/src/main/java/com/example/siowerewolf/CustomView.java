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
    public static Rect roomSelectButtonRect;
    public static Rect userSettingButtonRect;
    // TODO ユーザー設定中身

    //gameScene用
    public static Rect confirmButtonRect;
    public static Rect actionButtonRect;
    public static Rect topTextRect;
    public static Rect roleCardRect;
    public static Rect timerRect;

    // TODO GameSceneと共通の変数
    public static int day = 0;
    public static int selectedPlayerId;
    public static int mediumId;
    public static boolean isFirstNight;
    //    public static String scene;
    public static String settingPhase;
    public static String gamePhase;
    public static Boolean isSettingScene;
    public static Boolean isGameScene;

    //TODO Canvasに新要素追加時

    public CustomView(Context context) {
        super(context);
        setFocusable(true);

//        //WindowsManager取得
//        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
//        //Displayインスタンス生成
//        Display dp = wm.getDefaultDisplay();
//        //Displayサイズ取得
//        width = dp.getWidth();
//        height = dp.getHeight();

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
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);


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
        backgroundRect = new Rect(0,0,bitmapWidth,bitmapHeight);
        roomSelectButtonRect = new Rect(width * 10 / 100 ,height * 50 / 100,width * 90 / 100 ,height * 60 / 100);
        userSettingButtonRect = new Rect(width * 10 / 100 ,height * 65 / 100,width * 90 / 100 ,height * 75 / 100);

        //GameScene用Rect初期化
        confirmButtonRect = new Rect(width * 10 / 100 ,height * 80 / 100,width * 90 / 100 ,height * 90 / 100);
        actionButtonRect = new Rect (width * 75 / 100 ,height * 5 / 100,width * 95 / 100 ,height * 20 / 100);
        topTextRect = new Rect(width * 20 / 100 ,height * 5 / 100,width * 80 / 100 ,height * 15 / 100);
        roleCardRect = new Rect(width * 5 / 100, height * 5/100 ,width * 20 / 100 ,height * 20 / 100);
        timerRect = new Rect(width * 22 / 100, height * 5/100 ,width * 70 / 100 ,height * 20 / 100);

        //TODO GameSceneとの共有変数の初期化
        setSameVariable();

        backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.afternoon,bitmapWidth,bitmapHeight);
        canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);

        // default List非表示
//        MainActivity.drawListView(false);

        Log.d("if","if=");

        if(isSettingScene){
            switch (settingPhase){
                case "setting_menu":
                    /**background**/
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.afternoon,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);
                    /**room select**/
                    canvas.drawBitmap(buttonImg,null,roomSelectButtonRect,paint);
                    canvas.drawText("ルーム設定", width * 25 / 100, height * 55 / 100, paint);
                    /**user_setting**/
                    canvas.drawBitmap(buttonImg,null,userSettingButtonRect,paint);
                    canvas.drawText("ユーザー設定",width * 25/100,height * 70/100,paint);

                    break;
                case "user_setting":
                    // TODO User設定画面描画

                    /**background**/
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.afternoon,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);
                    /**user ID**/
                    canvas.drawText(MainActivity.myId,width * 20 / 100,height * 5/100,paint);
                    canvas.drawText(MainActivity.myName,width * 20 / 100,height * 15/100,paint);

                    /**user name**/

                    /**room select**/
                    canvas.drawBitmap(buttonImg,null,roomSelectButtonRect,paint);
                    canvas.drawText("名前変更", width * 25 / 100, height * 55 / 100, paint);
                    /**user_setting**/
                    canvas.drawBitmap(buttonImg,null,confirmButtonRect,paint);
                    canvas.drawText("戻る",width * 25/100,height * 93/100,paint);

                        /*
                        * username
                        * userId
                        * back button*/
                    break;
                case "room_select":
                    //TODO Client設定 部屋探索
                        /**
                        * bluetoothで部屋のIDを受信
                        * リストに表示
                        * リストの中身を選択したら待機画面に**/

                    confirmButtonRect = new Rect(width * 10 / 100 ,height * 80 / 100,width * 90 / 100 ,height * 90 / 100);
//                    backgroundImg = BitmapFactory.decodeResource(getResources(),R.drawable.night);
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.night,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);
                    paint.setColor(Color.WHITE);
                    canvas.drawText("待機中",width * 30/100,height * 50 / 100,paint);

                    canvas.drawBitmap(buttonImg, null, confirmButtonRect, paint);
                    canvas.drawText(MainActivity.receivedmsg,width * 25/100,height * 85/100,paint);

                    break;
                case "rule_confirm":
                    // background
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.afternoon,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);
                    //topText
                    canvas.drawBitmap(timerFrameImg,null,topTextRect,paint);
                    canvas.drawText("ルール",width * 30/100,height * 10/100,paint);

                    // TODO List表示
                    // 2行リスト GameSceneに記述

                    //confirmButton
                    canvas.drawBitmap(buttonImg, null, confirmButtonRect, paint);
                    canvas.drawText("確認",width * 25/100,height * 85/100,paint);
                    break;
                default:
                    break;
            }
        }else if(!isSettingScene && isGameScene){
            // background
            backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.night,bitmapWidth,bitmapHeight);
            canvas.drawBitmap(backgroundImg, null, backgroundRect, paint);

            switch (gamePhase){
                case "night_roleRotate":
                    // rotateImg 表示
                    Rect rotateCardRect = new Rect(width * 15 /100,height * 20 / 100 ,width * 85 / 100 ,height *20 /100 + width * 70 / 100  * 1125 /938 );
                    //TODO cardRotate
                    //TODO roleImgを取ってくる:デフォルトで村人

                    canvas.drawBitmap(backCard, null, rotateCardRect, paint);
                    //timer実装


                    canvas.drawBitmap(roleImg,null,rotateCardRect,paint);
                    // confirm button
                    canvas.drawBitmap(buttonImg, null, confirmButtonRect, paint);
                    canvas.drawText("詳細確認", width * 25 / 100, height * 85 / 100, paint);

                    break;
                case "night_roleCheck":

                    //Rect宣言
                    Rect topFrameRect = new Rect(width * 5 /100,height * 5 / 100 ,width * 95 / 100 ,height * 50 /100);
                    Rect belowFrameRect = new Rect(width * 20 /100,height * 60 / 100 ,width * 80 / 100 ,height * 75 /100);
                    Rect roleCheckCardRect = new Rect(width * 42 /100,height * 10 / 100 ,width * 58 / 100 ,height * 10 /100 + width * 16/100 * 1125/938);

                    // canvasDraw
                    // 画面上部のテキスト情報
                    canvas.drawBitmap(frameImg,null,topFrameRect,paint);
                    String roleText = String.format("あなたの役職は「%s」です。%s","村人","役職のテキスト");

                    TextPaint mTextPaint = new TextPaint();
                    mTextPaint.setTextSize(30);
                    StaticLayout mTextLayout = new StaticLayout(roleText,mTextPaint,width*3/5, Layout.Alignment.ALIGN_NORMAL,1.0f,0.0f,false);
                    canvas.translate(width * 2 / 10, height * 25 / 100);//text の左上座標の指定

                    mTextLayout.draw(canvas);
                    canvas.restore();

                    canvas.drawBitmap(frameImg,null,belowFrameRect,paint);
                    canvas.drawBitmap(roleImg, null, roleCheckCardRect, paint);
                    // confirm button
                    canvas.drawBitmap(buttonImg,null,confirmButtonRect,paint);
                    canvas.drawText("初日夜へ", width * 25 / 100, height * 85 / 100, paint);


                    break;

                case "night_chat":
//                    MainActivity.drawChat(true);
                    canvas.drawBitmap(roleImg,null,roleCardRect,paint);
                    canvas.drawBitmap(timerFrameImg,null,timerRect,paint);
                    canvas.drawBitmap(buttonImg,null,actionButtonRect,paint);

                    String action = "占う";
                    // TODO 役職ごとに文字を変えるswitch文
                    canvas.drawText(action, width * 75 / 100, height * 10 / 100, paint);

                    // TODO Chat実装
                    // confirm button
                    canvas.drawBitmap(buttonImg, null, confirmButtonRect, paint);
                    canvas.drawText("次へ", width * 25 / 100, height * 85 / 100, paint);

                    break;

                case "morning":
                    // background
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.morning,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);

                    canvas.drawBitmap(roleImg,null,roleCardRect,paint);
                    canvas.drawBitmap(timerFrameImg,null,timerRect,paint);
                    Rect morningFrameRect = new Rect(width * 15 / 100,height * 40 / 100,width * 85 / 100 ,height * 60 / 100);
                    canvas.drawBitmap(frameImg,null,morningFrameRect,paint);

                    //TODO Text表示

                    // confirm button
                    canvas.drawBitmap(buttonImg, null, confirmButtonRect, paint);
                    canvas.drawText("次へ", width * 25 / 100, height * 85 / 100, paint);


                    break;
                case "afternoon_meeting":
                    // background
                    backgroundImg = decodeSampledBitmapFromResource(getResources(), R.drawable.afternoon, width, height);
                    canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);

                    roleImg = decodeSampledBitmapFromResource(getResources(),R.drawable.back_card,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(roleImg,null,roleCardRect,paint);
                    canvas.drawBitmap(timerFrameImg,null,timerRect,paint);

                    // confirm button
                    canvas.drawBitmap(buttonImg, null, confirmButtonRect, paint);
                    canvas.drawText("次へ", width * 25 / 100, height * 85 / 100, paint);

                    break;
                case "evening_voting":
                    // background
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.evening,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(backgroundImg,null,backgroundRect,paint);

                    // TODO List表示

                    // confirm button
                    canvas.drawBitmap(buttonImg, null, confirmButtonRect, paint);
                    canvas.drawText("次へ", width * 25 / 100, height * 85 / 100, paint);

                    break;
                case "excution":
                    // background
                    backgroundImg = decodeSampledBitmapFromResource(getResources(),R.drawable.evening,bitmapWidth,bitmapHeight);
                    canvas.drawBitmap(roleImg,null,roleCardRect,paint);
                    canvas.drawBitmap(timerFrameImg,null,timerRect,paint);

                    Rect excutionFrameRect = new Rect(width * 15 / 100,height * 20 / 100,width * 85 / 100 ,height * 80 / 100);
                    canvas.drawBitmap(frameImg,null,excutionFrameRect,paint);

                    // confirm button
                    canvas.drawBitmap(buttonImg, null, confirmButtonRect, paint);
                    canvas.drawText("次へ", width * 25 / 100, height * 85 / 100, paint);

                    break;
                case "gameover":
                    // confirm button
                    canvas.drawBitmap(buttonImg, null, confirmButtonRect, paint);
                    canvas.drawText("次へ", width * 25 / 100, height * 85 / 100, paint);

                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float pointX = event.getX();
        float pointY = event.getY();



        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(isSettingScene){
                    switch (settingPhase){
                        case "setting_menu":
                            if(roomSelectButtonRect.contains((int)pointX,(int)pointY)){
                                MainActivity.settingPhase = "room_select";
                                MainActivity.drawListView(true);
//                                MainActivity.sendEvent(this);
                            }else if(userSettingButtonRect.contains((int)pointX,(int)pointY)){
                                MainActivity.settingPhase = "user_setting";
                                MainActivity.sendEvent(this);
//                                MainActivity.drawListView(true);
//                                MainActivity.sendEvent(this);
                            }
                            break;
                        case "room_select":
                            if(confirmButtonRect.contains((int)pointX,(int)pointY)){
//                                MainActivity.settingPhase = "rule_confirm";
//                                MainActivity.scene = "game_scene";
                                //コメントアウト
                                MainActivity.settingPhase = "setting_menu";
                            }
                            break;
                        case "rule_confirm":
                            if(confirmButtonRect.contains((int)pointX,(int)pointY)){
//                                MainActivity.isSettingScene = true;
//                                MainActivity.isGameScene = true;
                            }

                            break;
                        default:
                            break;
                    }

                }else if(!isSettingScene && isGameScene){
                    if(confirmButtonRect.contains((int)pointX,(int)pointY)){
//                        MainActivity.goNextPhase();
                    }

                }
                break;

            default:
                return true;
        }
        if(!settingPhase.equals("rule_confirm")){
            invalidate();
        }

        return true;

    }

    private void setDialog(String dialogPattern){
//        MainActivity.onDialog = true;
//        MainActivity.dialogPattern = dialogPattern;
    }

    public static void setSameVariable(){
        isSettingScene = MainActivity.isSettingScene;
        isGameScene = MainActivity.isGameScene;
//        selectedPlayerId = MainActivity.selectedPlayerId;
        isFirstNight = MainActivity.isFirstNight;
        settingPhase = MainActivity.settingPhase;
        gamePhase = MainActivity.gamePhase;

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
