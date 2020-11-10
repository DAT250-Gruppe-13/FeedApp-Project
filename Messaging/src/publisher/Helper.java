package publisher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Helper {

	 public static String getUrlString(HttpURLConnection connection) throws IOException {
         
  
         BufferedReader inReader = null;
         if (connection.getResponseCode() > 299) {
             inReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
         } else {
             inReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         }
         String input;
         StringBuffer content = new StringBuffer();
         while ((input = inReader.readLine()) != null) {
             content.append(input);
         }
         inReader.close();

         return  content.toString();
     }
	 
	public static String getParameterString(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder r = new StringBuilder();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			r.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			r.append("=");
			r.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			r.append("&");
		}

		String resultString = r.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
	}
	 
    public static List<Long> getUrlLongResponse(HttpURLConnection connection) throws IOException {
        
      
        BufferedReader in = null;
        if (connection.getResponseCode() > 299) {
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }
        String inputLine;
        StringBuffer conn = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            conn.append(inputLine);
        }
        in.close();

        String s = conn.toString();
        
        String s2 = s.replaceAll("\\[", "");
        String s3 = s2.replaceAll("\\]", "");
        String s4 = s3.replaceAll("\\s", "");
        String[] array = s4.split(",");
        
        List<Long> idList = new ArrayList<>();
        
        for (String string : array) {
        	idList.add(Long.parseLong(string));
        }
        
        return idList;
        
    }
    
    public static String removeSpacesHelper(String string) {
    	int n = string.length();  
    	char[] s =string.toCharArray();
        int in = 0;  
        int spaces=0;
      
        for (int i = 0; i < n; i++) {  

            if (s[i] == ' ') {  
            	spaces++; 
                s[i+1] = Character.toUpperCase(s[i + 1]);  
                continue;  
            }  
       
            else
                s[in++] = s[i];          
        }  
      
      
        String result = new String(s);
        String result2 =result.substring(0, result.length()-spaces);
        return result2;  
    } 

}
