/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.move.dashboard.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 *
 * @author nikko
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "no.difi.move.dashboard.repo")
public class ElasticSearchConfiguration {

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws UnknownHostException {
        return new ElasticsearchTemplate(client());
    }

    @Bean
    public Client client() throws UnknownHostException {
        TransportClient client = TransportClient.builder().settings(Settings.builder().put("client.transport.ignore_cluster_name", "true")).build();
        TransportAddress address = new InetSocketTransportAddress(InetAddress.getByName("itest-move-app01.difi.local"), 9300);
        client.addTransportAddress(address);
        return client;
    }

}
