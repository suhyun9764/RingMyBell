package com.ringmabell.whichme_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.datasource.url=jdbc:mysql://localhost:3306/ringmybell?useSSL=false&useUnicode=true&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true",
        "spring.datasource.username=root",
        "spring.datasource.password=whichme9764",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect",
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl",
        "spring.data.redis.host=localhost",
        "spring.data.redis.port=6379"
})
class WhichMeBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
