package online.syncio.dao;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import online.syncio.component.GlassPanePopup;
import online.syncio.config.Account;
import online.syncio.view.user.ErrorDetail;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

public class MongoDBConnectOld {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static MongoDatabase getDatabase(String connectionString, String databaseName) {
        if (database == null) {
            CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

            try {
                mongoClient = MongoClients.create(connectionString);
                database = mongoClient.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);
                try {
                    Bson command = new BsonDocument("ping", new BsonInt64(1));
                    Document commandResult = database.runCommand(command);
                    System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
                } catch (MongoException me) {
                    String errorInfo = me.getMessage();
                    GlassPanePopup.showPopup(new ErrorDetail(errorInfo), "errordetail");
                }
            } catch (MongoException me) {
                String errorInfo = me.getMessage();
                GlassPanePopup.showPopup(new ErrorDetail(errorInfo), "errordetail");
            }
        }

        return database;
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

            try {
                mongoClient = MongoClients.create(Account.CONNECTION_STRING);
                database = mongoClient.getDatabase(Account.DATABASE_NAME).withCodecRegistry(pojoCodecRegistry);
                try {
                    Bson command = new BsonDocument("ping", new BsonInt64(1));
                    Document commandResult = database.runCommand(command);
                    return database;
                } catch (MongoException me) {
                    String errorInfo = me.getMessage();
                    GlassPanePopup.showPopup(new ErrorDetail(errorInfo), "errordetail");
                }
            } catch (MongoException me) {
                String errorInfo = me.getMessage();
                GlassPanePopup.showPopup(new ErrorDetail(errorInfo), "errordetail");
            }
        }

        return database;
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
