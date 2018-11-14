public class Timer78 extends Thread {
    private final long time;
    private final Event evTime;
    private boolean cancel = false;

    public Timer78(long time, Event evTime) {
        this.time = time;
        this.evTime = evTime;
        setDaemon(true);
        start();
    }

    public void cancel() {
        cancel = true;
    }

    public void run() {
        try {
            sleep(time);
        } catch (InterruptedException ex) {
        }
        if (!cancel)
            evTime.sendEvent();
    }

}