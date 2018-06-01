package test.zx.learnwords;

/**
 * Created by THink on 2018/1/4.
 */

import java.io.FileWriter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DBInfo {
    private DataBase dataBase;
    private String configFileName=Constant.CONFIG_FILE_NAME;
    public static String path;
    private boolean successFlag=false;
    private DateConfirm dateConfirm;
    public DBInfo(){
        this.dataBase=new DataBase();
        this.dateConfirm=new DateConfirm();
    }
    public DataBase getDataBase() {
        return dataBase;
    }

    public void setDataBase(DataBase dataBase) {
        this.dataBase = dataBase;
    }
    public void checkConfigFile(){

        path=Environment.getExternalStorageDirectory()+"/daughter/";
        Log.d("Debug",path);
        File dir=new File(path);
        if(!dir.exists())
            dir.mkdir();
        File file=new File(path,configFileName);
//        if(file.exists())
//            file.delete();
        if(file.exists() && file.length()!=0)
            return;
        if(!file.exists()){
            try {
                if(!file.createNewFile()){
                    Log.d("Debug", "fail to create config file");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeToJsonFile(Constant.FILE_CONTENT);

    }
    synchronized void writeToJsonFile(String context){
        File file=new File(path,configFileName);
        FileWriter fileWriter = null;
        try {
            fileWriter=new FileWriter(file,false);
            fileWriter.write(context);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileWriter!=null){
                try {
                    fileWriter.close();
                    Log.d("Debug","Create config file successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void iniConfigInfo() throws IOException {
        FileReader fileReader=null;
        File file=new File(path,configFileName);
        StringBuffer content=new StringBuffer();
        char[] buf;
        int len=0;
        try {
            len=(int) file.length();
            if(len==0){
                Log.d("Debug", "Config file is null");
                setConfigState(false);
            }
            buf=new char[len];
            fileReader=new FileReader(file);
            // get info from the format of Json
            fileReader.read(buf,0,len);
            content.append(buf,0,len);
            try {
                jsonDataAnalyse(content.toString());
            } catch (JSONException e) {
                writeToJsonFile(Constant.FILE_CONTENT);
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(fileReader!=null){
                try {
                    fileReader.close();
                    setConfigState(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    void setConfigState(boolean flag){
        successFlag=flag;
    }
    boolean getConfigState(){
        return successFlag;
    }
    void jsonDataAnalyse(String jsonContent) throws JSONException {
        int faultCount=0;
        String faultWords="";
        JSONObject jsonObject=new JSONObject(jsonContent);
        JSONArray historicWordsArray=jsonObject.getJSONArray("historicWords");
        int len=historicWordsArray.length();
        HistoricWords historicWords=new HistoricWords();
        String[] words=new String[len];
        for(int i=0;i<len;i++){
            words[i]=historicWordsArray.getString(i);
        }
        historicWords.setWords(words);
        historicWords.setWordsNum(len);
        dataBase.setHistoricWords(historicWords);
        JSONArray historicTestArray=jsonObject.getJSONArray("historicTest");
        len=historicTestArray.length();
        dataBase.setTestDayCount(len);
        HistoricTest[] historicTest=new HistoricTest[len];  //只是申请了引用
        for(int i=0;i<len;i++){
            JSONObject object=historicTestArray.getJSONObject(i);
            historicTest[i]=new HistoricTest();    //需要继续申请空间
            historicTest[i].setTime(object.getString("time"));
            JSONArray arrayWords=object.getJSONArray("words");
            int lenArrayWords=arrayWords.length();
            words=new String[lenArrayWords];
            historicTest[i].setWordsNum(lenArrayWords);
            for(int j=0;j<lenArrayWords;j++){
                words[j]=arrayWords.getString(j);
            }
            historicTest[i].setWords(words);
            JSONArray arrayFPosition=object.getJSONArray("fPosition");
            int lenFPosition=arrayFPosition.length();
            int[] position=new int[lenFPosition];
            historicTest[i].setfPositionNum(lenFPosition);
            faultCount=faultCount+lenFPosition;
            for(int k=0;k<lenFPosition;k++){
                position[k]=arrayFPosition.getInt(k);
                faultWords=faultWords+historicTest[i].getWords()[position[k]-1];
            }
            historicTest[i].setfPosition(position);
        }
        dataBase.setHistoricTest(historicTest);
        dataBase.setTestFaultWordsCount(faultCount);
        dataBase.setTestFaultWordsDetail(faultWords);
        JSONObject object=jsonObject.getJSONObject("newWords");
        NewWords newWords = new NewWords();
        newWords.setTime(object.getString("time"));
        JSONArray arrayWords = object.getJSONArray("words");
        int lenArrayWords = arrayWords.length();
        newWords.setWordsNum(lenArrayWords);
        words = new String[lenArrayWords];
        for (int j = 0; j < lenArrayWords; j++) {
            words[j] = arrayWords.getString(j);
        }
        newWords.setWords(words);
        dataBase.setNewWords(newWords);
        boolean newDate=dateConfirm.checkIfNewDate(object.getString("time"));
        if(newDate)
            clearAllNewWordsAndSetNewTime();
    }
    void writeNewWord(String word){
        int num;
        String[] newWords ;
        String[] temp;
        num=dataBase.getNewWords().getWordsNum();
        temp=dataBase.getNewWords().getWords();
        newWords=new String[num+1];
        for(int i=0;i<num;i++){
            newWords[i]=temp[i];
        }
        newWords[num]=word;
        dataBase.getNewWords().setWordsNum(num+1);
        dataBase.getNewWords().setWords(newWords);
    }
    void  writeHistoricWords(String text){
        int len;
        HistoricWords historicWords;
        len=dataBase.getHistoricWords().getWordsNum();
        String[] newString=new String[len+1];
        historicWords=dataBase.getHistoricWords();
        for(int i=0;i<len;i++){
            newString[i]=historicWords.getWords()[i];
        }
        newString[len]=text;
        historicWords.setWords(newString);
        historicWords.setWordsNum(len+1);
    }
    boolean checkIncreaseHistoricNeeded(String text){
        int len;
        HistoricWords historicWords;
        len=dataBase.getHistoricWords().getWordsNum();
        historicWords=dataBase.getHistoricWords();
        for(int i=0;i<len;i++){
            if(historicWords.getWords()[i].equals(text)){ //必须用equals()来比较，否则是比较地址和对象，永远为false
                return false;
            }
        }
        return true;
    }
    String createNewJsonContents() throws JSONException {
        JSONObject root=new JSONObject();
        JSONArray historicWords = new JSONArray();
        HistoricWords words=dataBase.getHistoricWords();
        int len=words.getWordsNum();
        for(int i=0;i<len;i++){
            historicWords.put(words.getWords()[i]);  //生成字符数组
        }
        root.put("historicWords",historicWords);

        JSONArray historicTest=new JSONArray();
        HistoricTest[] test=dataBase.getHistoricTest();
        len=dataBase.getTestDayCount();
        for(int i=0;i<len;i++) {
            JSONObject eachWords=new JSONObject();
            JSONArray testWordsArray=new JSONArray();
            int wordsNum = test[i].getWordsNum();
            for (int j = 0; j <wordsNum;j++){
                testWordsArray.put(test[i].getWords()[j]); //words
            }
            JSONArray fPosition=new JSONArray();  //false position
            int fpNum=test[i].getfPositionNum();
            for(int j=0;j<fpNum;j++){
                fPosition.put(test[i].getfPosition()[j]);
            }
            eachWords.put("time",test[i].getTime());
            eachWords.put("words",testWordsArray);
            eachWords.put("fPosition",fPosition);
            historicTest.put(eachWords);
        }
        root.put("historicTest",historicTest);

        JSONObject newWords=new JSONObject();
        len=dataBase.getNewWords().getWordsNum();
        newWords.put("time",dataBase.getNewWords().getTime());
        JSONArray eachWords=new JSONArray();
        for(int i=0;i<len;i++) {
            eachWords.put(dataBase.getNewWords().getWords()[i]);
        }
        newWords.put("words",eachWords);
        root.put("newWords",newWords);
        return root.toString();
    }
    public void updateJsonFile(){
        String context;

        path=Environment.getExternalStorageDirectory()+"/daughter/";
        File dir=new File(path);
        if(!dir.exists())
            dir.mkdir();
        File file=new File(path,configFileName);
        if(!file.exists()){
            try {
                if(!file.createNewFile()){
                    Log.d("Debug", "fail to create config file");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writeToJsonFile(createNewJsonContents());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void clearAllNewWordsAndSetNewTime(){
        dataBase.getNewWords().setWordsNum(0);
        dataBase.getNewWords().setTime(dateConfirm.getDateAndMon());
    }
    public void deleteOneNewWords(int position){
        String[] str=dataBase.getNewWords().getWords();
        int wordsLen=dataBase.getNewWords().getWordsNum()-1;
        for(int i=position;i<wordsLen;i++){
            str[i]=str[i+1];
        }

        dataBase.getNewWords().setWords(str);
        dataBase.getNewWords().setWordsNum(wordsLen);
    }
    public int deleteOneHistoricWords(String text){
        String[] str;
        int len=dataBase.getHistoricWords().getWordsNum();
        str=dataBase.getHistoricWords().getWords();
        int deletePoint=len-1;
        for(int i=0;i<len;i++){
            if(str[i].equals(text)){
                deletePoint=i;
                break;
            }
        }
        len=len-1;
        for(int i=deletePoint;i<len;i++){
            str[i]=(str[i+1]);
        }
        dataBase.getHistoricWords().setWordsNum(len);
        return deletePoint;
    }
    public void createTestWords(){
        String[] testWord=new String[Constant.MAX_TEST_WORDS_EACH_DAY];
        List<String> list=createRandom();
        for(int i=0;i<Constant.MAX_TEST_WORDS_EACH_DAY;i++){
            testWord[i]=list.get(i);
        }
        int count=dataBase.getTestDayCount();
        int newCount=count+1;
        HistoricTest[] historicTests=new HistoricTest[newCount];
        for(int i=0;i<count;i++)
            historicTests[i]=dataBase.getHistoricTest()[i];
        historicTests[count]=new HistoricTest(); //申请空间，很重要
        historicTests[count].setTime(dateConfirm.getDateAndMon());
        historicTests[count].setWords(testWord);
        historicTests[count].setWordsNum(Constant.MAX_TEST_WORDS_EACH_DAY);
        dataBase.setHistoricTest(historicTests);
        dataBase.setTestDayCount(newCount);
    }
    ArrayList<String> createRandom(){
        String[] test=new String[12];
        int testTimes=dataBase.getTestDayCount();
        for(int i=0;i<12;i++)
            test[i]= dataBase.getHistoricTest()[testTimes-1-i/4].getWords()[i%4];
        int position=0;
        int max=Constant.MAX_TEST_WORDS_EACH_DAY;
        ArrayList<String> testWords=new ArrayList<>();
        ArrayList<String> list=new ArrayList<>();
        String[] historicWord=dataBase.getHistoricWords().getWords();
        int count=dataBase.getHistoricWords().getWordsNum();
        for(int i=0;i<count;i++)
            list.add(historicWord[i]);
        for(int j=0;j<12;j++) {
            for (int i = 0; i < (count - j); i++)
            {
                if(test[j] == list.get(i)){
                    list.remove(i);
                    break;
                }
            }
        }
        for(int i=0;i<max;i++) {
            position = (int) (Math.random() * (count-12));
            testWords.add(list.get(position));
            list.remove(position);
            count--;
        }
        return testWords;

    }
    void setFaultWords(int position,String word,String time){
        int len=dataBase.getTestDayCount();
        int temp=0,currentNo=0;
        for(int i=0;i<len;i++) {
            temp = dataBase.getHistoricTest()[i].getWordsNum();
            if((position - temp) > -1)
                position = position - temp;
            else {
                currentNo=i;
                break;
            }
        }
        len=dataBase.getHistoricTest()[currentNo].getfPositionNum()+1;
        int[] sPosition=new int[len];
        for(int i=0;i<(len-1);i++)
            sPosition[i]=dataBase.getHistoricTest()[currentNo].getfPosition()[i];
        sPosition[len-1]=position+1;
        dataBase.getHistoricTest()[currentNo].setfPosition(sPosition);
        dataBase.getHistoricTest()[currentNo].setfPositionNum(len);
        dataBase.setTestFaultWordsCount(dataBase.getTestFaultWordsCount()+1);
        dataBase.setTestFaultWordsDetail(dataBase.getTestFaultWordsDetail()+word);
    }
}
class DataBase{
    private HistoricWords historicWords;
    private HistoricTest[] historicTest;
    private NewWords newWords;
    private int testDayCount;
    private int testFaultWordsCount;

    public String getTestFaultWordsDetail() {
        return testFaultWordsDetail;
    }

    public void setTestFaultWordsDetail(String testFaultWordsDetail) {
        this.testFaultWordsDetail = testFaultWordsDetail;
    }

    private String testFaultWordsDetail;

    public int getTestDayCount() {
        return testDayCount;
    }
    public void setTestDayCount(int testDayCount) {
        this.testDayCount = testDayCount;
    }
    public HistoricTest[] getHistoricTest() {
        return historicTest;
    }
    public void setHistoricTest(HistoricTest[] historicTest) {
        this.historicTest = historicTest;
    }
    public HistoricWords getHistoricWords() {
        return historicWords;
    }

    public void setHistoricWords(HistoricWords historicWords) {
        this.historicWords = historicWords;
    }
    public int getTestFaultWordsCount() {
        return testFaultWordsCount;
    }
    public void setTestFaultWordsCount(int testFaultWordsCount) {
        this.testFaultWordsCount = testFaultWordsCount;
    }
    public NewWords getNewWords() {
        return newWords;
    }

    public void setNewWords(NewWords newWords) {
        this.newWords = newWords;
    }
}
class HistoricWords{
    public int getWordsNum() {
        return wordsNum;
    }

    public void setWordsNum(int wordsNum) {
        this.wordsNum = wordsNum;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    private int wordsNum;
    private String[] words;

}
class HistoricTest{
    private String time;
    private String[] words;
    private int[] fPosition;
    private int wordsNum;
    private int fPositionNum;
    public int getWordsNum() {
        return wordsNum;
    }

    public void setWordsNum(int wordsNum) {
        this.wordsNum = wordsNum;
    }

    public int getfPositionNum() {
        return fPositionNum;
    }

    public void setfPositionNum(int fPositionNum) {
        this.fPositionNum = fPositionNum;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    public int[] getfPosition() {
        return fPosition;
    }

    public void setfPosition(int[] fPosition) {
        this.fPosition = fPosition;
    }
}
class NewWords{
    private String time;
    private String[] words;
    private int wordsNum;
    public int getWordsNum() {
        return wordsNum;
    }

    public void setWordsNum(int wordsNum) {
        this.wordsNum = wordsNum;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }
}