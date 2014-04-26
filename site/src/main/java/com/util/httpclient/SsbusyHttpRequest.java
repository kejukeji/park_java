package com.util.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Vector;

/**
 * 
 * @author song
 * 
 */
public class SsbusyHttpRequest {

	private String contentEncoding;

	public SsbusyHttpRequest() {
		this.contentEncoding = "utf-8";
	}

	public SsbusyHttpResponse sendGet(String urlString) throws IOException {
		return this.send(urlString, "GET", null, null);
	}
	
	public SsbusyHttpResponse sendGet(String urlString, Map<String, String> params)
			throws IOException {
		return this.send(urlString, "GET", params, null);
	}

	
	public SsbusyHttpResponse sendGet(String urlString, Map<String, String> params,
			Map<String, String> propertys) throws IOException {
		return this.send(urlString, "GET", params, propertys);
	}

	
	public SsbusyHttpResponse sendPost(String urlString) throws IOException {
		return this.send(urlString, "POST", null, null);
	}


	public SsbusyHttpResponse sendPost(String urlString, Map<String, String> params)
			throws IOException {
		return this.send(urlString, "POST", params, null);
	}

	public SsbusyHttpResponse sendPost(String urlString, Map<String, String> params,
			Map<String, String> propertys) throws IOException {
		return this.send(urlString, "POST", params, propertys);
	}

	/**
	 * 
	 * @param urlString
	 * @param method
	 * @param parameters
	 * @param propertys
	 * @return
	 * @throws IOException
	 */
	private SsbusyHttpResponse send(String urlString, String method,
			Map<String, String> parameters, Map<String, String> propertys)
			throws IOException {
		HttpURLConnection urlConnection = null;
		if (method.equalsIgnoreCase("GET") && parameters != null) {
			StringBuffer param = new StringBuffer();
			int i = 0;
			for (String key : parameters.keySet()) {
				if (i == 0)
					param.append("?");
				else
					param.append("&");
				param.append(key).append("=").append(parameters.get(key));
				i++;
			}
			urlString += param;
		}
		URL url = new URL(urlString);
		urlConnection = (HttpURLConnection) url.openConnection();

		urlConnection.setRequestMethod(method);
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);

		if (propertys != null)
			for (String key : propertys.keySet()) {
				urlConnection.addRequestProperty(key, propertys.get(key));
			}

		if (method.equalsIgnoreCase("POST") && parameters != null) {
			StringBuffer param = new StringBuffer();
			for (String key : parameters.keySet()) {
				param.append("&");
				param.append(key).append("=").append(parameters.get(key));
			}
			urlConnection.getOutputStream().write(param.toString().getBytes());
			urlConnection.getOutputStream().flush();
			urlConnection.getOutputStream().close();
		}

		return this.makeContent(urlString, urlConnection);
	}

	/**
	 * 
	 * @param urlString
	 * @param urlConnection
	 * @return
	 * @throws IOException
	 */
	private SsbusyHttpResponse makeContent(String urlString,
			HttpURLConnection urlConnection) throws IOException {
		SsbusyHttpResponse httpResponser = new SsbusyHttpResponse();
		try {
			InputStream in = urlConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in));
			Vector<String> contentCollection = new Vector<String>();
			
			StringBuffer temp = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				contentCollection.add(line);
				temp.append(line).append("\r\n");
				line = bufferedReader.readLine();
			}
			bufferedReader.close();

			String ecod = urlConnection.getContentEncoding();
			if (ecod == null)
				ecod = this.contentEncoding;
			httpResponser.setContentCollection(contentCollection);
			httpResponser.setUrlString(urlString);
			httpResponser.setDefaultPort(urlConnection.getURL().getDefaultPort());
			httpResponser.setFile(urlConnection.getURL().getFile());
			httpResponser.setHost(urlConnection.getURL().getHost());
			httpResponser.setPath(urlConnection.getURL().getPath());
			httpResponser.setPort(urlConnection.getURL().getPort());
			httpResponser.setProtocol(urlConnection.getURL().getProtocol());
			httpResponser.setQuery(urlConnection.getURL().getQuery());
			httpResponser.setRef(urlConnection.getURL().getRef());
			httpResponser.setUserInfo(urlConnection.getURL().getUserInfo());
			httpResponser.setContent(new String(temp.toString().getBytes(), ecod));
			httpResponser.setContentEncoding(ecod);
			httpResponser.setCode(urlConnection.getResponseCode());
			httpResponser.setMessage(urlConnection.getResponseMessage());
			httpResponser.setContentType(urlConnection.getContentType());
			httpResponser.setMethod(urlConnection.getRequestMethod());
			httpResponser.setConnectTimeout(urlConnection.getConnectTimeout());
			httpResponser.setReadTimeout(urlConnection.getReadTimeout());
			return httpResponser;
		} catch (IOException e) {
			throw e;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}
}
