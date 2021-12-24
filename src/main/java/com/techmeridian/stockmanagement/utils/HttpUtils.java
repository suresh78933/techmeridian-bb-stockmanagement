package com.techmeridian.stockmanagement.utils;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techmeridian.stockmanagement.nav.service.NavProperties;

@Service
public class HttpUtils {

	private static final String SOAP_ACTION = "SOAPAction";
	private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
	private static final String ACCEPT = "Accept";
	private static final String APPLICATION_XML_CHARSET_UTF_8 = "application/xml; charset=utf-8";
	private static final String CONTENT_TYPE = "UTF-8";

	@Autowired
	private NavProperties navProperties;

	public String postToNav(String wsURL, String soapAction, String content) throws Exception {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();

		credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
				navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

		StringEntity stringEntity = new StringEntity(content, CONTENT_TYPE);
		stringEntity.setChunked(true);

		HttpPost httpPost = new HttpPost(wsURL);
		httpPost.setEntity(stringEntity);
		httpPost.addHeader(ACCEPT, APPLICATION_XML_CHARSET_UTF_8);
		httpPost.addHeader(CONTENT_TYPE_HEADER_NAME, APPLICATION_XML_CHARSET_UTF_8);
		httpPost.addHeader(SOAP_ACTION, soapAction);

		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			return EntityUtils.toString(entity);
		}
		return null;
	}
}
