package logic;

import java.util.Timer;
import java.util.TimerTask;

public class AutoSaveService {
    private final Runnable saveTask;
    private final long intervalMs;
    private Timer timer;

    public AutoSaveService(Runnable saveTask, long intervalMs) {
        this.saveTask = saveTask;
        this.intervalMs = intervalMs;
    }

    public void start() {
        timer = new Timer(true); // Daemon thread
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                saveTask.run(); // Call the save method
            }
        }, intervalMs, intervalMs);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
