import java.util.ArrayList;
import java.util.Stack;


/*Implementation of the Thompson algorithm.*/
public class Thompson {

    private static ArrayList<Character> tChar = new ArrayList<>();

    public static NFA kleene(NFA current) {

        NFA newNFA = new NFA(current.states.size() + 2);
        tChar.add('~');
        newNFA.transitions.add(new trans(0, 1, tChar));

        for (trans t : current.transitions) {

            newNFA.transitions.add(new trans(t.stateFrom + 1, t.stateTo + 1, t.symp));

        }

        newNFA.transitions.add(new trans(current.states.size(), current.states.size() + 1, tChar));
        newNFA.transitions.add(new trans(current.states.size(), 1, tChar));
        newNFA.transitions.add(new trans(0, current.states.size() + 1, tChar));
        newNFA.finalState = current.states.size() + 1;
        tChar.clear();
        return newNFA;

    }


    public String contain(String input) {
        String output = input;
        for (String name : decider.NFAsub.keySet()) {

            if (input.contains(name)) {
                int index = input.indexOf(name);
                String value = decider.NFAsub.get(name);
                output = output.replace(name, value);

            }
        }
        return output;
    }


    public static NFA question(NFA current) {
        NFA newNFA = new NFA(current.states.size() + 2);
        tChar.add('~');
        newNFA.transitions.add(new trans(0, 1, tChar));
        for (trans t : current.transitions) {
            newNFA.transitions.add(new trans(t.stateFrom + 1, t.stateTo + 1, t.symp));

        }

        newNFA.transitions.add(new trans(current.states.size(), current.states.size() + 1, tChar));
        newNFA.transitions.add(new trans(0, current.states.size() + 1, tChar));
        newNFA.finalState = current.states.size() + 1;
        tChar.clear();
        return newNFA;
    }


    public static NFA concat(NFA first, NFA second) {
        second.states.remove(0);
        for (trans item : second.transitions) {

            first.transitions.add(new trans(item.stateFrom + first.states.size() - 1, item.stateTo + first.states.size() - 1, item.symp));

        }

        for (Integer state : second.states) {

            first.states.add(state + first.states.size() + 1);
        }

        first.finalState = (first.finalState) + (second.finalState);

        return first;

    }

    public static NFA positive(NFA current) {
        tChar.add('~');
        NFA newNFA = new NFA(current.states.size() + 2);
        newNFA.transitions.add(new trans(0, 1, tChar));

        for (trans t : current.transitions) {

            newNFA.transitions.add(new trans(t.stateFrom + 1, t.stateTo + 1, t.symp));

        }

        newNFA.transitions.add(new trans(current.states.size(), current.states.size() + 1, tChar));
        newNFA.transitions.add(new trans(current.states.size(), 1, tChar));

        newNFA.finalState = current.states.size() + 1;
        tChar.clear();
        return newNFA;

    }


    public static NFA union(NFA first, NFA second) {
        tChar.add('~');
        NFA unioned = new NFA(first.states.size() + second.states.size() + 2);
        unioned.transitions.add(new trans(0, 1, tChar));

        for (trans t : first.transitions) {

            unioned.transitions.add(new trans(t.stateFrom + 1, t.stateTo + 1, t.symp));

        }

        unioned.transitions.add(new trans(first.states.size(), first.states.size() + second.states.size() + 1, tChar));


        unioned.transitions.add(new trans(0, first.states.size() + 1, tChar));

        for (trans t : second.transitions) {

            unioned.transitions.add(new trans(t.stateFrom + first.states.size() + 1, t.stateTo + first.states.size() + 1, t.symp));

        }

        unioned.transitions.add(new trans(first.states.size() + second.states.size(), first.states.size() + second.states.size() + 1, tChar));

        unioned.finalState = first.finalState + second.finalState + 3;
        tChar.clear();
        return unioned;

    }

    public static boolean usedoperator(char c) {
        return c==' ' ||c == '\\' || c == '=' || c == '!' || c == '>' || c == '<' || c == '/' || c == ';' || c == '{' || c == '}' || c == ','||c == '.'||c==':';
    }

