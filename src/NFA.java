import java.util.ArrayList;
public class NFA {

    public ArrayList<Integer> states;
    public ArrayList<trans> transitions;
    public int finalState;

    public NFA() {

        this.states = new ArrayList<Integer>();
        this.transitions = new ArrayList<trans>();
        this.finalState = 0;

    }

    public NFA(int size) {

        this.states = new ArrayList<Integer>();
        this.transitions = new ArrayList<trans>();
        this.finalState = 0;
        this.addStatesSize(size);

    }

    public NFA(ArrayList c) {

        this.states = new ArrayList<Integer>();
        this.transitions = new ArrayList<trans>();
        this.addStatesSize(2);
        this.finalState = 1;
        this.transitions.add(new trans(0, 1, c));


    }

    public void addStatesSize(int size) {


        for (int i = 0; i < size; i++)
            this.states.add(i);

    }

    public void display() {
        for (trans t : transitions) {
            System.out.println("(" + t.stateFrom + ", " + t.symp + ", " + t.stateTo + ")");
        }

    }
}