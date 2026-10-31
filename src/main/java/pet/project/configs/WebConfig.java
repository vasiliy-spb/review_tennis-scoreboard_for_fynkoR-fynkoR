package pet.project.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.hibernate.HibernateTransactionManager;
import org.springframework.orm.jpa.hibernate.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("pet.project") // В веб конфигурации лучше оставить
    // сканирование пакетов только веб слоя, а остальные сканировать в другом классе (например в AppConfig).
@PropertySource("file:/var/lib/tomcat10/conf/hibernate.properties") // @PropertySource с абсолютным путём — это хрупкий подход.
    // Конфиг сломается например при другой версии Tomcat.
//@PropertySource("classpath:hibernate.properties") // Неиспользуемый в приложении код стоит удалять перед коммитом.
@EnableTransactionManagement
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    // Класс собрал в себя настройки вообще всего приложения. Это нарушает принцип Единой ответственности (SRP).
        // Можно разнести настройки на разные классы, соответствующие слоям приложения:
        // - общие настройки приложения
        // - веб настройки
        // - настройки, связанные с работой с БД (persistence)

    // Везде, где есть возможность, лучше внедрять зависимости через аргументы метода,
        // а не через обращение к методу создания бина.
        /*
        То есть вместо этого:
        @Bean
        public LocalSessionFactoryBean sessionFactory() {
            // ...
            sessionFactory.setDataSource(dataSource());
            // ...
        }

        Лучше так:
        @Bean
        public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
            // ...
            sessionFactory.setDataSource(dataSource);
            // ...
        }
         */

    private final Environment environment;

    // Если конструктор единственный в классе, то @Autowired над ним можно не писать
    @Autowired
    public WebConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName(environment.getRequiredProperty("db.driver_class"));
        config.setJdbcUrl(environment.getRequiredProperty("db.url"));
        config.setUsername(environment.getRequiredProperty("db.username"));
        config.setPassword(environment.getRequiredProperty("db.password"));

        return new HikariDataSource(config);
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();

        // В Hibernate 6+ диалект определяется автоматически, поэтому явное указание не обязательно.
            // Для PostgreSQL диалект подхватится сам.
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));

        return properties;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setPackagesToScan("pet.project.entities");

        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(sessionFactory().getObject());

        return hibernateTransactionManager;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
