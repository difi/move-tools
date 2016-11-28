package no.difi.move.dashboard.app;

import no.difi.move.dashboard.domain.ConversationAuditLog;
import no.difi.move.dashboard.repo.ConversationAuditLogRepository;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private ConversationAuditLogRepository auditLogRepository;

    public Page<ConversationAuditLog> findConversation(String conversationId, Pageable pageable) {
        SearchQuery query = new NativeSearchQueryBuilder()
                .withFilter(QueryBuilders.boolQuery()
                        .filter(new MatchQueryBuilder("conversation_id", conversationId)))
                .withSort(new FieldSortBuilder("@timestamp").order(SortOrder.DESC))
                .withQuery(new MatchAllQueryBuilder())
                .withPageable(pageable).build();
        Page<ConversationAuditLog> search = auditLogRepository.search(query);
        return search;
    }

}
