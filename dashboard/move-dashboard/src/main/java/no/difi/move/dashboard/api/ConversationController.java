/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.move.dashboard.api;

import no.difi.move.dashboard.app.MessageTrackerService;
import no.difi.move.dashboard.domain.ConversationAuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author nikko
 */
@Controller
@RequestMapping("/api/track")
@ExposesResourceFor(ConversationAuditLog.class)
public class ConversationController {

    @Autowired
    private MessageTrackerService messageTrackerService;

    @CrossOrigin
    @RequestMapping(method = GET, value = "/{conversationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> getMessage(
            @PathVariable String conversationId,
            Pageable pageable,
            PagedResourcesAssembler<ConversationAuditLog> assembler) {
        Page<ConversationAuditLog> search = messageTrackerService.findConversation(conversationId, pageable);
        return ResponseEntity.ok(assembler.toResource(search));
    }

}
