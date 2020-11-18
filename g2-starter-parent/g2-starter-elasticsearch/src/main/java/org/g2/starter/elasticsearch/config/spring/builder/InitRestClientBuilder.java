package org.g2.starter.elasticsearch.config.spring.builder;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.g2.starter.elasticsearch.config.ElasticsearchProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.sniff.ElasticsearchNodesSniffer;
import org.elasticsearch.client.sniff.NodesSniffer;
import org.elasticsearch.client.sniff.SniffOnFailureListener;
import org.elasticsearch.client.sniff.Sniffer;
import org.springframework.boot.context.properties.PropertyMapper;

/**
 * @author wenxi.wu@hand-china.com 2020/4/18
 */
public class InitRestClientBuilder {

    private RestClientBuilder restClientBuilder;
    private RestClient restClient;
    private ElasticsearchProperties properties;
    private RestHighLevelClient restHighLevelClient;
    /**
     * 嗅探器
     */
    private Sniffer sniffer;

    private static final PropertyMapper MAPPER = PropertyMapper.get();

    public InitRestClientBuilder(ElasticsearchProperties properties) {
        this.properties = properties;
    }

    public RestClient getRestClient() {
        return restClient;
    }

    public RestHighLevelClient getRestHighLevelClient() {
        return restHighLevelClient;
    }

    /**
     * es 集群时，客户端负载均衡轮询访问节点，设置节点选择器，{@link RestClientBuilder#setNodeSelector(NodeSelector)}
     * 自定义节点选择器{@link CustomNodeSelector}
     *
     * @see NodeSelector
     */
    private void init() {
        HttpHost[] httpHosts = Arrays.stream(properties.getClusterNodes()
                .split(","))
                .map(this::buildHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        restClientBuilder = RestClient.builder(httpHosts);
        buildRequestConfigCallback();
        buildHttpClientConfigCallback();
        restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        // 查考es 官方文档 https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/_usage.html
        if (properties.isEnableSniff()) {
            /*
             *   设置节点选择器
             * The client sends each request to one of the configured nodes in round-robin fashion
             */
            restClientBuilder.setNodeSelector(NodeSelector.ANY);
            // 设置嗅探器
            SniffOnFailureListener sniffOnFailureListener = new SniffOnFailureListener();
            restClientBuilder.setFailureListener(sniffOnFailureListener);
            //
            restClient = restClientBuilder.build();

            NodesSniffer nodesSniffer = new ElasticsearchNodesSniffer(restClient, TimeUnit.SECONDS.toMillis(5), ElasticsearchNodesSniffer.Scheme.HTTP);
            sniffer = Sniffer.builder(restClient).setNodesSniffer(nodesSniffer).build();
            sniffOnFailureListener.setSniffer(sniffer);
        } else {
            restClient = restClientBuilder.build();
        }
    }

    /**
     * 参考 RestClientConfigurations.RestClientBuilder 配置类
     */
    private void buildHttpClientConfigCallback() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        MAPPER.from(properties::getUsername).whenHasText().to((username) -> {
            Credentials credentials = new UsernamePasswordCredentials(properties.getUsername(),
                    properties.getPassword());
            credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        });
        restClientBuilder.setHttpClientConfigCallback(
                (httpClientBuilder) -> {
                    httpClientBuilder.disableAuthCaching();
                    // 认证信息
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    httpClientBuilder.setMaxConnTotal(properties.getPool().getMaxConnectionNum());
                    httpClientBuilder.setMaxConnPerRoute(properties.getPool().getMaxConnectionPerRoute());
                    return httpClientBuilder;
                });
    }

    /**
     * 参考 RestClientConfigurations.RestClientBuilder 配置类
     */
    private void buildRequestConfigCallback() {
        restClientBuilder.setRequestConfigCallback((requestConfigBuilder) -> {
            MAPPER.from(properties.getPool()::getConnectionTimeout).whenNonNull().asInt(Duration::toMillis)
                    .to(requestConfigBuilder::setConnectTimeout);
            MAPPER.from(properties.getPool()::getReadTimeout).whenNonNull().asInt(Duration::toMillis)
                    .to(requestConfigBuilder::setSocketTimeout);
            return requestConfigBuilder;
        });
    }

    private HttpHost buildHttpHost(String address) {
        String[] clusterNode = address.split(":");
        if (clusterNode.length != 2) {
            return null;
        }
        String ip = clusterNode[0];
        int port = Integer.parseInt(clusterNode[1]);
        return new HttpHost(ip, port, properties.getScheme());
    }

    private void close() {
        if (null != sniffer) {
            sniffer.close();
        }
        if (null != restClient) {
            try {
                restClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
