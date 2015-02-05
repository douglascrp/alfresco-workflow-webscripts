package org.orderofthebee.workflow.meta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class WebScriptHelper {

	private static Logger log = Logger.getLogger(WebScriptHelper.class);

	private final static boolean MAKE_UNIQUE = true;
	
	private CloseableHttpClient client;

	public WebScriptHelper() {
	}

	public WebScriptHelper(final String username, final String password) {

		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(TestMeta.HOST,
				TestMeta.PORT, null), new UsernamePasswordCredentials(username,
				password));

		client = HttpClients.custom().setConnectionManager(getConnectionManager())
				.setDefaultCredentialsProvider(credsProvider).build();

	}
	
	public String genUniqueId() {
		return UUID.randomUUID().toString();
	}

	public String get(String url) throws IOException {
		
		url = makeUnique(url);
		log.debug("get " + url);
		
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		String text = null;
		InputStream is = response.getEntity().getContent();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			text = readAll(rd);
		} finally {
			is.close();
		}
		return text;
	}

	private HttpClientConnectionManager getConnectionManager() {
		// TODO Auto-generated method stub
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		// Increase max total connection to 200
		cm.setMaxTotal(200);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(20);
		
		return cm;
	}

	private String makeUnique(String url) {
		
		if(!MAKE_UNIQUE) return url;
		
		String uniq = "";
		
		if(url.contains("?")){
			uniq = url + "&uniqueId=" + genUniqueId();
		} else {
			uniq = url + "?uniqueId=" + genUniqueId();
		}
		
		return uniq;
	}

	public InputStream post(String url) throws IOException {
		return this.post(url, null);
	}

	public InputStream post(String url, Map<String, String> bodyParameters)
			throws IOException {
		
		url = makeUnique(url);
		log.debug("post " + url);
		
		HttpPost post = new HttpPost(url);

		post.addHeader(new BasicHeader("Connection", "close"));
		
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		if (bodyParameters != null) {
			Iterator<String> it = bodyParameters.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				data.add(new BasicNameValuePair(key, bodyParameters.get(key)));
			}

		}
		post.setEntity(new UrlEncodedFormEntity(data));

		HttpResponse response = client.execute(post);

		return response.getEntity() == null ? null : response.getEntity()
				.getContent();
	}

	// http://stackoverflow.com/a/4308662/370191
	public String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public JSONObject readJsonFromInputStream(InputStream is)
			throws IOException, JSONException {
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	// http://stackoverflow.com/a/4308662/370191
	public JSONObject readJsonFromUrl(String url) throws IOException,
			JSONException {

		url = makeUnique(url);
		log.debug("readJsonFromUrl " + url);
		
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);

		InputStream is = response.getEntity().getContent();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}



	public JSONObject readJsonFromUrlPost(String url) throws IOException,
			JSONException {
		return readJsonFromInputStream(post(url));
	}

}