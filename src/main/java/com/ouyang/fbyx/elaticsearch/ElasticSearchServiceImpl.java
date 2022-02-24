package com.ouyang.fbyx.elaticsearch;

import com.alibaba.fastjson.JSON;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.matrix.stats.ParsedMatrixStats;
import org.elasticsearch.search.aggregations.pipeline.ParsedSimpleValue;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @Author ouyangxingjie
 * @Description
 * @Date 13:59 2022/2/23
 */
@Service
@Slf4j
public class ElasticSearchServiceImpl implements ElasticSearchService{


    @Autowired
    private RestHighLevelClient client;

    /**
     * @Author ouyangxingjie
     * @Description
     * @Date 14:12 2022/2/23
     * @param indexName 索引名称
     * @param settings 索引settings
     * @param mappings 索引mappings
     * @return org.elasticsearch.client.indices.CreateIndexResponse
     */
    @Override
    public CreateIndexResponse createIndex(String indexName, Settings settings, XContentBuilder mappings) throws IOException {
        CreateIndexRequest request = buildCreateIndexRequest(indexName,settings,mappings);
        log.info("source:" + request);
        if (!existsIndex(indexName)) {
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            log.info("索引创建结查：" + response.isAcknowledged());
            return response;
        } else {
            log.warn("索引：{}，已经存在，不能再创建。", indexName);
        }
        return null;
    }

