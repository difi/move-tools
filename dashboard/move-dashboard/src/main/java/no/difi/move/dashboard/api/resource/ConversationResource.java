package no.difi.move.dashboard.api.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author kons-nlu
 */
@Data
public class ConversationResource extends ResourceSupport {
    
    @JsonProperty("conversation_id")
    private String conversationId;
    
    @JsonProperty("receiver_org_number")
    private String receiverOrgNumber;

    @JsonProperty("sender_org_number")
    private String senderOrgNumber;
    
    @JsonProperty("timestamp")
    private String timestamp;
}
