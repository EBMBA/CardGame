package com.example.storemicroservice.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.catalina.connector.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientException;

import com.example.common.model.StoreOperationRequest;
import com.example.common.model.Enum.TransactionType;
import com.example.storemicroservice.controller.StoreRestCRT;
import com.example.storemicroservice.controller.StoreService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StoreRestCRT.class)
public class StoreRestCRTTest {
    @MockBean
    private StoreService storeService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransaction_Success_ReturnsOkStatus() throws Exception {
        StoreOperationRequest operationRequest = new StoreOperationRequest("1","1", TransactionType.BUY);

        when(storeService.doTransactionCard(Mockito.any(StoreOperationRequest.class))).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(operationRequest));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        verify(storeService, times(1)).doTransactionCard(Mockito.any(StoreOperationRequest.class));
    }

    @Test
    public void testTransaction_Failure_ReturnsBadRequestStatus() throws Exception {
        StoreOperationRequest operationRequest = new StoreOperationRequest("1","1", TransactionType.BUY);

        when(storeService.doTransactionCard(Mockito.any(StoreOperationRequest.class))).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(operationRequest));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andReturn();
        
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        verify(storeService, times(1)).doTransactionCard(Mockito.any(StoreOperationRequest.class));
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
