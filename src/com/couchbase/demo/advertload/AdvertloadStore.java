
package com.couchbase.demo.advertload;

/**
 *
 * @author ingenthr
 */
public interface AdvertloadStore {

    AdvertloadFuture<Boolean> add(String key, int exp, Object o);

    Object get(String key);

    AdvertloadFuture<Boolean> set(String key, int exp, Object o);

}
