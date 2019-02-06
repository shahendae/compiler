import java.util.ArrayList;

/* DFA state used in DFA generation.*/
public class DFA_State {


    public int id;
    public ArrayList<Integer> stateTo;
    public ArrayList <String> symbol;
    public ArrayList<Integer> nfa_states;


    public  DFA_State(int id){
        this.id =id;
        this.stateTo = new ArrayList<>();
        this.symbol = new ArrayList<>();
        this.nfa_states = new ArrayList<>();
    }
    public DFA_State(int id,ArrayList<Integer>states){
        this.id = id;
        this.stateTo = new ArrayList<>();
        this.symbol = new ArrayList<>();
        this.nfa_states = new ArrayList<>(states);

    }

}