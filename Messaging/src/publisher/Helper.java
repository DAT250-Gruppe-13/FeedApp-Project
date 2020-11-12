package publisher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class Helper {

	 public static String getUrlString(HttpURLConnection connection) throws IOException {
         
  
         BufferedReader inReader = null;
         if (connection.getResponseCode() > 299) {
             inReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
         } else {
             inReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         }
         String input;
         StringBuffer con = new StringBuffer();
         while ((input = inReader.readLine()) != null) {
             con.append(input);
         }
         inReader.close();

         return  con.toString();
     }
	 
	
	 
    public static List<Long> getUrlLongResponse(HttpURLConnection connection) throws IOException {
        
      
        BufferedReader inReader = null;
        if (connection.getResponseCode() > 299) {
            inReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        } else {
            inReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }
        String input;
        StringBuffer con = new StringBuffer();
        while ((input = inReader.readLine()) != null) {
            con.append(input);
        }
        inReader.close();

        String s = con.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "");
       
        String[] stringArray = s.split(",");
        
        List<Long> list = new ArrayList<>();
        
        for (String string : stringArray) {
        	list.add(Long.parseLong(string));
        }
        
        return list;
        
    }
    
    public static String removeSpacesHelper(String string) {
    	  
    	char[] stringToChar = string.toCharArray();
        int in = 0;  
        int spaces=0;
      
        for (int i = 0; i < string.length(); i++) {  

            if (stringToChar[i] == ' ') {  
            	spaces++; 
            	stringToChar[i+1] = Character.toUpperCase(stringToChar[i + 1]);  
                continue;  
            }  
       
            else
            	stringToChar[in++] = stringToChar[i];          
        }  
      
      
        String result = new String(stringToChar);
        String result2 =result.substring(0, result.length()-spaces);
        return result2;  
    } 

}
