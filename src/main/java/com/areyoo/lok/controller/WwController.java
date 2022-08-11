package com.areyoo.lok.controller;

import com.areyoo.lok.service.api.WwService;
import com.areyoo.lok.vo.ITestTwoVo;
import com.areyoo.lok.vo.TestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author xusong
 */
@RestController
public class WwController extends BaseController {
    @Autowired
    private WwService wwService;

    private int a;

    public void thisIs() {
        wwService.index2("123");
    }

    public void thisIs(ITestTwoVo vo) {
        wwService.index2(vo.toString());
        wwService.indexTo(new ArrayList<>());
        wwService.indexTo2(new TestVo());
    }

    public List<String> thisIs2() {
        return Arrays.asList("1", "2");
    }

    public String testMoot(Map<String, TestVo> vo, Map<String, List<String>> vo2, String[] str, List<String>[] list, BigDecimal m)
            throws ApplicationContextException {
        return vo.toString();
    }

	@RequestMapping("/")
    public String index(String mess) {
        if (mess == null) {
            mess = "";
        }
        return wwService.index(mess);
    }

    /**
     * @param vo
     *
     * @throws Exception
     */
    @RequestMapping("/test")
    public String test(TestVo vo) throws ApplicationContextException {
	    vo.setId(2L);
        vo.setUuid(UUID.randomUUID());
        getTestVo(vo);
        return vo.toString();
    }

    public String tttt(UUID uuid, Map<String, String> map) {
        return uuid.toString();
    }

    private TestVo getTestVo(TestVo vo) {
        wwService.indexMap("mess");
	    vo.setCost(new BigDecimal("1"));
        emptyInput();
	    return vo;
    }

    private StringBuffer emptyInput() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(wwService.indexMap2("mess2"));
        return stringBuffer;
    }

    private StringBuffer emptyInput(StringBuffer stringBuffer) {
        stringBuffer.append(wwService.indexMap2("mess2"));
        return stringBuffer;
    }

    private TestVo getTestVo2(TestVo vo, Boolean hao, Boolean isYes, List<TestVo> list, Map<String, String> map, Map<String, List<String>> map2) {
        if (hao || isYes) {
            vo.setCost(new BigDecimal("1"));
        }
        return vo;
    }
}
