
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class TimeConversion {

    static String timeConversion(String s) {
        
    	String part = s.substring(8);
    	int hh = (Integer.parseInt(s.substring(0,2)));
    			 
    	if ( (part.equalsIgnoreCase("AM") && hh == 12) 
    			|| (part.equalsIgnoreCase("PM") && hh != 12) ) {
    		int hour = (hh + 12) % 24 ;
    		
    		return (hour < 10 ? "0"+hour : hour) + s.substring(2,8);
    	}
    	
    	return s.substring(0,8);
    }

    public static void main(String[] args) {
       Scanner in = new Scanner(System.in);
        String s = in.next();
    	
//    	String st = ":49:00AM"; 
//    	
//    	for (int i = 1; i<=12; i++) {
//    		
//    		String s = ( i<10 ? "0"+i : i ) + st;
//    		
//    		String result = timeConversion(s);
//            System.out.println(s + " --> " + result);
//    	}
//
//    	st = ":12:31PM"; 
//    	
//    	for (int i = 1; i<=12; i++) {
//    		
//    		String s = ( i<10 ? "0"+i : i ) + st;
//    		
    		String result = timeConversion(s);
            System.out.println(s + " --> " + result);
    	}
        
//    }
}

