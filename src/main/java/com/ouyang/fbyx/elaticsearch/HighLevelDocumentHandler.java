package com.ouyang.fbyx.elaticsearch;

import java.io.IOException;

/**
 * @Author ouyangxingjie
 * @Description
 * @Date 13:41 2022/2/22
 */
public interface HighLevelDocumentHandler<T, ID> {
     void save(T t) throws Exception;
     String detail(ID id) throws IOException;
     void update(T t) throws Exception;
     void delete(ID id) throws IOException;
}