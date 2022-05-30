package user_requests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Login_request {

	public static void main(String[] args) {
		try {
			Socket s = new Socket("0.0.0.0", 1234);
			
			DataOutputStream output = new DataOutputStream(s.getOutputStream());
			
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("username", "Manolo");
			data.put("password", "manololama");
			
			String strData = data.toString().substring(1, data.toString().length() - 1);
			
			output.writeUTF(strData);
			
			System.out.println(strData);
			System.out.println("done");
			
			output.close();
			s.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
