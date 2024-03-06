package main;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class DBconnect {
	private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> reviewCollection;

    public DBconnect() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("mydatabase");
        reviewCollection = database.getCollection("reviews");
    }

    public void insertReview(String drugName, String review, String reviewType) {
        MongoCollection<Document> collection = database.getCollection("reviews");

        Document document = new Document("drug_name", drugName)
                .append("review", review)
                .append("review_type", reviewType);

        collection.insertOne(document);

        System.out.println("Review inserted successfully!");
    }

    public List<String> fetchReviews(String drugName) {
        List<String> reviews = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("reviews");

        // Find all reviews for the given drug name
        for (Document doc : collection.find(Filters.eq("drug_name", drugName))) {
            reviews.add(doc.getString("review"));
        }

        return reviews;
    }
    public Map<String, Integer> fetchSentiment(String drugName) {
        Map<String, Integer> sentimentCount = new HashMap<>();
        sentimentCount.put("positive", 0);
        sentimentCount.put("negative", 0);
        sentimentCount.put("neutral", 0);

        Document query = new Document("drug_name", drugName);
        FindIterable<Document> result = reviewCollection.find(query);
        for (Document doc : result) {
            String reviewType = doc.getString("review_type");
            sentimentCount.put(reviewType, sentimentCount.get(reviewType) + 1);
        }
        return sentimentCount;
    }
    public void closeConnection() {
        mongoClient.close();
    }
}
