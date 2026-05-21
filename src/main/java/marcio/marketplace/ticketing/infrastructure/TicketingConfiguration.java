package marcio.marketplace.ticketing.infrastructure;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.jpa.autoconfigure.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.LinkedHashMap;

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackages = "marcio.marketplace.ticketing",
        entityManagerFactoryRef = "ticketingEntityManagerFactory",
        transactionManagerRef = "ticketingTransactionManager")
@EnableRedisRepositories(basePackages = "marcio.marketplace.ticketing",
        redisTemplateRef = "ticketingRedisTemplate")
public class TicketingConfiguration {

    @Qualifier("ticketing")
    @Bean(defaultCandidate = false)
    @ConfigurationProperties("ticketing.datasource")
    public DataSourceProperties ticketingDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Qualifier("ticketing")
    @Bean(defaultCandidate = false)
    @ConfigurationProperties("ticketing.datasource.configuration")
    public HikariDataSource ticketingDataSource(@Qualifier("ticketing") DataSourceProperties properties){ //usando Hikari como 'connection pull'
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Qualifier("ticketing")
    @Bean(defaultCandidate = false)
    @ConfigurationProperties("ticketing.jpa")
    public JpaProperties ticketingJpaProperties(){
        return new JpaProperties();
    }

    @Qualifier("ticketing")
    @Bean(defaultCandidate = false)
    public LocalContainerEntityManagerFactoryBean ticketingEntityManagerFactory(@Qualifier("ticketing") DataSource dataSource,
                                                                                @Qualifier("ticketing") JpaProperties jpaProperties){
        var builder = new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(),
                x -> new LinkedHashMap<>(jpaProperties.getProperties()),
                null
        );

        return builder
                .dataSource(dataSource)
                .packages("marcio.marketplace.ticketing")
                .persistenceUnit("ticketing")
                .properties(jpaProperties.getProperties())
                .build();

    }

    @Qualifier("ticketing")
    @Bean
    public PlatformTransactionManager ticketingTransactionManager(@Qualifier("ticketing") LocalContainerEntityManagerFactoryBean emf){
        return new JpaTransactionManager(emf.getObject());
    }

    @Qualifier("ticketing")
    @Bean(defaultCandidate = false)
    public RedisConnectionFactory ticketingRedisConnectionFactory(@Value("${ticketing.redis.host}") String hostName,
                                                                @Value("${ticketing.redis.port}") int port ){
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(hostName, port));
        //Sobreescreve o RedisConnectionFactory abaixo para conectar a um bando especifico...
    }

    @Qualifier("ticketing")
    @Bean(defaultCandidate = false)
    public RedisTemplate<?, ?> ticketingRedisTemplate(@Qualifier("ticketing") RedisConnectionFactory connectionFactory){
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template; //Para saber como persistir no banco e como ler ela direito...
    }
}
