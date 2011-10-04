package com.atoito.please.core.components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.atoito.please.core.exception.PleaseException;
import com.atoito.please.core.util.M;
import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.Response;
import com.ning.http.client.Response.ResponseBuilder;
import com.ning.http.client.resumable.ResumableIOExceptionFilter;

/**
 *  Downloader component; to fetch files from remote url.
 *  It uses Asynchronous Http Client (https://github.com/sonatype/async-http-client) under the cover.
 * 
 *  System.setProperty("http.proxySet" , "true");
 *  System.setProperty("http.proxyHost", "192.168...");
 *  System.setProperty("http.proxyPort", "...");
 *  File destination = new File("file-"+System.currentTimeMillis()+".down");
 *  DefaultDownloader d = new DefaultDownloader();
 *  File downloaded = d.fetch("http://...", destination.getAbsolutePath());
 *
 */
public class DefaultDownloader implements Downloader {

	private File buildDownloadFile(String path) {
		File file = new File(path);
		try {
			Files.touch(file);
		} catch (IOException e) {
			throw new PleaseException("error saving file to "
					+ file.getAbsolutePath(), e);
		}
		return file;
	}
	
	private OutputStream buildDownloadOutputStream(File destination) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(destination);
		} catch (FileNotFoundException e) {
			throw new PleaseException("error writing to "+destination.getAbsolutePath(), e);
		}
		return outputStream;
	}
	
	public File fetch(String urlToFetch) {
		String path = "download-"+System.currentTimeMillis();
		return fetch(urlToFetch, path);
	}

	public File fetch(String urlToFetch, String pathToSave) {
		final String url = Preconditions.checkNotNull(urlToFetch);
		final String path = Preconditions.checkNotNull(pathToSave);
		File downloaded = buildDownloadFile(path);

		final OutputStream downloadOutputStream = buildDownloadOutputStream(downloaded);
		
		AsyncHandler<Response> httpHandler = new AsyncHandler<Response>() {

	        private ResponseBuilder builder = new Response.ResponseBuilder();
	        
	        private final OutputStream outputStream = downloadOutputStream;

			public void onThrowable(Throwable t) {
				throw new PleaseException("error downloading "+url, t);
			}

			public com.ning.http.client.AsyncHandler.STATE onBodyPartReceived(
					HttpResponseBodyPart bodyPart) throws Exception {
				M.debug(".");
				bodyPart.writeTo(outputStream);
	            return STATE.CONTINUE;
			}

			public com.ning.http.client.AsyncHandler.STATE onStatusReceived(
					HttpResponseStatus responseStatus) throws Exception {
				M.debug("onStatusReceived %s%n", responseStatus);
			    builder.accumulate(responseStatus);
				return STATE.CONTINUE;
			}

			public com.ning.http.client.AsyncHandler.STATE onHeadersReceived(
					HttpResponseHeaders headers) throws Exception {
				M.debug("onHeadersReceived %s%n", headers);
			    builder.accumulate(headers);
				return STATE.CONTINUE;
			}

			public Response onCompleted() throws Exception {
				Closeables.closeQuietly(outputStream);
				return builder.build();
			}
		};

		AsyncHttpClientConfig cf = new AsyncHttpClientConfig.Builder()
		.setUseProxyProperties(true)
		.addIOExceptionFilter(new ResumableIOExceptionFilter())
	    .setMaximumConnectionsPerHost(10)
	    .setMaximumConnectionsTotal(100)
		.build();
		AsyncHttpClient client = new AsyncHttpClient(cf);

		try {
			BoundRequestBuilder get = client.prepareGet(url);
			Response response = get.execute(httpHandler).get();

			if (response != null) {
				debugResponse(response);
				
				String contentType = response.getContentType();
				if ((contentType != null) &&
					(contentType.startsWith("text/"))) {
					M.debug("I think it's text content");
				}
				M.debug("status code '%s'", response.getStatusCode());
				M.debug("status text '%s'", response.getStatusText());
				M.debug("has response body '%s'", response.hasResponseBody());
				M.debug("is redirected '%s'", response.isRedirected());
			}
		} catch (Exception e) {
			throw new PleaseException("error downloading " + url, e);
		} finally {
			// maybe something gone wrong
			Closeables.closeQuietly(downloadOutputStream);
		}
		return downloaded;
	}
	
	private void debugResponse(Response response) {
		FluentCaseInsensitiveStringsMap headers = response.getHeaders();
		M.debug("response headers:");
		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			M.debug(" - %s = %s", entry.getKey(), entry.getValue());
		}
		M.debug("content type '%s'", response.getContentType());
	}

}
