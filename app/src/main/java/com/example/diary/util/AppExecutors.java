package com.example.diary.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.MainThread;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors instance;
    private final Executor diskIO, mainThread, networkIO;

    private AppExecutors(Executor diskIO, Executor networkIO,Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public static AppExecutors getInstance(){
        if(instance==null){
            synchronized (LOCK){
                instance=new AppExecutors(Executors.newSingleThreadExecutor(),Executors.newFixedThreadPool(3),new MainThreadExecutor());
            }
        }
        return instance;
    }

    private static class MainThreadExecutor implements Executor{

      private Handler mainThreadHandler=new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
              mainThreadHandler.post(command);
        }
    }

}