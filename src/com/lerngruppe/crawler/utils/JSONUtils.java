package com.lerngruppe.crawler.utils;

import org.jsoup.nodes.Element;

public class JSONUtils {

	public static String parseNutritionTable(Element table) {
		String nutrition;
		nutrition="{\"head\":{";
		for(Element th:table.getElementsByTag("thead").get(0).getElementsByTag("th")) {
			nutrition+="\""+th.text()+"\":";
		}
		nutrition=nutrition.substring(0,nutrition.length()-1)+"},\"body\":{";
		for(Element row:table.getElementsByTag("tbody").get(0).getElementsByTag("tr")) {
			for(Element td:row.getElementsByTag("td")) {
				nutrition+="\""+td.text()+"\":";
			}
			nutrition=nutrition.substring(0,nutrition.length()-1)+",";
		}
		nutrition=nutrition.substring(0,nutrition.length()-1)+"}}";
		return nutrition;
	}
}
