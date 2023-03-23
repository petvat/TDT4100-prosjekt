package project;

/// UNØDVENDIG!
public class MineCounter {
    private int minesTotal;
    private int count;
    //private Board board;

    public MineCounter(int mines) {
        minesTotal = mines;
        count = mines;
    }

    public int getCount() {
        return count;
    }

    public void addTo() {
        if (count < minesTotal)
            count++;
    }

    public void remove() {
        if (count > 0)
            count--;
    }
}
