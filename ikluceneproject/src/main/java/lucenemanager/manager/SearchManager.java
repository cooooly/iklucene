package lucenemanager.manager;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cooooly on 2018/2/3.
 */
/*客户端搜索,外加ik分词库拓展*/
public class SearchManager {
    @Test
    public void searchManager() throws IOException, ParseException {
        /*从索引库搜索,读取索引库,核心API  IndexSearch*/

        /*指定读取索引目录*/
        Directory d = FSDirectory.open(new File("d://盘符"));
        DirectoryReader indexReader = DirectoryReader.open(d);
        /*搜索核心API IndexSearch*/
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        /*客户端搜索,将关键字分词再查询*/

        /*分词器ik*/
        Analyzer analyzer = new IKAnalyzer();
        /*查询解析器*/
        QueryParser queryParser = new QueryParser("name",analyzer);
        /*查询对象 , 第一个参数是哪个域查找,第二个是关键字*/
        Query query = queryParser.parse("name:编程思想");
        System.out.println(query);//查看查询语句

         /*根据查询条件得到所有匹配的Documents*/
        TopDocs topDocs = indexSearcher.search(query, 10);
        /*对documents进行权重排名,的到权重大的documents*/
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        /*遍历拿到每个document*/
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            Document document = indexReader.document(doc);
            System.out.println("id:"+document.get("id")+"    name:"+document.get("name")+"     price:"+document.get("price"));
        }
        indexReader.close();
    }

    /*按照price范围查询*/
    @Test
    public void randomQuery() throws IOException {

        Directory d = FSDirectory.open(new File("d:/bhello/lucene/indexdata"));
        //IndexReader indexReader = IndexReader.open(d);
        DirectoryReader directoryReader = DirectoryReader.open(d);
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        /*查询*/
        NumericRangeQuery numericRangeQuery = NumericRangeQuery.newFloatRange("price",50f,70f,true,true);
        TopDocs topDocs = indexSearcher.search(numericRangeQuery, 5);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            Document document = directoryReader.document(doc);
            System.out.println(document.get("id"));
            System.out.println(document.get("name"));
            System.out.println(document.get("price"));
        }
        directoryReader.close();
    }

    /*联合查询booleanQUery*/
    @Test
    public void booleanQuery() throws IOException {

        Directory d = FSDirectory.open(new File("d:/bhello/lucene/indexdata"));
        //IndexReader indexReader = IndexReader.open(d);
        DirectoryReader directoryReader = DirectoryReader.open(d);
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        /*查询termquery:name-java*/
        TermQuery termQuery = new TermQuery(new Term("name","spring"));

        /*查询价格*/
        NumericRangeQuery numericRangeQuery = NumericRangeQuery.newFloatRange("price",0f,70f,true,true);

        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(termQuery, BooleanClause.Occur.MUST);
        booleanQuery.add(numericRangeQuery, BooleanClause.Occur.MUST);

        TopDocs topDocs = indexSearcher.search(booleanQuery, 5);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            Document document = directoryReader.document(doc);
            System.out.println(document.get("id"));
            System.out.println(document.get("name"));
            System.out.println(document.get("price"));
        }
        directoryReader.close();
    }
    /*联合查询用queryparser 查询解析器*/
    @Test
    public void  queryParserTest() throws IOException, ParseException {

        Directory d = FSDirectory.open(new File("d:/bhello/lucene/indexdata"));
        DirectoryReader directoryReader = DirectoryReader.open(d);

        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        /*以上表示从哪个索引库查*/

        /*客户查询条件,分词查询*/

        String[] fileds = {"name","description"};
        Map<String,Float> boosts  = new HashMap<String,Float>();
        /*干预打分*/
        boosts.put("name",300.0f);

        Analyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new MultiFieldQueryParser(fileds,analyzer,boosts);
        Query query = queryParser.parse("java");

        TopDocs topDocs = indexSearcher.search(query, 5);
        /*拿到匹配度高的document*/
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            Document document = directoryReader.document(doc);
            System.out.println(document.get("id"));
            System.out.println(document.get("name"));

        }
        directoryReader.close();
    }
}
