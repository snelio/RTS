package rtspkg;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Scanner;

/*
 *
 *
 */

public class RTSMenu {

    //private final char cur_Opt;
    private char new_Opt;
    private String cur_Pos;

    /*
     * Load configuration from xml files
     */
    // Get main configuration for ...
    RTSxmlMap xmlmap = new RTSxmlMap(new File("./config/config.xml")); // fixed path for main XML config file
    // ... database
    public String DBNAME = xmlmap.lookupSymbol("DBNAME");

    //... debug mode (if set to true additional info will be send to stdout)
    public Boolean DEBUG_MODE = Boolean.valueOf(xmlmap.lookupSymbol("DEBUG_MODE"));

    //... SIMULATION PARAMETER: average number of vehicles per one day 
    private int AVG_NUM = Integer.parseInt(xmlmap.lookupSymbol("SIM_PARAM_AVG_NUM"));

    //... SIMULATION PARAMETER: maximum allowed average number of vehicles in AVG_NUM
    private final int MAX_AVG = Integer.parseInt(xmlmap.lookupSymbol("SIM_PARAM_MAX_AVG"));

    //... SIMULATION PARAMETER: standard deviation from SIM_PARAM_AVG_NUM
    private int STD_DEV = Integer.parseInt(xmlmap.lookupSymbol("SIM_PARAM_STD_DEV"));

    //... SIMULATION PARAMETER: number of days to be simulated (generated)
    private int DAY_NUM = Integer.parseInt(xmlmap.lookupSymbol("SIM_PARAM_DAY_NUM"));

    //... SIMULATION PARAMETER: init day - 1st date to be saved in results
    private String DAY_INI = xmlmap.lookupSymbol("SIM_PARAM_DAY_INI");

    //... SIMULATION PARAMETER: who made last changes (user or by default)
    private String PRM_BY = "default";

    //... ANIMATION PARAMETER: progress bar min percent animation step
    private final double ANIM_BAR_STEP = Double.parseDouble(xmlmap.lookupSymbol("ANIMATION_BAR_STEP"));

    //... ANIMATION PARAMETER: progress bar animation sleep time
    private final int ANIM_SLEEP_TIME = Integer.parseInt(xmlmap.lookupSymbol("ANIMATION_SLEEP_TIME"));


    // ... filepaths for MENU config files
    // Path filepath_MENU_0 = Paths.get("./config/txt/MENU_0.txt");
    // Path filepath_MENU_00 = Paths.get("./config/txt/MENU_00.txt");
    // Path filepath_MENU_01 = Paths.get("./config/txt/MENU_01.txt");
    // Path filepath_MENU_02 = Paths.get("./config/txt/MENU_02.txt");
    // Path filepath_MENU_03 = Paths.get("./config/txt/MENU_03.txt");
    // Path filepath_MENU_09 = Paths.get("./config/txt/MENU_09.txt");
    Path filepath_MENU_0 = Paths.get(xmlmap.lookupSymbol("MENU_0"));
    Path filepath_MENU_00 = Paths.get(xmlmap.lookupSymbol("MENU_00"));
    Path filepath_MENU_01 = Paths.get(xmlmap.lookupSymbol("MENU_01"));
    Path filepath_MENU_02 = Paths.get(xmlmap.lookupSymbol("MENU_02"));
    Path filepath_MENU_03 = Paths.get(xmlmap.lookupSymbol("MENU_03"));
    Path filepath_MENU_09 = Paths.get(xmlmap.lookupSymbol("MENU_09"));

    /*
     * Load and initialize menus from txt files
     */
    String MENU_0;
    String MENU_00;
    String MENU_01;
    String MENU_02;
    String MENU_03;
    String MENU_09;


    /*
     * Constructor
     */
    public RTSMenu(char newOpt) 
    {
        //cur_Opt = Character.toUpperCase(curOpt);
        new_Opt = Character.toUpperCase(newOpt);

        //Correct value if it is greater than max allowed
        AVG_NUM = (AVG_NUM > MAX_AVG) ? MAX_AVG : AVG_NUM;
    }

    /*
     * Standard getters:
     */
    // Database name
    public String getDB() {
        return DBNAME;
    }
    // Debug mode
    public Boolean getDEBUG_MODE() {
        return DEBUG_MODE;
    }


