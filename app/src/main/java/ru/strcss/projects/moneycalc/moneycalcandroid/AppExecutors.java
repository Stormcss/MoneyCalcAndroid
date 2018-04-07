package ru.strcss.projects.moneycalc.moneycalcandroid;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

/**
 * Global executor pools for the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
public class AppExecutors {
    private final Executor diskIO;

    private final ExecutorService networkIO;

    private final Executor mainThread;

//    public AppExecutors(Executor diskIO, ExecutorService networkIO, Executor mainThread) {
//        this.diskIO = diskIO;
//        this.networkIO = networkIO;
//        this.mainThread = mainThread;
//    }

    @Inject
    public AppExecutors() {
        this.diskIO = Executors.newSingleThreadExecutor();
        this.networkIO = Executors.newFixedThreadPool(3);
        this.mainThread = new MainThreadExecutor();
        //        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
//                new MainThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public ExecutorService networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
