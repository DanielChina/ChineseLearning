package test.zx.learnwords;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.AlertDialogLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

/**
 * Created by THink on 2018/1/9.
 */

public class DialogInfo {
    private int position;
    private MyGridView myGridView;
    private MainActivity activity;
    public DialogInfo(MainActivity activity){
        this.activity=activity;
    }
    public DialogInfo(MyGridView myGridView,MainActivity activity){
        this.position=position;
        this.myGridView=myGridView;
        this.activity=activity;
    }
    void deleteCurrentWords(int position){ //刪字对话框
        final int p=position;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("确认删除所选字吗？");
        builder.setTitle("提示");
        builder.setIcon(R.drawable.snow);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int historicPosition=0;
                String text;
                //删除字
                text=myGridView.removeOneNewWords(p);
                dialog.dismiss();
                activity.getDbInfo().deleteOneNewWords(p);
                historicPosition=activity.getDbInfo().deleteOneHistoricWords(text); //删除库信息
                myGridView.removeOneHistoricWords(historicPosition);
            }
        });
        builder.create().show();
    }
    void comeOutTest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("确认出题吗？");
        builder.setTitle("提示");
        activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        builder.setIcon(R.drawable.snow);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.confirmHaveTest();
            }
        });
        builder.create().show();
    }
    void setCurrentTestWords(int position,String word,String time){
        final int p=position;
        final String w=word;
        final String t=time;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("认错该字了吗？");
        builder.setTitle("提示");
        builder.setIcon(R.drawable.sadness);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.confirmFault(p,w,t);
            }
        });
        builder.create().show();
    }
    void uploadFile(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("上传xml文件吗？");
        builder.setTitle("提示");
        builder.setIcon(R.drawable.snow);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.confirmSendFile();
            }
        });
        builder.create().show();
    }
    void downloadFile(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("下载xml文件吗？");
        builder.setTitle("提示");
        builder.setIcon(R.drawable.snow);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.receiveFile();
            }
        });
        builder.create().show();
    }
}
