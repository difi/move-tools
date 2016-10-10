/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.move.dashboard.api;

import java.util.stream.Collectors;
import no.difi.move.dashboard.domain.ConversationAuditLog;
import no.difi.move.dashboard.repo.ConversationAuditLogRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author nikko
 */
@Controller
public class MessageTracker {

    @Autowired
    private ConversationAuditLogRepository auditLogRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @RequestMapping(method = GET, value = "/", produces = MediaTypes.HAL_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> getMessages(
        @RequestParam String orgNumber,
        @RequestParam String conversationId) {

        SearchQuery query = new NativeSearchQueryBuilder()
            .withFilter(
                QueryBuilders.boolQuery()
                .must(new MatchQueryBuilder("sender_org_number", orgNumber).operator(MatchQueryBuilder.Operator.OR))
                .must(new MatchQueryBuilder("receiver_org_number", orgNumber))
                .must(new MatchQueryBuilder("conversation_id", "*"))
            )
            .addAggregation(terms("conversation_id").field("conversation_id").subAggregation(AggregationBuilders.topHits("hits").addSort("timestamp", SortOrder.DESC).setSize(1)))
            .build();
        Aggregations aggregations = template.query(query, (SearchResponse response) -> response.getAggregations());

        return ResponseEntity.ok(
            ((StringTerms) aggregations.get("conversation_id"))
            .getBuckets().stream()
            .collect(Collectors.toMap(
                b -> b.getKeyAsString(),
                b -> ((TopHits) b.getAggregations().get("hits")).getHits().getAt(0)
                .getSource()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                    f -> f.getKey(),
                    f -> f.getValue().toString())))));
    }

    @RequestMapping(method = GET, value = "/track", produces = MediaTypes.HAL_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> getMessage(
        @RequestParam String orgNumber,
        @RequestParam String conversationId,
        Pageable pageable,
        PagedResourcesAssembler<ConversationAuditLog> assembler) {
        SearchQuery query = new NativeSearchQueryBuilder()
            .withFilter(
                QueryBuilders.boolQuery()
                .must(new MatchQueryBuilder("conversation_id", conversationId))
                .must(new MatchQueryBuilder("sender_org_number", orgNumber).operator(MatchQueryBuilder.Operator.OR))
                .must(new MatchQueryBuilder("receiver_org_number", orgNumber))
            )
            .withSort(new FieldSortBuilder("timestamp").order(SortOrder.DESC))
            .withQuery(new MatchAllQueryBuilder())
            .withPageable(pageable)
            .build();
        Page<ConversationAuditLog> search = auditLogRepository.search(query);

        return ResponseEntity.ok(assembler.toResource(search));
    }
}
