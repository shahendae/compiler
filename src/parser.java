import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.List;

public class parser {


    public static String[][] table;
    public static Stack<String> s=new Stack<String>();
    public static int index=0;
    public static ArrayList<String>  left_derivation= new ArrayList<String> ();
    public static String[] test ;
    public static List<String> derivation=new ArrayList<String>();


    void parser(){}


    public static String[] reverse(String str){
        String[] s=str.split(" ");
        String[] t=new String[s.length];
        int y=0;
        for(int i=s.length-1;i>=0;i--)
            t[y++]=s[i];

        return t;
    }

    public static void shiftRight(String[] s,int index)
    {
        int j=0;
        for(j=0;j<s.length;j++){

            if(left_derivation.size()-1>=1){    left_derivation.add("null");
                for(int i = left_derivation.size()-1; i > index; i--)
                    left_derivation.set(i,left_derivation.get(i-1));



                left_derivation.set(index, s[j]);
            }
            else if(left_derivation.size()-1==0){
                left_derivation.add(left_derivation.get(0));
                left_derivation.set(index, s[j]);

            }
            else if(left_derivation.size()==0)  {
                left_derivation.add( s[j]);
            }
        }




    }



    public static void parse(){
        s.push("$");
        s.push(CFG.start);
        String c="";
        String epsilon="~";
        String top="";
        left_derivation.add(CFG.start);
        int derivation_index=0;
        while(index<test.length){

            derivation.add(left_derivation.toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(",", ""));
            System.out.println();
            c=test[index];
            System.out.print(s.toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(",", "").replaceAll(" ", ""));
            System.out.print("\t");
            for(int y=index;y<test.length;y++)
                System.out.print(test[y]+" ");
            top=s.pop();
            if(top.equals("$")){
                // System.out.println(top);

                break;}
            if(top.equals(c)){
                // System.out.println(c);

                index++;
                continue;
            }
            else if(first_follow.terminals.contains(top)){

                System.out.print("\tError: missing "+top+", inserted");
                continue;
            }

            for(int r=1;r<parserTable.table.length;r++){
                if(parserTable.table[r][0].equals(top)){
                    for(int cols=1;cols<parserTable.table[0].length;cols++){ // System.out.print("\t first"+top+"->"+parserTable.table[r][cols]+"   "+c);
                        if(c.equals(parserTable.table[0][cols])){
                            if(epsilon.equals(parserTable.table[r][cols]) ){
                                System.out.print("\t"+top+"->"+parserTable.table[r][cols]);
                                break;
                            }
                            else if(parserTable.table[r][cols] == null){
                                System.out.print("\tError:(illegal "+top+") – discard "+c);
                                s.push(top);
                                index++;
                                break;
                            }
                            else if(parserTable.table[r][cols].equals("SYNC") ){
                                System.out.print("\t"+top+"-> SYNC");
                                break;
                            }

                            else{
                                String[] rev=reverse(parserTable.table[r][cols]);
                                for(int i=0;i<rev.length;i++)
                                    s.push(rev[i]);
                                System.out.print("\t"+top+"->"+parserTable.table[r][cols]);
                                if(first_follow.nonterminals.contains(left_derivation.get(derivation_index))){
                                    left_derivation.remove(derivation_index);
                                }

                                String[] temp=parserTable.table[r][cols].split(" ");
                                shiftRight(rev,derivation_index);
                                if(first_follow.terminals.contains(left_derivation.get(derivation_index))){
                                    while(first_follow.terminals.contains(left_derivation.get(derivation_index))){
                                        derivation_index++;
                                    }
                                }

                            }
                        }
                    }
                }

            }

        }
        if(top.equals("$") && c.equals("$"))
            System.out.println("\naccept, successful completion");
        else
            System.out.println("\nreject");




    }


    public static void readParsingTable() throws FileNotFoundException {


        Maximal_munch.output+="$" ;
        test=Maximal_munch.output.split(" ");





        System.out.println();
        parse();
        // System.out.println("accept, successful completion");
        System.out.println("\n\nLeft derivation:\n");
        for(int j=0;j<derivation.size();j++)
            System.out.println(derivation.get(j));

    }


}