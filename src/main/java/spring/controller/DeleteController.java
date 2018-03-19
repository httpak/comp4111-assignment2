package spring.controller;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import spring.lib.MongoDB;

@Controller
public class DeleteController {

	@RequestMapping(value = "/BookManagementService/books/{_id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> handler(@PathVariable String _id) throws Exception {
		Document doc = new Document();
		try {
			doc.append("_id", new ObjectId(_id));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new ResponseEntity<>("No book record", HttpStatus.NOT_FOUND);
		}
		JSONArray books = MongoDB.fetch(MongoDB.mongodb.getCollection("books").find().filter(doc));
		if (books.length() != 0) {
			MongoDB.mongodb.getCollection("books").deleteOne(doc);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>("No book record", HttpStatus.NOT_FOUND);
		}
	}

}
