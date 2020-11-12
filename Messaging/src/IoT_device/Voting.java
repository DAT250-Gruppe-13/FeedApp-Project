package IoT_device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import publisher.Helper;

public class Voting {

	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Poll id: ");
		int pollId = Integer.parseInt(in.nextLine());
		System.out.println("Press 1 for: green vote");
		System.out.println("Press 2 for: red vote");
		System.out.println("Press 3 to reset votes");
		System.out.println("Press 4 fto submit votes");
		
		try {
			URL url = new URL("http://localhost:8080/polls/" + pollId);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			connection.connect();
			System.out.println(connection.getResponseCode());
			System.out.println(connection.getContentType());

			String content = Helper.getUrlString(connection);

			JSONObject json = new JSONObject(content);
			int noVotes = json.getInt("noVotes");
			int yesVotes = json.getInt("yesVotes");
			
			System.out.println(json.getInt("yesVotes"));
			System.out.println(json.getInt("noVotes"));
			
			int yes = 0;
			int no = 0;
			
			boolean done = false;
			while (!done) {
				int i = Integer.parseInt(in.nextLine());
				switch (i) {
				case 1:
					yes++;
					break;
				case 2:
					no++;
					break;
				case 3:
					no = 0;
					yes = 0;
					break;
				case 4:
					
					System.out.println("Submitting votes");
					URL url2 = new URL("http://localhost:8080/polls/");
					HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
					
					connection2.setRequestProperty("Content-Type", "application/json");
					connection2.setRequestProperty("Accept", "application/json");

					connection2.setRequestMethod("PUT");
					connection2.setDoOutput(true);
					
					json.put("noVotes", no + noVotes);
					json.put("yesVotes", yes + yesVotes);
	
					try(OutputStream os = connection2.getOutputStream()) {
					    byte[] input = json.toString().getBytes("utf-8");
					    os.write(input, 0, input.length);			
					}
					try(BufferedReader br = new BufferedReader(
							  new InputStreamReader(connection2.getInputStream(), "utf-8"))) {
							    StringBuilder response = new StringBuilder();
							    String responseLine = null;
							    while ((responseLine = br.readLine()) != null) {
							        response.append(responseLine.trim());
							    }
							    System.out.println(response.toString());
							}
					
					break;
				case 0:
					done = true;
				default:
					break;
				
				}
				
			}
			
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		
		in.close();

	}

}
