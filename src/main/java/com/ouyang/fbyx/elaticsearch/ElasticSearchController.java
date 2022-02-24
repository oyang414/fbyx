package com.ouyang.fbyx.elaticsearch;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.MatrixStatsAggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.matrix.stats.MatrixStatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.AvgBucketPipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.CumulativeSumPipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.PercentilesBucketPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private ElasticSearchService elasticSearchService;

    @PostMapping("/create_index")
    public void createIndex(String index) throws IOException {
        Settings settings = Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 0).build();

        XContentBuilder mappings = JsonXContent.contentBuilder()
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

        elasticSearchService.createIndex(index,settings,mappings);
    }


    @PostMapping("/del_index")
    public void deleteIndex(String index) throws IOException {
        elasticSearchService.deleteIndex(index);
    }



    @PostMapping("/bulk")
    public String bulk() throws IOException {
        List<DocWriteRequest<?>> requests = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        //新增三条数据
        users.add(new User("11001","小白",29));
        users.add(new User("11002","小蓝",30));
        users.add(new User("11003","小红",31));
        for (int i = 0; i < users.size(); i++) {
            requests.add(new IndexRequest("test_stud")
                    // 不指定ID的话，新增时ID是随机的
                    .id(users.get(i).getId().toString())
                    .source(JSON.toJSONString(users.get(i)),XContentType.JSON));
        }
        //更新一条数据将小红更新
        requests.add(new UpdateRequest("test_stud","11003")
                .doc(JSON.toJSONString(new User("11003","小红红",31)),XContentType.JSON));
        //删除一条数据将小白删除
        requests.add(new DeleteRequest("test_stud","11001").id("11001"));
        elasticSearchService.bulkDocument(requests);
        return "bulk OK";
    }


    @PostMapping("/del")
    public String del(String id) throws IOException {
        elasticSearchService.deleteDocument(new DeleteRequest("test_stud").id(id));
        return "del OK";
    }

    @PostMapping("/update")
    public String update(String id) throws IOException {
        User user = new User("22001","小花",30);
        elasticSearchService.updateDocument(new UpdateRequest("test_stud",id)
                .doc(JSON.toJSONString(user),XContentType.JSON),true);
        return "update OK";
    }

    @PostMapping("/detail")
    public String detail(String id) throws IOException {
        GetRequest request = new GetRequest("test_stud", id);
        GetResponse getResponse = elasticSearchService.getDocument(request);
        System.out.println(getResponse.getSourceAsString());
        return "update OK";
    }


    @PostMapping("/search")
    public SearchResultEntity search(String keyword, int from, int size) throws IOException {
        // 条件构造器
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 查询构建器 QueryBuilders工具类可以快速构建
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        MatchQueryBuilder nameQueryBuilder = QueryBuilders.matchQuery("name", keyword);
        MatchQueryBuilder heroNameQueryBuilder = QueryBuilders.matchQuery("hero_name",keyword);
        boolQueryBuilder.should(nameQueryBuilder);
        boolQueryBuilder.should(heroNameQueryBuilder);
        //复合查询
       /* MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders
                .multiMatchQuery(keyword, "name", "hero_name");
        builder.query(multiMatchQueryBuilder);*/
        builder.query(boolQueryBuilder);
        builder.timeout(TimeValue.timeValueSeconds(10));
        // 分页查询
        builder.from(from);
        builder.size(size);
        //按照年龄分组统计个数
        TermsAggregationBuilder ageAggregationBuilder = AggregationBuilders.terms("age_group").field("age")
                //没有这个字段或者这个字段没有值的，按照0计算
                .missing(0);
        //按照性别分组统计个数，由于性别类型是text，进行了分词，所以只能用他的字段映射(sex.keyword)进行分组/排序
        TermsAggregationBuilder sexAggregationBuilder = AggregationBuilders.terms("sex_group").field("sex.keyword")
                .subAggregation(AggregationBuilders.sum("sum_age").field("age"));
        //嵌套分组
        ageAggregationBuilder.subAggregation(sexAggregationBuilder);
        //直方图聚合，年龄按照一岁一个梯度进行统计，按照年龄高低倒序排列
        HistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders
                .histogram("age_histogram")
                .field("age")
                .interval(1)
                .order(BucketOrder.key(false))
                .minDocCount(0);
        //管道聚合，将聚合后的结果再次进行聚合计算：根据性别分组,计算sex_group组中年龄的平均值（sex_group年龄总数除以性别的个数）
        AvgBucketPipelineAggregationBuilder pipelineAggregationBuilder = new AvgBucketPipelineAggregationBuilder("avg_pipeline_agg","sex_group>sum_age");
        //矩阵聚合
        MatrixStatsAggregationBuilder matrixStatsAggregationBuilder = new MatrixStatsAggregationBuilder("matrix_stats_agg");
        //根据年龄和身高两个纬度去统计
        matrixStatsAggregationBuilder.fields(Arrays.asList("age","height"));

        //查询指定的test_index和test_stud索引
        SearchRequest searchRequest = new SearchRequest( "test_index","test_stud");
        //将聚合添加到构造器中
        builder.aggregation(ageAggregationBuilder);
        builder.aggregation(sexAggregationBuilder);
        builder.aggregation(histogramAggregationBuilder);
        builder.aggregation(pipelineAggregationBuilder);
        builder.aggregation(matrixStatsAggregationBuilder);

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //设置前缀
        highlightBuilder.preTags("<strong>");
        //设置后缀
        highlightBuilder.postTags("</strong>");
        //设置高亮字段，可设置多个字段
        highlightBuilder.field("name").field("hero_name");
        //设置高亮信息
        builder.highlighter(highlightBuilder);
        searchRequest.source(builder);
        return elasticSearchService.searchDocument(searchRequest);
    }

}
