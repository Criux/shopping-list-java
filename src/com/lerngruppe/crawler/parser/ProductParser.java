package com.lerngruppe.crawler.parser;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lerngruppe.crawler.model.Product;
import com.lerngruppe.crawler.model.Store;
import com.lerngruppe.crawler.utils.ExtractReweItems;
import com.lerngruppe.crawler.utils.HTTPUtils;
import com.lerngruppe.crawler.utils.JSONUtils;

public class ProductParser {

	public static Product parseREWEProductFromElement(Element wrapper, Store store) {
		Product result = null;
		String id = "REWE" + wrapper.getElementsByTag("a").attr("href").replaceAll(".*/", "");
		String quantityInfo="";
		if(wrapper.getElementsByClass("search-service-productGrammage").get(0).children().size()>0) {
			quantityInfo=wrapper.getElementsByClass("search-service-productGrammage").get(0).child(0).text();			
		}		
		// replace this when price is handled as currency
		String priceString = wrapper.getElementsByClass("search-service-productPrice").text().replace("€", "").replace("ab ", "")
				.replace(",", ".");
		double originalPrice = 0.0;
		double currentPrice=0.0;
		boolean onSale=false;
		LocalDate saleUntil=null;
		if (!priceString.isEmpty()) {
			originalPrice = Double.valueOf(priceString);
		}else {
			String salesPrice=wrapper.getElementsByClass("search-service-productOfferOriginalPrice").text().replace("€", "").replace("ab ", "")
			.replace(",", ".");
			if(!salesPrice.isEmpty()) {
				originalPrice=Double.valueOf(salesPrice);
				currentPrice=originalPrice;				
			}
			if(wrapper.getElementsByClass("search-service-productOffer").size()!=0) {
				saleUntil=LocalDate.parse(wrapper.getElementsByClass("search-service-productOfferDuration").text().replace("bis ","").trim(),
						DateTimeFormatter.ofPattern("d.M.yyyy"));
				onSale=true;
				currentPrice=Double.valueOf(wrapper.getElementsByClass("search-service-productOfferPrice").text().replace("€", "").replace("ab ", "")
						.replace(",", "."));
			}
		}
		result = new Product(id);
		result.setQuantityInfo(quantityInfo);
		result.setOriginalPrice(originalPrice);
		result.setCurrentPrice(currentPrice);
		result.setOnSale(onSale);
		result.setSaleUntil(saleUntil);
		
		return result;
	}
	public static Product parseREWEProductMasterFromPage(Document doc,Product product) {
		product.setName(doc.getElementsByClass("pdr-QuickInfo__heading").text());
		product.setBrand(doc.getElementsByClass("pdr-QuickInfo__brandLink").text());
		product.setActive(true);
		
		Elements tables=doc.getElementsByClass("pdr-NutritionTable");
		if(tables.size()>0) {
			product.setNutrition(JSONUtils.parseNutritionTable(tables.get(0)));
		}
		for(Element attribute:doc.getElementsByClass("pdr-Attribute__label")) {
			if(attribute.text().equals("Zutaten:")) {
				product.setDescription2(attribute.parent().text());
			}
			if(attribute.text().equals("Allergene:")) {
				product.setDescription3(attribute.parent().text());
			}
		}
		for(Element pic:doc.getElementsByTag("picture")){
			if(pic.hasClass("pdr-ProductMedia")) {
				product.setPictureUrl(pic.getElementsByTag("img").attr("src"));
			}
		}
		String labelText="";
		Elements breadcrumbs=doc.getElementsByClass("lr-breadcrumbs").get(0).children();
		for(Element breadcrumb:breadcrumbs) {
			labelText+=breadcrumb.text().replace("&", "").replaceAll(" +", ",")+",";
		}
		product.setLabelText(labelText.substring(0,labelText.length()-1));
		return product;
	}
	public static Map<String,Object> getInsertMapMaster(Product product){
		Map<String, Object> map= new HashMap<>();
		map.put("ID", product.getId());
		map.put("FULL_NAME", product.getName());
		map.put("BRAND", product.getBrand());
		map.put("ISACTIVE", product.isActive());
		map.put("QUANTITY_INFO",product.getQuantityInfo());
		map.put("NUTRITION", product.getNutrition());
		map.put("DESCRIPTION2", product.getDescription2());
		map.put("DESCRIPTION3", product.getDescription3());
		map.put("PICTURE_URL", product.getPictureUrl());
		map.put("LABEL_TEXT", product.getLabelText());
		return map;
	}
	public static void main(String[] args) throws IOException {
//		Product pr=ProductParser.parseREWEProductFromPage(HTTPUtils.getPage("https://shop.rewe.de/p/ja-soft-feigen-getrocknet-250g/3151967",
//				"marketsCookie="+"%7B%22marketId%22%3A%22"+"320735"+"%22%2C%22zipCode%22%3A%2212205%22%2C%22customerZipCode%22%3A%2212205%22%2C%22marketZipCode%22%3A%2212347%22%2C%22serviceTypes%22%3A%5B%22"+"PICKUP"+"%22%5D%7D"));
		
	}
	public static Map<String, Object> getInsertMapTransactional(Product product, Store store) {
		Map<String, Object> map= new HashMap<>();
		map.put("STORE_ID", store.getId());
		map.put("PRODUCT_ID",product.getId());
		map.put("ORIGINAL_PRICE", product.getOriginalPrice());
		map.put("CURRENT_PRICE", product.getCurrentPrice());
		map.put("ONSALE", product.isOnSale());
		if(product.getSaleUntil()!=null) {
			map.put("SALE_UNTIL",java.sql.Date.valueOf(product.getSaleUntil()));			
		}
		return map;
	}
}
