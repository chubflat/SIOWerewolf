package com.example.siowerewolf;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kazuaki on 2016/02/25.
 */
public class Utility {

    public static enum SeerMode{
        SeerModeNone, // なし
        SeerModeFree, // あり
        SeerModeRevelation, //お告げ
    }

    public static enum Winner{
        Village,
        Wolf,
        Fox,
    }

    public static enum Role{
        /* TODO 役職追加時 */
        Villager,
        Werewolf,
        Seer,
        Medium,
        Minion,
        Bodyguard,
        Mason,
        Fox,
        Cat,
        Fanatic,
        Immoralist,
        Lycan,
        ToughGuy,
        ApprenticeSeer,
        //        Wolfboy,
//        Trapmaker,
        Cursed,
        //        King,
//        Dictator,
        Beholder,
        Hunter,
        //        Mimic,

        Baker,
        Noble,
        Slave,
        Sorcerer,

    }

    public static Map<String,Object> getRoleInfo(Role role) {
        Map<String, Object> infoDic = new HashMap<String, Object>();
        String name = "";
        String token = "";
        String actionButtonText = "";
        String actionDialogText = "";
//        Boolean hasTable = false;
        Boolean hasTableFirst = false;
        Boolean hasAction = false;
        Boolean hasActionFirst = false;
        String explain = "";
        int winner = 0;
        int seerResult = 1; //人狼判定
        int mediumResult = 1;//人狼判定
        int isFinishCount = 1;//人間カウント
        int cardId = R.drawable.card0;

        //TODO 役職追加時
        switch (role) {
            case Villager:
                name = "村人";
                token = "村";
                hasAction = false;
                hasActionFirst = false;
                hasTableFirst = false;
                explain = "村人は特殊な能力を持たないただの人です。夜時間は考察を書きましょう。";
                // 判定
                winner = Utility.Winner.Village.ordinal();
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card0;
                break;

            case Werewolf:
                name = "人狼";
                token = "狼";
                hasAction = true;
                hasActionFirst = false;
                hasTableFirst = true;
                actionDialogText = "噛みます";
                actionButtonText = "噛む";
                explain = "人狼は毎晩仲間同士で相談し人間を一人噛むことができます。夜時間は狼専用チャットで相談し、代表者が襲撃先を選択します。";
                // 判定
                winner = Utility.Winner.Wolf.ordinal();
                seerResult = -1;
                mediumResult = -1;
                isFinishCount = -1;
                //tableString = "";//未実装
                //tableStringFirst = "";//未実装
                cardId = R.drawable.card1;
                break;

            case Seer:
                name = "占い師";
                token = "占";
                hasAction = true;
                hasActionFirst = true;
                hasTableFirst = false;
                actionDialogText = "占います";
                actionButtonText = "占う";
                explain = "予言者は毎晩疑っている人物を１人指定してその人物が人狼かそうでないかを知ることができます。";
                winner = Utility.Winner.Village.ordinal();
                //判定
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card2;
                break;

            case Medium:
                name = "霊媒師";
                token = "霊";
                hasAction = false;
                hasActionFirst = false;
                hasTableFirst = false;
                explain = "霊媒師は毎晩その日の昼のターンに処刑された人が人狼だったのかそうでなかったのかを知ることができます。";
                //判定
                winner = Utility.Winner.Village.ordinal();
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card3;
                break;

            case Minion:
                name = "狂人";
                token = "狂";
                hasAction = false;
                hasActionFirst = false;
                hasTableFirst = false;
                explain = "あなたは「狂人」です。能力はありませんが嘘をついて村を混乱させましょう。";
                // 判定
                winner = Utility.Winner.Wolf.ordinal();
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card4;
                break;

            case Bodyguard:
                name = "狩人";
                token = "狩";
                hasAction = true;
                hasActionFirst = false;
                hasTableFirst = false;
                actionDialogText = "守ります";
                actionButtonText = "守る";
                explain = "狩人は毎晩誰かを一人指定してその人物を人狼の襲撃から守ります。ただし、自分自身を守ることはできません。";
                // 判定
                winner = Utility.Winner.Village.ordinal();
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card5;
                break;

            case Mason:
                name = "共有者";
                token = "共";
                hasAction = false;
                hasActionFirst = false;
                hasTableFirst = true;
                explain = "共有者は必ず複数人で一組として存在し、お互いに相手を確認できます。夜時間には共有者専用チャットを使用できます。";
                //判定
                winner = Utility.Winner.Village.ordinal();
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card6;
                break;
            case Fox:
                name = "妖狐";
                token = "狐";
                hasAction = false;
                hasActionFirst = false;
                hasTableFirst = true;
                explain = "妖狐は第３の勢力です。ゲームが終了した時に妖狐が生き残っていれば勝利します。妖狐同士は夜時間に専用チャットを使用できます。";
                //判定
                winner = Utility.Winner.Fox.ordinal();
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 0;
                cardId = R.drawable.card7;
                break;

            case Cat:
                name = "猫又";
                token = "猫";
                hasAction = false;
                hasActionFirst = false;
                explain = "猫又は村人陣営です。猫又は昼の会議で処刑された場合、生存者の中からランダムで一人を道連れにします。夜に人狼に噛まれた場合は、噛んだ人狼を道連れにします。";
                //判定
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card8;
                break;
            case Fanatic:
                name = "狂信者";
                token = "信";
                hasAction = false;
                hasActionFirst = false;
                hasTableFirst = true;
                explain = "狂信者は初日の夜に誰が人狼かがわかります。人狼が勝利するように場を混乱させましょう。占い結果、霊媒結果は人間と判定されます。";
                //判定
                winner = Utility.Winner.Wolf.ordinal();
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card9;
                break;

            case Immoralist:
                name = "背徳者";
                token = "背";
                hasAction = false;
                hasActionFirst = false;
                hasTableFirst = true;
                explain = "背徳者は妖狐陣営です。ゲーム終了時に妖狐が生き残っていれば、勝利します。背徳者は「妖狐」が誰かを知ることができ、妖狐が全滅した際、後を追って死亡します。その場合、後追い自殺として表示されます。";
                //判定
                winner = Utility.Winner.Fox.ordinal();
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card10;
                break;

            case Lycan:
                name = "狼憑き//未実装";
                hasAction = false;
                hasActionFirst = false;
                explain = "";
                //判定
                seerResult = -1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card8;
                break;

            case ToughGuy:
                name = "タフガイ//未実装";
                hasAction = false;
                hasActionFirst = false;
                explain = "";
                //判定
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card9;
                break;
            case ApprenticeSeer:
                name = "見習い予言者//未実装";
                hasAction = false;
                hasActionFirst = false;
                explain = "";
                //判定
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card10;
                break;

//            case Wolfboy:
//                name = "狼少年//未実装";
//                hasAction = true;
//                hasActionFirst = false;
//                explain = "";
//                //判定
//                seerResult = 1;
//                mediumResult = 1;
//                isFinishCount = 1;
//                cardId = R.drawable.card11;
//            break;

//            case Trapmaker:
//                name = "罠師//未実装";
//                hasAction = false;
//                hasActionFirst = false;
//                explain = "";
//                //判定
//                seerResult = 1;
//                mediumResult = 1;
//                isFinishCount = 1;
//                cardId = R.drawable.card12;
//            break;

            case Cursed:
                name = "呪われた者//未実装";
                hasAction = false;
                hasActionFirst = false;
                explain = "";
                //判定
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card0;//村人カード
                break;

//            case King:
//                name = "王様//未実装";
//                hasAction = false;
//                hasActionFirst = false;
//                explain = "";
//                //判定
//                seerResult = 1;
//                mediumResult = 1;
//                isFinishCount = 1;
//                cardId = R.drawable.card14;
//                break;

//            case Dictator:
//                name = "独裁者//未実装";
//                hasAction = false;
//                hasActionFirst = false;
//                explain = "";
//                //判定
//                seerResult = 1;
//                mediumResult = 1;
//                isFinishCount = 1;
//                cardId = R.drawable.card15;
//                break;

            case Beholder:
                name = "予言者のママ//未実装";
                hasAction = false;
                hasActionFirst = true;
                explain = "";
                //判定
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card16;
                break;

            case Hunter:
                name = "ハンター//未実装";
                hasAction = false;
                hasActionFirst = false;
                explain = "";
                //判定
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card17;
                break;

//            case Mimic:
//                name = "ものまね師//未実装";
//                hasAction = false;
//                hasActionFirst = false;
//                explain = "";
//                //判定
//                seerResult = 1;
//                mediumResult = 1;
//                isFinishCount = 1;
//                cardId = R.drawable.card18;
//                break;

            case Baker:
                name = "パン屋//未実装";
                hasAction = false;
                hasActionFirst = false;
                explain = "";
                //判定
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card22;
                break;

            case Noble:
                name = "貴族//未実装";
                hasAction = false;
                hasActionFirst = false;
                explain = "";
                //判定
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card23;
                break;

            case Slave:
                name = "奴隷//未実装";
                hasAction = false;
                hasActionFirst = false;
                explain = "";
                //判定
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card24;
                break;

//            case Sorcerer:
//                name = "人狼占い//未実装";
//                hasAction = false;
//                hasActionFirst = false;
//                explain = "";
//                //判定
//                seerResult = 1;
//                mediumResult = 1;
//                isFinishCount = 1;
//                cardId = R.drawable.card25;//カードなし
//                break;

            default:
                name = "//未実装";
                hasAction = false;
                hasActionFirst = false;
                explain = "";
                //判定
                seerResult = 1;
                mediumResult = 1;
                isFinishCount = 1;
                cardId = R.drawable.card0;
                break;
        }
        infoDic.put("name", name);
        infoDic.put("token",token);
        infoDic.put("explain", explain);
        infoDic.put("hasAction", hasAction);
        infoDic.put("hasActionFirst", hasActionFirst);
        infoDic.put("hasTableFirst",hasTableFirst);
        infoDic.put("actionButtonText",actionButtonText);
        infoDic.put("actionDialogText",actionDialogText);
        infoDic.put("winner",winner);
        infoDic.put("seerResult",seerResult);
        infoDic.put("mediumResult",mediumResult);
        infoDic.put("isFinishCount",isFinishCount);
        infoDic.put("cardId",cardId);
//        infoDic.put("tableString", tableString);
//        infoDic.put("tableStringFirst", tableStringFirst);

        return infoDic;
    }

    public static int getMaxRoleCount(){
        int maxCount = 11;// 背徳者まで実装;
        return maxCount;
    }
}
