package com.techmeridian.stockmanagment.testnav.apche;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class TestNav_ForTest {

	public static void main(String[] args) {
		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY,
					new NTCredentials("administrator", "itreenav", null, null));

			String now = (System.currentTimeMillis() + "").substring(0, 9);
			
			// Create a StringEntity for the SOAP XML.
			String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><v:Envelope xmlns:v=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:c=\"http://schemas.xmlsoap.org/soap/encoding/\" "
					+ "xmlns:d=\"http://www.w3.org/2001/XMLSchema\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><v:Header /><v:Body><Create xmlns=\"urn:microsoft-dynamics-schemas/page/test\">"
					+ "<Test><Code i:type=\"d:string\">"+now+"</Code><Description i:type=\"d:string\">"+now+"</Description><Posted>true</Posted></Test></Create></v:Body></v:Envelope>";
			StringEntity stringEntity = new StringEntity(body, "UTF-8");
			stringEntity.setChunked(true);

			// Request parameters and other properties.
			HttpPost httpPost = new HttpPost(
					"http://122.166.222.116:50047/DynNAV/WS/CRONUS%20India%20Ltd./Page/Test");
			httpPost.setEntity(stringEntity);
			httpPost.addHeader("Accept", "application/xml; charset=utf-8");
			httpPost.addHeader("Content-Type", "application/xml; charset=utf-8");
			httpPost.addHeader("SOAPAction", "urn:microsoft-dynamics-schemas/page/test:CreateMultiple");

			// Execute and get the response.
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			String strResponse = null;
			if (entity != null) {
				strResponse = EntityUtils.toString(entity);
				System.out.println(
						"*************************************************************************************");
				System.out.println(strResponse);

				System.out.println(
						"*************************************************************************************");
			} else {
				System.out.println("entity is null");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
