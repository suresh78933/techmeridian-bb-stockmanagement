For 122 Server
1) NAV properties should have 122 IP
2) application.properties  DB Should be on 122  
3) NavPurchaseOrderLinesService: Change last query item to PurchaseLinesList
JsonNode poLine = jsonNode.path("Soap:Envelope").path("Soap:Body")
									.path("ReadMultiple_Result").path("ReadMultiple_Result").path("PurchaseLinesList");

For Client Server
1) NAV Properties should have 0.2 IP
2) application.properties DB should be of Client machine
3) NavPurchaseOrderLinesService: Change last query item to PurchaseOrderLines
JsonNode poLine = jsonNode.path("Soap:Envelope").path("Soap:Body")
									.path("ReadMultiple_Result").path("ReadMultiple_Result").path("PurchaseOrderLines");