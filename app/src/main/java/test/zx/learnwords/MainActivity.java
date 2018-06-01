package test.zx.learnwords;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONException;

import java.io.IOException;

import static test.zx.learnwords.Constant.PERMISSIONS_STORAGE;
import static test.zx.learnwords.Constant.REQUEST_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ViewPagerFragment.OnFragmentInteractionListener {
    private DBInfo dbInfo=new DBInfo();
    private DialogInfo dialogInfo = new DialogInfo(this);
    private ViewPagerFragment viewPagerFragment;
    private KeyboardInfo keyboardInfo=new KeyboardInfo(this);
    private OkHttp okHttp=new OkHttp(this);
    private boolean downFileFlag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbInfo.checkConfigFile();
        try {
            dbInfo.iniConfigInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager mFragmentManager=getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction=mFragmentManager.beginTransaction();
        viewPagerFragment=ViewPagerFragment.newInstance(dbInfo.toString(),null);
        mFragmentTransaction.replace(R.id.fragment_content,viewPagerFragment);
        mFragmentTransaction.commit();











        getWindow().setBackgroundDrawable(null);
    }
    //后退键
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void onPause(){
        if(!downFileFlag){
            dbInfo.updateJsonFile();
        }
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public DBInfo getDbInfo() {
        return dbInfo;
    }

    public void setDbInfo(DBInfo dbInfo) {
        this.dbInfo = dbInfo;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.test_settings){
            dialogInfo.comeOutTest();
            return true;
        }
        if(id==R.id.static_data){
            Intent intent = new Intent(MainActivity.this, StatisticDataActivity.class);
            intent.putExtra("已认总字数:", dbInfo.getDataBase().getHistoricWords().getWordsNum());
            intent.putExtra("已测试次数:",dbInfo.getDataBase().getTestDayCount());
            intent.putExtra("总错误字数:",dbInfo.getDataBase().getTestFaultWordsCount());
            intent.putExtra("错误字如下:",dbInfo.getDataBase().getTestFaultWordsDetail());
            MainActivity.this.startActivity(intent);
        }
//        if(id==R.id.update_info){
//            dbInfo.dataRefresh();
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_camera){

        }
        if(id==R.id.upload_xml_file){
            dialogInfo.uploadFile();
        }
        if(id==R.id.down_xml_file){
            dialogInfo.downloadFile();
        }
        if(id==R.id.save_file){
            dbInfo.updateJsonFile();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public KeyboardInfo getKeyboardInfo() {
        return keyboardInfo;
    }

    public void setKeyboardInfo(KeyboardInfo keyboardInfo) {
        this.keyboardInfo = keyboardInfo;
    }
    public void confirmHaveTest(){
        dbInfo.createTestWords();
        MyGridView view=viewPagerFragment.getMyGridView();
        view.increaseHistoricTest(dbInfo);
    }
    public void confirmFault(int position,String word,String time){
        dbInfo.setFaultWords(position,word,time);
    }
    public void confirmSendFile(){
        okHttp.doMultiplePost();
    }
    public void receiveFile(){
        okHttp.doDownload();
        downFileFlag=true;
    }
}
