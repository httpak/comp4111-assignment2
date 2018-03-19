package spring.controller;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import spring.lib.MongoDB;

@Controller
public class LoanController {

	@RequestMapping(value = "/BookManagementService/books/{_id}", method = RequestMethod.PUT)
	public ResponseEntity<?> handler(@PathVariable String _id, @RequestBody String request) throws Exception {
		JSONObject json = new JSONObject(request);
		Document doc = new Document();
		try {
			doc.append("_id", new ObjectId(_id));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new ResponseEntity<>("No book record", HttpStatus.NOT_FOUND);
		}
		JSONArray books = MongoDB.fetch(MongoDB.mongodb.getCollection("books").find().filter(doc));
		if (books.length() != 0) {
			boolean status = books.getJSONObject(0).getBoolean("Available");
			if (status != json.getBoolean("Available")) {
				BasicDBObject mutator = new BasicDBObject();
				mutator.append("Available", json.getBoolean("Available"));
				BasicDBObject setter = new BasicDBObject();
				setter.append("$set", mutator);
				MongoDB.mongodb.getCollection("books").updateOne(doc, setter);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("No book record", HttpStatus.NOT_FOUND);
		}
	}

}
