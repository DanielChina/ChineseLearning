package test.zx.learnwords;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by THink on 2018/1/10.
 */

public class KeyboardInfo {
    private MainActivity activity;
    public KeyboardInfo(MainActivity activity){
        this.activity=activity;
    }
    void hideInput(){
        InputMethodManager imm=(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
    }
    void showInput(){
        InputMethodManager imm=(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
    }
}
