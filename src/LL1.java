import java.util.*;

public class LL1 {

    public CFG grammar;
    public ArrayList<String> terminals;
    public ArrayList<String>non_terminals;
    public ArrayList<String> new_non_terminals = new ArrayList<>();
    public ArrayList<String>final_nonterminals = new ArrayList<>();
    public Map<String, ArrayList<String>> productions;
    public Map<String, ArrayList<String>> new_productions;
    public Map<String, ArrayList<String>> final_productions = new LinkedHashMap<>();
    public String start;

    public LL1(CFG grammar){
        this.grammar = grammar;
        this.terminals = grammar.terminal;
        this.non_terminals = grammar.nonTerminal;
        this.productions = grammar.productions;
        this.start = grammar.start;
    }

    /* Elimination of left recursion and performing left factoring to produce LL1 grammar. */

    public CFG eliminate() {
        ArrayList<String> left = new ArrayList<>();
        Boolean Recursive;
        left.addAll(productions.keySet());
        new_productions = new LinkedHashMap<>();
        ArrayList<String> right;
        ArrayList<String> p = new ArrayList<>();
        ArrayList<String>to_remove = new ArrayList<>();

        for (int i = 0; i < left.size(); i++) {
            Recursive = false;
            String new_left = "";
            String alpha;
            String Beta = "";
            ArrayList<String> A = new ArrayList<>();
            ArrayList<String> new_rules = new ArrayList<>();
            right = productions.get(left.get(i));


            /*Non immediate left recursion Elimination.*/
            for(int k = 1; k< left.size(); k++){
                String temp = "";
                if(left.get(i)!=left.get(k)&&!non_terminals.contains(left.get(k))) {
                    right = productions.get(left.get(k));
                    for (String s : right) {
                        if (s.equals(left.get(i))) {
                            to_remove.add(s);
                            temp = s.replaceAll("\\b" + left.get(i) + "\\b??", "").trim();
                            p = productions.get(left.get(i));
                        }
                    }
                    right.removeAll(to_remove);
                    ArrayList<String> s= new ArrayList<>();
                    for(String q: p){
                        right.add(q+temp);
                        s.add(q+temp);
                    }
                    right.addAll(s);
                }
            }
            /*Immediate left recursion Elimination.*/
            for(int j=0;j<right.size();j++){
                if(right.get(j).startsWith(left.get(i))){
                    Recursive = true;
                    new_left = left.get(i) +"`";
                    alpha = right.get(j).replaceAll("\\b" +left.get(i) + "\\b??", "").trim();
                    alpha += " ";
                    alpha += new_left;
                    new_rules.add(alpha);
                }
                ArrayList<String> y = productions.get(left.get(i));
                for(String z: y){
                    if(Recursive && !z.contains(right.get(j))){
                        Beta += z+" ";
                        Beta += new_left;
                        if (!Beta.isEmpty()){
                            A.add(Beta);
                            Beta="";
                        }
                    }
                }
            }
            if(!A.isEmpty()&&!new_left.isEmpty()){
                new_rules.add("~"); // epsilon
                new_productions.put(left.get(i),A);
                new_productions.put(new_left, new_rules);
            }
            else{
                new_productions.put(left.get(i),right);
            }

        }
        new_non_terminals.addAll(new_productions.keySet());
        if(new_non_terminals.equals(non_terminals)){
//            System.out.println("No left recursion");
        }
        else {
//            System.out.println("This grammar had left recursion");
//            System.out.println("PRODUCTIONS: "+ new_productions);
//            System.out.println("terminals: "+this.terminals);
//            System.out.println("non terminals: "+ new_non_terminals);
        }


        /*Left Factoring. */
        left.clear();
        left.addAll(new_productions.keySet());

        for(int j=0;j<left.size();j++){
            String new_left = left.get(j);
            String alpha = "";
            String beta;
            HashMap <String, Integer> firsts = new HashMap<>();
            ArrayList<String> A = new ArrayList<>();
            ArrayList<String> B = new ArrayList<>();
            Boolean Factoring = false;
            right = new_productions.get(left.get(j));
            for (String r: right){
                String temp;
                if(r.contains(" ")){
                    temp = r.split(" ")[0];
                }
                else temp = r;
                if(!temp.equals("~")){
                    if(firsts.containsKey(temp)){
                        firsts.put(temp,firsts.get(temp)+1);
                    }
                    else firsts.put(temp,1);
                }
            }
            for(String y: firsts.keySet()){
                if (firsts.get(y) > 1&&!Factoring){
                    Factoring = true;
                    new_left +="`";
                    alpha += y+" "+new_left.trim();
                    A.add(alpha.trim());
                    for (String t: right){
                        if(t.startsWith(y)){
                            beta = t.replaceFirst(y,"").trim();
                            if (!beta.isEmpty()){
                                B.add(beta.trim());
                            }
                            else B.add("~");
                        }
                        else A.add(t);
                    }
                }

            }
            if(Factoring&&!A.isEmpty() && !new_left.isEmpty()&&!B.isEmpty()){
                new_productions.replace(left.get(j),right,A);
                new_productions.put(new_left,B);
                final_productions.put(left.get(j),A);
                final_productions.put(new_left,B);
//                j--;
            }
            else{
                new_productions.put(left.get(j),right);
                final_productions.put(left.get(j),right);
            }
            left.clear();
            left.addAll(new_productions.keySet());

        }

        final_nonterminals.addAll(final_productions.keySet());
        if (final_nonterminals.equals(new_non_terminals)){
//            System.out.println("No left factoring");
        }
        else{
//            System.out.println("This grammar required Left factoring");
//            System.out.println("PRODUCTIONS: "+ final_productions);
//            System.out.println("terminals: "+ this.terminals);
//            System.out.println("non terminals: "+ final_nonterminals);
        }


        return new CFG(this.terminals,this.final_nonterminals,this.final_productions,this.start);
    }
}

