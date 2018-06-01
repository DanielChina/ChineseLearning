package test.zx.learnwords;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by THink on 2018/1/6.
 */

public class NewWordsItem {
    private EditText mEtInputNewWords;
    private View view;
    private Button mBtConfirmInput;
    private MainActivity activity;
    private MyGridView myGridView;
    public NewWordsItem(MyGridView myGridView,View view,MainActivity activity ){
        this.myGridView=myGridView;
        this.view=view;
        this.activity=activity;
    }
    void iniNewWordsComponent() {
        mEtInputNewWords = (EditText) view.findViewById(R.id.input_new_text);
        mBtConfirmInput=(Button) view.findViewById(R.id.confirm_input);
        mBtConfirmInput.setOnClickListener(new View.OnClickListener() {
            String text;
            @Override
            public void onClick(View v) {
                boolean increaseNeeded=false;
                text=mEtInputNewWords.getText().toString();
                Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
                Matcher m = p.matcher(text);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEtInputNewWords.setText(null);
                    }
                });

                if(m.matches()){
                    activity.getKeyboardInfo().hideInput();
                    increaseNeeded=activity.getDbInfo().checkIncreaseHistoricNeeded(text);
                    if(increaseNeeded){
                        activity.getDbInfo().writeNewWord(text);
                        activity.getDbInfo().writeHistoricWords(text);
                        myGridView.increaseNewWords(activity.getDbInfo());
                        myGridView.increaseHistoricWords(activity.getDbInfo());
                    }
                    else{
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "这是已学字，请重新输入", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{ //输入内容非中文
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "请输入中文", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
