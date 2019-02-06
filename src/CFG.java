import java.util.*;

public class CFG {
    public static ArrayList <String> terminal = new ArrayList<>();
    public static ArrayList <String> nonTerminal = new ArrayList<>();
    public Map<String,ArrayList<String>> productions = new LinkedHashMap<>();
    public static String start = new String();


    public CFG(ArrayList terminal1,ArrayList nonTerminal1,Map productions1,String start1) {
        this.terminal = terminal1;
        this.nonTerminal = nonTerminal1;
        this.productions = productions1;
        this.start=start1;
    }

    public CFG(){
        this.terminal = new ArrayList<>();
        this.nonTerminal = new ArrayList<>();
        this.productions = new LinkedHashMap<>();

    }

    public CFG controlCFG(){
        Set<String> keys = Main.lines.keySet();
        start = keys.iterator().next();
        for(String k : keys){
            int t = 0;
            boolean ready = false;
            String production = new String("");
            String currentTerminal = new String("");
            ArrayList <String> currentProductions = new ArrayList<String>();
            nonTerminal.add(k);
            String value = Main.lines.get(k);
//            System.out.println(k+"\t"+value);
            for (int i = 0 ; i < value.length() ; i++) {
                char C = value.charAt(i);
                if (C == '‘') {
                    t++;
                } else if (C == '’') {
                    t--;
                    if(!terminal.contains(currentTerminal)){
                        terminal.add(currentTerminal);
                    }
                    if (i + 1 == value.length())
                        currentProductions.add(production);
                    currentTerminal = "";
                } else {
                    if (C == ' ') {
                        if (!ready) {
                            continue;
                        }
                    }


                    if (C == '|') {
                        currentProductions.add(production);
                        production = "";
                        ready = false;
                    } else {
                        production += C;
                        ready = true;
                        if(t == 1){
                            currentTerminal += C;
                        }
                        else if(t > 1){
                            System.out.println("ERROR : quotes are not equal in terminal keywords");
                            System.exit(7);
                        }
                        if (i + 1 == value.length())
                            currentProductions.add(production);
                    }
                }
            }
            productions.put(k,currentProductions);

        }

        return new CFG(terminal,nonTerminal,productions,start);
    }
}
