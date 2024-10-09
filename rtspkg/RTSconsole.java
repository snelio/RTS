package rtspkg;

/*
 *
 *
 */

public class RTSconsole 
{

    public final static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");
            // String[] arr;
            // arr = new String[1];

            if (os.contains("Windows")) //Windows
            {
                // arr[0] = "cls";
                // Runtime.getRuntime().exec(arr);
                System.out.print("\033[H\033[2J"); 
                System. out. flush();
                // Runtime.getRuntime().exec("cls");
                // exec("cls");
            }
            else // Unix, Linux, Solaris, z/OS, etc.
            {
                System.out.print("\033[H\033[2J");
                System. out. flush();
            }
        }
        catch (final Exception e)
        {
            // handle exception
        }
    }

    public final static void resetcolor()
    {
        try {
            
            // this doesn't work well, because it actually sets some colors.
            // System.out.print("\u001B[30m" + "\u001B[40;1m");

            // reset all styles to default
            System.out.print("\u001b[0m"); 

        } catch (final Exception e) {
            // handle exception
        }
    }

    /*
     *  Use following ANSI color codes to set font colors:
     *  BLACK 	\u001B[30m 	BLACK_BACKGROUND 	\u001B[40m
     *  RED 	\u001B[31m 	RED_BACKGROUND 	    \u001B[41m
     *  GREEN 	\u001B[32m 	GREEN_BACKGROUND 	\u001B[42m
     *  YELLOW 	\u001B[33m 	YELLOW_BACKGROUND 	\u001B[43m
     *  BLUE 	\u001B[34m 	BLUE_BACKGROUND 	\u001B[44m
     *  PURPLE 	\u001B[35m 	PURPLE_BACKGROUND 	\u001B[45m
     *  CYAN 	\u001B[36m 	CYAN_BACKGROUND 	\u001B[46m
     *  WHITE 	\u001B[37m 	WHITE_BACKGROUND 	\u001B[47m
     */

    // Set only foreground color.
    public static void setcolor(String foreground)
    {
  
        try {
            var newForeground = switch (foreground) {
                case "black", "bla" -> "\u001B[30m";
                case "red"          -> "\u001B[31m";
                case "green", "gre" -> "\u001B[32m";
                case "yellow", "yel"-> "\u001B[33m";
                case "blue", "blu"  -> "\u001B[34m";
                case "purple", "pur"-> "\u001B[35m";
                case "cyan", "cya"  -> "\u001B[36m";
                case "white", "whi" -> "\u001B[37m";
                default -> "\u001B[30m";
            };

            System.out.print(newForeground); 

        } catch (final Exception e) {
            // handle exception
        }
    }

    // Overload method to set foreground + additional format (blink, bold, etc.)
    public static void setcolor(String foreground, boolean blink, boolean bold, boolean underln)
    {
        RTSconsole.setcolor(foreground);
        if (blink) System.out.print("\u001b[5m");
        if (bold) System.out.print("\u001b[1m");
        if (underln) System.out.print("\u001b[4m");
    }

    // Overload method to set both: foreground and background colors.
    public static void setcolor(String foreground, String background)
    {
  
        try {
            var newForeground = switch (foreground) {
                case "black", "bla" -> "\u001B[30m";
                case "red"          -> "\u001B[31m";
                case "green", "gre" -> "\u001B[32m";
                case "yellow", "yel"-> "\u001B[33m";
                case "blue", "blu"  -> "\u001B[34m";
                case "purple", "pur"-> "\u001B[35m";
                case "cyan", "cya"  -> "\u001B[36m";
                case "white", "whi" -> "\u001B[37m";
                default -> "\u001B[30m";
            };

            var newBackground = switch (background) {
                case "black", "bla" -> "\u001B[40m";
                case "red"          -> "\u001B[41m";
                case "green", "gre" -> "\u001B[42m";
                case "yellow", "yel"-> "\u001B[43m";
                case "blue", "blu"  -> "\u001B[44m";
                case "purple", "pur"-> "\u001B[45m";
                case "cyan", "cya"  -> "\u001B[46m";
                case "white", "whi" -> "\u001B[47m";
                default -> "\u001B[40m";
            };

            System.out.print(newForeground + newBackground); 

        } catch (final Exception e) {
            // handle exception
        }
    }

   // Format text before printing to the console.
    public static String textFormat(String txt)
    {
        // BLINK
        txt = txt.replace("<i>","\u001b[5m");
        txt = txt.replace("</i>","\u001b[0m");

        // BOLD
        txt = txt.replace("<b>","\u001b[1m");
        txt = txt.replace("</b>","\u001b[0m");

        // UNDERLINE
        txt = txt.replace("<u>","\u001b[4m");
        txt = txt.replace("</u>","\u001b[0m");

        // FOREGROUND COLOR
        txt = txt.replace("<scf_bla>","\u001B[30m");
        txt = txt.replace("<scf_red>","\u001B[31m");
        txt = txt.replace("<scf_gre>","\u001B[32m");
        txt = txt.replace("<scf_yel>","\u001B[33m");
        txt = txt.replace("<scf_blu>","\u001B[34m");
        txt = txt.replace("<scf_pur>","\u001B[35m");
        txt = txt.replace("<scf_cya>","\u001B[36m");
        txt = txt.replace("<scf_whi>","\u001B[37m");
        txt = txt.replace("</sc>","\u001b[0m");

        // BACKGROUND COLOR
        txt = txt.replace("<scb_bla>","\u001B[40m");
        txt = txt.replace("<scb_red>","\u001B[41m");
        txt = txt.replace("<scb_gre>","\u001B[42m");
        txt = txt.replace("<scb_yel>","\u001B[43m");
        txt = txt.replace("<scb_blu>","\u001B[44m");
        txt = txt.replace("<scb_pur>","\u001B[45m");
        txt = txt.replace("<scb_cya>","\u001B[46m");
        txt = txt.replace("<scb_whi>","\u001B[47m");
        txt = txt.replace("</sc>","\u001b[0m");

        return txt;
    }

    /*
     * Percentage progress info
     * [  0%]⏳ [ 12%]⏳ [100%]🆗
     */
    public static void printProgressPct(int percent)
    {
        switch (String.valueOf(percent).length())
        {
            case 1 -> System.out.print("[  "+ String.valueOf(percent) +"%]⏳"); 
            case 2 -> System.out.print("[ "+ String.valueOf(percent) +"%]⏳"); 
            case 3 -> System.out.print("["+ String.valueOf(percent) +"%]🆗"); 
            default-> System.out.print("[    ]?");
        }

    }

    /*
     * Draw progress bar using following Unicode signs:
     * ██████████████████████████████████████████████████████████████
     * U+2588 		█ 	&#9608; &#x2588; 	FULL BLOCK
     * U+2589 		▉ 	&#9609; &#x2589; 	LEFT SEVEN EIGHTHS BLOCK
     * U+258A 		▊ 	&#9610; &#x258a; 	LEFT THREE QUARTERS BLOCK
     * U+258B 		▋ 	&#9611; &#x258b; 	LEFT FIVE EIGHTHS BLOCK
     * U+258C 		▌ 	&#9612; &#x258c; 	LEFT HALF BLOCK
     * U+258D 		▍ 	&#9613; &#x258d; 	LEFT THREE EIGHTHS BLOCK
     * U+258E 		▎ 	&#9614; &#x258e; 	LEFT ONE QUARTER BLOCK
     * U+258F 		▏ 	&#9615; &#x258f; 	LEFT ONE EIGHTH BLOCK
     */
    public static void printProgressBar(double percent,int signs)
    {
        double fullBlocks = Math.floor((double) percent/100*signs);
        //double partInt = Math.floor(25.0/100*64);
        double partBlock = (percent - 100*fullBlocks/signs)*signs/100;
        int quarterNo = (int) Math.floor(partBlock*8);

        //4test:
        //System.out.println("fullBlocks="+String.valueOf(fullBlocks)+";");
        //System.out.println("partBlock="+String.valueOf(partBlock)+";");
        //System.out.println("quarterNo="+String.valueOf(quarterNo)+";");

        //Initiate blocks used to print progress bar
        char c8 = '█';
        char c7 = '▉';
        char c6 = '▊';
        char c5 = '▋';
        char c4 = '▌';
        char c3 = '▍';
        char c2 = '▎';
        char c1 = '▏';

        //Print full blocks
        {for(int i = 0; i < fullBlocks; i++)
            System.out.print(c8);
        }

        //Print partial block
        switch(quarterNo)
        {
            case 1 -> System.out.print(c1);
            case 2 -> System.out.print(c2);
            case 3 -> System.out.print(c3);
            case 4 -> System.out.print(c4);
            case 5 -> System.out.print(c5);
            case 6 -> System.out.print(c6);
            case 7 -> System.out.print(c7);
            case 8 -> System.out.print(c8);
        }

        //Print remaining blanks to reach given number of signs
        {for(int i = 0; i < signs - (fullBlocks + Integer.signum(quarterNo)); i++)
            System.out.print(' ');
        }
        
    }

    public static void initDBwarning() {
        // If database is not print warning.
        RTSconsole.setcolor("yellow", true, true, false);
        System.out.println("Database has not been set up. Run: MAIN MENU > CONFIGURATION [9] > DB SET UP [S]");
        RTSconsole.resetcolor();
    }                    

    public static void noSimulationWarning() {
        // If there is no simulation data to be saved/printed/erased
        RTSconsole.setcolor("green", false, false, false);
        System.out.println("First you have to generate some data. MAIN MENU > START [1] > New simulation [C]");
        RTSconsole.resetcolor();
    }                    

    public static void invalidValueWarning() {
        RTSconsole.setcolor("red", false, false, false);
        System.out.println("This value is invalid!");
        RTSconsole.resetcolor();
    }

}
