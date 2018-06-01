package test.zx.learnwords;

/**
 * Created by THink on 2018/1/4.
 */

public class Constant {
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    public static final int NEW_WORDS=0;
    public static final int HISTORIC_WORDS=1;
    public static final int HISTORIC_TEST=2;
    public final static String FILE_CONTENT="{\"historicWords\":[\"兰\",\"云\",\"伯\",\"叶\",\"座\",\"飞\",\"广\",\"早\",\"坐\",\"叭\",\"弟\",\"弓\",\"平\",\"哥\",\"家\",\"乐\",\"牙\",\"我\",\"丫\",\"又\",\"儿\",\"子\",\"上\",\"下\",\"小\",\"大\",\"太\",\"翟\",\"艺\",\"可\",\"土\",\"月\",\"日\",\"目\",\"几\",\"米\",\"朵\",\"木\",\"山\",\"出\",\"多\",\"少\",\"水\",\"左\",\"右\",\"女\",\"中\",\"好\",\"兄\",\"父\",\"头\",\"个\",\"天\",\"丁\",\"鱼\",\"心\",\"不\",\"火\",\"田\",\"虫\",\"口\",\"牛\",\"里\",\"巨\",\"爸\",\"马\",\"手\",\"国\",\"鸟\",\"东\",\"西\",\"红\",\"人\",\"一\",\"二\",\"三\",\"呵\",\"五\",\"六\",\"十\",\"七\",\"九\",\"八\",\"四\",\"工\",\"炬\",\"巴\",\"妈\",\"河\",\"冰\",\"汪\",\"旺\",\"凸\",\"凹\",\"娃\",\"朋\",\"友\",\"林\",\"森\",\"匹\",\"本\",\"字\",\"品\",\"开\",\"卜\",\"明\",\"阳\",\"才\",\"何\",\"厂\",\"爪\",\"瓜\",\"立\",\"江\",\"区\",\"白\",\"鸡\",\"鹅\",\"去\",\"阿\",\"草\",\"灿\",\"学\",\"习\",\"羽\"],\"historicTest\":[{\"time\":\"01-01\",\"words\":[\"丫\",\"朵\",\"广\",\"伯\",\"弟\",\"平\"],\"fPosition\":[1]},{\"time\":\"02-01\",\"words\":[\"家\",\"山\",\"座\",\"太\",\"下\",\"叭\"],\"fPosition\":[]},{\"time\":\"03-01\",\"words\":[\"广\",\"目\",\"叶\",\"多\",\"旱\",\"牙\"],\"fPosition\":[]},{\"time\":\"04-01\",\"words\":[\"我\",\"水\",\"米\",\"哥\"],\"fPosition\":[]},{\"time\":\"05-01\",\"words\":[\"坐\",\"鱼\",\"天\",\"兰\"],\"fPosition\":[]},{\"time\":\"08-01\",\"words\":[\"日\",\"心\",\"乐\",\"丫\"],\"fPosition\":[]},{\"time\":\"09-01\",\"words\":[\"目\",\"左\",\"里\",\"兄\"],\"fPosition\":[2]},{\"time\":\"15-01\",\"words\":[\"丁\",\"父\",\"二\",\"丫\"],\"fPosition\":[]},{\"time\":\"16-01\",\"words\":[\"二\",\"牛\",\"坐\",\"虫\"],\"fPosition\":[]},{\"time\":\"17-01\",\"words\":[\"叶\",\"妈\",\"巨\",\"儿\",\"何\",\"多\",\"土\",\"手\"],\"fPosition\":[]},{\"time\":\"18-01\",\"words\":[\"阳\",\"丁\",\"哥\",\"个\"],\"fPosition\":[]},{\"time\":\"19-01\",\"words\":[\"头\",\"天\",\"河\",\"出\"],\"fPosition\":[]},{\"time\":\"22-01\",\"words\":[\"卜\",\"炬\",\"出\",\"头\"],\"fPosition\":[]}],\"newWords\":{\"time\":\"22-01\",\"words\":[\"草\",\"灿\",\"学\",\"习\",\"羽\"]}}";
    public static final int MIN_WORDS_EACH_DAY=5;
    public static final int MAX_TEST_WORDS_EACH_DAY=4;
    public static final String BASE_URL="http://192.168.1.69:8080/MyFirstWebApp/";
    public static final String CONFIG_FILE_NAME="config";
}
