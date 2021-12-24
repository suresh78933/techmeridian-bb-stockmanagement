package com.techmeridian.stockmanagment.testnav;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

import jcifs.Config;

public class NavClient {

	public static void main(String[] args) {
		Config.registerSmbURLHandler();

		String location = "http://122.166.222.116:50047/DynNAV/WS/CRONUS%20India%20Ltd./Page/PurchaseOrder";
		Config.setProperty("jcifs.smb.client.domain", "AruN");
		Config.setProperty("jcifs.smb.client.username", "administrator");
		Config.setProperty("jcifs.smb.client.password", "itreenav");

		try {
			System.out.println(Config.getProperty("jcifs.netbios.hostname", InetAddress.getLocalHost().getHostName()));
			Config.setProperty("jcifs.netbios.hostname",
					Config.getProperty("jcifs.netbios.hostname", InetAddress.getLocalHost().getHostName()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		URL url;
		HttpURLConnection httpURLConnection = null;

		try {
			// Create connection
			url = new URL(location);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

			httpURLConnection.setUseCaches(false);
			httpURLConnection.setDoOutput(true);

			// Get Response
			InputStream inputStream = httpURLConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			bufferedReader.close();

			System.out.println("*****************************************");
			System.out.println(response.toString());
			System.out.println("*****************************************");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}