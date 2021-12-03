package com.areyoo.lok.service.impl;

import com.areyoo.lok.service.api.WwService;
import com.areyoo.lok.vo.TestVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xusong
 */
@Service
public class WwServiceImpl implements WwService {
    @Override
    public String index(String mess) {
        return "Hello Spring Boot 2.2.6!" + mess;
    }

    @Override
    public List<String> indexList(String mess) {
        List<String> list = new ArrayList<>();
        list.add("Hello Spring Boot 2.2.6!" + mess);
        return list;
    }

    @Override
    public List<TestVo> indexList2(String mess) {
        List<TestVo> list = new ArrayList<>(1);
        list.add(new TestVo());
        return list;
    }

    @Override
    public void index2(String mess) {
        String hey = "Hello Spring Boot 2.2.6!" + mess;
    }

    @Override
    public Map<String, TestVo> indexMap(String mess) {
        Map<String, TestVo> map = new HashMap<>(16);
        return map;
    }

    @Override
    public Map<String, List<String>> indexMap2(String mess) {
        Map<String, List<String>> map = new HashMap<>(16);
        return map;
    }

    private void test() {

    }
}
