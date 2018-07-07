package com.yodean.site.web.common.service;

import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

/**
 * Created by rick on 2017/9/13.
 */
public interface ClientWriter {
    public void write(IndexWriter writer) throws IOException;
}
