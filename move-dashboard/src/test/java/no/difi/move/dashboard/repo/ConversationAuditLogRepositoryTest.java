/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.move.dashboard.repo;

import no.difi.move.dashboard.config.ElasticSearchConfiguration;
import no.difi.move.dashboard.domain.ConversationAuditLog;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author nikko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ElasticSearchConfiguration.class})
public class ConversationAuditLogRepositoryTest {

    @Autowired
    private ConversationAuditLogRepository auditLogRepository;

    @Test
    public void testSomeMethod() {
        String myOrgNumber = "910075918";

        SearchQuery query = new NativeSearchQueryBuilder()
            .withFilter(
                QueryBuilders.boolQuery()
                .must(new MatchQueryBuilder("conversation_id", "1ca54c1c-74ab-44b4-b3af-6f92a59b2f67"))
                .must(new MatchQueryBuilder("sender_org_number", myOrgNumber).operator(MatchQueryBuilder.Operator.OR))
                .must(new MatchQueryBuilder("receiver_org_number", myOrgNumber))
            )
            .withSort(new FieldSortBuilder("timestamp").order(SortOrder.DESC))
            .withQuery(new MatchAllQueryBuilder())
            .build();
        Page<ConversationAuditLog> search = auditLogRepository.search(query);
        System.out.println(search.getTotalElements());
    }

}
