package no.difi.move.dashboard.repo;

import no.difi.move.dashboard.domain.Conversation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 *
 * @author nikko
 */
public interface ConversationRepository extends ElasticsearchRepository<Conversation, Long> {

}
