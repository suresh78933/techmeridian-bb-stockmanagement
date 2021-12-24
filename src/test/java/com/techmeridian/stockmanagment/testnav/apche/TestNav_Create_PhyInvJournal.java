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

import com.techmeridian.stockmanagement.utils.Utility;

public class TestNav_Create_PhyInvJournal {

	public static void main(String[] args) {
		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY,
					new NTCredentials("administrator", "itreenav", null, null));

			// Create a StringEntity for the SOAP XML.
			String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><v:Envelope xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:d=\"http://www.w3.org/2001/XMLSchema\" "
					+ "xmlns:c=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:v=\"http://schemas.xmlsoap.org/soap/envelope/\"><v:Header /><v:Body>"
					+ "<Update xmlns=\"urn:microsoft-dynamics-schemas/page/phyinvjournal\" >"
					+ "<PhyInvJournal i:type=\"d:anyType\">"
					+ "<Journal_Template_Name i:type=\"d:string\">PHYS. INV.</Journal_Template_Name>"
					+ "<Journal_Batch_Name i:type=\"d:string\">DEFAULT</Journal_Batch_Name>"
					+ "<Line_No i:type=\"d:int\">10002</Line_No>"
					+ "<Entry_Type i:type=\"d:string\">Sale</Entry_Type>"
					+ "<Document_No i:type=\"d:string\">DC-10002</Document_No>"
					+ "<Item_No i:type=\"d:string\">1952-W</Item_No>"
					+ "<Location_Code>BLUE</Location_Code>"
					+ "<Posting_Date i:type=\"d:date\">"+Utility.getNavDate()+"</Posting_Date>"
					+ "<Quantity i:type=\"d:int\">10</Quantity>"
					+ "<Unit_of_Measure_Code i:type=\"d:string\">PCS</Unit_of_Measure_Code>"
					+ "<From_Mobile>True</From_Mobile>"
					+ "<Key>72;UwAAAAJ7/1AASABZAFMALgAgAEkATgBWAC4AAAACe/9EAEUARgBBAFUATABUAAAAAIcSJw==7;11481710;</Key>"
					+ "</PhyInvJournal>"
					+ "</Update>"
					+ "</v:Body></v:Envelope>";
			
			StringEntity stringEntity = new StringEntity(body, "UTF-8");
			stringEntity.setChunked(true);

			// Request parameters and other properties.
			HttpPost httpPost = new HttpPost(
					"http://122.166.222.116:50047/DynNAV/WS/CRONUS%20India%20Ltd./Page/PhyInvJournal");
			httpPost.setEntity(stringEntity);
			httpPost.addHeader("Accept", "application/xml; charset=utf-8");
			httpPost.addHeader("Content-Type", "application/xml; charset=utf-8");
			httpPost.addHeader("SOAPAction", "urn:microsoft-dynamics-schemas/page/phyinvjournal:Update");

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
