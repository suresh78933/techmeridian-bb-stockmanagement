package com.techmeridian.stockmanagment.testnav;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class NavClientForUserList {

	public static void main(String[] args) {
		try {
			SoapObject response = ConnectToNAVCodeUnit("urn:microsoft-dynamics-schemas/page/users",
					"http://122.166.222.116:50047/DynNAV/WS/CRONUS%20India%20Ltd./Page/Users",
					"urn:microsoft-dynamics-schemas/page/users:ReadMultiple", "ReadMultiple");

			System.out.println("***************************************");
			for (int j = 0; j < response.getPropertyCount(); j++) {
				SoapObject pii = (SoapObject) response.getProperty(j);
				System.out.println(getValue("Key", pii));
				System.out.println(getValue("User_Security_ID", pii));
				System.out.println(getValue("User_Name", pii));
				System.out.println(getValue("Windows_Security_ID", pii));
				System.out.println(getValue("License_Type", pii));
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
			ntlm.setCredentials(url, "administrator", "itreenav", "", "");
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
