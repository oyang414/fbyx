package com.ouyang.fbyx.elaticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ouyangxingjie
 * @Description
 * @Date 13:39 2022/2/22
 */
@Configuration
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("139.198.183.194", 9200, "http")
                        // 如果是集群，可以构建多个
                        /*,new HttpHost("localhost", 9201, "http")*/
                )
        );

        return restHighLevelClient;
    }
}
