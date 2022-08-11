package com.areyoo.lok.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xusong
 */
@Setter
@Getter
@ToString
public class TestTwoVo implements ITestTwoVo {
    private float f;

    private double d;

    private int count;
}