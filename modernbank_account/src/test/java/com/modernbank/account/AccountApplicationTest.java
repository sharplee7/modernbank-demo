package com.modernbank.account;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"KAFKA=localhost", "CUSTOMER=localhost"})
class AccountApplicationTest {
    @Test void context() {}
}