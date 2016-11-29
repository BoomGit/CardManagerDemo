package com.example.cardmanagerdemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cpucard.library.c;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;


public class MainActivity extends Activity implements OnClickListener{

	private ExecutorService exec;
    private c cardManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ChenTest chenTest=new ChenTest();
        findViewById(R.id.ReadBalance).setOnClickListener(this);
        findViewById(R.id.MinusFee).setOnClickListener(this);
        findViewById(R.id.RegetTac).setOnClickListener(this);
        findViewById(R.id.ReadPSAMID).setOnClickListener(this);
        findViewById(R.id.GetParameter).setOnClickListener(this);
        exec = Executors.newSingleThreadExecutor();
        cardManager=new c();
        cardManager.a(1,3,true);
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        System.out.println("退出界面");
        if(cardManager!=null)
        {
            exec.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    String result=cardManager.p();
                    Message message=new Message();
                    message.obj=result;
                    message.what=6;
                    handler.sendMessage(message);
                }
            }, "close_card"));
        }
    }

    private String cpuCardBalance="",serialNum="";
    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch(view.getId())
        {
            case R.id.ReadBalance:
                exec.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String result = cardManager.d();
                        cpuCardBalance=result;
                        Message message=new Message();
                        message.obj=result;
                        message.what=0;
                        handler.sendMessage(message);
                    }
                }, "read_balance"));
                break;
            case R.id.MinusFee:
                exec.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //参数（单位：分）
                        String result = cardManager.a(1, "20160501", "213629", cpuCardBalance, "E00A0601");
                        Message message=new Message();
                        message.obj=result;
                        message.what=1;
                        handler.sendMessage(message);
                    }
                }, "deduct_money"));
                break;
            case R.id.RegetTac:
                exec.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String result=cardManager.e();
                        Message message=new Message();
                        message.obj=result;
                        message.what=2;
                        handler.sendMessage(message);
                    }
                }, "retrieve_tac"));
                break;
            case R.id.ReadPSAMID:
                exec.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String result=cardManager.f();
                        Message message=new Message();
                        message.obj=result;
                        message.what=3;
                        handler.sendMessage(message);
                    }
                }, "read_psam_id"));
                break;
            case R.id.GetParameter:
            {
                //等扣完费再调用
                //获取城市代码
                long cardCityCode=cardManager.h();
                
                //获取行业代码
                long cardBusinessCode=cardManager.i();
                
                //获取卡号
                long cardPhysicsNumber=cardManager.j();
                
                //获取卡版本
                long cardVer=cardManager.k();
                
                //获取卡类型
                long cardType=cardManager.l();
                
                //获取卡启用日期
                long cardStartDate=cardManager.m();
                
                //卡交易次数
                long cardTradeCount=cardManager.n();
                
                //终端机流水
                String termSeq=cardManager.o();
                
                //CPU卡内号
                String cpuCardNo=cardManager.q();
                
                //卡表面号
                Long cardSurfaceNumber=cardManager.a(String.valueOf(Long.toHexString(cardPhysicsNumber)));

                System.out.println("cardCityCode="+cardCityCode+",cardBusinessCode="+cardBusinessCode+",cardPhysicsNumber="+cardPhysicsNumber+
                ",cardVer="+cardVer+",cardType="+cardType+",cardStartDate="+cardStartDate+",cardTradeCount="+cardTradeCount+",termSeq="+termSeq+
                ",cpuCardNo="+cpuCardNo+",cardSurfaceNumber="+cardSurfaceNumber);

                break;
            }
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0:
                    System.out.println("公交卡余额返回结果："+msg.obj.toString());
                    break;
                case 1:
                    System.out.println("公交卡扣费返回结果："+msg.obj.toString());
                    break;
                case 2:
                    System.out.println("公交卡重获tac值返回结果："+msg.obj.toString());
                    break;
                case 3:
                    System.out.println("获取PSAMID返回结果："+msg.obj.toString());
                    break;
                case 6:
                    System.out.println("关闭通道返回结果："+msg.obj.toString());
                    break;
            }
            super.handleMessage(msg);
        }
    };
    
}
