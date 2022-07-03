package com.areyoo.lok.service.api;

/**
 * @author xusong
 */
public interface BaseService<T> {
    /**
     * indexTo 用于测试
     *
     * @param mess
     * @return
     */
    default T indexTo(T mess) {
        return mess;
    }
}
