package spring.controller;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import spring.lib.MongoDB;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.regex.Pattern;

@Controller
public class LookupController {

	@RequestMapping(value = "/BookManagementService/books", method = RequestMethod.GET)
	public ResponseEntity<?> handler(HttpServletRequest request) throws Exception {
		Gson gson = new Gson();
		JSONObject json;
		try {
			json = new JSONObject(gson.toJson(request.getParameterMap()));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		Iterator keys = json.keys();
		Document doc = new Document();
		BasicDBObject options = new BasicDBObject();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			switch (key) {
				case "id": {
					try {
						doc.append("_id", new ObjectId((String) json.getJSONArray(key).get(0)));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						return new ResponseEntity<>(HttpStatus.NO_CONTENT);
					}
					break;
				}
				case "title": {
					doc.append("Title", Pattern.compile((String) json.getJSONArray(key).get(0)));
					break;
				}
				case "author": {
					doc.append("Author", Pattern.compile((String) json.getJSONArray(key).get(0)));
					break;
				}
				case "publisher": {
					doc.append("Publisher", Pattern.compile((String) json.getJSONArray(key).get(0)));
					break;
				}
				case "year": {
					doc.append("Year", Integer.parseInt((String) json.getJSONArray(key).get(0)));
					break;
				}
				case "limit": {
					options.append("limit", Integer.parseInt((String) json.getJSONArray(key).get(0)));
					break;
				}
				case "sortby": {
					String field = "";
					switch (json.getJSONArray("sortby").getString(0)) {
						case "title": {
							field = "Title";
							break;
						}
						case "author": {
							field = "Author";
							break;
						}
						case "publisher": {
							field = "Publisher";
							break;
						}
						case "year": {
							field = "Year";
							break;
						}
					}
					String order = json.getJSONArray("order").getString(0);
					BasicDBObject sort = new BasicDBObject();
					switch (order) {
						case "asc": {
							sort.put(field, 1);
							break;
						}
						case "desc": {
							sort.put(field, -1);
						}
					}
					options.put("sort", sort);
				}
			}
		}
		BasicDBObject projection = new BasicDBObject();
		projection.append("_id", 0);
		projection.append("Title", 1);
		projection.append("Author", 1);
		projection.append("Publisher", 1);
		projection.append("Year", 1);
		FindIterable<Document> cursor = MongoDB.mongodb.getCollection("books").find().filter(doc).projection(projection);
		if (options.get("sort") != null) {
			cursor.sort((BasicDBObject) (options.get("sort")));
		}
		if (options.get("limit") != null) {
			if (options.getInt("limit") == 0) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				cursor.limit(options.getInt("limit"));
			}
		}
		JSONArray books = MongoDB.fetch(cursor);
		if (books.length() != 0) {
			JSONObject result = new JSONObject();
			result.put("FoundBooks", books.length());
			result.put("Results", books);
			return new ResponseEntity<>(result.toString(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

}
