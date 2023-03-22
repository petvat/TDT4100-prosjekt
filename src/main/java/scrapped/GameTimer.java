package scrapped;


public class GameTimer {
    private int seconds;
    private int cap;

    public GameTimer(int start, int cap) {
        this.seconds = start;
        this.cap = cap;
    }

    public void runTimer() {
        if (seconds < cap)
            seconds++;
    }
    
    public int getCurrent() {
        return seconds;
    }
}
