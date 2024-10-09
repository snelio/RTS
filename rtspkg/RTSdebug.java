package rtspkg;

public class RTSdebug{

    // works only for String array
    // public static void showArray2Dstr(String array[][]){
    //     for(String row[]: array){
    //         for(String col: row){
    //             System.out.print("value="+col);
    //         }
    //         System.out.println();
    //     }
    // };
    // works for all datatypes array
    public static <T> void showArray(T[][] array){
        for(T row[]: array){
            for(T col: row){
                System.out.print("value="+col);
            }
            System.out.println();
        }
    };

    /*
     * Sleep program for some time
     * to make debug process easier
     */
    public static void sleep(int mstime){
        try {
            Thread.sleep(mstime);
        } catch (InterruptedException tobeignoredException) {
        }
    }


}