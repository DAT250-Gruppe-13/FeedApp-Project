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

	private final static String QUEUE_NAME = "iot";

	public static void main(String[] args) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		
		
		String messageString = "";
		List<Long> list = new ArrayList<>();

		while (true) {
			try {
				URL url = new URL("http://localhost:8080/polls/finished");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				connection.setRequestMethod("GET");
				connection.connect();
				System.out.println(connection.getResponseCode());
				System.out.println(connection.getContentType());
				if (connection.getResponseCode() == 200) {
					List<Long> newList = Helper.getUrlLongResponse(connection);

					for (int i = 0; i < newList.size(); i++) {
						if (!list.contains(newList.get(i))) {
							System.out.println("------- Publish Start -------");
							System.out.println("Publishing poll with id: " + newList.get(i));
							URL url2 = new URL("http://localhost:8080/polls/result/" + newList.get(i)); // newList.get(i);
							HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
							messageString = Helper.getUrlString(connection2);
							
							Connection client = factory.newConnection();
							Channel channel = client.createChannel();
							channel.queueDeclare(QUEUE_NAME, false, false, false, null);

							channel.exchangeDeclare("test", "topic");
							channel.basicPublish("test", "iot", null, messageString.getBytes());

							System.out.println("\tMessage '" + messageString + "' to 'iot'");

							client.close();
							connection2.disconnect();

							// Dweet starter
							System.out.println("------- Dweets -------");

							JSONObject json = new JSONObject(messageString);
							JSONObject content = new JSONObject();
							String title = Helper.removeSpacesHelper(json.getString("title"));
							URL dweetUrl = new URL("https://dweet.io/dweet/for/" + title);

							content.put(json.getJSONObject("red").getString("text"),
									String.valueOf(json.getJSONObject("red").getInt("value")));
							
							content.put(json.getJSONObject("green").getString("text"),
									String.valueOf(json.getJSONObject("green").getInt("value")));
							byte[] jsonBytes = content.toString().getBytes("UTF-8");
							HttpURLConnection dweetConnection = (HttpURLConnection) dweetUrl.openConnection();
							dweetConnection.setRequestMethod("POST");
							dweetConnection.setRequestProperty("Content-Type", "application/json");
							dweetConnection.setRequestProperty("Content-Length", String.valueOf(jsonBytes.length));
							dweetConnection.setDoOutput(true);
							OutputStream os = dweetConnection.getOutputStream();
							OutputStreamWriter out = new OutputStreamWriter(os, "UTF-8");
							out.write(content.toString());
							out.flush();
							out.close();
							os.close();
							System.out.println("Vote resource [post] - Response code: " + dweetConnection.getResponseCode());
							dweetConnection.disconnect();

							// Dweet done, getting response
							System.out.println("------- Getting Dweet Response -------");

							URL responseUrl = new URL("https://dweet.io/get/dweets/for/" + title);
							HttpURLConnection responseConnection = (HttpURLConnection) responseUrl.openConnection();
							String response = Helper.getUrlString(responseConnection);
							System.out.println(response);
							responseConnection.disconnect();

							System.out.println("-------Published -------");

						} else {
							System.out.println("Poll: " + newList.get(i) + " is sent");
						}

					}
					list = newList;
				} else if (connection.getResponseCode() == 204) {
					System.out.println(connection.getResponseCode());
				} else {
					System.out.println(connection.getResponseCode());
				}

				connection.disconnect();
				Thread.sleep(60000);
			} catch (Exception e) {
				e.printStackTrace();

			}

		}

	}

}
