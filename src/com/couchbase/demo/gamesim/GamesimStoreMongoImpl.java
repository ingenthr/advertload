/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.couchbase.demo.gamesim;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;

import com.mongodb.util.JSON;

import java.net.UnknownHostException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ingenthr
 */
public class GamesimStoreMongoImpl implements GamesimStore {

    private Mongo myMongo;
    private DB db;
    private DBCollection storer;

    GamesimStoreMongoImpl(String serverUri, String dbName, String collectionName) {
        try {
            myMongo = new Mongo(new MongoURI(serverUri));
        } catch (UnknownHostException ex) {
            Logger.getLogger(GamesimStoreMongoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        db = myMongo.getDB(dbName);

        Set<String> colls = db.getCollectionNames();

        for (String s : colls) {
            System.err.println("collection found: " + s);
        }

        storer = db.getCollection(collectionName);
    }

    @Override
    public GamesimFuture<Boolean> add(String key, int exp, Object o) {
//        Object parse = JSON.parse(o.toString());
        DBObject doc = (DBObject)JSON.parse(o.toString());

        storer.insert(doc);

        return new GamesimFuture(true);
    }

    @Override
    public Object get(String key) {

        BasicDBObject query = new BasicDBObject();

        query.put("name", key);

        DBCursor cur = storer.find(query);

        if(!cur.hasNext()) {
            throw new RuntimeException("Query failed to find data which should be there.");
        }
        DBObject retval = cur.next();
        return retval.toString();
    }

    @Override
    public GamesimFuture<Boolean> set(String key, int exp, Object o) {
        DBObject doc = (DBObject)JSON.parse(o.toString());
        WriteResult insertResult = storer.insert(doc);
        CommandResult lastError = insertResult.getLastError();

        if (lastError.ok())
            return new GamesimFuture(true);
        else
            return new GamesimFuture(false);
    }
}
