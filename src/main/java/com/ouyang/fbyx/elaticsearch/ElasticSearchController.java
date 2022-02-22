package com.ouyang.fbyx.elaticsearch;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author ouyangxingjie
 * @Description
 * @Date 13:49 2022/2/22
 */
@RestController
@RequestMapping(value = "/es",produces="application/json")
@Slf4j
public class ElasticSearchController {

    @Autowired
    private RestHighLevelClient client;

    @PostMapping("/create_index")
    public void createIndex(String index) throws IOException {
        CreateIndexRequest request = buildCreateIndexRequest(index);
        log.info("source:" + request);
        if (!existsIndex(index)) {
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            System.out.println(response.toString());
            log.info("索引创建结查：" + response.isAcknowledged());
        } else {
            log.warn("索引：{}，已经存在，不能再创建。", index);
        }
    }


    @PostMapping("/del_index")
    public void deleteIndex(String index) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        if (client.indices().exists(getIndexRequest,RequestOptions.DEFAULT)) {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
            log.info("source:" + deleteIndexRequest);
            client.indices().delete(deleteIndexRequest,RequestOptions.DEFAULT);
        }
    }
    private CreateIndexRequest buildCreateIndexRequest(String index) throws IOException{
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        buildSetting(createIndexRequest);
        buildIndexMapping(createIndexRequest);
        return createIndexRequest;
    }

    private void buildSetting(CreateIndexRequest request) {
        request.settings(Settings.builder().put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 0));
    }
    private void  buildIndexMapping(CreateIndexRequest request) throws IOException {
        XContentBuilder mappingBuilder = JsonXContent.contentBuilder()
                .startObject()
                .startObject("properties")
                .startObject("mobile")
                .field("type", "keyword")
                .field("index", "true")
                .endObject()

                .startObject("createDate")
                .field("type", "date")
                .field("index", "true")
                .endObject()

                .startObject("sendDate")
                .field("type", "date")
                .field("index", "true")
                .endObject()

                .startObject("longCode")
                .field("type", "keyword")
                .field("index", "true")
                .endObject()

                .startObject("corpName")
                .field("type", "keyword")
                .field("index", "true")
                .endObject()

                .startObject("smsContent")
                .field("type", "text")
                .field("index", "true")
                .field("analyzer", "ik_max_word")
                .endObject()

                .startObject("state")
                .field("type", "integer")
                .field("index", "true")
                .endObject()

                .startObject("province")
                .field("type", "keyword")
                .field("index", "true")
                .endObject()

                .startObject("operatorId")
                .field("type", "integer")
                .field("index", "true")
                .endObject()

                .startObject("ipAddr")
                .field("type", "ip")
                .field("index", "true")
                .endObject()

                .startObject("replyTotal")
                .field("type", "integer")
                .field("index", "true")
                .endObject()

                .startObject("fee")
                .field("type", "integer")
                .field("index", "true")
                .endObject()
                .endObject()
                .endObject();
        request.mapping(mappingBuilder);
    }
    /**
     * 判断索引是否存在
     * @param index
     * @return
     * @throws IOException
     */
    public boolean existsIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        log.info("source:" + request);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        log.debug("existsIndex: " + exists);
        return exists;
    }



    @PostMapping("/bulk")
    public String bulk() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(10));
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("1002","小白",29));
        users.add(new User("1002","小蓝",30));
        users.add(new User("1002","小红",31));
        for (int i = 0; i < users.size(); i++) {
            bulkRequest.add(new IndexRequest("test_stud")
                    // 不指定ID的话，新增时ID是随机的
                    //.id(items.get(i).getId().toString())
                    .source(JSON.toJSONString(users.get(i)),XContentType.JSON));
            // bulkRequest.add(UpdateRequest)   批量更新
            // bulkRequest.add(DeleteRequest)   批量删除
        }
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return "bulk insert OK";
    }


    @PostMapping("/del")
    public String del(String id) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        DeleteRequest deleteRequest = new DeleteRequest("test_stud")
                .id(id);
        bulkRequest.add(deleteRequest);
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return "del OK";
    }

    @PostMapping("/update")
    public String update(String id) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        User user = new User("2001","小花",30);
        UpdateRequest updateRequest = new UpdateRequest("test_stud",id)
                .doc(JSON.toJSONString(user),XContentType.JSON);
        bulkRequest.add(updateRequest);
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return "update OK";
    }

    @PostMapping("/detail")
    public String detail(String id) throws IOException {
        GetRequest request = new GetRequest("test_stud", id);
        GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());
        return "update OK";
    }


    @PostMapping("/search")
    public String search(String name, int from, int size) throws IOException {
        // 条件构造器
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 查询构建器 QueryBuilders工具类可以快速构建
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", name);
        builder.query(matchQueryBuilder);
        builder.timeout(TimeValue.timeValueSeconds(10));
        /** 分页查询 */
        builder.from(from);
        builder.size(size);

        SearchRequest searchRequest = new SearchRequest("test_index");
        searchRequest.source(builder);

        SearchResponse result = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit temp : result.getHits().getHits()) {
            System.out.println(temp.getSourceAsMap());
        }
        return "query OK";
    }


    @PostMapping("/age_range")
    public String query() throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .filter(new RangeQueryBuilder("age").gte(20));
        sourceBuilder.query(queryBuilder);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);

        SearchResponse result = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit temp : result.getHits().getHits()) {
            System.out.println(temp.getSourceAsMap());
        }
        return "query OK";
    }

}
