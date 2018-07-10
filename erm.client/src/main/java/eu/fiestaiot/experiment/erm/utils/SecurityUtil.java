package eu.fiestaiot.experiment.erm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class SecurityUtil {

	public String getUserID(String token, String getSecurityUserURL){
		
		
		String userID="";

       try{
			URL url = new URL(getSecurityUserURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST"); 
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("iPlanetDirectoryPro", token);

			OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
			wr.flush();
			wr.close();
			int responseMC = conn.getResponseCode();
			System.out.println(responseMC);
			if (responseMC == HttpURLConnection.HTTP_OK){
				String security = getContent(conn.getInputStream());
	        	JSONObject jObject=new JSONObject(security);
	        	if (jObject.has("id")){
	        		userID=jObject.getString("id");
	        	}
			}
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return userID;
	}

	public String login(String authenticationURL, String username, String password){
		

		String token="";
		try{
			URL url = new URL(authenticationURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST"); 
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("X-OpenAM-Username", username);
			conn.setRequestProperty("X-OpenAM-Password", password);
	
			OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
			wr.flush();
			wr.close();
			int responseMC = conn.getResponseCode();
			System.out.println(responseMC);
			if (responseMC == HttpURLConnection.HTTP_OK){
				String security = getContent(conn.getInputStream());
	        	JSONObject jObject=new JSONObject(security);
	        	if (jObject.has("tokenId")){
	        		token=jObject.getString("tokenId");
	        	}
			}
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return token;
	}

private String getContent(InputStream input) {
	 StringBuilder sb = new StringBuilder();
	 byte [] b = new byte[1024];
	 int readBytes = 0;
	 try {
		while ((readBytes = input.read(b)) >= 0) {
			 sb.append(new String(b, 0, readBytes, "UTF-8"));
		 }
		input.close();
		return sb.toString().trim();
	} catch (IOException e) {
		e.printStackTrace();
		if (input != null)
			try {
				input.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}
	 return null;
 }
}
