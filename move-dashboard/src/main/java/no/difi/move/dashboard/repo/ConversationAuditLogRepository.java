package no.difi.move.dashboard.repo;

import no.difi.move.dashboard.domain.ConversationAuditLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 *
 * @author nikko
 */
public interface ConversationAuditLogRepository extends ElasticsearchRepository<ConversationAuditLog, Long> {

}
