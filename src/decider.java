import java.util.HashMap;
import java.util.ArrayList;

public class decider {

    Thompson n = new Thompson();
    static HashMap<String,NFA> NFAused = new HashMap<>();
    static HashMap<String,NFA> NFAneeded = new HashMap<>();
    static HashMap<String,String> NFAsub = new HashMap<>();
    public static ArrayList<String>keywords= new ArrayList<>();
    public static ArrayList<String>relop= new ArrayList<>();
    public static ArrayList<String>mulop= new ArrayList<>();
    public static ArrayList<String>addop= new ArrayList<>();
    public static ArrayList<String>assign= new ArrayList<>();
    public static ArrayList<String>incop= new ArrayList<>();
    public static ArrayList<String>decop= new ArrayList<>();
    public static ArrayList<String>punctuations= new ArrayList<>();


    String seperated[] = new String[3];

    public decider(String[] seperated) {
        this.seperated = seperated;
    }

    public NFA decide(String separated []){
        NFA nfa_input = new NFA();
        int i=0;
        if(separated[2] == "{"){
            boolean start = false;
            String temp =new String();
            temp = "";
            for(int  x = 0;x < separated[0].toCharArray().length;x++){
                char x2 = separated[0].toCharArray()[x];

                if((x2 == ' '&& start && separated[0].toCharArray()[x+1] != ' ') || separated[0].toCharArray()[x] == '}'){
                    if(temp!=""){
                        start = false;
                        NFA input = n.generateNFA(temp,separated[2]);
                        NFAused.put(separated[0].substring(0,separated[0].length()-1),input);
                        temp = "";
                    }
                }

                else {
                    temp += x2;
                    start = true;

                }

            }
            String [] s=separated[0].substring(0,separated[0].length()-1).split(" ");

            for (int r=0;r<s.length;r++){
                keywords.add(s[r]);

            }

        }

        else{
            //System.out.println(separated[0]);
            //System.out.println(separated[1]);

            String subLine = n.contain(separated[1]);

            nfa_input = n.generateNFA(subLine,separated[2]);
            if(separated[2].equals(":")){
                NFAused.put(separated[0], nfa_input);
                String  s1=separated[1].replaceFirst("\\(", "").replaceAll("\\\\", "").replaceAll("\\|", " ");
                String [] s=s1.split(" ");
                if(separated[0].equals("relop")){
                    for (int r=0;r<s.length;r++){
                        relop.add(s[r]);
                    }
                }
                else if(separated[0].equals("mulop")){
                    for (int r=0;r<s.length;r++){
                        mulop.add(s[r]);
                    }
                }
                else if(separated[0].equals("addop")){
                    for (int r=0;r<s.length;r++){
                        addop.add(s[r]);
                    }
                }
                else if(separated[0].equals("incop")){
                    for (int r=0;r<s.length;r++){
                        incop.add(s[r]);
                    }
                }
                else if(separated[0].equals("decop")){
                    for (int r=0;r<s.length;r++){
                        decop.add(s[r]);
                    }
                }
                else if(separated[0].equals("assign")){
                    for (int r=0;r<s.length;r++){
                        assign.add(s[r]);
                    }
                }
                else {
                    punctuations.add(s1);
                  //  punctuations.add(separated[0]);
                }
            }
            else{
                NFAneeded.put(separated[0], nfa_input);
                NFAsub.put(separated[0],separated[1]);


            }


        }

        return nfa_input;
    }
}