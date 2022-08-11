package com.areyoo.lok.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author xusong
 */
@Setter
@Getter
@ToString
public class TestVo {
    private List<TestTwoVo> list;

    private TestTwoVo testTwoVo;

    private Set<String> set;

    private Date date;

    private UUID uuid;

    private Float f;

    private Double d;

    private Integer count;

    private Long id;

    private String name;

    private Byte b;

    private char c2;
    private long l;
    private int i;
    private float f2;
    private double d2;
    private short s2;
    private byte b2;
    private Short s;

    private BigDecimal cost;

    public void test() {

    }
}