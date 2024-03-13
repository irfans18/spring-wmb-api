package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.enums.TransactionStatus;
import com.enigma.wmb_api.entity.Payment;
import com.enigma.wmb_api.entity.Transaction;
import com.enigma.wmb_api.model.request.payment.PaymentDetailRequest;
import com.enigma.wmb_api.model.request.payment.PaymentItemDetailRequest;
import com.enigma.wmb_api.model.request.payment.PaymentRequest;
import com.enigma.wmb_api.repo.PaymentRepo;
import com.enigma.wmb_api.service.PaymentService;
import com.enigma.wmb_api.util.PaymentClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo repo;
    private final PaymentClientUtil client;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment create(Transaction transaction) {
        int amount = transaction.getBillDetails().stream()
                .mapToInt(detail -> detail.getQty() * detail.getPrice())
                .reduce(0, Integer::sum);

        List<PaymentItemDetailRequest> paymentItemDetailRequests = transaction.getBillDetails().stream()
                .map(detail -> PaymentItemDetailRequest
                        .builder()
                        .price(detail.getPrice())
                        .quantity(detail.getQty())
                        .name(detail.getMenu().getName())
                        .build())
                .toList();
        List<String> paymentMethod = List.of("shopeepay","gopay");

        PaymentRequest paymentRequest = PaymentRequest
                .builder()
                .paymentDetail(PaymentDetailRequest
                        .builder()
                        .orderId(transaction.getId())
                        .amount(amount)
                        .build())
                .paymentItemDetails(paymentItemDetailRequests)
                .paymentMethod(paymentMethod)
                .build();

        // TODO: Call Midtrans API
        // ResponseEntity<Map<String, String>> response = restClient.post()
        //         .uri(MIDTRANS_SNAP_URL)
        //         .header("Authorization", "Basic " + MIDTRANS_API_KEY)
        //         .body(paymentRequest)
        //         .retrieve()
        //         .toEntity( new ParameterizedTypeReference<>() {
        //         });
        // TODO: Return PaymentResponse
        Map<String, String> body = client.requestPayment(paymentRequest);

        Payment payment = Payment.builder()
                .token(body.get("token"))
                .redirectUrl(body.get("redirect_url"))
                .transactionStatus(TransactionStatus.ORDERED)
                .build();

        return repo.saveAndFlush(payment);

        // return convertToPaymentResponse(payment);

    }

    // @Transactional(rollbackFor = Exception.class)
    // @Override
    // public void checkFailedAndUpdatePayments() {
    //     List<TransactionStatus> statusList = List.of(
    //             TransactionStatus.DENY,
    //             TransactionStatus.FAILURE,
    //             TransactionStatus.CANCEL,
    //             TransactionStatus.EXPIRE
    //     );
    //     List<Payment> payments = repo.findAllByTransactionStatusIn(statusList);
    //     payments.forEach(payment -> {
    //         Collection<BillDetail> details = payment.getTransaction().getBillDetails();
    //         for (BillDetail detail : details) {
    //             Menu menu = detail.getMenu();
    //             menu.setStock(menu.getStock() + detail.getQty());
    //         }
    //         payment.setTransactionStatus(TransactionStatus.RETURNED);
    //     });
    // }
}
