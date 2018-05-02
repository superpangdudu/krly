package cn.krly.service.payment.configuration;

import cn.krly.utility.db.DBUtils;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "cn.krly.service.payment.mapper")
public class MybatisConfiguration {
    static final String MAPPER_LOCATION = "classpath:/mapper/*.xml";

    @Value("${payment.database.url}")
    private String url;
    @Value("${payment.database.username}")
    private String userName;
    @Value("${payment.database.password}")
    private String password;
    @Value("${payment.database.driverClassName}")
    private String driverClassName;

    //
    @Bean(name = "payment")
    public DataSource getDataSource() {
        DruidDataSource dataSource = DBUtils.getDruidDataSource(driverClassName, url, userName, password);
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager getTransactionManager() {
        return new DataSourceTransactionManager(getDataSource());
    }

    @Bean
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("payment") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));

        return sessionFactory.getObject();
    }
}
