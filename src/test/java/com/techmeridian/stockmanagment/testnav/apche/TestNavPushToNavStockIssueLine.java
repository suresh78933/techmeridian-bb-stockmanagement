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

public class TestNavPushToNavStockIssueLine {

	public static void main(String[] args) {
		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY,
					new NTCredentials("administrator", "itreenav", null, null));

			String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
					+ "<soap:Body>"
					
					+ "<Update xmlns=\"urn:microsoft-dynamics-schemas/page/isssub\">"
					+ "<IssSub>"
					
					+ "<Issue_Document_No>IS001</Issue_Document_No>"
					+ "<Line_No>1000</Line_No>"
					+ "<Type>Item</Type>"
					+ "<No>TE</No>"
					+ "<Quantity>1</Quantity>"
					+ "<Key>32;UsMAAAJ7/0kAUwAwADAAMQAAAACH6AM=6;7201950;</Key>"
					
					+ "</IssSub>"
					+ "</Update>"
					
					+ "</soap:Body>"
					+ "</soap:Envelope>";
			
			StringEntity stringEntity = new StringEntity(body, "UTF-8");
			stringEntity.setChunked(true);

			// Request parameters and other properties.
			HttpPost httpPost = new HttpPost(
					"http://122.166.222.116:50047/DynNAV/WS/CRONUS%20India%20Ltd./Page/IssSub");
			httpPost.setEntity(stringEntity);
			httpPost.addHeader("Accept", "application/xml; charset=utf-8");
			httpPost.addHeader("Content-Type", "application/xml; charset=utf-8");
			httpPost.addHeader("SOAPAction", "urn:microsoft-dynamics-schemas/page/isssub:Update");

			// Execute and get the response.
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			String strResponse = null;
			if (entity != null) {
				strResponse = EntityUtils.toString(entity);
				System.out.println(
						"*************************************************************************************");
				System.out.println(strResponse);

				/*JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);
				
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());
				
				JsonNode journalList = jsonNode.path("Soap:Envelope").path("Soap:Body").path("Create_Result").path("Create_Result");
				if(journalList.size()>0){
					
					journalList.spliterator().forEachRemaining(action -> {
						System.out.println(action.path("Journal_Template_Name"));
						System.out.println(action.path("Name"));
					});
					
					for(int i=0;i<journalList.size();i++){
					}
				}*/

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
