package test.zx.learnwords;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.CopyOnWriteArrayList;

import okio.BufferedSink;

/**
 * Created by THink on 2018/1/15.
 */

public class OkHttp {
    private MainActivity activity;
    private OkHttpClient okHttpClient= new OkHttpClient();
    private String baseUrl;
    private String path= Environment.getExternalStorageDirectory()+"/daughter/";
    public OkHttp(MainActivity activity) {
        super();
        this.activity=activity;
    }
    public void iniCookie(){
        okHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }
    void doGet(){
        Request.Builder builder=new Request.Builder();
        Request request=builder.get().url(Constant.BASE_URL+"login?"+"user=abc&key=123").build();
        execute(request);
    }
    void doPost(String content,String type){
        RequestBody requestBody=null;
        if(type=="UserAndKey"){
            FormEncodingBuilder code=new FormEncodingBuilder();
            requestBody=code.add("user","abc").add("key","123").build();
            //doPost需要一个body
        } else if(type=="String"){
            requestBody=RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),content);
        } else if(type=="File"){
            String fileName= Constant.CONFIG_FILE_NAME;
            String path= Environment.getExternalStorageDirectory()+"/daughter/";
            File file=new File(path,fileName);
            requestBody=RequestBody.create(MediaType.parse("application/charset=utf-8"),file);
        }
        Request.Builder builder=new Request.Builder();
        Request request=builder.url(Constant.BASE_URL+"postResolve").post(requestBody).build();
        execute(request);

    }
    void doMultiplePost(){
        String fileName= Constant.CONFIG_FILE_NAME;

        File file=new File(path,fileName);
        MultipartBuilder multipartBuilder=new MultipartBuilder();
        RequestBody requestBody = multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("user", "abc")
                .addFormDataPart("key", "123")
                //"text/plain;charset=utf-8",这里的文件格式很重要，否则无法识别
                .addFormDataPart("config", "config.xml", RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), file))
                .build();
        Request.Builder builder=new Request.Builder();
        Request request=builder.url(Constant.BASE_URL+"upload").post(requestBody).build();
        execute(request);
    }
    void execute(Request request){
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
            }
        });
    }
    void doDownload(){
        Request.Builder builder=new Request.Builder();
        final Request request=builder.get().url(Constant.BASE_URL+"files/config.xml").build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                InputStream in=response.body().byteStream();
                int len=0,offset=0;
                String line="";
                String fileName= Constant.CONFIG_FILE_NAME;
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                line=buffer.toString();
                File file=new File(path,fileName);
                FileWriter fileWriter=new FileWriter(file,false);
                fileWriter.write(line);
                fileWriter.close();
                in.close();
            }
        });
    }

}
