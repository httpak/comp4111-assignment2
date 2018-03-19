package spring.controller;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import spring.lib.MongoDB;

@Controller
public class AddController {

	@RequestMapping(value = "/BookManagementService/books", method = RequestMethod.POST)
	public ResponseEntity<?> handler(@RequestBody String request) throws Exception {
		JSONObject json;
		try {
			json = new JSONObject(request);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		Document doc = Document.parse(json.toString());
		JSONArray books = MongoDB.fetch(MongoDB.mongodb.getCollection("books").find().filter(doc));
		if (books != null) {
			if (books.length() == 0) {
				doc.append("Available", true);
				MongoDB.mongodb.getCollection("books").insertOne(doc);
				return new ResponseEntity<>("Location: /BookManagementService/books/" + doc.get("_id"), HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Duplicate record: /BookManagementService/books/" + books.getJSONObject(0).get("_id"), HttpStatus.CONFLICT);
			}
		} else {
			return null;
		}
	}

}
