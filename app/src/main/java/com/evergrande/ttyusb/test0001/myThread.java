package com.evergrande.ttyusb.test0001;

import android.util.Log;
import java.util.concurrent.locks.*;

public class myThread {

    private final String TAG = "TEST";
    private boolean isStart =true;
    Lock lock = new ReentrantLock();

    public int successCount = 0;

    private ReceiveThread mReceiveThread = null;
    private SendThread mSendThread = null;

    private DataValue mDataValue = new DataValue();
    private rwDataValue mrwDataValue = new rwDataValue();

    /*
     * 初始化线程
     */
    public void InitThread(){
        if (mSendThread == null){
            mSendThread = new SendThread();
        }

        mSendThread.start();

        if (mReceiveThread == null){
            mReceiveThread = new ReceiveThread();
        }

        mReceiveThread.start();
        Log.d(TAG, "启动线程完成");
    }

    /**
     * 线程实体
     */
    public class SendThread extends Thread {


        public void run() {
            isStart = true;
            for(int i=0;i<10;i++)
            {
/*                Log.d(TAG,"开始写线程~");
                mDataValue.setData(i);
                Log.d(TAG, "写数据:testCount:" + successCount);
                Log.d(TAG,"写线程休眠~");
                sendTreadSleep(10);*/
                mrwDataValue.setData(i);
                Log.d(TAG,Thread.currentThread().getName() +"写休眠");
                sendTreadSleep(10);
            }
            isStart = false;
        }
    }

    /**
     * 接收数据的线程
     */
    public class ReceiveThread extends Thread {

        @Override
        public void run() {

            int testCount = 20;
            Log.d(TAG,"s开始读线程~");
            super.run();
            //条件判断，只要条件为true，则一直执行这个线程
            while (isStart == true) {
/*                Log.d(TAG,"开始读线程~");
                testCount = mDataValue.getData();
                Log.d(TAG, "读数据~:testCount:" + testCount);
                Log.d(TAG,"读线程休眠~");
                receiveTreadSleep(5);*/
                mrwDataValue.getData();
                Log.d(TAG,Thread.currentThread().getName() +"读休眠");
                receiveTreadSleep(5);
            }
        }
    }

    /**
     * 发送线程延迟
     * @param millis 毫秒
     */
    private void sendTreadSleep(int millis)
    {
        try{
            mSendThread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 接收线程延迟
     * @param millis 毫秒
     */
    private void receiveTreadSleep(int millis)
    {
        try{
            mReceiveThread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 使用synchronized完成互斥
     */
    private class DataValue{

        private synchronized void setData(int value){
            Log.d(TAG,"设置数据~setData");
            successCount = value;
        }

        private synchronized  int getData(){
            Log.d(TAG,"获取数据~getData");
            return successCount;
        }

    }

    /*
     * 使用读写锁完成互斥ReadWriteLock
     */
    private class rwDataValue{
        private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private int Data;

        private void setData(int value){
            readWriteLock.writeLock().lock();
            try {
                Log.d(TAG, Thread.currentThread().getName() +"写数据~setData:"+value);
                Data = value;
                Thread.sleep(30);
            }catch (Exception i){
                Log.e(TAG,"error");
            }finally {
                readWriteLock.writeLock().unlock();
            }
        }

        private  void getData(){
            readWriteLock.readLock().lock();
            try {
                Log.d(TAG,Thread.currentThread().getName() +"获取数据~getData：" +Data);
                Thread.sleep(10);
            }catch (Exception i){
                Log.e(TAG,"error");
            }finally {
                readWriteLock.readLock().unlock();
            }
        }

    }
}