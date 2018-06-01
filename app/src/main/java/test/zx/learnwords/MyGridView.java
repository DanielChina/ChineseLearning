package test.zx.learnwords;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by THink on 2018/1/5.
 */

public class MyGridView{
    public GridView getGridNewWords() {
        return gridNewWords;
    }

    public void setGridNewWords(GridView gridNewWords) {
        this.gridNewWords = gridNewWords;
    }

    private GridView gridNewWords,gridHistoricTest,gridHistoricWord;
    private DateConfirm dateConfirm=new DateConfirm();
    private View view[]=new View[3];
    private SimpleAdapter simpleAdapter;
    private FragmentActivity fragmentActivity;
    private MainActivity activity;
    private KeyboardInfo keyboardInfo;
    private LayoutInflater inflater;
    private List<Map<String,String>> listNewWords=new ArrayList<>();
    private List<Map<String,String>> listTestWords=new ArrayList<>();
    private List<Map<String,String>> listHistoricWords=new ArrayList<>();
    public MyGridView(LayoutInflater inflater,View[] view, FragmentActivity fragmentActivity, MainActivity activity){
        this.fragmentActivity=fragmentActivity;
        this.view=view;
        this.activity=activity;
        this.inflater=inflater;
    }
    void iniGridView(){
        iniGridNewWords();
        iniGridTest();
        iniGridHistoric();

    }
    public void iniGridNewWords(){
        gridNewWords = (GridView) view[Constant.NEW_WORDS].findViewById(R.id.grid_new_words);
        simpleAdapter = new SimpleAdapter(fragmentActivity, getData(activity.getDbInfo(),Constant.NEW_WORDS), R.layout.item_new_words,
                new String[]{"word", "time"},
                new int[]{R.id.new_words, R.id.new_words_time});
        gridNewWords.setAdapter(simpleAdapter);
    }
    private void iniGridTest(){
        gridHistoricTest = (GridView) view[Constant.HISTORIC_TEST].findViewById(R.id.grid_historic_test);
        simpleAdapter = new SimpleAdapter(fragmentActivity, getData(activity.getDbInfo(),Constant.HISTORIC_TEST), R.layout.item_historic_test,
                new String[]{"word", "time"},
                new int[]{R.id.historic_test, R.id.historic_test_time});
        gridHistoricTest.setAdapter(simpleAdapter);
    }
    private void iniGridHistoric(){
        gridHistoricWord = (GridView) view[Constant.HISTORIC_WORDS].findViewById(R.id.grid_historic_words);
        simpleAdapter = new SimpleAdapter(fragmentActivity, getData(activity.getDbInfo(),Constant.HISTORIC_WORDS), R.layout.item_historic_words,
                new String[]{"word", "time"},
                new int[]{R.id.historic_words, R.id.historic_words_time});
        gridHistoricWord.setAdapter(simpleAdapter);
    }
    boolean currentGridEmpty(int position){
        String str=listNewWords.get(position).get("word");
        if(str.equals(""))
            return true;
        else
            return false;
    }
    protected void setGridViewListener() {
        keyboardInfo=new KeyboardInfo(activity);
        final DialogInfo dialogInfo=new DialogInfo(MyGridView.this,activity);
        gridNewWords.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(!currentGridEmpty(position)){
                    dialogInfo.deleteCurrentWords(position);
                }
                return false;
            }
        });
        gridNewWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                keyboardInfo.showInput();
            }
        });
        gridNewWords.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        gridHistoricTest.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String time=listTestWords.get(position).get("time");
                if(!time.equals(dateConfirm.getDateAndMon())){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "非当天不可修改测试记录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    String word = listTestWords.get(position).get("word");
                    String words=activity.getDbInfo().getDataBase().getTestFaultWordsDetail();
                    if(words.indexOf(word)!=-1){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "该字已设为错字", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else
                        dialogInfo.setCurrentTestWords(position,word,time);
                }
                return false;
            }
        });
        gridHistoricTest.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        gridHistoricWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //add code to complish the click event
            }
        });
        gridHistoricWord.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }
    public void emptyNewWordsGrid(){
        int len=listNewWords.size();
        String time=activity.getDbInfo().getDataBase().getNewWords().getTime();
        for(int i=0;i<len;i++){
            Map<String,String> map=new HashMap<String,String>();
            map.put("time",time);
            map.put("word","");
            listNewWords.set(i,map);
        }
    }
    public List<Map<String,String>> getData(DBInfo dbInfo,int type){
        if(type==Constant.NEW_WORDS){
            String time=dbInfo.getDataBase().getNewWords().getTime();
            int len=dbInfo.getDataBase().getNewWords().getWordsNum();
            int num=Math.max(len,Constant.MIN_WORDS_EACH_DAY);
            for (int i = 0; i < num; i++) {
                Map<String, String> map = new HashMap<String, String>();
                if(i>=len)
                    map.put("word","");
                else {
                    map.put("word"," "+dbInfo.getDataBase().getNewWords().getWords()[i]);
                }
                map.put("time", time);
                listNewWords.add(map);   //add操作是引用，所以要不停的new
            }
            return listNewWords;
        }else if(type==Constant.HISTORIC_WORDS){
            int len=dbInfo.getDataBase().getHistoricWords().getWordsNum();
            for(int i=0;i<len;i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("word", dbInfo.getDataBase().getHistoricWords().getWords()[i]);
                map.put("time", "");
                listHistoricWords.add(map);   //add操作是引用，所以要不停的new
            }
            return listHistoricWords;
        } else {
            int len=dbInfo.getDataBase().getTestDayCount();
            Map<String, String> map;
            String time;
            for(int i=0;i<len;i++){
                int lenWords=dbInfo.getDataBase().getHistoricTest()[i].getWordsNum();
                time=dbInfo.getDataBase().getHistoricTest()[i].getTime();
                for(int j=0;j<lenWords;j++) {
                    map = new HashMap<String, String>();
                    map.put("word", dbInfo.getDataBase().getHistoricTest()[i].getWords()[j]);
                    map.put("time", time);
                    listTestWords.add(map);
                }
            }
            return listTestWords;
        }
    }
    public void increaseNewWords(DBInfo dbInfo){
        int increaseNum,len,oldLen;
        Map<String, String> map;
        String time;
        len=listNewWords.size();
        oldLen=len;
        for(int i=0;i<len;i++) {
            if(listNewWords.get(i).get("word").equals("")){
                oldLen = i;
                break;
            }
        }
        int newLen=dbInfo.getDataBase().getNewWords().getWordsNum();
        increaseNum=newLen-oldLen;
        time=dateConfirm.getDateAndMon();
        if(increaseNum>0){
            for(int i=0;i<increaseNum;i++){
                map = new HashMap<String, String>();
                map.put("word", " "+dbInfo.getDataBase().getNewWords().getWords()[oldLen+i]);
                map.put("time", time);
                if((oldLen+i)<listNewWords.size()){
                    listNewWords.set(oldLen+i,map);
                } else{
                    listNewWords.add(map);   //add操作是引用，所以要不停的new
                }
            }
            updateGridView(gridNewWords);
        }
    }
    public void increaseHistoricWords(DBInfo dbInfo){
        int len,increaseLen;
        Map<String, String> map;
        len=dbInfo.getDataBase().getHistoricWords().getWordsNum();
        int oldLen=listHistoricWords.size();
        increaseLen=len-oldLen;
        if(increaseLen>0){
            for(int i=0;i<increaseLen;i++) {
                map = new HashMap<String, String>();
                map.put("word", dbInfo.getDataBase().getHistoricWords().getWords()[oldLen+i]);
                map.put("time", "");
                listHistoricWords.add(map);
            }
            updateGridView(gridHistoricWord);
        }
    }
    public void increaseHistoricTest(DBInfo dbInfo) {
        int len=dbInfo.getDataBase().getTestDayCount();
        Map<String, String> map;
        String time=dbInfo.getDataBase().getHistoricTest()[len-1].getTime();
        for(int i=0;i<Constant.MAX_TEST_WORDS_EACH_DAY;i++){
            String str=dbInfo.getDataBase().getHistoricTest()[len-1].getWords()[i];
            map=new HashMap<>();
            map.put("word",str);
            map.put("time",time);
            listTestWords.add(map);
        }
        updateGridView(gridHistoricTest);
    }
    void updateGridView(GridView gridView){
        final GridView view=gridView;
        fragmentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SimpleAdapter simpleAdapter=(SimpleAdapter)view.getAdapter();
                simpleAdapter.notifyDataSetChanged();
            }
        });
    }

    String removeOneNewWords(int position){
        String text;
        Map<String,String> map;
        map=listNewWords.get(position);
        text=activity.getDbInfo().getDataBase().getNewWords().getWords()[position];
        map.put("word","");
        listNewWords.remove(position);
        if(listNewWords.size()<Constant.MIN_WORDS_EACH_DAY)
            listNewWords.add(map);
        updateGridView(gridNewWords);
        return text;
    }
    void removeOneHistoricWords(int position){
        listHistoricWords.remove(position);
        updateGridView(gridHistoricWord);
    }
}
