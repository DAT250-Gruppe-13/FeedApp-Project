package publisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class Publisher {
	
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {
		 
			  
			  ConnectionFactory factory = new ConnectionFactory();
			  factory.setHost("localhost");
			  String messageString = "";
				List<Long> oldlist = new ArrayList<>();
				
				while(true) {
					try {
			            URL url = new URL("http://localhost:8080/polls");
			            HttpURLConnection con = (HttpURLConnection) url.openConnection();
			            
			            con.setRequestMethod("GET");
			            con.connect();
			            System.out.println(con.getResponseCode());
			            System.out.println(con.getContentType());
			            if(con.getResponseCode() == 200) {
			            	List<Long> newlist = HelperClass.getFullLongResponse(con);
			            	
			            	for(int i =0; i< newlist.size(); i++) {
			            		if(!oldlist.contains(newlist.get(i))){
			            			System.out.println("== START PUBLISHER ==");
			            			URL url2 = new URL("http://localhost:8080/polls/"+"2"); //newList.get(i);
			        	            HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
			        	            messageString = HelperClass.getFullStringResponse(con2);
				        			Connection client = factory.newConnection();
				        			Channel channel = client.createChannel();
				        			
				        			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
				        			
				        	
				        			channel.exchangeDeclare("test", "fanout");
				        			channel.basicPublish("test", "", null,  messageString.getBytes());
				        	
				        			System.out.println("\tMessage '"+ messageString +"' to 'iot_data'");
				        			
				        			client.close();
				        			con2.disconnect();
				        			
				        			
				        			//DWEET IO
				        			System.out.println("== DWEETING ==");
				        			
				        			JSONObject json = new JSONObject(messageString);
				        			JSONObject content = new JSONObject();
				        			String title = HelperClass.removeSpacesAndAddCamelcase(json.getString("title"));
				        			URL dweet = new URL("https://dweet.io/dweet/for/"+ title);
				        			
				        			content.put(json.getJSONObject("red").getString("text"), String.valueOf(json.getJSONObject("red").getInt("amount")));
				    				//parameters.put("red", String.valueOf(json.getInt("red")));
				        			content.put(json.getJSONObject("green").getString("text"), String.valueOf(json.getJSONObject("green").getInt("amount")));
				    				byte[] postDataBytes = content.toString().getBytes("UTF-8");
				    				HttpURLConnection dweetcon = (HttpURLConnection) dweet.openConnection();
				    				dweetcon.setRequestMethod("POST");
				    				dweetcon.setRequestProperty("Content-Type", "application/json");
				    		        dweetcon.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
				    				dweetcon.setDoOutput(true);
				    				OutputStream os = dweetcon.getOutputStream();
				    				OutputStreamWriter out = new OutputStreamWriter(os,"UTF-8");
				    				out.write(content.toString());
				    				out.flush();
				    				out.close();
				    				os.close();
				    				System.out.println("Vote resource [post] - Response code: " + dweetcon.getResponseCode());
				    				dweetcon.disconnect();
				    				
				    				//DWEETIO DONE
				    				System.out.println("== DWEETING DONE, GETTING DWEET ==");
				    				
				    				URL dweetget = new URL("https://dweet.io/get/dweets/for/"+title);
				    				HttpURLConnection dweetgetcon = (HttpURLConnection) dweetget.openConnection();
				    				String dweetresponse = HelperClass.getFullStringResponse(dweetgetcon);
				    				System.out.println(dweetresponse);
				    				dweetgetcon.disconnect();
				        			
				        				
				        			System.out.println("== END PUBLISHER ==");
				        			
			            		}else {
			            		System.out.println("Poll with id: " + newlist.get(i) + " has already been sent");
			            		}
			            		
				            	
				            }
			            	oldlist = newlist;
			            }else if(con.getResponseCode() == 204) {
			            	System.out.println("Nothing to update");
			            }else {
			            	System.out.println("Da frick?");
			            }
			            
			            con.disconnect();
			            Thread.sleep(60000);
			        } catch (Exception e) {
			            e.printStackTrace();
			            
			        }
					
				}
				
				
			    
			    
			    
			    
			    

			}

}
