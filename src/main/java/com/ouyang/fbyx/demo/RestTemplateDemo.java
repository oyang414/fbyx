package com.ouyang.fbyx.demo;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: restTemplate使用Demo
 * @author: ouyangxingjie
 * @create: 2021/7/14 23:42
 **/
public class RestTemplateDemo {
    public static void main(String [] args){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://139.198.183.194:8012/fbyx/rosters?page_num={pageNum}&page_size={pageSize}&name={name}";
        Map<String,String> params = new HashMap<>();
        params.put("name","燃烧的妞妞");
        params.put("pageNum","1");
        params.put("pageSize","5");
        // restTemplate getForObject中 execute方法的 URI expanded = getUriTemplateHandler().expand(url, uriVariables);
        // 通过 expandInternal 构建URI时，会将url进行encode
       /* String response = restTemplate.getForObject(url,String.class,params);
        System.out.println(response);*/
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("http://139.198.183.194:8012/fbyx/rosters")
                .queryParam("page_num", "1")
                .queryParam("page_size", "5")
                .queryParam("name","燃烧的妞妞");
        //如果url已经进行过encode 或者 url请求参数中，有的参数不需要encode，那么可以使用这种方法
        //用 UriComponentsBuilder我们自己构建的URI 然后请求getForObject接受URI的方法
        /*System.out.println(builder.build().toUriString());
        String resp = restTemplate.getForObject(builder.build().toUri(),String.class);
        System.out.println(resp);*/

        // build(encoded)中 encoded参数 为true，则认为用户传进的url是合法的，被encode过，不需要encode，并且build方法中会进行各种校验，
        // 如果该路径还有=或&或者者中文之类的符号则build()会报错，认为这不是一个合法的url，如果校验通过，那么该方法生成的url路径则不会进行encode
        // encoded参数 为false 那么说明该url 还没有被encode过，则该方法生成的uri路径会进行encode(除了&和=都会被转义)，那么当调用encode()方法 则会将=和&也转义
        // 请看 testBuilder这个例子  参数test的值 执行情况
        UriComponentsBuilder testBuilder = UriComponentsBuilder
                .fromHttpUrl("http://www.baidu.com")
                .queryParam("age","12")
                .queryParam("test","=@%80&");
        System.out.println(testBuilder.build(false).toUri());//只转义了%
        System.out.println(testBuilder.build(false).encode().toUri());//不仅转义了%，&和=也被转义了
        System.out.println(testBuilder.build(true).toUri());//因为有=和&会报错

        /*MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("name", "燃烧的妞妞");
        //HttpEntity
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody,  new HttpHeaders());
        HttpEntity<String> resp = restTemplate.exchange( "http://139.198.183.194:8012/fbyx/rosters?name==",  HttpMethod.GET,  requestEntity,  String.class);
        System.out.println(resp);*/
    }
}
