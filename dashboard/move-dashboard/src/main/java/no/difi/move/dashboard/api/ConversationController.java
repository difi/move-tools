/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.move.dashboard.api;

import java.util.List;
import no.difi.move.dashboard.api.resource.ConversationResource;
import no.difi.move.dashboard.api.resource.ConversationResourceAssembler;
import no.difi.move.dashboard.app.MessageTrackerService;
import no.difi.move.dashboard.domain.Conversation;
import no.difi.move.dashboard.domain.ConversationAuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author nikko
 */
@Controller
@RequestMapping("/api/track/{orgNumber}")
@ExposesResourceFor(ConversationAuditLog.class)
public class ConversationController {

    
    @Autowired
    private MessageTrackerService messageTrackerService;

    @CrossOrigin
    @RequestMapping(method = GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> getMessages(
            @PathVariable String orgNumber,
            Pageable pageable,
            @RequestParam(required = false, value = "c") String conversationId,
            PagedResourcesAssembler assembler) {

        Page<Conversation> result = messageTrackerService.findConversations(orgNumber, conversationId, pageable);
        
        return ResponseEntity.ok(assembler.toResource(result,
                        new ConversationResourceAssembler(
                                ConversationController.class, 
                                ConversationResource.class,
                                orgNumber,
                                conversationId
                        )));
    }


    @CrossOrigin
    @RequestMapping(method = GET, value = "/{conversationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> getMessage(
        @PathVariable String orgNumber,
        @PathVariable String conversationId,
        Pageable pageable,
        PagedResourcesAssembler<ConversationAuditLog> assembler) {
        Page<ConversationAuditLog> search = messageTrackerService.findConversation(conversationId, orgNumber, pageable);
        return ResponseEntity.ok(assembler.toResource(search));
    }

}
