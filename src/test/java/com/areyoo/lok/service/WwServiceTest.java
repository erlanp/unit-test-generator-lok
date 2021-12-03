package com.areyoo.lok.service;

import com.areyoo.lok.service.api.WwService;
import com.areyoo.lok.service.impl.WwServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WwServiceTest {
    @InjectMocks
    private WwServiceImpl wwService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void index() {
        Assert.assertTrue(wwService.index("123").indexOf("123") > 0);
    }
}