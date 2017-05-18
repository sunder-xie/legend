package com.tqmall.legend.bi.entity;

/**
 * Created by mokala on 9/22/15.
 */
public class CommonPair<T,S> {
    private T dataF;
    private S dataS;

    public T getDataF() {
        return dataF;
    }

    public void setDataF(T dataF) {
        this.dataF = dataF;
    }

    public S getDataS() {
        return dataS;
    }

    public void setDataS(S dataS) {
        this.dataS = dataS;
    }
}