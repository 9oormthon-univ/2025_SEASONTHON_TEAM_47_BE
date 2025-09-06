// com/_oormthon/goEuro/common/config/JpaConfig.java
package com._oormthon.goEuro.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig { } // created.at , updated.at 사용하기 위해서 사용
