
public class separator {

    public String complete = new String();


    public separator(String complete) {
        this.complete = complete;
    }

    public String[] separate(String one) {
        int k=0;
        boolean end = false;
        if (one.equals("")) {
            System.out.println("Blank line found.");
            System.exit(9);

        }
        String separated[] = new String[3];
        separated[0] = "";
        separated[1] = "";
        separated[2] = "";
        for (int i = 0; i < one.length(); i++) {
            if (one.charAt(i) == ' ') {

                continue;
            }
            else {
                k=i;
                break;
            }

        }

        char x = one.charAt(k);

        if (x == '{') {
            for(int item = k+1;item<one.length(); item++){
                if(one.charAt(item ) == ' '){continue;}
                else if(one.charAt(item) == '='||one.charAt(item) == ':'){
                    {
                        separated[0] += one.charAt(item-1);
                        for ( int i = item;i<one.length();i++){

                            char c = one.charAt(i);
                            if (c == ' ') {
                            } else {
                                if (!end) {
                                    if (c == ':' || c == '=') {
                                        if (c == ':')
                                            separated[2] = ":";
                                        else
                                            separated[2] = "=";
                                        end = true;


                                    } else {
                                        separated[0] += c;
                                    }

                                } else {

                                    separated[1] += c;

                                }
                            }
                            item = i;
                        }


                    }

                }
                else{
                    separated[0] = one.substring(k + 1, one.length());
                    separated[2] = "{";

                }
            }
        }

        else {
            for (char c : one.toCharArray()) {


                if (c == ' ') {
                } else {
                    if (!end) {
                        if (c == ':' || c == '=') {
                            if (c == ':')
                                separated[2] = ":";
                            else
                                separated[2] = "=";
                            separated[1] = "(";
                            end = true;


                        } else {
                            separated[0] += c;
                        }

                    }
                    else {

                        separated[1] += c;

                    }
                }
            }

            if(separated[2] == "="){
                separated[1] += ')';
            }
        }


        return separated;

    }

}