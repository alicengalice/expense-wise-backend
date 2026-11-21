package com.expensewise.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Shared Given/When/Then test support for controller API tests.
 * Extend this class in @WebMvcTest classes to reuse common HTTP actions.
 */
public abstract class ControllerGwtTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected ResultActions whenGet(String urlTemplate, Object... uriVars) throws Exception {
        return mockMvc.perform(get(urlTemplate, uriVars));
    }

    protected ResultActions whenPostJson(String url, Object requestBody) throws Exception {
        return mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody)));
    }

    protected ResultActions whenPostRawJson(String url, String rawJson) throws Exception {
        return mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(rawJson));
    }

    protected ResultActions whenPutJson(String urlTemplate, Object requestBody, Object... uriVars) throws Exception {
        return mockMvc.perform(put(urlTemplate, uriVars)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody)));
    }

    protected ResultActions whenDelete(String urlTemplate, Object... uriVars) throws Exception {
        return mockMvc.perform(delete(urlTemplate, uriVars));
    }

    protected <T> void thenNoInteractions(T mock) {
        then(mock).shouldHaveNoInteractions();
    }
}
