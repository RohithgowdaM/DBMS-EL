package connect;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class MongoDBExample {
    public static void main(String[] args) {
        DBconnect connection = new DBconnect();
        MongoClient mongoClient = connection.getMongoClient();

        MongoDatabase database = mongoClient.getDatabase("mydatabase");
        MongoCollection<Document> collection = database.getCollection("mycollection");

        Document document = new Document("name", "John Doe")
                .append("age", 30)
                .append("email", "butwhyyy@example.com");

        collection.insertOne(document);

        System.out.println("Document inserted successfully!");

        mongoClient.close(); // Close the MongoDB connection
    }
}
