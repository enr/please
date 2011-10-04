package com.atoito.please.core.components;

import java.io.File;

/**
 * Sample usage:
 * DefaultDownloader d = new DefaultDownloader();
 * File downloaded = d.fetch("http://path/to/file", "/destination/path");
 *
 */
public interface Downloader {

	/**
	 * Fetch the given url and returns the file with contents.
	 * 
	 * @param url
	 * @return the file
	 */
	File fetch(String url);
	
	/**
	 * Fetch the given url, save the file in the given path and returns the file with contents.
	 * 
	 * @param url
	 * @param path
	 * @return the file
	 */
	File fetch(String url, String path);
}
