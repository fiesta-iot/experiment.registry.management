package eu.fiestaiot.experiment.erm.jpa.utils;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author Rachit Agarwal
 * @author Nikos Kefalakis (nkef) e-mail: nkef@ait.edu.gr
 * 
 */

public class SecurityUtil {

	//Logger's initialization
	final static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
	
	
	public String getUserID(String token, String getSecurityUserURL) {
		String userID = "";
		try {
			URL url = new URL(getSecurityUserURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("iPlanetDirectoryPro", token);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.flush();
			wr.close();
			int responseMC = conn.getResponseCode();
			logger.info("response code getUserID:"+responseMC);
			if (responseMC == HttpURLConnection.HTTP_OK) {
				String security = getContent(conn.getInputStream());
				JSONObject jObject = new JSONObject(security);
				if (jObject.has("id")) {
					userID = jObject.getString("id");
				}
			}
		} catch (IOException e) {
			logger.error("IOException:",e);//e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception:",e);//e.printStackTrace();
		}
		return userID;
	}
	

	private static String getContent(InputStream input) {
		StringBuilder sb = new StringBuilder();
		byte[] b = new byte[1024];
		int readBytes = 0;
		try {
			while ((readBytes = input.read(b)) >= 0) {
				sb.append(new String(b, 0, readBytes, "UTF-8"));
			}
			input.close();
			return sb.toString().trim();
		} catch (IOException e) {
			logger.error("",e);//e.printStackTrace();
			if (input != null)
				try {
					input.close();
				} catch (IOException e1) {
					logger.error("",e1);//e1.printStackTrace();
				}
		}
		return null;
	}
	
	
	
}
