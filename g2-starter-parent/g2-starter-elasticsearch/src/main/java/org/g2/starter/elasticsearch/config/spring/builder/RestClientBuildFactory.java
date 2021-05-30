package org.g2.starter.elasticsearch.config.spring.builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.sniff.ElasticsearchNodesSniffer;
import org.elasticsearch.client.sniff.NodesSniffer;
import org.elasticsearch.client.sniff.SniffOnFailureListener;
import org.elasticsearch.client.sniff.Sniffer;
import org.g2.core.base.BaseConstants;
import org.g2.core.util.StringUtil;
import org.g2.starter.elasticsearch.config.ElasticsearchProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.PropertyMapper;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * RestHighLevelClient 初始化工厂
 *
 * @author wuwenxi 2021-05-28
 */
public class RestClientBuildFactory {

    private static final Logger log = LoggerFactory.getLogger(RestClientBuildFactory.class);

    private static final PropertyMapper MAPPER = PropertyMapper.get();
    private static final int NODE_LENGTH = 2;

    /**
     * 保证多线程下restHighLevelClient 原子性和可见性
     */
    private final AtomicReference<RestHighLevelClient> client = new AtomicReference<>();

    private ElasticsearchProperties properties;
    private int connectTimeOutMillis;
    private int socketTimeoutMillis;
    private int connectionRequestTimeoutMillis;
    private String userName;
    private String password;
    private String clusterNodes;
    private int maxConnectNum;
    private int maxConnectPerRoute;
    private String scheme;

    public RestClientBuildFactory(ElasticsearchProperties properties) {
        this.properties = properties;
        this.userName = properties.getUsername();
        this.password = properties.getPassword();
        this.clusterNodes = properties.getClusterNodes();
        this.scheme = properties.getScheme();
        this.connectTimeOutMillis = properties.getPool().getConnectionTimeout() == null ? 1000 : properties.getPool().getConnectionTimeout();
        this.socketTimeoutMillis = properties.getPool().getSocketTimeout() == null ? 1800000 : properties.getPool().getSocketTimeout();
        this.connectionRequestTimeoutMillis = properties.getPool().getConnectionRequestTimeout() == null ? 500 : properties.getPool().getConnectionRequestTimeout();
        this.maxConnectNum = properties.getPool().getMaxConnectionNum() == null ? 10 : properties.getPool().getMaxConnectionNum();
        this.maxConnectPerRoute = properties.getPool().getMaxConnectionPerRoute() == null ? 30 : properties.getPool().getMaxConnectionPerRoute();
    }

    private void init() {
        if (this.client.get() == null) {
            this.client.set(createRestHighLevelClientInstance());
        }
    }

    private void close() {
        RestHighLevelClient restHighLevelClient;
        if ((restHighLevelClient = this.client.get()) != null) {
            RestClient restClient = restHighLevelClient.getLowLevelClient();
            try {
                restClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取restHighLevelClient
     */
    public RestHighLevelClient restHighLevelClient() {
        return this.client.get();
    }

    public ElasticsearchProperties getProperties() {
        return properties;
    }

    public RestHighLevelClient restClientSelfHealing() {
        RestHighLevelClient expectClient = this.client.get();

        try {
            RestHighLevelClient updateClient = createRestHighLevelClientInstance();
            if (this.client.compareAndSet(expectClient, updateClient)) {
                expectClient.close();
                log.info("rest client self healing  successful ... ");
                return updateClient;
            }
            log.warn("rest client self healing  error ... ");
        } catch (IOException e) {
            log.error("rest client self healing error , msg : {}", StringUtil.exceptionString(e));
        }
        return expectClient;
    }

    /**
     * 初始化restHighLevelClient
     * es 集群时，客户端负载均衡轮询访问节点，设置节点选择器，{@link RestClientBuilder#setNodeSelector(NodeSelector)}
     * 自定义节点选择器{@link CustomNodeSelector}
     *
     * @return factory
     * @see NodeSelector
     */
    private RestHighLevelClient createRestHighLevelClientInstance() {
        HttpHost[] httpHosts = Arrays.stream(clusterNodes.split(BaseConstants.Symbol.COMMA))
                .map(this::buildHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        RestClientBuilder builder = RestClient.builder(httpHosts);
        buildRequestConfigCallback(builder);
        buildHttpClientConfigCallback(builder);
        // 查考es 官方文档 https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/_usage.html
        if (properties.isEnableSniff()) {
            /*
             *   设置节点选择器
             * The client sends each request to one of the configured nodes in round-robin fashion
             */
            builder.setNodeSelector(NodeSelector.ANY);
            // 设置嗅探器
            SniffOnFailureListener sniffOnFailureListener = new SniffOnFailureListener();
            builder.setFailureListener(sniffOnFailureListener);
            //
            RestClient restClient = builder.build();

            NodesSniffer nodesSniffer = new ElasticsearchNodesSniffer(restClient, TimeUnit.SECONDS.toMillis(5), ElasticsearchNodesSniffer.Scheme.HTTP);
            Sniffer sniffer = Sniffer.builder(restClient).setNodesSniffer(nodesSniffer).build();
            sniffOnFailureListener.setSniffer(sniffer);
        }
        return new RestHighLevelClient(builder);
    }

    /**
     * 参考 RestClientConfigurations.RestClientBuilder 配置类
     */
    private void buildHttpClientConfigCallback(RestClientBuilder restClientBuilder) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        //xpack账户认证
        if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
        }
        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.disableAuthCaching();
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            httpClientBuilder.setMaxConnTotal(this.maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(this.maxConnectPerRoute);
            return httpClientBuilder;
        });
    }

    /**
     * 参考 RestClientConfigurations.RestClientBuilder 配置类
     */
    private void buildRequestConfigCallback(RestClientBuilder restClientBuilder) {
        restClientBuilder.setRequestConfigCallback((requestConfigBuilder) -> {
            requestConfigBuilder.setConnectTimeout(this.connectTimeOutMillis);
            requestConfigBuilder.setSocketTimeout(this.socketTimeoutMillis);
            requestConfigBuilder.setConnectionRequestTimeout(this.connectionRequestTimeoutMillis);
            return requestConfigBuilder;
        });
    }

    private HttpHost buildHttpHost(String address) {
        String[] clusterNode = address.split(BaseConstants.Symbol.COLON);
        if (clusterNode.length != NODE_LENGTH) {
            return null;
        }
        String ip = clusterNode[0];
        int port = Integer.parseInt(clusterNode[1]);
        return new HttpHost(ip, port, scheme);
    }

    /**
     * 参考es 官网 https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/_node_selector.html
     */
    private static class CustomNodeSelector implements NodeSelector {
        @Override
        public void select(Iterable<Node> nodes) {
            /*
             * Prefer any node that belongs to rack_one. If none is around
             * we will go to another rack till it's time to try and revive
             * some of the nodes that belong to rack_one.
             */
            boolean foundOne = false;
            for (Node node : nodes) {
                String rackId = node.getAttributes().get("rack_id").get(0);
                if ("rack_one".equals(rackId)) {
                    foundOne = true;
                    break;
                }
            }
            if (foundOne) {
                Iterator<Node> nodesIt = nodes.iterator();
                while (nodesIt.hasNext()) {
                    Node node = nodesIt.next();
                    String rackId = node.getAttributes().get("rack_id").get(0);
                    if (!"rack_one".equals(rackId)) {
                        nodesIt.remove();
                    }
                }
            }
        }
    }
}
