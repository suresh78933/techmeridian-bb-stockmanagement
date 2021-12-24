package com.techmeridian.stockmanagment.testnav.apche;

import java.util.ArrayList;
import java.util.List;

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
import com.techmeridian.stockmanagement.soap.SoapParam;
import com.techmeridian.stockmanagement.soap.SoapUtils;

public class TestNav_ReadMultiple_PurchaseOrderLines {

	public static void main(String[] args) {
		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY,
					new NTCredentials("administrator", "itreenav", null, null));

			List<SoapParam> readPurchaseOrderLinesParams = new ArrayList<SoapParam>();

			readPurchaseOrderLinesParams.add(SoapUtils.getSoapParam("Status", "Open", "string"));

			String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><v:Envelope xmlns:v=\"http://schemas.xmlsoap.org/soap/envelope/\" "
					+ "xmlns:c=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:d=\"http://www.w3.org/2001/XMLSchema\" "
					+ "xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" + "<v:Body>"
					+ "<ReadMultiple xmlns=\"urn:microsoft-dynamics-schemas/page/purchaseorderlines\">"
					+ "<filter i:type=\"d:anyType\">" + "<Field i:type=\"d:string\">Document_No</Field>"
					+ "<Criteria i:type=\"d:string\">104001</Criteria>" + "</filter>" + "</ReadMultiple>" + "</v:Body>"
					+ "</v:Envelope>";
			
			/*<?xml version="1.0" encoding="UTF-8"?><v:Envelope xmlns:i="http://www.w3.org/2001/XMLSchema-instance" 
					xmlns:d="http://www.w3.org/2001/XMLSchema" xmlns:c="http://schemas.xmlsoap.org/soap/encoding/" 
					xmlns:v="http://schemas.xmlsoap.org/soap/envelope/">
			<v:Body>
			<ReadMultiple xmlns="urn:microsoft-dynamics-schemas/page/purchaselineslist">
			<filter i:type="d:anyType"><Field i:type="d:string">Document_No</Field>
			<Criteria i:type="d:string">104001</Criteria></filter></ReadMultiple></v:Body>
			</v:Envelope>*/

			StringEntity stringEntity = new StringEntity(body, "UTF-8");
			stringEntity.setChunked(true);

			// Request parameters and other properties.
			HttpPost httpPost = new HttpPost(
					"http://49.206.24.37:50047/DynNAV/WS/CRONUS%20India%20Ltd./Page/PurchaseOrderLines");
			httpPost.setEntity(stringEntity);
			httpPost.addHeader("Accept", "application/xml; charset=utf-8");
			httpPost.addHeader("Content-Type", "application/xml; charset=utf-8");
			httpPost.addHeader("SOAPAction", "urn:microsoft-dynamics-schemas/page/purchaseorderlines:ReadMultiple");

			// Execute and get the response.
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			String strResponse = null;
			if (entity != null) {
				strResponse = EntityUtils.toString(entity);
				// System.out.println("Response body: " + strResponse);

				JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);

				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());

				JsonNode poLine = jsonNode.path("Soap:Envelope").path("Soap:Body").path("ReadMultiple_Result")
						.path("ReadMultiple_Result").path("PurchaseOrderLines");
				if (poLine.isArray()) {
					if (poLine.size() > 0) {
						poLine.spliterator().forEachRemaining(thisPOLine -> {
							System.out.println(thisPOLine.path("Document_No").asText());
							System.out.println(thisPOLine.path("Line_No").asText());
							System.out.println("---------------------------------------------");
						});
					}
				} else {
					System.out.println(poLine.path("Document_No").asText());
				}
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
