package com.lerngruppe.crawler.utils;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

public class TestMe {

	public static void submitForm() throws IOException {
		// * Connect to website
		String url = "http://www.yopmail.com/en/";
		Connection.Response resp = Jsoup.connect(url) //
				.timeout(30000) //
				.method(Connection.Method.GET).ignoreHttpErrors(true) //
				.execute();

		// * Find the form
		Document responseDocument = resp.parse();
		Element potentialForm = responseDocument.select("form#f").first();
		FormElement form = (FormElement) potentialForm;
		
		// ** Name search
		Element textBoxNameSearch = form.select("input#login").first();
		textBoxNameSearch.val("kostas");

		// ** Submit the form
		Document searchResults = form.submit().cookies(resp.cookies()).post();

		// * Extract results 
		System.out.println(searchResults.select(".bname.b.al_l").first().text());
		
	}

	public static void main(String[] args) throws IOException {
		
		Document doc = Jsoup.connect(
				"https://reiseauskunft.bahn.de/bin/query.exe/dn?ld=3811&protocol=https:&seqnr=5&ident=ii.02525111.1554626667&rt=1&rememberSortType=minDeparture&REQ0HafasScrollDir=2")
				.get();
		for (Element trip : doc.getElementsByClass("boxShadow  scheduledCon")) {
			System.out.println(trip.getElementsByClass("duration").text());
		}
		TestMe.submitForm();
	}
}
