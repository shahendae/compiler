import java.util.*;

public class first_follow {
    Map<String, ArrayList<String>> Follow_set = new LinkedHashMap<>();
    public CFG cfg;
    public static ArrayList<String> terminals;
    public static ArrayList<String> nonterminals;
    public Map<String, ArrayList<String>> production;
    int termnal_length, nonterminal_length;
    public static String start;

    public first_follow(CFG cfg) {

        this.cfg = cfg;
        this.terminals = cfg.terminal;
        this.nonterminals = cfg.nonTerminal;
        this.production = cfg.productions;
        this.termnal_length = cfg.terminal.size();
        this.nonterminal_length = cfg.nonTerminal.size();
        this.start = cfg.start;

//        System.out.println("terminals " + terminals);
//        System.out.println("non terminals " + nonterminals);
//        System.out.println("productions " + production);


    }

    public String first(String firstToken, ArrayList f) {
        String str = "";
        ArrayList<String> LHS = new ArrayList<>();
        ArrayList<String> nonTERMINAL = new ArrayList<>();
        ArrayList<String> TERMINAL = new ArrayList<>();
        ArrayList<String> first = new ArrayList<>();
        for (String z : nonterminals) {
            nonTERMINAL.add(z);
        }
        for (String k : production.keySet()) {
            LHS.add(k);
        }
        for (String t : terminals) {
            TERMINAL.add(t);
        }
        firstToken = firstToken.split(" ")[0];
        if (TERMINAL.contains(firstToken)) {
            str = firstToken;
            first.add(str);
            System.out.println(first);
            f.add(str);

        } else if (firstToken.equals("~") | firstToken.equals("epsilon")) {
            str = firstToken;
            first.add(str);
            System.out.println(first);
            f.add(str);
        } else {
            if (nonTERMINAL.contains(firstToken)) {
                for (String m : production.get(firstToken)) {
                    //System.out.println("right = "+m);
                    first(m, f);
                }
            }
        }
        return str;
    }


    public void follow() {
        String str = "";
        ArrayList<String> LHS = new ArrayList<>();
        ArrayList<String> TERMINAL = new ArrayList<>();
        ArrayList<String> follow = new ArrayList<>();
        ArrayList<String> right = new ArrayList<>();
        //Map<String, ArrayList<String>> Follow_set = new LinkedHashMap<>();

        for (String k : production.keySet()) {
            LHS.add(k);
        }
        for (String t : terminals) {
            TERMINAL.add(t);
        }


        for (int i = 0; i < LHS.size(); i++) {
            if (LHS.get(i) == start) {
                str = "$";
                follow.add(str);
                Follow_set.put(LHS.get(i), follow);
//                System.out.println(Follow_set);
            }
            for (String j : nonterminals) {
                right = production.get(j);
//                System.out.println("right "+right);
                for (int x = 0; x < right.size(); x++) {
                    String[] split = right.get(x).split(" ");
                    for (String s : split) {
                        if (nonterminals.contains(s)&&s.equals(LHS.get(i))) {
                            int index = Arrays.asList(split).indexOf(s);
//                            System.out.println(s);
                            /*if it is the last non terminal in the production.*/
                            if (index == split.length - 1) {
                                if(Follow_set.keySet().contains(j)){
                                    Follow_set.put(s,Follow_set.get(j));
//                                    System.out.println(Follow_set);
                                } }
                            else {
                                for (String temp : split) {
                                    /*If followed by non terminal. */
                                    if (Arrays.asList(split).indexOf(temp) == index + 1 && nonterminals.contains(temp) && Main.Fmap.keySet().contains(temp)) {
                                        ArrayList<String> b = new ArrayList<>();
//                                        System.out.println("temp: " + temp);
                                        for (String q : Main.Fmap.get(temp)) {
//                                            System.out.println(q);
                                            if(!q.equals("~"))
                                            { b.add(q);}
                                        }
                                        if(Follow_set.keySet().contains(j)){
                                            for(String z: Follow_set.get(j)){
                                                if(!b.contains(z))
                                                {b.add(z);
                                                }
                                            }
                                        }
//                                        System.out.println("b:" + b);
                                        if(!Follow_set.keySet().contains(s)) {
                                            Follow_set.put(s,b);
//                                            System.out.println(Follow_set);
                                        }
                                        else break;
                                    }
                                    /*If followed by terminal. */
                                    else if(Arrays.asList(split).indexOf(temp) == index + 1 && terminals.contains(temp)){
//                                        System.out.println("temp: " + temp);
                                        ArrayList<String>c = new ArrayList<>();
                                        c.add(temp);
//                                        System.out.println("c:" + c);
                                        if(Follow_set.keySet().contains(s)) {
                                            for(String r: Follow_set.get(s)){
                                                if(!c.contains(r))
                                                {c.add(r);}
                                            }
                                            Follow_set.replace(s,c);
//                                            System.out.println(Follow_set);
                                        }
                                        else Follow_set.put(s,c);
//                                        System.out.println(Follow_set);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("\nFollow: ");
        System.out.println(Follow_set);
    }




}
