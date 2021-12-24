package com.techmeridian.stockmanagment.testnav;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

public class TestNavisionWebservice {

	URL url;
	String theUsername = "administrator";
	String thePassword = "itreenav";

	public TestNavisionWebservice() {
	}

	private void setAuthenticator() {
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				System.out.printf("url=%s, host=%s, ip=%s, port=%s%n", getRequestingURL(), getRequestingHost(),
						getRequestingSite(), getRequestingPort());

				return new PasswordAuthentication(theUsername, thePassword.toCharArray());
			}
		});
	}

	public void execute() {
		try {
			setAuthenticator();
			//
			String userPassword = theUsername + ":" + thePassword;
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());

			url = new URL("http://122.166.222.116:50048/DynNAV/OData/Company('CRONUS%20India%20Ltd.')/ItemList");
			URLConnection c = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) c;
			httpconn.setRequestProperty("Authorization", "NTLM " + encoding);
			httpconn.setRequestProperty("Proxy-Authorization", "NTLM " + encoding);

			System.out.println(c.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TestNavisionWebservice test = new TestNavisionWebservice();
		test.execute();
		System.exit(0);
	}

}