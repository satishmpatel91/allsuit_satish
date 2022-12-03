package com.allsuit.casual.suit.photo;

import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServiceHandler {

	static String response = null;
	public final static int GET = 1;
	public final static int POST = 2;

	public ServiceHandler() {

	}

	/*
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * */
	/*public String makeServiceCall(String url, int method) {
		return this.makeServiceCall(url, method, null);
	}*/

	/*
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * @params - http request params
	 * */
	public String makeServiceCall(String url, int method,
			List<NameValuePair> params) {
		/*try {
			// http client
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			
			// Checking http request method type
			if (method == POST) {
				HttpPost httpPost = new HttpPost(url);
				// adding post params
				if (params != null) {
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				}

				httpResponse = httpClient.execute(httpPost);

			} else if (method == GET) {
				// appending params to url
				if (params != null) {
					String paramString = URLEncodedUtils
							.format(params, "utf-8");
					url += "?" + paramString;
				}
				HttpGet httpGet = new HttpGet(url);

				httpResponse = httpClient.execute(httpGet);

			}
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
					.url(url)
					.build();
			Response response = client.newCall(request).execute();

			String string=response.body().toString();
			Log.e("DAta",string+"----------");
			return string;

		}catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
