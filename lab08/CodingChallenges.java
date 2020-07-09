import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CodingChallenges {

    /**
     * Return the missing number from an array of length N containing all the
     * values from 0 to N except for one missing number.
     */
    public static int missingNumber(int[] values) {
        // TODO
        ArrayList<Integer> lst = new ArrayList<>();
        for(int x: values) {
            lst.add(x);
        }
        Collections.sort(lst);
        for(int i = 0; i < values.length; i++) {
            if (i !=lst.get(i)) {
                return i;
            }
        }
        return values.length;
    }

    /**
     * Returns true if and only if two integers in the array sum up to n.
     * Assume all values in the array are unique.
     */
    public static boolean sumTo(int[] values, int n) {
        // TODO
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < values.length-1; i++) {
            if(map.get(values[i])!=null) {
                return true;
            }
            map.put(n-values[i], values[i]);
        }
        return false;
    }

    /**
     * Returns true if and only if s1 is a permutation of s2. s1 is a
     * permutation of s2 if it has the same number of each character as s2.
     */
    public static boolean isPermutation(String s1, String s2) {
        // TODO
        if(s1.length() != s2.length()) { return false; }
        Map<Character, Integer> map1 = new HashMap<>();
        Map<Character, Integer> map2 = new HashMap<>();
        for(int i = 0; i < s1.length(); i++) {
            if(map1.containsKey(s1.charAt(i))) {
                map1.put(s1.charAt(i), map1.get(s1.charAt(i))+1);
            } else {
                map1.put(s1.charAt(i), 1);
            }
            if(map2.containsKey(s2.charAt(i))) {
                map2.put(s2.charAt(i), map2.get(s2.charAt(i))+1);
            } else {
                map2.put(s2.charAt(i), 1);
            }
        }
        return map1.equals(map2);
    }
}