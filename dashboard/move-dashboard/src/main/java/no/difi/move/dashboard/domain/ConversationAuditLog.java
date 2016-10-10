package no.difi.move.dashboard.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.hateoas.Identifiable;

/**
 *
 * @author nikko
 */
@Data
@Document(indexName = "audit-*", createIndex = false, useServerConfiguration = true, type = "audit")
public class ConversationAuditLog implements Identifiable<String> {

    @Field
    @JsonProperty("document_id")
    private String id;

    @Field
    @JsonProperty("conversation_id")
    private String conversationId;

    @Field
    @JsonProperty("journalpost_id")
    private String journalpostId;

    @Field
    @JsonProperty("message")
    private String message;

    @Field
    @JsonProperty("orgnumber")
    private String orgNumber;

    @Field
    @JsonProperty("receiver_org_number")
    private String receiverOrgNumber;

    @Field
    @JsonProperty("sender_org_number")
    private String senderOrgNumber;

    @Field
    @JsonProperty("message-type")
    private String messageType;

    @Field
    @JsonProperty("level")
    private String level;

    @Field
    @JsonProperty("payload-size")
    private String payloadSize;

    @Field
    @JsonProperty("soap_fault")
    private String soapFault;

    @Field
    @JsonProperty("@timestamp")
    private String timestamp;

}
