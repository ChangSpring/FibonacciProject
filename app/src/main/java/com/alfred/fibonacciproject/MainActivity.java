package com.alfred.fibonacciproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 正序
    private Button positiveBtn;
    // 倒序
    private Button negativeBtn;
    private ListView listView;

    // 是否倒序
    private boolean isDescending = false;
    private ProgressDialog dialog;
    private Handler mSubHandler;
    private FibonacciAdapter adapter;
    private List<BigInteger> list = new ArrayList<BigInteger>();

    private static final int LINE_SIZE = 450;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.setList(list);
            if (dialog != null) {
                dialog.dismiss();
            }
            Log.i("FibonacciActivity", "更新UI的Thread : " + Thread.currentThread().getId());
        }

    };
    private Handler.Callback mSubCallBack = new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // 运行在子线程中,处理耗时操作
            Log.i("FibonacciActivity", "计算的Thread : " + Thread.currentThread().getId());
            switch (msg.what) {
                case 0:
                    getFibonacciData();
                    break;
                case 1:
                    getDescendingList();
                    break;

                default:
                    break;
            }
            handler.sendMessage(Message.obtain());

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        positiveBtn = (Button) findViewById(R.id.positive_btn);
        negativeBtn = (Button) findViewById(R.id.negative_btn);
        listView = (ListView) findViewById(R.id.listView);

        HandlerThread handlerThread = new HandlerThread("FibonacciActivity") {
            @Override
            protected void onLooperPrepared() {
                super.onLooperPrepared();
                dialog = ProgressDialog.show(MainActivity.this, "温馨提示", "请稍后");
            }
        };
        handlerThread.start();
        mSubHandler = new Handler(handlerThread.getLooper(), mSubCallBack);
        // getFibonacciData();
        initAdapter();
        mSubHandler.sendEmptyMessage(0);
        positiveBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // getFibonacciData();
                // adapter.setList(list);

                mSubHandler.sendEmptyMessage(0);

            }
        });

        negativeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mSubHandler.sendEmptyMessage(1);
                // getDescendingList();
                // adapter.setList(list);
            }
        });
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new FibonacciAdapter(this);
        listView.setAdapter(adapter);
    }

    /**
     * 获取正序的数据
     */
    private void getFibonacciData() {
        list.clear();
        list.add(new BigInteger("0"));
        list.add(new BigInteger("1"));
        BigInteger one = new BigInteger("0"), two = new BigInteger("1"), three = new BigInteger("1");
        int index = 2;
        for (int i = 2; i <= LINE_SIZE * LINE_SIZE; i++) {
            three = one.add(two);
            if (i == index * index) {
                list.add(three);
                index++;
            }
            one = two;
            two = three;
        }
    }

    /**
     * list翻转
     */
    private void getDescendingList() {

        for (int i = 0; i < list.size() / 2; i++) {
            BigInteger one = list.get(i);
            BigInteger two = list.get(list.size() - i - 1);
            list.set(i, two);
            list.set(list.size() - i - 1, one);

        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            if (isDescending) {
                // 倒序
                getDescendingList();
            } else {
                // 正序
                getFibonacciData();
            }

            listView.post(new Runnable() {

                @Override
                public void run() {
                    adapter.setList(list);
                    isDescending = !isDescending;
                }
            });
        }

    }

}
