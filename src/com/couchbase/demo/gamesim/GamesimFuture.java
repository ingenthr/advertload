package com.couchbase.demo.gamesim;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author ingenthr
 */
public class GamesimFuture<Boolean> implements Future {

    boolean operationStatus;

    GamesimFuture(boolean b) {
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
