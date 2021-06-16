package tw.hyin.mySpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry //spring boot 主程式，不行自動重跑
@SpringBootApplication
@EnableAutoConfiguration(exclude = {
        // 不要自動取得 db 設定，將自行於 config 設定
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
//we initialize the Servlet context required by Tomcat
//by implementing the SpringBootServletInitializer interface
public class MyRestApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MyRestApiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MyRestApiApplication.class);
    }

}
