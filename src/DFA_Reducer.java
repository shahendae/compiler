import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/* Apply minimization by partition. */
public class DFA_Reducer {

    public DFA dfa;
    public ArrayList<DFA_State> states;
    public ArrayList<String> symbols = new ArrayList<>();
    public ArrayList<Integer> accepting_states = Main.accept_states;
    public boolean[][] twoD;
    public ArrayList<ArrayList<HashSet<Point>>> P;
    public HashSet<Integer> acceptStates = new HashSet<>();


    public DFA_Reducer(DFA dfa) {
        this.dfa = dfa;
        this.states = dfa.states;
    }

    private void set_Symbols() {
        for (DFA_State d : states) {
            for (String k : d.symbol) {
                if(!symbols.contains(k)){
                    symbols.add(k);}
            }
        }
    }

    private void set_hashset() {
        acceptStates.addAll(accepting_states);
    }


    //remove unreachable states (two states have the same transitions)
    public void BFS() {
        int num = states.size();
        ArrayList<Integer>[] transitions = new ArrayList[num];
        int k = 0;
        for (DFA_State d : states) {
            transitions[k] = d.stateTo;
            k++;

        }

        boolean[] visited = new boolean[transitions.length];
        Queue<Integer> visitedQueue = new LinkedList<>();
        visitedQueue.add(1);
        visited[1] = true;
        while (!visitedQueue.isEmpty()) {
            int visit = visitedQueue.remove();
            ArrayList<Integer> visitedStates = transitions[visit];
            for (int neighbour : visitedStates) {

                //dead state
                if (neighbour == 0) {
                    break;
                }
                if (!visited[neighbour]) {
                    visitedQueue.add(neighbour);
                }
            }

            visited[visit] = true;

        }

        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {

                transitions[i] = null;

            }
        }
    }


    // {{0},{3},{1,2}} first partition accepted states ana non accepted states then continue partition.
    //that only the same transitions be together
    public void partition() {

        int num = states.size();
        ArrayList<Integer>[] transitions = new ArrayList[num];
        int k = 0;

        for (DFA_State d : states) {

            transitions[k] = d.stateTo;
            k++;

        }

        set_hashset();

        twoD = new boolean[transitions.length][transitions.length];
        P = new ArrayList<ArrayList<HashSet<Point>>>();


        for (int i = 0; i < transitions.length; i++) {
            ArrayList<HashSet<Point>> innerList = new ArrayList<HashSet<Point>>();


            for (int j = 0; j < transitions.length; j++) {
                Arrays.fill(twoD[i], false);
                innerList.add(new HashSet<Point>());
            }
            P.add(innerList);
        }


        for (int i = 0; i < transitions.length; i++) {
            for (int j = i + 1; j < transitions.length; j++) {
                if (acceptStates.contains(i) != acceptStates.contains(j)) {
                    twoD[i][j] = true;
                }
            }
        }


        for (int i = 0; i < transitions.length; i++) {
            for (int j = i + 1; j < transitions.length; j++) {
                if (twoD[i][j]) {
                    continue;
                }

                ArrayList<Integer> qi = transitions[i];
                ArrayList<Integer> qj = transitions[j];


                if (qi == null || qj == null) {
                    continue;
                }


                boolean distinguished = false;
                for (int z = 0; z < qi.size(); z++) {
                    int m = qi.get(z);
                    int n = qj.get(z);


                    if (twoD[m][n] || twoD[n][m]) {
                        sameTrans(i, j);
                        distinguished = true;
                        break;
                    }
                }

                if (!distinguished) {

                    for (int z = 0; z < qi.size(); z++) {
                        int m = qi.get(z);
                        int n = qj.get(z);

                        if (m < n && !(i == m && j == n)) {
                            P.get(m).get(n).add(new Point(i, j));
                        } else if (m > n && !(i == n && j == m)) {
                            P.get(n).get(m).add(new Point(i, j));
                        }
                    }
                }

            }

            // System.out.println("transitions of Partition "+transitions[i]);
        }

        try {
            mergeStates();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //merge states that comes from partition and sorted it by smallest
    private void mergeStates() throws IOException {
        int num = states.size();
        ArrayList<Integer>[] transitions = new ArrayList[num];
        int k = 0;
        for (DFA_State d : states) {
            transitions[k] = d.stateTo;
            k++;

        }
        set_hashset();

        ArrayList<ArrayList> newStates = new ArrayList<>();
        HashSet<Integer> newAcceptStates = new HashSet<>();
        HashMap<Integer, Integer> merged = new HashMap<>();
        ArrayList<ArrayList<Integer>> mergeGroups = new ArrayList<>();

        for (int i = 0; i < twoD.length; i++) {
            if (merged.get(i) != null || transitions[i] == null) {
                continue;
            }

            ArrayList<Integer> state = transitions[i];
            //System.out.println("transitions from partitions "+transitions[i]);

            ArrayList<Integer> toMerge = new ArrayList<>();
            for (int j = i + 1; j < twoD.length; j++) {
                if (!twoD[i][j]) {
                    toMerge.add(j);
                    merged.put(j, i);
                }
            }


            for (int j = 0; j < state.size(); j++) {
                Integer transition = state.get(j);
                if (merged.containsKey(transition)) {
                    state.set(j, merged.get(transition));
                }

            }

            if (acceptStates.contains(i)) {
                newAcceptStates.add(i);
            }
            toMerge.add(i);
            mergeGroups.add(toMerge);
            newStates.add(state);

            // System.out.println("transition after merge "+transitions[i]);
        }


        renumberStates(mergeGroups, newAcceptStates);

        //System.out.print("Merged Groups " + mergeGroups);
        //System.out.print("\nNew Accept States " + newAcceptStates);


        ArrayList<Integer>[] newStatesArray = new ArrayList[newStates.size()];
        newStatesArray = newStates.toArray(newStatesArray);
        transitions = newStatesArray;
        acceptStates = newAcceptStates;

        FileWriter writer = new FileWriter("minimized_output.txt");
        set_Symbols();
        //System.out.println("\nTransition table for the minimal DFA: ");
        for(int i : acceptStates){
            writer.write(i+" ");
        }
        writer.append(System.lineSeparator());
        writer.write("s ");
        //  System.out.print("s ");
        for(String s: symbols){
            //      System.out.print(s+" ");
            writer.write(s+" ");
        }
        //  System.out.println();
        writer.append(System.lineSeparator());
        for (int r = 0; r < transitions.length; r++) {

            //     System.out.println(r+ " "+ transitions[r]);
            writer.write(r+" ");
            for(int l=0;l<transitions[r].size();l++){
                writer.write(transitions[r].get(l)+" ");
            }
            writer.append(System.lineSeparator());

        }
        writer.close();
    }


    //renumber new states and new accepted states from merge states
    private void renumberStates(ArrayList<ArrayList<Integer>> groups, HashSet<Integer> newAcceptStates) {

        int num = states.size();
        ArrayList<Integer>[] transitions = new ArrayList[num];
        int k = 0;

        for (DFA_State d : states) {

            transitions[k] = d.stateTo;
            k++;

        }

        for (int i = 0; i < groups.size(); i++) {
            ArrayList<Integer> group = groups.get(i);
            //  System.out.println("merged groups from merge fun. "+group);
            for (ArrayList<Integer> state : transitions) {
                if (state == null) {
                    continue;
                }


                for (int j = 0; j < state.size(); j++) {
                    Integer val = state.get(j);
                    if (group.contains(val)) {

                        state.set(j, i);

                    }
                }
            }
            for (Integer state : new HashSet<>(newAcceptStates)) {
                if (group.contains(state)) {
                    newAcceptStates.remove(state);
                    newAcceptStates.add(i);
                }
            }

            // System.out.println("RENUMBER STATES "+i);
        }
    }
    private void sameTrans(int i, int j) {
        _sameTrans(new Point(i, j), new HashSet<>());

    }
    private void _sameTrans(Point point, HashSet<Point> visited) {
        if (visited.contains(point)) {
            return;
        }
        int i = point.x, j = point.y;
        twoD[i][j] = true;
        visited.add(point);
        for (Point pair : P.get(i).get(j)) {
            _sameTrans(pair, visited);
        }
    }
}