package com.techcode.studentmgmt.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.techcode.studentmgmt.constants.ErrorCodeEnum;
import com.techcode.studentmgmt.exceptions.PaypalProviderException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HttpServiceEngine {

    private final RestClient restClient;
    
    


    public ResponseEntity<?> makeHttpCall(HttpRequest httpRequest) {

        try {
            log.info("HTTP call | method={} | url={}",
                    httpRequest.getHttpmethod(),
                    httpRequest.getUrl()
            );

            RestClient.RequestBodySpec requestSpec = restClient
                    .method(httpRequest.getHttpmethod())
                    .uri(httpRequest.getUrl())
                    .headers(headers -> headers.addAll(httpRequest.getHeaders()));

            if (httpRequest.getRequestBody() != null) {
                requestSpec = requestSpec.body(httpRequest.getRequestBody());
            }

            ResponseEntity<String> response =
                    requestSpec.retrieve().toEntity(String.class);

            log.info("HTTP response | status={}", response.getStatusCode());
            return response;

        } catch (HttpClientErrorException | HttpServerErrorException e) {

            HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
            log.error("HTTP error | status={} | body={}",
                    status,
                    e.getResponseBodyAsString()
            );

            if (status.is5xxServerError()) {
                throw new RuntimeException(
                        "Unable to connect to PayPal provider",
                        e
                );
            }

            throw new PaypalProviderException(
                    ErrorCodeEnum.RESOURCE_NOT_FOUND.getCode(),
                    ErrorCodeEnum.RESOURCE_NOT_FOUND.getMessage(),
                    status
            );

        } catch (Exception e) {
            log.error("Unexpected error during HTTP call", e);

            throw new PaypalProviderException(
                    ErrorCodeEnum.GENERIC_ERROR.getCode(),
                    ErrorCodeEnum.GENERIC_ERROR.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
