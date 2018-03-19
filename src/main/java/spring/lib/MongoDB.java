package spring.lib;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;

public class MongoDB {

	public static MongoClient mongo = null;
	public static MongoCredential credential = null;
	public static MongoDatabase mongodb = null;

	public static void init() {
		mongo = new MongoClient("localhost", 27017);
		credential = MongoCredential.createCredential("user", "spring", "password".toCharArray());
		mongodb = mongo.getDatabase("spring");
	}

	public static JSONArray fetch(FindIterable<Document> iterable) {
		JSONArray list = new JSONArray();
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) {
			list.put(cursor.next());
		}
		cursor.close();
		return list;
	}

}
