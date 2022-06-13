package utils;

import java.util.HashMap;

public class Core {

	public static HashMap<String, String> strToHashMap(String strData) {
		strData = strData.replace("{", "");
		strData = strData.replace("}", "");
		
		String[] aux = strData.split(", ");
		HashMap<String, String> data = new HashMap<String, String>();

		for (String s : aux) {
			String[] pair = s.split("=");
			System.out.println(s);
			data.put(pair[0], pair[1]);
		}
		
		return data;
	}

}
