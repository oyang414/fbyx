package com.ouyang.fbyx.elaticsearch;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @description: ES demo
 * @author: ouyangxingjie
 * @create: 2022/2/21 22:16
 **/
public class ElaticSearchDemo {

    /**
     * @Author ouyangxingjie
     * @Description ES查询
     * @Date 22:27 2022/2/21
     * @param keyword 关键词
     * @return com.alibaba.fastjson.JSONObject
     */
    public JSONObject query(String keyword) {

        // 引入client，配置按各自修改
        RestHighLevelClient client = new RestHighLevelClient( RestClient.builder( new HttpHost("139.198.183.194", 9200, "http")));

        JSONObject resJSON = new JSONObject();
        JSONArray jsonArr = new JSONArray();

        // 查询流程***（重要）：子查询对象（QueryBuilder）-->父查询对象（BoolQueryBuilder）-->查询函数构造对象（SearchSourceBuilder）-->请求发起对象（SearchRequest ）-->发起请求-->返回结果（SearchResponse）
        // 创建父查询对象
        BoolQueryBuilder srBuilder = QueryBuilders.boolQuery();
        // 创建子查询对象
        QueryBuilder keywordBuilder = null;
        // 创建查询函数构造对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        System.out.println("打印提交的DSL语句：sourceBuilder--：" + sourceBuilder);
        // 参数注入
        if (!"".equals(keyword)){
            keywordBuilder = QueryBuilders.matchQuery("name",keyword);// 根据字段匹配查询，keyword会被分词器分词
            //keywordBuilder = QueryBuilders.matchPhraseQuery("name",keyword);// 根据字段匹配查询，keyword不会被分词器分词
            srBuilder.must(keywordBuilder);//子查询对象放入父查询对象中
        }

        sourceBuilder.query(srBuilder); // 把父查询对象放入函数构造对象中
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));// 设置超时时间
        sourceBuilder.trackTotalHits(true); // 取消默认最大查询数量上限（默认10000）

        // 构造 请求发起对象
        SearchRequest searchRequest = new SearchRequest("test_index");// 这里直接配置索引名即可
        // searchRequest.indices("phone");
        searchRequest.source(sourceBuilder);// 把查询函数构造对象注入查询请求中
        SearchResponse searchResponse;// 创建响应对象

        SearchHits searchHits = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            searchHits =  searchResponse.getHits();//获取响应中的列表数据
            Long total = searchHits.getTotalHits().value;//获取响应中的列表数据总数

            for(SearchHit hit:searchHits.getHits()){// 遍历构造返回JSON，以下不再多说
                JSONObject dataJSON = new JSONObject();
                String tempRes = hit.getSourceAsString();
                dataJSON = JSONObject.parseObject(tempRes);
                jsonArr.add(dataJSON);
            }

            resJSON.put("resArr", jsonArr);
            resJSON.put("total", total);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resJSON;
    }

    public static void main(String[] args){
        ElaticSearchDemo demo = new ElaticSearchDemo();
        JSONObject result = demo.query("欧阳");
        System.out.println("查询结果：");
        System.out.println(result);
    }
}