    /*
     * Check if menu position is valid or not.
     */
    public boolean isValidPos(String menuPos)
    {
        //Ensure user picks only allowed options
        return switch (menuPos)
        {
            //Main menu screens
            case "0", "00", "01", "02", "03", "09" -> true;
            //Quit option picked
            case "0Q", "00Q", "01Q", "02Q", "03Q", "09Q" -> true;
            //Other
            case "010","01C","01R","01P" -> true;
            case "020","02S","02E","02P" -> true;
            case "030" -> true;
            case "090","09S","09X" -> true;
            //Unknown
            default -> false;
        };
    };

    /*
     * 
     */
    public String setNewPos(String curPos, char newOpt)
    {
        cur_Pos = curPos.toUpperCase();
        new_Opt = Character.toUpperCase(newOpt);

        //New menu option/position based on user input
        if (Character.isLetter(new_Opt)) 
            cur_Pos = cur_Pos + new_Opt;
        else if (Character.isDigit(new_Opt)) 
            switch(new_Opt){
                // New option [0] means "go back" to previous screen, 
                // only if we are not in main (initial) menu screen.
                // If so cut last character from current menu position.
                case '0'-> cur_Pos = cur_Pos.substring(0, cur_Pos.length() - 1);
                //Any other digit means user picked another screen.
                default -> cur_Pos = cur_Pos + new_Opt;
            }

        // New option [0] on main menu "0" means go to "About" screen. 
        if ("".equals(cur_Pos)) cur_Pos ="00";

        //Do nothing if wrong menu option was picked
        if (!isValidPos(cur_Pos)) return curPos;

        //Otherwise return new menu position to process
        return cur_Pos;

    };

