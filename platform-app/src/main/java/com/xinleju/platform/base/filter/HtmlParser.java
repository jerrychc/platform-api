package com.xinleju.platform.base.filter;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HtmlParser {
	
	public Map<String, String> parseCasPostRequestArg(String html) {
		Map<String, String> result = new HashMap<String, String>();
		if (html != null && !"".equals(html)) {
			Document doc = Jsoup.parse(html);
			String action = doc.select("#fm1").get(0).attr("action");
			String lt = doc.select("input[name=lt]").get(0).attr("value");
			result.put("action", action);
			result.put("lt",lt);
			result.put("_eventId", "submit");
			Elements exs = doc.select("input[name=execution]");
			if(exs != null && exs.size() > 0) {
				String execution = exs.get(0).attr("value");
				result.put("execution", execution);
			}
		}
		return result;
	}

}
