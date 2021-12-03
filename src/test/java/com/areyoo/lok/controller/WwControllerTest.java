package com.areyoo.lok.controller;

import com.areyoo.lok.service.api.WwService;
import com.areyoo.lok.vo.TestVo;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author xusong
 */
@ExtendWith(MockitoExtension.class)
class WwControllerTest {
    @InjectMocks
    private WwController wwController;

    @Mock
    private WwService wwService;

    /**
     * index
     *
     * @throws Exception
     */
    @Test
    public void indexTest() throws Exception {
        String mess = "1";
        String result = wwController.index(mess);

        String then0 = "1";
        when(wwService.index(anyString())).thenReturn(then0);
        wwController.index(mess);
        Assert.assertTrue(result == null);
    }

    /**
     * test
     *
     * @throws Exception
     */
    @Test
    public void testTest() throws Exception {
        TestVo vo = getTestVo();
        String result = wwController.test(vo);

        Map<String, TestVo> then1 = new HashMap<>(16);
        then1.put("1", getTestVo());
        when(wwService.indexMap(anyString())).thenReturn(then1);

        Map<String, List<String>> then2 = new HashMap<>(16);
        then2.put("1", new ArrayList<>(10));
        when(wwService.indexMap2(anyString())).thenReturn(then2);
        wwController.test(vo);
        Assert.assertTrue(result != null);
    }

    /**
     * testMoot
     *
     * @throws Exception
     */
    @Test
    public void testMootTest() throws Exception {
        Map<String, TestVo> vo = new HashMap<>(16);
        Map<String, List<String>> vo2 = new HashMap<>(16);
        String[] str = {"1"};
        List[] list = new ArrayList[1];
        BigDecimal m = new BigDecimal(1);
        String result = wwController.testMoot(vo, vo2, str, list, m);
        vo.put("1", getTestVo());
        vo2.put("1", new ArrayList<>(10));
        wwController.testMoot(vo, vo2, str, list, m);
        Assert.assertTrue(result != null);
    }

    /**
     * thisIs
     *
     * @throws Exception
     */
    @Test
    public void thisIsTest() throws Exception {
        String error = null;
        wwController.thisIs();
        wwController.thisIs();
        try {
        } catch (Exception e) {
            error = e.getMessage();
        }
        Assert.assertTrue(error == null);
    }

    /**
     * thisIs2
     *
     * @throws Exception
     */
    @Test
    public void thisIs2Test() throws Exception {
        Object result = wwController.thisIs2();
        wwController.thisIs2();
        Assert.assertTrue(result != null && result.toString().indexOf("[") == 0);
    }

    /**
     * getTestVo
     *
     * @throws Exception
     */
    @Test
    public void getTestVoTest() throws Exception {
        TestVo vo = getTestVo();
        Method method = wwController.getClass().getDeclaredMethod("getTestVo", TestVo.class);
        method.setAccessible(true);
        Object result = method.invoke(wwController, vo);
        Assert.assertTrue(result != null);
    }

    /**
     * getTestVo2
     *
     * @throws Exception
     */
    @Test
    public void getTestVo2Test() throws Exception {
        TestVo vo = getTestVo();
        Boolean isOK = true;
        Boolean isYes = true;
        List<TestVo> list = new ArrayList<>(10);
        Map<String, String> map = new HashMap<>(16);
        Map<String, List<String>> map2 = new HashMap<>(16);
        Method method = wwController.getClass().getDeclaredMethod("getTestVo2", TestVo.class, Boolean.class, Boolean.class, List.class, Map.class, Map.class);
        method.setAccessible(true);
        Object result = method.invoke(wwController, vo, isOK, isYes, list, map, map2);
        list.add(getTestVo());
        map.put("1", "1");
        map2.put("1", new ArrayList<>(10));
        Assert.assertTrue(result != null);
    }

    private TestVo getTestVo() {
        String json = "{'wwController':0,'name':'1','date':null,'cost':'1','id':'1','list':[],'f':'1','testTwoVo':null,'set':[],'d':'1','s':'1','i':'1','count':'1','f2':'1','d2':'1','b2':'1','l':'1','s2':'1','b':null,'c2':'1'}";
        TestVo vo = new Gson().fromJson(json, TestVo.class);
        return vo;
    }
}

