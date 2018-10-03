package cm.com.letsgo.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(cm.com.letsgo.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(cm.com.letsgo.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Partner.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Partner.class.getName() + ".resellers", jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Distributor.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Distributor.class.getName() + ".distributors", jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Town.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Company.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Company.class.getName() + ".resellers", jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Company.class.getName() + ".companies", jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Route.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Route.class.getName() + ".paths", jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Resa.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Travel.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Travel.class.getName() + ".travels", jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.ConfigFare.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.ConfigFare.class.getName() + ".companies", jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.ConfigCommission.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Planning.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Planning.class.getName() + ".travels", jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Cabin.class.getName(), jcacheConfiguration);
            cm.createCache(cm.com.letsgo.domain.Cabin.class.getName() + ".cabins", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
