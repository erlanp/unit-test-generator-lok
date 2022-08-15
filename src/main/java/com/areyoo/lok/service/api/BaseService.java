package com.areyoo.lok.service.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * indexTo3 用于测试
     *
     * @param mess
     * @return
     */
    default Map<String, K> indexTo3(K mess) {
        Map<String, K> map = new HashMap<>(1);
        map.put("1", mess);
        return map;
    }
}
