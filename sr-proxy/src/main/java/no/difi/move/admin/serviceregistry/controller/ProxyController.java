package no.difi.move.admin.serviceregistry.controller;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.admin.serviceregistry.service.ProxyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@Slf4j
@RestController
public class ProxyController {

    private ProxyService service;

    public ProxyController(ProxyService service) {
        this.service = Objects.requireNonNull(service);
    }

    @RequestMapping(value = "/identifier/{identifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> lookupEntityById(@PathVariable String identifier) {

        HttpStatus httpStatus = HttpStatus.OK;
        String result = "";
        try {
            result = service.lookupIdentifier(identifier);
        } catch (HttpClientErrorException e) {
            log.error("Client error exception occurred.", e);
            httpStatus = e.getStatusCode();
        }

        return new ResponseEntity<>(result, httpStatus);
    }

}
