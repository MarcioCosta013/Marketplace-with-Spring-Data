package marcio.marketplace.registration.infrastructure;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.jpa.autoconfigure.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.LinkedHashMap;

@Configuration(proxyBeanMethods = false) //P/ o Spring sobreescrever algumas configs dele por essas...
@EnableJpaRepositories( //tudo isso basicamente para separar o que vai para cada DB.
        basePackages = "marcio.marketplace.registration",
        entityManagerFactoryRef = "registrationEntityManagerFactory",
        transactionManagerRef = "registrationTransactionManager")
public class RegistrationConfiguration {

    @Primary
    @Bean
    @ConfigurationProperties("registration.datasource")
    public DataSourceProperties registrationDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Primary //Serve para o spring identificar qual DataSourceProperties ele deve injetar, quando tem muitos DBs...
    @Bean //Quando não tem o 'Qualifier' o spring jhá carrega o Primary...
    @ConfigurationProperties("registration.datasource.configuration")
    public HikariDataSource registrationDataSource(DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean
    @ConfigurationProperties("registration.jpa")
    public JpaProperties registrationJpaProperties(){
        return new JpaProperties();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean registrationEntityManagerFactory(DataSource dataSource,
                                                                                  JpaProperties jpaProperties){
        var builder = new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(),
                x -> new LinkedHashMap<>(jpaProperties.getProperties()),
                null
        );

        return builder
                .dataSource(dataSource)
                .packages("marcio.marketplace.registration")
                .persistenceUnit("registration")
                .properties(jpaProperties.getProperties())
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager registrationTransactionManager(LocalContainerEntityManagerFactoryBean emf){
        return new JpaTransactionManager(emf.getObject());
    }

}
