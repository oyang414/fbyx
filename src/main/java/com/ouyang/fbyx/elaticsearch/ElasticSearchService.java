package com.ouyang.fbyx.elaticsearch;

import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.List;

/**
 * @Author ouyangxingjie
 * @Description ElasticSearch 服务接口
 * @Date 11:35 2022/2/23
 */
public interface ElasticSearchService {

    /**
     * @Author ouyangxingjie
     * @Description 创建索引
     * @Date 14:12 2022/2/23
     * @param indexName 索引名称
     * @param settings 索引settings
     * @param mappings 索引mappings
     * @return org.elasticsearch.client.indices.CreateIndexResponse
     */
    CreateIndexResponse createIndex(String indexName, Settings settings, XContentBuilder mappings) throws IOException;

    /**
     * @Author ouyangxingjie
     * @Description 根据索引名称删除索引
     * @Date 14:14 2022/2/23
     * @param indexName 索引名称
     * @return org.elasticsearch.action.support.master.AcknowledgedResponse
     */
    AcknowledgedResponse deleteIndex(String indexName) throws IOException;

    /**
     * @Author ouyangxingjie
     * @Description 新增文档
     * @Date 14:53 2022/2/23
     * @param request 新增文档请求
     * @return org.elasticsearch.action.index.IndexResponse
     */
    IndexResponse indexDocument(IndexRequest request) throws IOException;

    /**
     * @Author ouyangxingjie
     * @Description 删除文档
     * @Date 14:53 2022/2/23
     * @param request 删除文档请求
     * @return org.elasticsearch.action.delete.DeleteResponse
     */
    DeleteResponse deleteDocument(DeleteRequest request) throws IOException;

    /**
     * @Author ouyangxingjie
     * @Description 更新文档
     * @Date 14:53 2022/2/23
     * @param request 更新文档请求
     * @param insert 如果为true，那么如果文档不存在则执行插入操作
     * @return org.elasticsearch.action.update.UpdateResponse
     */
    UpdateResponse updateDocument(UpdateRequest request,boolean insert) throws IOException;

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
    BulkResponse bulkDocument(List<DocWriteRequest<?>> requests) throws IOException;


    /**
     * @Author ouyangxingjie
     * @Description 搜索文档
     * @Date 15:07 2022/2/23
     * @param searchRequest 查询请求
     * @return com.ouyang.fbyx.elaticsearch.SearchResultEntity
     */
    SearchResultEntity searchDocument(SearchRequest searchRequest);

    /**
     * @Author ouyangxingjie
     * @Description 查询文档详情
     * @Date 15:26 2022/2/23
     * @param request 查询请求
     * @return org.elasticsearch.action.get.GetResponse
     */
    GetResponse getDocument(GetRequest request) throws IOException;
}
