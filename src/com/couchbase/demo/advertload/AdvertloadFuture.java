package com.couchbase.demo.advertload;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author ingenthr
 */
public class AdvertloadFuture<Boolean> implements Future {

    boolean operationStatus;

    AdvertloadFuture(boolean b) {
        operationStatus = b;
    }

    @Override
    public boolean cancel(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return this;
    }

    @Override
    public Object get(long l, TimeUnit tu) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
