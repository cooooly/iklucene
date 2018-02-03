package lucenemanager.manager;

import lucenemanager.po.Product;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cooooly on 2018/2/3.
 */
/*创建索引*/
public class IndexManager {
    @Test
    public void indexManager() throws IOException {

        /*数据源来自数据库 , 调方法获取 , 存入list集合List<Product> products*/

        /*遍历products , 拿到每一个对象 , 拿到每一个字段存入filed域*/
        Product product = new Product();
        List<Document> documents = new ArrayList<Document>();

        
        /*创建filed域*/
        //id--不分词 要索引 要存储
        StringField id = new StringField("id", product.getId().toString(), Field.Store.YES);
        //name--要分词 要索引 要存储
        TextField name = new TextField("name", product.getP_name(), Field.Store.YES);
        //price--要分词 要索引 要存储
        FloatField price = new FloatField("price", product.getPrice(), Field.Store.YES);
        //description 要分词 要索引 不存储
        TextField description = new TextField("description", product.getDescription(), Field.Store.NO);

        /*将filed域加入document*/
        Document document = new Document();
        document.add(id);
        document.add(name);
        document.add(price);
        document.add(description);
        /*将document加入到集合documents中*/
        documents.add(document);


        /*创建索引 , 核心API IndexWriter*/
        /*创建索引目录的流对象*/
        Directory d = FSDirectory.open(new File("D://盘符路径"));
        /*ik分词*/
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);

        /*创建索引的对象*/
        IndexWriter indexWriter = new IndexWriter(d,conf);

        /*索引对象创建索引*/
        indexWriter.addDocuments(documents);
        /*利用索引对象还可以删除更新索引
            indexWriter.deleteDocuments(query);
            indexWriter.deleteAll();
            indexWriter.updateDocument(term,analyzer);
        */
        indexWriter.commit();
        indexWriter.close();
    }
}
