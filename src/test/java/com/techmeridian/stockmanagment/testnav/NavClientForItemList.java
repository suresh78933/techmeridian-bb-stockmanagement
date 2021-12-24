package com.techmeridian.stockmanagment.testnav;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class NavClientForItemList {

	public static void main(String[] args) {
		try {
			SoapObject response = ConnectToNAVCodeUnit("urn:microsoft-dynamics-schemas/page/itemlist",
					"http://122.166.222.116:50047/DynNAV/WS/CRONUS%20India%20Ltd./Page/ItemList",
					"urn:microsoft-dynamics-schemas/page/itemlist:ReadMultiple", "ReadMultiple");

			System.out.println("***************************************");
			for (int j = 0; j < response.getPropertyCount(); j++) {
				SoapObject pii = (SoapObject) response.getProperty(j);
				System.out.println(getValue("No", pii));
				System.out.println(getValue("Description", pii));
				System.out.println(getValue("Created_From_Nonstock_Item", pii));
				System.out.println(getValue("Production_BOM_No", pii));
				System.out.println(getValue("Shelf_No", pii));
				System.out.println(getValue("Costing_Method", pii));
				System.out.println(getValue("Standard_Cost", pii));
				System.out.println(getValue("Last_Direct_Cost", pii));
				System.out.println(getValue("Unit_Price", pii));
				System.out.println(getValue("Vendor_No", pii));

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
