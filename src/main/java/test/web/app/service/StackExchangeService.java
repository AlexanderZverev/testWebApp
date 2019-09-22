package test.web.app.service;

import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.message.GZipEncoder;
import test.web.app.model.ServiceResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class StackExchangeService implements IQuestionsService {
    public static class CacheKey {
        private String searchString;
        private int page;
        private boolean descending;

        public CacheKey(String searchString, int page, boolean descending) {
            this.searchString = searchString;
            this.page = page;
            this.descending = descending;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return page == cacheKey.page &&
                    descending == cacheKey.descending &&
                    Objects.equal(searchString, cacheKey.searchString);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(searchString, page, descending);
        }

        public String getSearchString() {
            return searchString;
        }

        public int getPage() {
            return page;
        }

        public boolean isDescending() {
            return descending;
        }
    }

    private WebTarget target;
    private LoadingCache<CacheKey, ServiceResponse> cache;
    private Configuration configs;
    private static Logger logger = LogManager.getLogger(StackExchangeService.class);

    public StackExchangeService() throws ConfigurationException {
        File configurationFile = new File("stackExchangeService.properties");
        configs = new Configurations().properties(configurationFile);

        configureCache();
        configureTarget();
    }

    public StackExchangeService(WebTarget target, Configuration configs) {
        this.target = target;
        this.configs = configs;

        configureCache();
    }

    @Override
    public ServiceResponse getQuestions(String searchString, int page, boolean descendingOrder) throws ExecutionException {
        CacheKey cacheKey = new CacheKey(searchString, page, descendingOrder);
        ServiceResponse answer = cache.get(cacheKey);

        return answer;
    }

    private ServiceResponse fetchAnswers(String searchString, int page, boolean descending) {
        String requestDetails = String.format(
                "query='%s', page='%d', desc='%s'",
                searchString,
                page,
                descending);

        logger.debug("Fetching: " + requestDetails);

        ServiceResponse result;

        try {
            result = target
                    .queryParam("order", descending ? "desc" : "asc")
                    .queryParam("intitle", searchString)
                    .queryParam("page", page)
                    .request(MediaType.APPLICATION_JSON)
                    .get(ServiceResponse.class);
        } catch (Exception e) {
            logger.error("", e);
            throw e;
        }

        if (result.getItems().isEmpty()) {
            logger.warn("StackExchange service returns empty result. Request details:" + requestDetails);
        }

        result.setPageSize(configs.getInt("stackExchangeService.pageSize"));
        result.setInDescendingOrder(descending);
        result.setPage(page);
        result.setSearchString(searchString);

        return result;
    }

    private void configureTarget() {
        String apiKey = configs.getString("stackExchangeService.apiKey");
        String endPoint = configs.getString("stackExchangeService.endPoint");
        int pageSize = configs.getInt("stackExchangeService.pageSize");
        String filter = configs.getString("stackExchangeService.filter");

        target = ClientBuilder.newBuilder()
                .build().target(endPoint)
                .queryParam("key", apiKey)
                .queryParam("sort", "creation")
                .queryParam("pageSize", pageSize)
                .queryParam("site", "stackoverflow")
                .queryParam("filter", filter)
                .register(GZipEncoder.class);
    }

    private void configureCache() {
        CacheLoader<CacheKey, ServiceResponse> loader = new CacheLoader<CacheKey, ServiceResponse>() {
            @Override
            public ServiceResponse load(CacheKey key) {
                ServiceResponse response = fetchAnswers(key.searchString, key.page, key.descending);

                return response;
            }
        };

        int cacheExpirationTime = configs.getInt("stackExchangeService.cacheDurationMinutes");
        int cacheCapacity = configs.getInt("stackExchangeService.cacheCapacity");

        cache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(cacheExpirationTime, TimeUnit.MINUTES)
                .maximumSize(cacheCapacity)
                .build(loader);
    }
}