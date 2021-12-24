package com.techmeridian.stockmanagment.testnav;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class NavClientForVendorList {

	public static void main(String[] args) {
		try {
			SoapObject response = ConnectToNAVCodeUnit("urn:microsoft-dynamics-schemas/page/vendorlist",
					"http://122.166.222.116:50047/DynNAV/WS/CRONUS%20India%20Ltd./Page/VendorList",
					"urn:microsoft-dynamics-schemas/page/vendorlist:ReadMultiple", "ReadMultiple");

			System.out.println("***************************************");
			for (int j = 0; j < response.getPropertyCount(); j++) {
				SoapObject pii = (SoapObject) response.getProperty(j);
				System.out.println(getValue("No", pii));
				System.out.println(getValue("Name", pii));
				System.out.println(getValue("Location_Code", pii));
				System.out.println(getValue("Post_Code", pii));
				System.out.println(getValue("Country_Region_Code", pii));
				System.out.println(getValue("Phone_No", pii));
				System.out.println(getValue("Fax_No", pii));
				System.out.println(getValue("IC_Partner_Code", pii));
				System.out.println(getValue("ETag", pii));
				System.out.println(getValue("Search_Name", pii));
				System.out.println(getValue("Language_Code", pii));
				System.out.println(getValue("Purchaser_Code", pii));
				System.out.println(getValue("Contact", pii));

				System.out.println("____________________________________");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static String getValue(String key, SoapObject pii) {
		try {
			return pii.getProperty(key).toString();
		} catch (Exception exception) {
			return "";
		}
	}

	public static SoapObject ConnectToNAVCodeUnit(String namespace, String url, String soap_action,
			String method_name) {
		try {
			SoapObject request = new SoapObject(namespace, method_name);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;

			envelope.setOutputSoapObject(request);

			NtlmTransport ntlm = new NtlmTransport();
			ntlm.debug = false;
			
			ntlm.setCredentials(url, "android", "india@1947", "IT", "");
			ntlm.call(soap_action, envelope);
			if (envelope.bodyIn != null) {
				return (SoapObject) envelope.getResponse();

			} else {
				System.out.println("envelope.bodyIn is null");

			}
			if (envelope.bodyIn instanceof SoapFault) {
				String str = ((SoapFault) envelope.bodyIn).faultstring;
				System.out.println(str);

			} else {
				SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
				String ut = String.valueOf(resultsRequestSOAP);
				System.out.println(String.valueOf(resultsRequestSOAP));
				return resultsRequestSOAP;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
