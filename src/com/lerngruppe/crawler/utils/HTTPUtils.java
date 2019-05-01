package com.lerngruppe.crawler.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTTPUtils {

	public static String CONNECTION_TYPE = "xPROXY";

	public static Document getPage(String pageUrl, String cookie) throws IOException {
		Document doc = null;
		URL url = new URL(pageUrl);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.1.1.3", 8080));
		HttpURLConnection uc;
		if (CONNECTION_TYPE.equals("PROXY")) {
			uc = (HttpURLConnection) url.openConnection(proxy);
		} else {
			uc = (HttpURLConnection) url.openConnection();
		}
		uc.setRequestProperty("Cookie", cookie);
		uc.connect();

		String line = null;
		StringBuffer tmp = new StringBuffer();
		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
		while ((line = in.readLine()) != null) {
			tmp.append(line);
		}
		doc = Jsoup.parse(String.valueOf(tmp));

		// Document doc2 = Jsoup.connect(pageUrl)
		// .cookie("marketsCookie",
		// "%7B%22marketId%22%3A%22541754%22%2C%22zipCode%22%3A%2233649%22%2C%22serviceTypes%22%3A%5B%22PICKUP%22%5D%2C%22customerZipCode%22%3A%2233649%22%2C%22marketZipCode%22%3A%2233611%22%7D")
		// .get();
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

	public static void main(String[] args) {
		try {

			// set HTTP proxy host to 127.0.0.1 (localhost)
			System.setProperty("http.proxyHost", "10.1.1.3");

			// set HTTP proxy port to 3128
			System.setProperty("http.proxyPort", "8080");

			String strText = Jsoup.connect("http://www.example.com").userAgent("Mozilla/5.0").timeout(10 * 1000).get()
					.text();

			System.out.println(strText);

		} catch (IOException ioe) {
			System.out.println("Exception: " + ioe);
		}
	}
}
