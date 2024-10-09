package rtspkg;

/*
 *
 *
 */

public class RTSmath {
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
     * Concatenate two arrays
     */
    public static String[][] arrayAdd(String[][] arr1, String[][] arr2) {
        // array arr1 has no length (is null or empty) => copy only arr2
        if (isArrayNullOrEmpty(arr1)) {
            String[][] outArray = new String[arr2.length][];
            System.arraycopy(arr2, 0, outArray, 0, arr2.length);
            return outArray;
        } 
        // array arr2 has no length (is null or empty) => copy only arr1
        else if (isArrayNullOrEmpty(arr2)) {
            String[][] outArray = new String[arr1.length][];
            System.arraycopy(arr1, 0, outArray, 0, arr1.length);
            return outArray;            
        } 
        // both arrays are non empty (both have lengths) => copy both
        else {
            String[][] outArray = new String[arr1.length + arr2.length][];
            System.arraycopy(arr1, 0, outArray, 0, arr1.length);
            System.arraycopy(arr2, 0, outArray, arr1.length, arr2.length);
            return outArray;
        }

    }

    /*
     * Check if array is null or empty, i.e. has no length
     */
    public static <T> boolean isArrayNullOrEmpty(T[] array) {
        try {
            return array == null || array.length == 0;   
        } catch (Exception e) {
            return true;
        }        
    }    

    /*
     * Set INCRemental POPulation for later proportional randomization
     */
    public static int setIncrPop(String[][] array) {

        int maxVal = 0; // maximum boundary for random picks

        for(int i =0, aggr_sum=0; i< array.length; i++, aggr_sum=0){
            // plateSet[0][i][0] = plates[i][0];
            for(int aggr = 0; aggr <= i; aggr++){
                // increasing sum of previous population values
                aggr_sum += Integer.valueOf(array[aggr][2]);
            }
            // correct 00000 to 0
            array[i][2] = String.valueOf(Integer.valueOf(array[i][2]));
            // incremental value from population fields
            array[i][3] = String.valueOf(aggr_sum);
            maxVal = aggr_sum;
            setMaxRandBound(aggr_sum);
        }

        return maxVal;

    }

    /*
     * Increase counter
     */
    public static int counterUp (int i){
        i++;
        return i;
    };


}
