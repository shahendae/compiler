import java.util.ArrayList;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static ArrayList <Integer>accept_states;
    public static Map<String,String> lines= new LinkedHashMap<>();
    public static ArrayList<String> fileIndex = new ArrayList<>();
    public static LinkedHashMap<String,String[]> Fmap = new LinkedHashMap<>();
    public static void main(String[] args) throws IOException {
        NFA last = new NFA();
        String fileName = "input.txt";
        File file = new File(fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line=reader.readLine())!= null){
                separator sep = new separator(line);
                String separated[];
                separated = sep.separate(line);
                decider worker = new decider(separated);
                last = worker.decide(separated);
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("NFA :");
//        last.display();

        DFA dfa = new DFA(last);
        Subset_Constructor s = new Subset_Constructor(last);
        accept_states = s.accepting_states;
        DFA dfa_output = s.generate_DFA();
        dfa_output.display_DFA();
        DFA_Reducer reducer = new DFA_Reducer(dfa_output);
        reducer.BFS();
        reducer.partition();
        System.out.println("Transition table for the minimal DFA ");
        Maximal_munch.tokenize();

        String input = "parser_input.txt";
        File parserfile = new File(input);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(parserfile));
            String line;
            while ((line = reader.readLine()) != null) {
                fileIndex.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        constructor myConst = new constructor(fileIndex);
        myConst.separateLines();
        CFG myCFG = new CFG();
        CFG lastCFG = myCFG.controlCFG();

        System.out.println("START STATE : "+lastCFG.start);
        System.out.println("TERMINALS : "+lastCFG.terminal);
        System.out.println("NON-TERMINALS : "+lastCFG.nonTerminal);
        System.out.println("PRODUCTIONS : "+lastCFG.productions);

        LL1 l = new LL1(lastCFG);
        CFG ambuguity_free = l.eliminate();
        first_follow fst=new first_follow(ambuguity_free);
        for(String k:ambuguity_free.nonTerminal){
            ArrayList<String> temp = new ArrayList<>();
            System.out.println("\nfirst of "+k+" : ");
            fst.first(k,temp);
            //System.out.println(temp);
            String temp2[] = new String[temp.size()];
            temp2 = temp.toArray(temp2);
            Fmap.put(k,temp2);
        }

        fst.follow();
        parserTable myTable = new parserTable(ambuguity_free,fst);
        System.out.println("\npredictive parsing table");
        myTable.createTable();

        System.out.println("\nLL(1) parser");
        parser.readParsingTable();




    }
}