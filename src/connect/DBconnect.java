package connect;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;

public class DBconnect {
    private MongoClient mongoClient;

    public DBconnect() {
        mongoClient = MongoClients.create("mongodb://localhost:27017"); // MongoDB connection URL
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
