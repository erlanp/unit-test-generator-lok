package com.areyoo.lok.controller;

import com.areyoo.lok.service.api.WwService;
import com.areyoo.lok.vo.TestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xusong
 */
@RestController
public class WwController extends BaseController {
    @Autowired
    private WwService wwService;

    private int a;

    public void thisIs() {

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
        getTestVo(vo);
        return vo.toString();
    }

    private TestVo getTestVo(TestVo vo) {
        wwService.indexMap("mess");
        wwService.indexMap2("mess2");
	    vo.setCost(new BigDecimal("1"));
	    return vo;
    }

    private TestVo getTestVo2(TestVo vo, Boolean hao, Boolean isYes, List<TestVo> list, Map<String, String> map, Map<String, List<String>> map2) {
        if (hao || isYes) {
            vo.setCost(new BigDecimal("1"));
        }
        return vo;
    }
}
