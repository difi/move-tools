package no.difi.move.serviceregistry.controller;

import no.difi.move.serviceregistry.service.ProxyService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(ProxyController.class)
@AutoConfigureMockMvc
public class ProxyControllerTest {

    private static final String VALID_JSON = "{}";

    @MockBean
    private ProxyService service;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        when(service.lookupIdentifier("123")).thenReturn(VALID_JSON);
    }

    @Test
    public void lookupValidIdentifierTest() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/identifier/123").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assert.assertEquals(VALID_JSON, result.getResponse().getContentAsString());
    }
}
