package test.zx.learnwords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StatisticDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView mTvTestTime,mTvHistoricCount,mTvFaultCount,mTvFaultDetails;
        super.onCreate(savedInstanceState);
        setTitle("Elsa学习情况显示");
        setContentView(R.layout.activity_statistic_data);
        Intent intent=getIntent();
        mTvFaultDetails=findViewById(R.id.fault_words_details);
        mTvHistoricCount=findViewById(R.id.historic_words_count);
        mTvTestTime=findViewById(R.id.test_count);
        mTvFaultCount=findViewById(R.id.fault_words_count);
        int testTime=intent.getIntExtra("已测试次数:",0);
        int historicCount=intent.getIntExtra("已认总字数:",0);
        int faultCount= intent.getIntExtra("总错误字数:",0);
        String words=intent.getStringExtra("错误字如下:");
        mTvTestTime.setText("已测试次数:  "+testTime);
        mTvFaultCount.setText("总错误字数:  "+faultCount);
        mTvHistoricCount.setText("已认总字数:  "+historicCount);
        mTvFaultDetails.setText("错误字如下:"+"\r\n"+words);
    }
}
