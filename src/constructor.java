import java.util.ArrayList;

public class constructor {

    public ArrayList<String> myLines = new ArrayList<>();
    private String part1 = new String("");
    private String part2 = new String("");
    private boolean change = false;

    public constructor(ArrayList myLines) {
        this.myLines = myLines;
    }

    public void separateLines() {
        for (int line = 0; line < this.myLines.size(); line++) {
            String currentLine = this.myLines.get(line);
            char C;
            if (currentLine == "") {
                System.out.println("input contain empty line");
                System.exit(4);
            }
            for (int i = 0; i < currentLine.length(); i++) {
                C = currentLine.charAt(i);
                // if (C == ' ') continue;
                if (C == '=' && !change) {
                    change = true;

                } else if (C == '#' ) {
                    if(line == 0)continue;
                    Main.lines.put(part1, part2);
                    part1 = "";
                    part2 = "";
                    change = false;
                } else {
                    if (change) {
                        part2 += C;
                    } else {
                        part1 += C;
                    }
                }
            }
        }
        Main.lines.put(part1,part2);
        //System.out.println(main.lines);
        //Set keys = main.lines.keySet();
        //System.out.println(keys);

        //System.out.println(main.lines.keySet());
        //System.out.println(main.lines.values());

    }
}
