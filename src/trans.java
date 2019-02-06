import java.util.ArrayList;

public class trans {

    public int stateFrom, stateTo;
    public ArrayList <Character> symp ;

    public trans(int from, int to, ArrayList symbol) {

        this.stateFrom = from;
        this.stateTo = to;
        this.symp = new ArrayList<>(symbol);

    }
}