    /**
     * @Author ouyangxingjie
     * @Description 构造创建索引请求
     * @Date 14:08 2022/2/23
     * @param indexName 索引名称
     * @param settings 索引settings
     * @param mappings 索引mappings
     * @return org.elasticsearch.client.indices.CreateIndexRequest
     */
    private CreateIndexRequest buildCreateIndexRequest(String indexName, Settings settings, XContentBuilder mappings) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        createIndexRequest.settings(settings);
        createIndexRequest.mapping(mappings);
        return createIndexRequest;
    }

    /**
     * @Author ouyangxingjie
     * @Description 校验索引是否存在
     * @Date 14:09 2022/2/23
     * @param indexName 索引名称
     * @return boolean ture-存在，false-不存在
     */
    public boolean existsIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        log.info("source:" + request);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        log.debug("existsIndex: " + exists);
        return exists;
    }

    /**
     * @Author ouyangxingjie
     * @Description 根据索引名称删除索引
     * @Date 14:14 2022/2/23
     * @param indexName 索引名称
     * @return org.elasticsearch.action.support.master.AcknowledgedResponse
     */
    @Override
    public AcknowledgedResponse deleteIndex(String indexName) throws IOException{
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        if (client.indices().exists(getIndexRequest,RequestOptions.DEFAULT)) {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
            log.info("source:" + deleteIndexRequest);
            return client.indices().delete(deleteIndexRequest,RequestOptions.DEFAULT);
        }
        return null;
    }

    /**
     * @Author ouyangxingjie
     * @Description 新增文档
     * @Date 14:53 2022/2/23
     * @param request 新增文档请求
     * @return org.elasticsearch.action.index.IndexResponse
     */
    @Override
    public IndexResponse indexDocument(IndexRequest request) throws IOException {
        return client.index(request,RequestOptions.DEFAULT);
    }

    /**
     * @Author ouyangxingjie
     * @Description 删除文档
     * @Date 14:53 2022/2/23
     * @param request 删除文档请求
     * @return org.elasticsearch.action.delete.DeleteResponse
     */
    @Override
    public DeleteResponse deleteDocument(DeleteRequest request) throws IOException {
        return client.delete(request,RequestOptions.DEFAULT);
    }

    /**
     * @Author ouyangxingjie
     * @Description 更新文档
     * @Date 14:53 2022/2/23
     * @param request 更新文档请求
     * @param insert 如果为true，那么如果文档不存在则执行插入操作
     * @return org.elasticsearch.action.update.UpdateResponse
     */
    @Override
    public UpdateResponse updateDocument(UpdateRequest request,boolean insert) throws IOException {
        if(insert) {
            request.upsert(request.doc());
        }
        return client.update(request,RequestOptions.DEFAULT);
    }

    /**
     * @Author ouyangxingjie
     * @Description 批量处理文档（增删改都可以）
     * @Date 14:33 2022/2/23
     * @param requests 请求集合
     * ->IndexRequest 新增
     * ->UpdateRequest 更新
     * ->DeleteRequest 删除
     * @return org.elasticsearch.action.bulk.BulkResponse
     */
    @Override
    public BulkResponse bulkDocument(List<DocWriteRequest<?>> requests) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (DocWriteRequest<?> writeRequest : requests) {
            bulkRequest.add(writeRequest);
        }
        return client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    /**
     * @Author ouyangxingjie
     * @Description 搜索文档
     * @Date 15:07 2022/2/23
     * @param searchRequest 查询请求
     * @return com.ouyang.fbyx.elaticsearch.SearchResultEntity
     */
    @Override
    public SearchResultEntity searchDocument(SearchRequest searchRequest) {
        List<Map<String, Object>> list = new ArrayList<>();
        SearchResponse searchResponse;
        try {
            log.info("查询DSL：{}",searchRequest.source());
            //执行搜索
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            log.info("返回结果：{}",searchResponse);
            //获取搜索的结果集
            SearchHit[] searchHit = searchResponse.getHits().getHits();
            //结果总数
            long totalHits = searchResponse.getHits().getTotalHits().value;
            //搜索耗时
            long took = searchResponse.getTook().getMillis();
            for (SearchHit document : searchHit) {
                Map<String, Object> item = document.getSourceAsMap();
                if (item == null) {
                    continue;
                }
                //获取需要设置的高亮字段，遍历结果将关键字设置高亮
                Map<String, HighlightField> highlightFields = document.getHighlightFields();
                if (!highlightFields.isEmpty()) {
                    for (String key : highlightFields.keySet()) {
                        Text[] fragments = highlightFields.get(key).fragments();
                        if (item.containsKey(key)) {
                            item.put(key, fragments[0].string());
                        }
                        String[] fieldArray = key.split("[.]");
                        if (fieldArray.length > 1) {
                            item.put(fieldArray[0], fragments[0].string());
                        }
                    }
                }
                list.add(item);
            }
            //构造聚合结果，目前只能做分组统计
            Map<String, Map<String, Object>> aggregations = getAggregation(searchResponse.getAggregations());
            return new SearchResultEntity(totalHits, list, took, aggregations);
        } catch (Exception e) {
            log.error("ES检索出错:{}",e);
        }
        return new SearchResultEntity();
    }

    /**
     * @Author ouyangxingjie
     * @Description 查询文档详情
     * @Date 15:26 2022/2/23
     * @param request 查询请求
     * @return org.elasticsearch.action.get.GetResponse
     */
    @Override
    public GetResponse getDocument(GetRequest request) throws IOException{
        return client.get(request, RequestOptions.DEFAULT);
    }

    /**
     * @Author ouyangxingjie
     * @Description 构造聚合数据
     * @Date 15:09 2022/2/23
     * @param aggregations 聚合参数
     * @return java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.lang.Long>>
     */
    private Map<String, Map<String, Object>> getAggregation(Aggregations aggregations) {
        if (aggregations == null) {
            return Collections.EMPTY_MAP;
        }
        Map<String, Map<String, Object>> result = new HashMap<>();
        Map<String, Aggregation> aggregationMap = aggregations.getAsMap();
        aggregationMap.forEach((k, v) -> {
            Map<String, Object> agg = new HashMap<>();
            if(v instanceof ParsedHistogram){
                //桶聚合（直方图）返回结果解析
                List<? extends Histogram.Bucket> buckets = ((ParsedHistogram) v).getBuckets();
                for (Histogram.Bucket bucket : buckets) {
                    agg.put(bucket.getKeyAsString(), bucket.getDocCount());
                }
            }else if(v instanceof ParsedSimpleValue){
                //管道聚合返回结果解析
                ParsedSimpleValue parsedSimpleValue = (ParsedSimpleValue)v;
                agg.put(parsedSimpleValue.getName(),parsedSimpleValue.value());
            }else if(v instanceof ParsedMatrixStats) {
                //矩阵聚合返回结果解析,parsedMatrixStats数据结构太恶心了，懒得解析了
                ParsedMatrixStats parsedMatrixStats = (ParsedMatrixStats)v;
            }else{
                //指标聚合和部分桶聚合返回结果解析
                List<? extends Terms.Bucket> buckets = ((ParsedTerms) v).getBuckets();
                for (Terms.Bucket bucket : buckets) {
                    agg.put(bucket.getKeyAsString(), bucket.getDocCount());
                }
            }
            result.put(k, agg);
        });
        return result;
    }

}
