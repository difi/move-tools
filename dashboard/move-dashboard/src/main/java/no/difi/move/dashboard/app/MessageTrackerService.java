/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.move.dashboard.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import no.difi.move.dashboard.domain.Conversation;
import no.difi.move.dashboard.domain.ConversationAuditLog;
import no.difi.move.dashboard.repo.ConversationAuditLogRepository;
import no.difi.move.dashboard.repo.ConversationRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

/**
 *
 * @author kons-nlu
 */
@Service
public class MessageTrackerService {

    @Autowired
    private ElasticsearchTemplate template;
    @Autowired
    private ConversationAuditLogRepository auditLogRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    
    public Page<Conversation> findConversations(String orgNumber, String conversationId, Pageable pageable) {
        SearchQuery query = new NativeSearchQueryBuilder()
                .withFilter(QueryBuilders.boolQuery()
                        .filter(conversationId == null ? 
                                new ExistsQueryBuilder("conversation_id")
                                : new MatchQueryBuilder("conversation_id", conversationId))
                        .filter(new MatchQueryBuilder("sender_org_number", orgNumber)
                                .operator(MatchQueryBuilder.Operator.OR))
                        .filter(new MatchQueryBuilder("receiver_org_number", orgNumber)))
                .withSort(new FieldSortBuilder("@timestamp").order(SortOrder.DESC))
                .withQuery(new MatchAllQueryBuilder())
                .withPageable(pageable).build();
        Page<Conversation> search = conversationRepository.search(query);
        return search;
    }

    public Page<ConversationAuditLog> findConversation(String conversationId, String orgNumber, Pageable pageable) {
        SearchQuery query = new NativeSearchQueryBuilder()
                .withFilter(QueryBuilders.boolQuery()
                        .filter(new MatchQueryBuilder("conversation_id", conversationId))
                        .filter(new MatchQueryBuilder("sender_org_number", orgNumber)
                                .operator(MatchQueryBuilder.Operator.OR))
                        .filter(new MatchQueryBuilder("receiver_org_number", orgNumber)))
                .withSort(new FieldSortBuilder("@timestamp").order(SortOrder.DESC))
                .withQuery(new MatchAllQueryBuilder())
                .withPageable(pageable).build();
        Page<ConversationAuditLog> search = auditLogRepository.search(query);
        return search;
    }
    
}
