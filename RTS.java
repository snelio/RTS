/*
 * 
 *
 *
 *
 */

import java.io.Console;
import rtspkg.RTSMenu;
import rtspkg.RTSconsole;
import rtspkg.RTSdebug;
import rtspkg.RTSfile;
import rtspkg.RTSmath;

public class RTS {

    private static int maxRandBound = 0;

    /*
     * Standard getters:
     */
    public static int getMaxRandBound() {
        return maxRandBound;
    }
    public static void setMaxRandBound(int newVal) {
        maxRandBound = newVal;
    }

    /*
     * MAIN
     */
    public static void main(String[] args) {

        //Init values
        String input;               //user input from stdin
        String prompter = ">";      //prompter sign at the beginning of the line
        char newOpt = ' ';          //new picked (typed) menu option - 1st input char
        String curPos = "0";        //current position in the program menu

        //Obtaining a reference to the console. 
        Console con = System.console(); 

        /*
         * Constructors:
         */
        RTSMenu menuRTS = new RTSMenu(newOpt);
        menuRTS.loadRTSMenus();

        /*
         * Load information about countries and allowed plate numbers' prefixes.
         */
        // general information per country (code, xml file, etc.)
        String country_data[][] = RTSfile.loadXMLcountries(menuRTS.DEBUG_MODE);
        // plate no. prefixes for one country
        String country_plates[][];
        // plate no. prefixes for all countries
        String all_plates[][] = null;
        // Final results are put here
        String RTS_result[][] = null;
        // menuEntryCounter
        int menuEntryCounter = 0;

        // for each country load all plate prefixes
        for(int i = 0; i<country_data.length; i++){
            // load country plates from given xml file as input arg.
            country_plates = RTSfile.loadCountryPlates(country_data[i][0], country_data[i][1], menuRTS.DEBUG_MODE);
            all_plates = RTSmath.arrayAdd(all_plates, country_plates);

            /*
                SQL: load all plates from this country into a RTSplates table
                Below conditions are artificial and were made only to show
                two equivalent methods of accesing database name (DBNAME).
                switch (i % 2) {
                    case 0 -> RTSdb.sqlLoadPlates(menuRTS.DBNAME, menuRTS.DEBUG_MODE, country_data[i][0], country_plates);
                    case 1 -> RTSdb.sqlLoadPlates(menuRTS.getDB(), menuRTS.getDEBUG_MODE(), country_data[i][0], country_plates);
                }
            */
        }
        // set incremental population based values and get max value
        maxRandBound = RTSmath.setIncrPop(all_plates);
        // RTS.setMaxRandBound(RTSmath.getMaxRandBound());
        
        //DEBUG_MODE: print country code + array [plates] values
        if (menuRTS.DEBUG_MODE) {
            System.out.println("<DEBUG_MODE=true LogSrc=RTS>");
            System.out.println("### MAIN ARRAY WITH ALL POSSIBLE PLATES ###");
            //RTSdebug.showArray2Dstr(all_plates);
            RTSdebug.showArray(all_plates);
            RTSdebug.sleep(3000); // pause before printing menu
        }


        /*
         * Main loop
         */
        while ('Q' != curPos.charAt(curPos.length()-1)) { 
            RTSconsole.clearConsole();

            // DEBUG_MODE: How many times menu was printed
            if(menuRTS.getDEBUG_MODE()) {
                menuEntryCounter = RTSmath.counterUp(menuEntryCounter);
                System.out.println("##### MENU PRINTED "+menuEntryCounter+" TIME(S) #####");
            }

            //4test only
            //System.out.println("before procPos curPos="+curPos);
            //System.out.println("curPos.charAt(curPos.length()-1))="+curPos.charAt(curPos.length()-1));

            // Print menu            
            menuRTS.printMenu(curPos);

            // Process current menu position (curPos)
            RTS_result = menuRTS.procPos(curPos, all_plates, RTS_result);

            // Trim last letter (if there is any) from curPos
            curPos = menuRTS.trimMenuPosLetter(curPos);

            // DEBUG_MODE: print whole content of RTS_result array
            // if(menuRTS.getDEBUG_MODE() && !RTSmath.isArrayNullOrEmpty(RTS_result)) RTSdebug.showArray(RTS_result);

            //4test only
            //System.out.println("after procPos curPos="+curPos);


            // Get user input and convert uppercase.
            input = con.readLine(prompter);
            if (input.length()==0) input = " ";
            newOpt = Character.toUpperCase(input.charAt(0));
            curPos = menuRTS.setNewPos(curPos, newOpt);


            // DEBUG_MODE: pause before printing menu
            // if (menuRTS.DEBUG_MODE) {
            //     System.out.println("curPos="+curPos);
            //     System.out.println("newOpt="+newOpt);
            //     RTSdebug.sleep(3000);
            // }

        }
        
    }    

}
