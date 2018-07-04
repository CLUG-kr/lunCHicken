package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;

public class TestLoginServer {

	
	public static void main(String[] args) {

		Socket socket;
		try {
			
			socket = new Socket("localhost", 7777);
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			/*writer.println("hi");
			writer.flush();*/
			
			JSONObject test = new JSONObject();
			test.put("account_id", "owlsogul");
			test.put("account_pw", "1234");
			test.put("account_email", "owlsogul@naver.com");
			
			JSONObject testObj = new JSONObject();
			testObj.put("action", "login");
			testObj.put("data", test.toJSONString());
			writer.println(testObj.toJSONString());
			writer.flush();
			
			while(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
