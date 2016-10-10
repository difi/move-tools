package no.difi.move.dashboard.api.resource;

import no.difi.move.dashboard.domain.Conversation;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

/**
 *
 * @author kons-nlu
 */

public class ConversationResourceAssembler extends IdentifiableResourceAssemblerSupport<Conversation, ConversationResource> {

    private final String orgNumber;
    private final String conversationId;

    public ConversationResourceAssembler(Class<?> controllerClass, Class<ConversationResource> resourceType, String orgNumber, String conversationId) {
        super(controllerClass, resourceType);
        this.orgNumber = orgNumber;
        this.conversationId = conversationId;
    }

    @Override
    public ConversationResource toResource(Conversation t) {
        ConversationResource res = createResourceWithId(t.getId(), t, orgNumber);
        res.setConversationId(t.getId());
        res.setReceiverOrgNumber(t.getReceiverOrgNumber());
        res.setSenderOrgNumber(t.getSenderOrgNumber());
        res.setTimestamp(t.getTimestamp());
        return res;
    }
    
}
