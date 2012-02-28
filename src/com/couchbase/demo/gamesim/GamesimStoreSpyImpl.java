/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.couchbase.demo.gamesim;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.spy.memcached.CouchbaseClient;

/**
 *
 * @author ingenthr
 */
public class GamesimStoreSpyImpl implements GamesimStore {

    private CouchbaseClient storer;

    GamesimStoreSpyImpl(CouchbaseClient spyStore) {
        storer = spyStore;
    }

    GamesimStoreSpyImpl(ArrayList<URI> servers, String bucketname, String bucketpass) {
        try {
            storer = new CouchbaseClient(servers, bucketname, bucketpass);
        } catch (IOException ex) {
            Logger.getLogger(GamesimStoreSpyImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Could not create new CouchbaseClient", ex);
        }
    }

    @Override
    public GamesimFuture<Boolean> add(String key, int exp, Object o) {
        GamesimFuture result;
        try {
            result = new GamesimFuture(storer.add(key, exp, o).get());
        } catch (InterruptedException ex) {
            Logger.getLogger(GamesimStoreSpyImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(GamesimStoreSpyImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new GamesimFuture(true);
    }

    @Override
    public Object get(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GamesimFuture<Boolean> set(String key, int exp, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
