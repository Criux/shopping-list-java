package com.lerngruppe.crawler.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lerngruppe.crawler.dao.DatabaseQueries;
import com.lerngruppe.crawler.model.Product;
import com.lerngruppe.crawler.model.Store;
import com.lerngruppe.crawler.parser.ProductParser;


public class ExtractReweItems {

	private static final Logger logger = Logger.getLogger(ExtractReweItems.class.getName());
	private static final String serviceType = "PICKUP"; // PICKUP, STATIONARY
	public static List<String> existingProductIds;
	public static List<Store> existingStores;
	public DatabaseQueries db;

	public ExtractReweItems() {
		db = new DatabaseQueries();
		existingProductIds = db.getAllProductIds();
		existingStores = db.getAllStores();
	}

	public static void main(String[] args) throws IOException {
		ExtractReweItems rewe = new ExtractReweItems();
		Stopwatch time = new Stopwatch();
		time.start();
		// Test 1: all products from productList
		for (Store store : rewe.getStoreInfoFromPage("https://www.rewe.de/marktseite/bielefeld/",
				"nordrhein-westfalen")) {
			rewe.db.deleteTransactionalForStore(store.getId());
			System.out.println("working on store:"+store.getId());
			List<Product> productList = rewe
					.getProductsFromCategory("https://shop.rewe.de/productList?objectsPerPage=250", store);
			productList = //productList.stream().distinct().collect(Collectors.toList());
					productList.stream().distinct().collect(Collectors.toCollection(()->new ArrayList<Product>()));
			System.out.println("Total products found: " + productList.size());
			existingProductIds = rewe.db.getAllProductIds();
		}

		// Test 2: find all rewe stores
//		List<Store> storeList = rewe.getStores("Nordrhein-Westfalen");
//		storeList = storeList.stream().distinct().collect(Collectors.toList());
//		logger.info("Total stores found: " + storeList.size());
//		for (Store store : storeList) {
//			rewe.db.insertStore(StoreParser.getInsertMap(store));
//		}
		// Test 3: all products from bielefeld
		// List<Product> productList=new ArrayList<Product>();
		// for(Store
		// store:rewe.getStoreInfoFromPage("https://www.rewe.de/marktseite/bielefeld/",
		// "nordrhein-westfalen")) {
		// System.out.println(store.getId());
		// productList.addAll(rewe.getProductsFromCategory("https://shop.rewe.de/productList?objectsPerPage=250",store.getId()));
		// }
		// productList=productList.stream().distinct().collect(Collectors.toList());
		// Database db= new Database();
		// for(Product product:productList) {
		// db.insertProduct(ProductParser.getInsertMap(product));
		// }
		System.out.println(time.stop());

	}

	public List<Store> getStores(String stateName) throws IOException {
		List<Store> resultList = Collections.synchronizedList(new ArrayList<Store>());
		Document doc = HTTPUtils.getPage("https://www.rewe.de/marktseite", "");

		Elements states = doc.getElementsByTag("market-search-accordion").get(1)
				.getElementsByClass("col-12 col-sm-6 col-md-3");
		for (Element state : states) {
			if (!stateName.equals("all")) {
				if (state.child(0).text().toLowerCase().contains(stateName.toLowerCase())) {
					resultList.addAll(getStoresFromState(state));
				}
			} else {
				resultList.addAll(getStoresFromState(state));
			}

		}

		return resultList;
	}

	private List<Store> getStoresFromState(Element state) throws IOException {
		List<Store> resultList = Collections.synchronizedList(new ArrayList<Store>());

		String stateLink = state.child(0).attr("href");
		Document stateDoc = HTTPUtils.getPage(stateLink, "");

		Elements cityContainers = stateDoc.getElementsByClass("font-style-body four-columns");
		String stateText = stateLink.substring(0, stateLink.length() - 1).replaceAll(".*/", "");
		logger.trace("Importing state: " + stateText);
		List<String> cityList = new ArrayList<>();
		if (cityContainers.size() > 0) {
			cityContainers.get(0).getElementsByTag("a").forEach(c -> cityList.add(c.attr("href")));
		} else {
			cityList.add(stateLink);
		}
		new ParserThreadPool<String, Store>(s -> getStoreInfoFromPage(s, stateText), cityList, resultList);
		return resultList;
	}

