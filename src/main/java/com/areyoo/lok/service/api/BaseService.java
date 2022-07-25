package com.areyoo.lok.service.api;

import java.util.Arrays;
import java.util.List;

/**
 * @author xusong
 */
public interface BaseService<T, K> {
    /**
     * indexTo 用于测试
     *
     * @param mess
     * @return
     */
    default T indexTo(T mess) {
        return mess;
    }

    /**
     * indexTo2 用于测试
     *
     * @param mess
     * @return
     */
    default List<K> indexTo2(K mess) {
        return Arrays.asList(mess);
    }
}
