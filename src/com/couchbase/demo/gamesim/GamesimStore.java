/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.couchbase.demo.gamesim;

/**
 *
 * @author ingenthr
 */
public interface GamesimStore {

    GamesimFuture<Boolean> add(String key, int exp, Object o);

    Object get(String key);

    GamesimFuture<Boolean> set(String key, int exp, Object o);

}
