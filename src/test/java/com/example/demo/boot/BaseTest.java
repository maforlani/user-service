package com.example.demo.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(
        classes = {InternalTestConfiguration.class}
)
@ActiveProfiles({"test"})
public abstract class BaseTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public BaseTest() {
    }
}