	public List<Store> getStoreInfoFromPage(String url, String state) {
		List<Store> resultList = new ArrayList<>();
		try {
			Document doc = HTTPUtils.getPage(url, "");
			Elements markets = doc.getElementsByTag("market-tile");
			for (Element market : markets) {
				String name = market.getElementsByClass("tile-name").get(0).text();
				String addressFull = market.getElementsByClass("tile-address").get(0).text();
				String id = "";
				String geoFull = ",";
				for (Element link : market.getElementsByTag("a")) {
					String href = link.attr("href");
					if (href.contains("marktseite")) {
						id = "REWE" + href.split("=")[1];
					}
					if (href.contains("google")) {
						geoFull = href.replaceAll(".*=", "");
					}
				}
				String openingHours = ""; // Information not yet available
				String stateText = state;
				resultList.add(new Store(id, name, addressFull, stateText, openingHours, geoFull, url));
			}
		} catch (IOException e) {
			logger.error(url + ": could not get parsed because: " + e.getMessage());
		}
		return resultList;

	}

	public List<Product> getProductsFromCategory(String pageUrl, Store store) throws IOException {

		List<Product> resultList = Collections.synchronizedList(new ArrayList<Product>());
		Document doc = HTTPUtils.getPage(pageUrl, getMarketCookie(store.getId()));
		if (doc.getElementsByClass("search-service-paginationPagesContainer").size() == 0) {
			return resultList;
		}
		Element paginationWrapper = doc.getElementsByClass("search-service-paginationPagesContainer").get(0);
		int totalPages = Integer
				.valueOf(paginationWrapper.child(paginationWrapper.children().size() - 1).attr("page-index"));
		System.out.println("Total pages: " + totalPages);
		List<String> urls = new ArrayList<>();
		for (int i = 1; i <= totalPages; i++) {
			urls.add(pageUrl + "&page=" + i);
		}
		new ParserThreadPool<String, Product>(s -> getProductsFromPage(s, store), urls, resultList);
		return resultList;
	}

	private List<Product> getProductsFromPage(String url, Store store) {
		List<Product> resultList = new ArrayList<>();
		Document document;
		try {
			document = HTTPUtils.getPage(url, getMarketCookie(store.getId()));
			Elements productWrapperList = document.getElementsByClass("search-service-product");
			for (Element wrapper : productWrapperList) {
				Product product = ProductParser.parseREWEProductFromElement(wrapper, store);
				if (!existingProductIds.contains(product.getId())) {
					try {
						product = ProductParser.parseREWEProductMasterFromPage(HTTPUtils.getPage(
								"https://shop.rewe.de"
										+ wrapper.getElementsByClass("search-service-productDetailsWrapper").get(0)
												.child(0).attr("href"),
								getMarketCookie(store.getId())), product);
						db.insertProductMaster(ProductParser.getInsertMapMaster(product));
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				db.insertProductTransactional(ProductParser.getInsertMapTransactional(product,store));
				resultList.add(product);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}

	public static String getMarketCookie(String storeId) {
		// 4040441
		return "marketsCookie=" + "%7B%22marketId%22%3A%22" + storeId.replace("REWE", "")
				+ "%22%2C%22zipCode%22%3A%2212205%22%2C%22customerZipCode%22%3A%2212205%22%2C%22marketZipCode%22%3A%2212347%22%2C%22serviceTypes%22%3A%5B%22"
				+ serviceType + "%22%5D%7D";
	}
}