    /*
     * Load text for each menu screen
     */
    public void loadRTSMenus (){
        try {
            //Java 11 Files.readString returns a String () from a file (max 2 GB).
            // MENU_00 = Files.readString(Paths.get("MENU_00.txt"), StandardCharsets.UTF_8);
            MENU_0 = Files.readString(filepath_MENU_0, StandardCharsets.UTF_8);
            MENU_00 = Files.readString(filepath_MENU_00, StandardCharsets.UTF_8);
            MENU_01 = Files.readString(filepath_MENU_01, StandardCharsets.UTF_8);
            MENU_02 = Files.readString(filepath_MENU_02, StandardCharsets.UTF_8);
            MENU_03 = Files.readString(filepath_MENU_03, StandardCharsets.UTF_8);
            MENU_09 = Files.readString(filepath_MENU_09, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            MENU_00 = MENU_01 = MENU_02 = MENU_03 = MENU_09
            = "ERROR: Some configuration file(s) is missing.";
        }

        //format text in all menu
        this.formatRTSMenus();
    };

    /*
     * Format text in each menu (bold, undeline, blinks, etc.) before printing to console.
     */
    public void formatRTSMenus (){
        MENU_0 = RTSconsole.textFormat(MENU_0);
        MENU_00 = RTSconsole.textFormat(MENU_00);
        MENU_01 = RTSconsole.textFormat(MENU_01);
        MENU_02 = RTSconsole.textFormat(MENU_02);
        MENU_03 = RTSconsole.textFormat(MENU_03);
        MENU_09 = RTSconsole.textFormat(MENU_09);
    }

    /*
     * Print appropriate menu
     */
    public void printMenu (String menuPos){

            switch (menuPos) {
                case "0"
                    -> System.out.print(MENU_0);
                case "00"
                    -> System.out.print(MENU_00);
                case "01", "01C", "01R", "01P"
                    -> System.out.print(MENU_01);
                case "02","02S","02E", "02P" 
                    -> System.out.print(MENU_02);
                case "03"
                    -> System.out.print(MENU_03);
                case "09","09S","09X" 
                    -> System.out.print(MENU_09);
                default 
                    -> System.out.print(MENU_0);
            }

    };

    /*
     * Show Simulation Parameters
     */
    public void showSimParams (){
        System.out.println("SIM_PARAM_AVG_NUM = " + AVG_NUM);
        System.out.println("SIM_PARAM_STD_DEV = " + STD_DEV);
        System.out.println("SIM_PARAM_DAY_NUM = " + DAY_NUM);
        System.out.println("SIM_PARAM_DAY_INI = " + DAY_INI);
        System.out.println("Above parameters were set by " + PRM_BY);
    };

    /*
     * Set up Simulation Parameters
     */
    public void setSimParams (){
        String strDate;
        boolean isOK;
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        // set AVG_NUM
        do { 
            try {
                System.out.print("Enter int value for [SIM_PARAM_AVG_NUM] in (0,"+MAX_AVG+"] interval: "); 
                AVG_NUM = Math.abs(Integer.parseInt(sc.nextLine()));
                isOK = (AVG_NUM > 0 & AVG_NUM <= MAX_AVG);
            } catch (NumberFormatException e) {
                RTSconsole.invalidValueWarning();
                isOK = false;
            }
        } while(!isOK);

        // set STD_DEV
        do { 
            try {
                System.out.print("Enter integer value for [SIM_PARAM_STD_DEV] (lower than "+AVG_NUM+"): "); 
                STD_DEV = Math.abs(Integer.parseInt(sc.nextLine()));
                isOK = AVG_NUM >= STD_DEV;
            } catch (NumberFormatException e) {
                RTSconsole.invalidValueWarning();
                isOK = false;
            }
        } while (!isOK);

        // set DAY_NUM
        do { 
            System.out.print("Enter a number for [SIM_PARAM_DAY_NUM]: "); 
            try {
                DAY_NUM = Math.abs(Integer.parseInt(sc.nextLine()));
                isOK = DAY_NUM > 0;
            } catch (NumberFormatException e) {
                RTSconsole.invalidValueWarning();
                isOK = false;
            }
        } while (!isOK);

        // set DAY_INI
        do { 
            System.out.print("Enter initial date [SIM_PARAM_DAY_INI] in YYYY-MM-DD format: "); 
            strDate = sc.next();
            try {
                //if date input is correct set DAY_INI
                DAY_INI = sdf.format(sdf.parse(strDate));
                isOK = true;
            } catch (ParseException e) {
                RTSconsole.invalidValueWarning();
                //wrong date input
                isOK = false;
            }
        } while (!isOK);
        
        // set PRM_BY
        PRM_BY = "user";

    };

    /*
     * START Simulation
     */
    public String[][] startSim(int AVG_NUM, int STD_DEV, int DAY_NUM, String[][] spaceArr, String[][] observArr ) {
        Random random = new SecureRandom();
        int randNum;            //random number of observations per one day (in curDate)
        int range;              // +/- (STD_DEV) from AVG_NUM determining randNum range
        String curDate;         //current date for which we will prepare (simulate) data
        String[][] stageArr;    //stage temporary array for data preparation for one day
        double currperc;        //current percent of progress (used to draw bar animation)
        double prevperc;        //previous percent of progress (used to draw bar animation)

        System.out.println("SIMULATION STARTED ...");

        //DEBUG_MODE: test progress bar animation
        if (DEBUG_MODE) {
            for(int i = 0; i < 301; i++) {
                //progress bar + progress [%]
                //test only:
                System.out.print("[TESTBAR]");
                RTSconsole.printProgressBar((double) i/3, 64); 
                RTSconsole.printProgressPct(i/3);
                RTSdebug.sleep(15);
                System.out.print("\u001b[100D");//move cursor left to the beginning of the line
                
            }
            System.out.println();
        }

        for(int day=0; day< DAY_NUM; day++) {
            //reset previous bar percent value
            prevperc = 0;

            curDate = RTSdate.addDaysToDate(DAY_INI,day);
            range = random.nextInt(0, STD_DEV+1);
            randNum = random.nextInt(AVG_NUM - range, AVG_NUM + range + 1);

            // DEBUG_MODE
            if(DEBUG_MODE) {
                System.out.println("DAY_INI = "+DAY_INI+" + ("+day+") day(s) gives curDate = "+curDate);
                System.out.println("AVG_NUM = "+AVG_NUM+" + ("+range+") random range gives = "+randNum+" (randNum)");
            }

            // Populate new observations for one day (curDate)
            stageArr = new String[randNum][3]; //each row has 3 values: date, country_code, plate no.
            for(int loop=0; loop < randNum; loop++) {


                stageArr[loop][0] = curDate;
                stageArr[loop][1] = spaceArr[1][0];
                stageArr[loop][2] = spaceArr[1][1] + " 12345";

                // To prevent technical issues with cursor blinking
                // draw progress bar and progress [%] when progress
                // raised at least ANIM_BAR_STEP % or reached 100 %
                currperc = (double) (loop+1)*100 / randNum;
                if (currperc == 100 || currperc - prevperc >= ANIM_BAR_STEP) {
                    prevperc = currperc;
                    System.out.print(curDate.replace("-", "") + ":");
                    RTSconsole.printProgressBar( currperc , 64 ); 
                    RTSconsole.printProgressPct( (int) currperc );
                    System.out.print("\u001b[100D");//move cursor left to line start
                    RTSdebug.sleep(ANIM_SLEEP_TIME);//sleep to make animation smooth
                }
            }
            System.out.println();

            // DEBUG_MODE
            // if(DEBUG_MODE) RTSdebug.showArray(stageArr);

            observArr = RTSmath.arrayAdd (stageArr, observArr);
           
        }

        //Return result
        System.out.println("... SIMULATION FINISHED.");
        return observArr;

    }

    /*
     * Process (menu) position/option
     */
    public String[][] procPos (String menuPos, String[][] spaceArr, String[][] observArr){
        switch (menuPos)
        {
            //Main menu screens
            case "0","00","09" -> {
                //do nothing
            }
 
            //Main menu screens
            case "01","03" -> {
                if (!RTSdb.isDBready(DBNAME, DEBUG_MODE)) {
                    RTSconsole.initDBwarning();
                } else {
                    //do nothing                    
                }
            }

            //Main menu screen
            case "02" -> {
                // Fist database has to be initialized
                if (!RTSdb.isDBready(DBNAME, DEBUG_MODE)) RTSconsole.initDBwarning(); 
                // First there has to be simulation data
                else if (RTSmath.isArrayNullOrEmpty(observArr)) RTSconsole.noSimulationWarning(); 
                // Do nothing if simulation and DB init have been done
                else ;                
            }

            //01C - Start new or continue simulation
            case "01C" -> {
                observArr = startSim(AVG_NUM, STD_DEV, DAY_NUM, spaceArr, observArr );
            }

            //01R - Reconfigure - set own parameters
            case "01R" -> {
                setSimParams();
            }

            //01P - Print simulation parameters
            case "01P" -> {
                showSimParams();
            }

            //02S - Save current simulation
            case "02S" -> {
                if (!RTSdb.isDBready(DBNAME, DEBUG_MODE)) RTSconsole.initDBwarning();
                else if (RTSmath.isArrayNullOrEmpty(observArr)) RTSconsole.noSimulationWarning();
                else {
                    // Save to the database
                    System.out.println("SAVING CURRENT SIMULATION DATA ...");

                    //to be done later

                    System.out.println("SIMULATION DATA SAVED TO DATABASE.");

                }
            }

            //02E - Erase current data
            case "02E" -> {
                if (!RTSdb.isDBready(DBNAME, DEBUG_MODE)) RTSconsole.initDBwarning();
                else if (RTSmath.isArrayNullOrEmpty(observArr)) RTSconsole.noSimulationWarning();
                else {
                    //Erase content of array with all observations (current simulation)
                    observArr = null;
                }
            }

            //02P - Print current data
            case "02P" -> {
                if (!RTSdb.isDBready(DBNAME, DEBUG_MODE)) RTSconsole.initDBwarning();
                else if (RTSmath.isArrayNullOrEmpty(observArr)) RTSconsole.noSimulationWarning();
                else {
                    // If observArr is not empty show all content
                    System.out.println("##################################");
                    System.out.println("### CURRENT SIMULATION CONTENT ###");
                    System.out.println("##################################");
                    if(!RTSmath.isArrayNullOrEmpty(observArr)) RTSdebug.showArray(observArr);
                }
            }

            //09S - SET UP Database (init db + connection test)
            case "09S" -> {
                System.out.println("To connect to the sqlite database JDBC driver is required.");
                System.out.println("Here are some examples of how to run RTS program with JDBC.");
                System.out.println("<guest@unix> java -classpath \".:sqlite-jdbc-3.46.1.3.jar\" RTS");
                System.out.println("<guest@windows> java -classpath \".;sqlite-jdbc-3.46.1.3.jar\" RTS");
                RTSdb.sqlTest(DBNAME, DEBUG_MODE);
            }

            //09X - RESET all settings
            case "09X" -> {
                RTSdb.resetDB(DBNAME, DEBUG_MODE);
            }

            //..Q - Quit
            case "0Q", "00Q", "01Q", "02Q", "03Q", "09Q" -> {
                //do nothing
            }

            //?? - Case out of scenario
            default -> {
                //do nothing
            }
        }
        //return menuPos;
        return observArr;
    };

    /*
     * Post process to adjust correct menu position/option
     */
    public String trimMenuPosLetter (String menuPos){
        if (Character.isLetter(menuPos.charAt(menuPos.length()-1))) 
             return menuPos.substring(0,menuPos.length()-1);
        else return menuPos;
    };

}