    public static boolean alpha(char c) {
        return c >= 'a' && c <= 'z';
    }

    public static boolean Calpha(char c) {
        return c >= 'A' && c <= 'Z';
    }

    public static boolean isOperand(char c) {
        return alpha(c) || Calpha(c) || Character.isDigit(c) || c == '~' || c == '.';
    }

    public static boolean isOperator(char c) {
        return c == '(' || c == ')' || c == '*' || c == '+' || c == '|' || c == '[' || c == ']' || c == '-';

    }

    public static boolean validateChar(char c) {

        return isOperand(c) || isOperator(c) || usedoperator(c);
    }

    public static boolean validateRegEx(String regEx) {

        if (regEx.isEmpty()) {
            System.out.print("Empty Regular expression!");
            return false;

        }
        for (char c : regEx.toCharArray()) {
            if (c == '*' || c == '+' || c == '|' || c == '-') {
                System.out.println("Invalid expression");
                System.exit(7);

            } else {

                break;
            }
        }


        for (char c : regEx.toCharArray()) {

            if (!validateChar(c)) {
                System.out.print("Invalid Regular Expression!");
                return false;

            }

        }

        return true;
    }


    public NFA generateNFA(String regex, String condition) {

        if (!validateRegEx(regex)) {
            System.out.println("Invalid regular expression!");
            return new NFA();
        }


        Stack<Character> operators = new Stack<>();
        Stack<NFA> operands = new Stack<>();
        Stack<NFA> waitedNFA = new Stack<>();
        boolean concatFlag = false;
        char op, c;
        int count = 0;
        boolean sBrackets = false;
        int sBcount = 0;
        NFA first, second;
        int used = 0;


        for (int i = 0; i < regex.length(); i++) {

            c = regex.charAt(i);
            if(c==' '){

                continue;
            }



            if (c == '\\' && i + 1 < regex.length() && (isOperator(regex.charAt(i + 1))|| usedoperator(c)))
                continue;

            else if (usedoperator(c) || (isOperator(c) && i - 1 >= 0 && regex.charAt(i - 1) == '\\')) {
                tChar.add(c);
                sBcount++;
                operands.push(new NFA(tChar));
                tChar.clear();

                if (sBcount % 2 == 0 || (i + 1 < regex.length() && regex.charAt(i + 1) == '(' || (i+1<regex.length()&&i-1 >= 0 && usedoperator(c)&& !isOperator(regex.charAt(i+1)) &&!usedoperator(regex.charAt(i+1))))) {

                    operators.push('.');

                    if (i + 1 < regex.length() && regex.charAt(i + 1) == '(') {
                        sBcount = 0;
                    }

                } else if (concatFlag && !sBrackets) {

                    operators.push('.');
                } else {

                    concatFlag = true;

                }

            }



            else if (isOperand(c) && !sBrackets && i + 1 < regex.length() && regex.charAt(i + 1) == '-') {


                sBrackets = true;
                operators.push(c);
                sBcount++;





            } else if (c == '-') {


                operators.push(c);

            } else if (isOperand(c) && i - 1 >= 0 && regex.charAt(i - 1) == '-') {
                operators.push(c);
                if (!sBrackets) {
                    System.out.println("Invalid expression error in interval");
                    System.exit(4);
                } else {


                    char c1 = operators.pop();
                    operators.pop();
                    char c2 = operators.pop();
                    for (int i1 = (int) c2; i1 <= (int) c1; i1++) {

                        tChar.add((char) i1);
                    }
                    operands.push(new NFA(tChar));
                    tChar.clear();
                    sBrackets = false;
                    //concatFlag = true;
                    sBcount++;
                    if (sBcount % 2 == 0) {
                        if (i + 1 < regex.length() && (regex.charAt(i + 1) != '|' && regex.charAt(i + 1) != '*' && regex.charAt(i + 1) != '+' && regex.charAt(i + 1) != ')')) {

                            operators.push('.');

                        }
                    }

                }
            } else if (isOperand(c) && !sBrackets) {


                tChar.add(c);
                sBcount++;
                operands.push(new NFA(tChar));
                tChar.clear();

                if (sBcount % 2 == 0 || (i + 1 < regex.length() && regex.charAt(i + 1) == '(')) {

                    operators.push('.');
                    if (i + 1 < regex.length() && regex.charAt(i + 1) == '(') {
                        sBcount = 0;
                    }
                } else if (concatFlag && !sBrackets) {

                    operators.push('.');
                } else {

                    concatFlag = true;

                }

            } else {

                if (c == ')') {
                    concatFlag = false;
                    if (count == 0) {

                        System.out.println("ERROR: more ending parentheses than beginning parentheses.");
                        System.exit(2);

                    } else {

                        count--;

                    }

                    while (!operators.isEmpty() && operators.peek() != '(') {
                        op = operators.pop();


                        if (op == '.') {

                            first = operands.pop();
                            second = operands.pop();
                            operands.push(concat(second, first));

                        } else if (op == '|') {

                            second = operands.pop();

                            if (!operators.isEmpty() && operators.peek() == '.') {

                                waitedNFA.push(operands.pop());


                                while (!operators.isEmpty() && operators.peek() == '.') {

                                    waitedNFA.push(operands.pop());
                                    operators.pop();

                                }

                                first = concat(waitedNFA.pop(), waitedNFA.pop());

                                while (!waitedNFA.isEmpty()) {

                                    first = concat(first, waitedNFA.pop());
                                }

                            } else {


                                first = operands.pop();

                            }
                            operands.push(union(first, second));

                        }


                    }
                    operators.pop();
                    if(i+1 < regex.length() && ((regex.charAt(i+1) == '(')|| usedoperator(regex.charAt(i+1)))){

                        operators.push('.');
                    }

                } else if (c == '*') {

                    operands.push(kleene(operands.pop()));
                    concatFlag = true;
                    if(i+1<regex.length()&&usedoperator(regex.charAt(i+1)) && i-1 > 0 && isOperator(regex.charAt(i-1))){

                        operators.push('.');
                        used =0;

                    }
                } else if (c == '+') {

                    operands.push(positive(operands.pop()));
                    concatFlag = true;
                    if(i+1<regex.length()&&usedoperator(regex.charAt(i+1)) && i-1 > 0 && isOperator(regex.charAt(i-1))){

                        operators.push('.');
                        used =0;

                    }
                } else if (c == '(') {

                    operators.push(c);
                    count++;
                } else if (c == '|') {

                    operators.push(c);
                    concatFlag = false;
                    if(sBcount%2 !=0)
                        sBcount--;

                }



            }
        }

        while (operators.size() > 0) {

            if (operands.isEmpty()) {

                System.out.println("Imbalance between operands and operators!");
                System.exit(3);
            }

            op = operators.pop();
            if (op == '.') {

                second = operands.pop();
                first = operands.pop();
                operands.push(concat(first, second));

            } else if (op == '|') {
                second = operands.pop();

                if (!operators.isEmpty() && operators.peek() == '.') {

                    waitedNFA.push(operands.pop());
                    while (!operators.isEmpty() && operators.peek() == '.') {

                        waitedNFA.push(operands.pop());
                        operators.pop();
                    }

                    first = concat(waitedNFA.pop(), waitedNFA.pop());

                    while (!waitedNFA.empty()) {

                        first = concat(first, waitedNFA.pop());
                    }
                } else {

                    first = operands.pop();
                }

                operands.push(union(first, second));

            }
        }


        NFA last;
        if (condition.equals("=")) {
            //System.out.println("Done!");
            last =operands.pop();
        } else {


            for (String name : decider.NFAused.keySet()) {

                operands.push(decider.NFAused.get(name));


            }
            decider.NFAused.clear();
            while (operands.size() > 1) {

                operands.push(union(operands.pop(), operands.pop()));
            }

            last = operands.pop();




            return last;
        }
        return last;
    }
}