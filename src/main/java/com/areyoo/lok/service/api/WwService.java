package com.areyoo.lok.service.api;

import com.areyoo.lok.vo.TestVo;

import java.util.List;

import java.util.Map;

/**
 * @author xusong
 */
public interface WwService extends BaseService<String> {
    /**
     * index 用于测试
     *
     * @param mess
     * @return
     */
    String index(String mess);

    /**
     * indexList 用于测试
     *
     * @param mess
     * @return
     */
    List<String> indexList(String mess);

    /**
     * indexList2 用于测试
     *
     * @param mess
     * @return
     */
    List<TestVo> indexList2(String mess);

    /**
     * indexMap 用于测试
     *
     * @param mess
     * @return
     */
    Map<String, TestVo> indexMap(String mess);

    /**
     * indexMap2 用于测试
     *
     * @param mess
     * @return
     */
    Map<String, List<String>> indexMap2(String mess);

    /**
     * index2 用于测试
     *
     * @param mess
     */
    void index2(String mess);
}
