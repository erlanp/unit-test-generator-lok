package com.areyoo.lok.controller;

import com.areyoo.lok.service.api.WwService;
import com.areyoo.lok.vo.ITestTwoVo;
import com.areyoo.lok.vo.TestTwoVo;
import com.areyoo.lok.vo.TestVo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.Assert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WwControllerTest {
    @InjectMocks
    private WwController wwController;

    @Mock
    private WwService wwService;

    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * thisIs2
     *
     * @throws Exception
     */
    @Test
    public void thisIs2Test() throws Exception {
        List result = wwController.thisIs2();
        Assert.assertTrue(result != null && result.toString().indexOf("[") == 0);
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
        BigDecimal m = BigDecimal.ONE;
        String result = wwController.testMoot(vo, vo2, str, list, m);
        vo.put("1", getTestVo());
        vo2.put("1", new ArrayList<>(10));
        wwController.testMoot(vo, vo2, str, list, m);
        Assert.assertTrue(result != null);
    }

    /**
     * index
     *
     * @throws Exception
     */
    @Test
    public void indexTest() throws Exception {
        String mess = "1";
        String result = wwController.index(mess);

        String then2 = "1";
        when(wwService.index(nullable(String.class))).thenReturn(then2);
        wwController.index(mess);
        Assert.assertTrue(result != null);
    }

    /**
     * tttt
     *
     * @throws Exception
     */
    @Test
    public void ttttTest() throws Exception {
        UUID uuid = getUUID();
        Map<String, String> map = new HashMap<>(16);
        String result = wwController.tttt(uuid, map);
        map.put("1", "1");
        wwController.tttt(uuid, map);
        Assert.assertTrue(result != null);
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

        Map<String, List<String>> then0 = new HashMap<>(16);
        then0.put("1", new ArrayList<>(10));
        when(wwService.indexMap2(nullable(String.class))).thenReturn(then0);

        Map<String, TestVo> then5 = new HashMap<>(16);
        then5.put("1", getTestVo());
        when(wwService.indexMap(nullable(String.class))).thenReturn(then5);
        wwController.test(vo);
        Assert.assertTrue(result != null);
    }

    /**
     * thisIs
     *
     * @throws Exception
     */
    @Test
    public void thisIsTest() throws Exception {
        ITestTwoVo vo = getITestTwoVo();
        String error = null;
        wwController.thisIs(vo);

        doAnswer((InvocationOnMock invocation) -> {
            return null;
        }).when(wwService).index2(nullable(String.class));

        doAnswer((InvocationOnMock invocation) -> {
            Map<String, Object> tmpMap = new HashMap<>(1);
            tmpMap.put("1", invocation.getArgument(0));
            return tmpMap;
        }).when(wwService).indexTo3(any());
        wwController.thisIs(vo);
        try {
        } catch (Exception exp) {
            error = exp.getMessage();
        }
        Assert.assertTrue(error == null);
    }

    /**
     * thisIs
     *
     * @throws Exception
     */
    @Test
    public void thisIsTwoTest() throws Exception {
        String error = null;
        wwController.thisIs();

        doAnswer((InvocationOnMock invocation) -> {
            return null;
        }).when(wwService).index2(nullable(String.class));

        wwController.thisIs();
        try {
        } catch (Exception exp) {
            error = exp.getMessage();
        }
        Assert.assertTrue(error == null);
    }

    private TestVo getTestVo() {
        TestVo vo = new TestVo();
        vo.setS2((short)0);
        vo.setC2('1');
        vo.setName("1");
        vo.setI(1);
        vo.setL(1L);
        vo.setId(1L);
        vo.setUuid(getUUID());
        vo.setB((byte)1);
        vo.setB2((byte)1);
        vo.setD(1.0D);
        vo.setCost(BigDecimal.ONE);
        vo.setF2(1.0F);
        vo.setSet(new HashSet<>(16));
        vo.setF(1.0F);
        vo.setDate(new Date());
        vo.setList(new ArrayList<>(10));
        vo.setD2(1.0D);
        vo.setS((short)0);
        vo.setTestTwoVo(new TestTwoVo());
        return vo;
    }

    private ITestTwoVo getITestTwoVo() {
        ITestTwoVo vo = mock(ITestTwoVo.class);
        when(vo.getD()).thenReturn(1.0D);
        when(vo.getF()).thenReturn(1.0F);
        when(vo.getCount()).thenReturn(1);
        return vo;
    }

    private UUID getUUID() {
        return UUID.randomUUID();
    }
}