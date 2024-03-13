package com.enigma.wmb_api.util;

import com.enigma.wmb_api.model.request.payment.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class PaymentClientUtil {
    private final String MIDTRANS_API_KEY;
    private final String MIDTRANS_SNAP_URL;
    private final RestClient restClient;

    public PaymentClientUtil(
            @Value("${midtrans.api.key}") String midtransApiKey,
            @Value("${midtrans.api.snap-url}") String midtransSnapUrl,
            RestClient restClient
    ) {
        MIDTRANS_API_KEY = midtransApiKey;
        MIDTRANS_SNAP_URL = midtransSnapUrl;
        this.restClient = restClient;
    }

    public Map<String, String> requestPayment(PaymentRequest request){
        ResponseEntity<Map<String, String>> response = restClient.post()
                .uri(MIDTRANS_SNAP_URL)
                .header("Authorization", "Basic " + MIDTRANS_API_KEY)
                .body(request)
                .retrieve()
                .toEntity( new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }
}
