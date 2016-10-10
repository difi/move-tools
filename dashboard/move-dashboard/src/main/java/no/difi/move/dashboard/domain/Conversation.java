package no.difi.move.dashboard.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.hateoas.Identifiable;

/**
 *
 * @author kons-nlu
 */
@Data
@Document(indexName = "audit-*", createIndex = false, useServerConfiguration = true, type = "conversation")
public class Conversation implements Identifiable<String> {

    @Field
    @JsonProperty("conversation_id")
    private String id;

    @Field
    @JsonProperty("receiver_org_number")
    private String receiverOrgNumber;

    @Field
    @JsonProperty("sender_org_number")
    private String senderOrgNumber;

    @Field
    @JsonProperty("@timestamp")
    private String timestamp;
    
}
