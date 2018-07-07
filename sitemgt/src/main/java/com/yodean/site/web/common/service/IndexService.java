package com.yodean.site.web.common.service;

import com.rick.dev.config.Global;
import com.rick.dev.service.DefaultService;
import com.rick.dev.service.JdbcTemplateService;
import com.yodean.site.web.common.dto.QueryDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/9/13.
 */
@Service
public class IndexService extends DefaultService {
    @Value("${upload}")
    private String upload;

    private static final String INDEX_DIR = "index";

    public void buildIndex(int webId) throws IOException {
        final String indexSQL = "com.yodean.site.web.content.fulltext";

        //create index
        String indexFolder = upload + File.separator + Global.SITE_FOLDER + File.separator + webId + File.separator + INDEX_DIR;
        File file = new File(indexFolder);
        if (file.exists())
            FileUtils.cleanDirectory(file);


        createIndex(indexFolder, new ClientWriter() {
            @Override
            public void write(IndexWriter writer) throws IOException {
                Map<String, Object> params = new HashMap<String, Object>(1);
                params.put("webId", webId);

                jdbcTemplateService.queryForSpecificParam(indexSQL, params, new JdbcTemplateService.JdbcTemplateExecutor<Void>() {
                    @Override
                    public Void query(JdbcTemplate jdbcTemplate, String queryString, Object[] args) {
                        jdbcTemplate.query(queryString, args, new RowCallbackHandler() {
                            @Override
                            public void processRow(ResultSet rs) throws SQLException {
                                try {
                                    addDoc(writer, rs.getInt("type"), rs.getString("href"), rs.getString("cover"),
                                            rs.getString("title"), rs.getString("content"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        return null;
                    }
                });
            }
        });
    }


    public void createIndex(String indexFolder, ClientWriter writer) throws IOException {
        IndexWriter w = null;
        try {
            Directory index = FSDirectory.open(Paths.get(indexFolder));
            Analyzer analyzer = new StandardAnalyzer();

            IndexWriterConfig config = new IndexWriterConfig(analyzer);


            w = new IndexWriter(index, config);
            writer.write(w);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (w != null) w.close();
        }

    }

    private void addDoc(IndexWriter writer, Integer type, String href, String cover, String title, String content) throws IOException {
        Document doc = new Document();
        doc.add(new StoredField("type", type));
        doc.add(new StringField("href", href==null ? "": href, Field.Store.YES));
        doc.add(new StoredField("cover", cover));
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("content", content, Field.Store.YES));
        writer.addDocument(doc);
    }

    public List<QueryDto> search(Integer webId, String keywords) throws ParseException, IOException, InvalidTokenOffsetsException {
        int hitsPerPage = 10;

        List<QueryDto> queryDtoList = new ArrayList<QueryDto>(hitsPerPage);
        // 2. query
        Analyzer analyzer = new StandardAnalyzer();
        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.

        String[] fields = {"title", "content"};
        Query q = new MultiFieldQueryParser(fields, analyzer).parse(keywords);

        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<em>", "</em>");
        Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(q));

        // 3. search
        String indexFolder = upload + File.separator + Global.SITE_FOLDER + File.separator + webId + File.separator + INDEX_DIR;
        Directory index = FSDirectory.open(Paths.get(indexFolder));

        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        // 4. display results
//        System.out.println("Found " + hits.length + " hits.");
        for(int i = 0; i< hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);

            String title = d.get("title");
            String _title = highlighter.getBestFragment(analyzer, "title", title);
            if (_title != null)
                    title = _title;

            String content = d.get("content");
            String _content = highlighter.getBestFragment(analyzer, "content", content) ;
            if (_content != null)
                content = _content;

            content = StringUtils.abbreviate(content, 300);

            queryDtoList.add(new QueryDto(d.get("type"), d.get("href"), d.get("cover"), title ,content));

        }

        return queryDtoList;
    }

}
