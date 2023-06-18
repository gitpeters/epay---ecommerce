package com.peters.User_Registration_and_Email_Verification.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.webjars.NotFoundException;

import java.io.IOException;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatus statusCode = (HttpStatus) httpResponse.getStatusCode();
        return (statusCode.is4xxClientError() || statusCode.is5xxServerError());
    }


    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatus statusCode = (HttpStatus) httpResponse.getStatusCode();

        if (statusCode.is5xxServerError()) {
            // handle SERVER_ERROR
        } else if (statusCode.is4xxClientError()) {
            // handle CLIENT_ERROR
            if (statusCode == HttpStatus.NOT_FOUND) {
                throw new NotFoundException("Something went wrong");
            }
        }
    }

}
