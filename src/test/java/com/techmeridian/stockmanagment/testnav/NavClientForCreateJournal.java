package com.techmeridian.stockmanagment.testnav;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class NavClientForCreateJournal {

	public static void main(String[] args) {
		try {
			SoapObject response = ConnectToNAVCodeUnit("urn:microsoft-dynamics-schemas/page/phyinvjournal",
					"http://122.166.222.116:50047/DynNAV/WS/CRONUS%20India%20Ltd./Page/PhyInvJournal",
					"urn:microsoft-dynamics-schemas/page/phyinvjournal:Create", "Create");

			System.out.println("***************************************" + response.getPropertyCount());
			for (int j = 0; j < response.getPropertyCount(); j++) {
				SoapObject pii = (SoapObject) response.getProperty(j);

				for (int i = 0; i < 10; i++) {
					System.out.println(pii.getProperty(i).toString());
				}

				System.out.println("____________________________________");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static PropertyInfo createPropertyInfo(String name, Object value, boolean isInteger) {
		PropertyInfo propertyInfo = new PropertyInfo();
		if (isInteger) {
			propertyInfo.setType(Integer.class);
		} else {
			propertyInfo.setType(String.class);
		}
		propertyInfo.setName(name);
		propertyInfo.setValue(value);
		return propertyInfo;
	}

	public static SoapObject ConnectToNAVCodeUnit(String namespace, String url, String soap_action,
			String method_name) {
		try {
			SoapObject soapObject = new SoapObject(namespace, method_name);

			PhyInvJournal phyInvJournal = new PhyInvJournal("Sale", "DCT-NO-1", "LS-150", "Test Journal", 1, 1, 1,
					"PCS", "Test Desc", "DEFAULT", "PHYS. INV.", 10001);
			soapObject.addProperty("PhyInvJournal", phyInvJournal);

			/*soapObject.addProperty(createPropertyInfo("Entry_Type", 1, true));
			soapObject.addProperty(createPropertyInfo("Document_No", "DCT-NO-1", false));
			soapObject.addProperty(createPropertyInfo("Item_No", "LS-150", false));
			soapObject.addProperty(createPropertyInfo("Description", "Test Journal", false));
			soapObject.addProperty(createPropertyInfo("Qty_Calculated", 1, true));
			soapObject.addProperty(createPropertyInfo("Qty_Phys_Inventory", 1, true));
			soapObject.addProperty(createPropertyInfo("Quantity", 1, true));
			soapObject.addProperty(createPropertyInfo("Unit_of_Measure_Code", "PCS", false));
			soapObject.addProperty(createPropertyInfo("ItemDescription", "TestDesc", false));
			soapObject.addProperty(createPropertyInfo("Journal_Batch_Name", "DEFAULT", false));
			// soapObject.addProperty(createPropertyInfo("CurrentJnlBatchName",
			// "1887837225"));
			soapObject.addProperty(createPropertyInfo("Journal_Template_Name", "PHYS. INV.", false));
			soapObject.addProperty(createPropertyInfo("Line_No", 10000, true));

			// soapObject.addProperty(createPropertyInfo("CurrentJnlBatchName",
			// "1887837225"));
*/
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;

			envelope.setOutputSoapObject(soapObject);

			NtlmTransport ntlm = new NtlmTransport();
			ntlm.debug = false;
			ntlm.setCredentials(url, "administrator", "itreenav", "", "");
			ntlm.call(soap_action, envelope);
			if (envelope.bodyIn != null) {
				if (envelope.bodyIn instanceof SoapFault) {
					SoapFault soapFault = (SoapFault) envelope.bodyIn;
					System.out.println(soapFault.faultcode);

				} else if (envelope.bodyIn instanceof SoapObject) {
					return (SoapObject) envelope.getResponse();
				}
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
