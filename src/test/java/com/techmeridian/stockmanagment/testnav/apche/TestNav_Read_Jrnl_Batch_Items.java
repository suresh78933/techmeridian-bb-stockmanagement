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
import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestNav_Read_Jrnl_Batch_Items {

	public static void main(String[] args) {
		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY,
					new NTCredentials("administrator", "itreenav", null, null));

			// Create a StringEntity for the SOAP XML.
			String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<v:Envelope xmlns:v=\"http://schemas.xmlsoap.org/soap/envelope/\" "
					+ "xmlns:c=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:d=\"http://www.w3.org/2001/XMLSchema\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">"
					+ "<v:Body>" 
					+ "<ReadMultiple xmlns=\"urn:microsoft-dynamics-schemas/page/phyinvjournal\">"
					+ "<filter i:type=\"d:anyType\">" 
					+ "<Field i:type=\"d:string\">Location_Code</Field>"
					+ "<Criteria i:type=\"d:string\">GREEN</Criteria>" 
					+ "</filter>" 
					+ "<filter i:type=\"d:anyType\">"
					+ "<Field i:type=\"d:string\">Journal_Template_Name</Field>"
					+ "<Criteria i:type=\"d:string\">PHYS. INV.</Criteria>" + "</filter>"
					+ "<filter i:type=\"d:anyType\">" 
					+ "<Field i:type=\"d:string\">Journal_Batch_Name</Field>"
					+ "<Criteria i:type=\"d:string\">JRNL-6</Criteria>" 
					+ "</filter>" 
					+ "</ReadMultiple>" 
					+ "</v:Body>"
					+ "</v:Envelope>";
			
			System.out.println("Request Body: "+body);
			
			StringEntity stringEntity = new StringEntity(body, "UTF-8");
			stringEntity.setChunked(true);

			// Request parameters and other properties.
			HttpPost httpPost = new HttpPost(
					"http://49.206.24.37:50047/DynNAV/WS/CRONUS%20India%20Ltd./Page/PhyInvJournal");
			httpPost.setEntity(stringEntity);
			httpPost.addHeader("Accept", "application/xml; charset=utf-8");
			httpPost.addHeader("Content-Type", "application/xml; charset=utf-8");
			httpPost.addHeader("SOAPAction", "urn:microsoft-dynamics-schemas/page/phyinvjournal:ReadMultiple");

			// Execute and get the response.
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			String strResponse = null;
			if (entity != null) {
				strResponse = EntityUtils.toString(entity);
				System.out.println(
						"*************************************************************************************");
				// System.out.println("Response Body: "+strResponse);

				JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);

				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());

				JsonNode phyInvList = jsonNode.path("Soap:Envelope").path("Soap:Body").path("ReadMultiple_Result")
						.path("ReadMultiple_Result").path("PhyInvJournal");
				if (phyInvList.isArray()) {
					for (int i = 0; i < phyInvList.size(); i++) {
						itemHandler(phyInvList.get(i));
					}
				} else {
					itemHandler(phyInvList);
				}
				System.out.println(
						"*************************************************************************************");
			} else {
				System.out.println("entity is null");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private static void itemHandler(JsonNode itemNode) {
		System.out.print(itemNode.path("Journal_Batch_Name").asText() + "\t");
		System.out.print(itemNode.path("Line_No").asText() + "\t");
		System.out.print(itemNode.path("Location_Code").asText() + "\t");
		System.out.print(itemNode.path("Quantity").asText() + "\t");
		System.out.print(itemNode.path("Line_No").asText() + "\t");
		System.out.println();
	}
}
