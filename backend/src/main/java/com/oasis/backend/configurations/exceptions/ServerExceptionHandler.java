package com.oasis.backend.configurations.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oasis.backend.models.bases.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.transaction.SystemException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.JDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTimeoutException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * The ServerExceptionHandler class handles exceptions globally for the server.
 * It extends the ResponseEntityExceptionHandler class provided by Spring using {@link RestControllerAdvice}.
 * It provides exception handling methods for various custom exceptions and standard exceptions.
 * @see ResponseEntityExceptionHandler
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class ServerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ServerExceptionHandler.class);

    @ExceptionHandler(OasisException.class)
    public ApiResponse<String> handleOasisException(OasisException exception) {
        log.error(exception.getMessage());

        ApiResponse<String> response = new ApiResponse<>(exception.getMessage());
        response.setData(exception.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST);

        return response;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException exception) {
        log.error(exception.getMessage());

        int violationsCount = exception.getConstraintViolations().size();
        String message;

        if (violationsCount > 1) {
            // More than one violation, format the messages as a list
            String violations = exception.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            message = "Your request needs to comply with these violations: " + violations;
        } else if (violationsCount == 1) {
            // Only one violation, return that single message
            message = exception.getConstraintViolations().iterator().next().getMessage();
        } else {
            // No violations (this should not happen under normal circumstances)
            message = "Error in validating input";
        }

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(message);

        Map<String, Object> data = new HashMap<>();
        AtomicInteger count = new AtomicInteger(1);
        for(var reason : exception.getConstraintViolations()) {
            count.getAndIncrement();
            data.put("Reason %s".formatted(count), reason.getMessage());
        }
        response.setData(data);
        response.setStatus(HttpStatus.BAD_REQUEST);


        return response;
    }

    @ExceptionHandler(DisabledException.class)
    public ApiResponse<String> handleDisabledException(DisabledException exception) {
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getMessage(), HttpStatus.LOCKED);
    }

    @ExceptionHandler(LockedException.class)
    public ApiResponse<String> handleLockedException(LockedException exception) {
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getMessage(), HttpStatus.LOCKED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponse<String> handleBadCredentialsException(BadCredentialsException exception) {
        log.error(exception.getMessage());

        return new ApiResponse<>("Incorrect user details", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public ApiResponse<String> handleSocketTimeoutException(SocketTimeoutException exception) {
        log.error(exception.getMessage());

        return new ApiResponse<>("No network connection. Check your internet.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnknownHostException.class)
    public ApiResponse<String> handleUnknownHostException(UnknownHostException exception) {
        log.error(exception.getMessage());

        return new ApiResponse<>("No network connection. Check your internet.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ApiResponse<String> handleUnexpectedTypeException(UnexpectedTypeException exception) {
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleException(Exception exception) {
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<String> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.error(exception.getMessage());

        ApiResponse<String> response;
        if(exception.getMessage().contains("Detail:")) {
            int detail = exception.getMessage().indexOf("Detail:");
            int stop = exception.getMessage().indexOf(".]");
            response = new ApiResponse<>(exception.getMessage().substring(detail, stop));
        } else {
            response = new ApiResponse<>(exception.getMessage());
        }
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setData(exception.getLocalizedMessage());

        return response;
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ApiResponse<String> handleHttpServerErrorException(HttpServerErrorException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("Invalid user input.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectException.class)
    public ApiResponse<String> handleConnectException(ConnectException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>(
                "Connection timed out. Please check your internet connection",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ApiResponse<String> handleJsonProcessingException(JsonProcessingException exception) {
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ApiResponse<String> handleDateTimeParseException(DateTimeParseException exception) {
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResponse<String> handleUsernameNotFoundException(UsernameNotFoundException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ApiResponse<String> handleHttpClientErrorException(HttpClientErrorException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JDBCException.class)
    public ApiResponse<String> handleJDBCException(JDBCException exception){
        log.error(exception.getSQLException().getMessage());

        return new ApiResponse<>("An error happened while fetching data, try again.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JDBCConnectionException.class)
    public ApiResponse<String> handleJDBCConnectionException(JDBCConnectionException exception){
        log.error(exception.getSQLException().getMessage());

        return new ApiResponse<>("Couldn't complete connection while fetching data, try again", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ApiResponse<String> handleIOException(IOException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ApiResponse<String> handleExpiredJwtException(ExpiredJwtException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("Token is expired. Try login or request for another", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureException.class)
    public ApiResponse<String> handleSignatureException(SignatureException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("Error processing user token", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ApiResponse<String> handleUnsupportedJwtException(UnsupportedJwtException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("Error reading user token", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ApiResponse<String> handleMalformedJwtException(MalformedJwtException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("Incorrect token", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoRouteToHostException.class)
    public ApiResponse<String> handleNoRouteToHostException(NoRouteToHostException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("Host not found for specified route. Please check your internet", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GeneralSecurityException.class)
    public ApiResponse<String> handleGeneralSecurityException(GeneralSecurityException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ApiResponse<String> handleNoSuchAlgorithmException(NoSuchAlgorithmException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StringIndexOutOfBoundsException.class)
    public ApiResponse<String> handleStringIndexOutOfBoundsException(StringIndexOutOfBoundsException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidKeyException.class)
    public ApiResponse<String> handleInvalidKeyException(InvalidKeyException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status, @NonNull WebRequest request
    ) {
        int violationsCount = ex.getBindingResult().getAllErrors().size();
        String message;

        if (violationsCount > 1) {
            // More than one violation, format the messages as a list
            String violations = ex.getBindingResult().getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            message = "Your request needs to comply with these violations: " + violations;
        } else if (violationsCount == 1) {
            // Only one violation, return that single message
            message = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        } else {
            // No violations (this should not happen under normal circumstances)
            message = "Error in validating input";
        }

        log.error(ex.getMessage());
        log.error(message);

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(message);

        Map<String, Object> data = new HashMap<>();
        AtomicInteger count = new AtomicInteger(1);
        for(var reason : ex.getBindingResult().getAllErrors()) {
            count.getAndIncrement();
            data.put("Reason %s".formatted(count), reason.getDefaultMessage());
        }
        response.setData(data);
        response.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status, WebRequest request
    ) {
        log.error(ex.getMessage());

        ApiResponse<String> response = new ApiResponse<>("An error occurred while sending your request message.");
        response.setData(request.getContextPath());
        response.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage());

        ApiResponse<String> response = new ApiResponse<>(ex.getMessage());
        response.setData(request.getContextPath());
        response.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(NullPointerException.class)
    public ApiResponse<String> handleNullPointerException(NullPointerException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<String> handleIllegalArgumentException(IllegalArgumentException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("Invalid data format", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(AssertionError.class)
    public ApiResponse<String> handleAssertionError(AssertionError exception){
        log.error(exception.getMessage());

        return new ApiResponse<>(exception.getMessage(),HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DataSourceLookupFailureException.class)
    public ApiResponse<String> handleDataSourceLookupFailureException(DataSourceLookupFailureException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("An error occurred while fetching data, please try again",HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ApiResponse<String> handleInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("An error occurred while fetching data, please try again",HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ApiResponse<String> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("An error occurred while fetching data, please try again",HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(SystemException.class)
    public ApiResponse<String> handleSystemException(SystemException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("An error occurred while fetching data, please try again",HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(SQLException.class)
    public ApiResponse<String> handleSQLException(SQLException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("An error occurred while fetching data, please try again",HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(SQLDataException.class)
    public ApiResponse<String> handleSQLDataException(SQLDataException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("An error occurred while fetching data, please try again",HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(SQLNonTransientConnectionException.class)
    public ApiResponse<String> handleSQLNonTransientConnectionException(SQLNonTransientConnectionException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("Connection error occurred, please try again",HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(SQLTimeoutException.class)
    public ApiResponse<String> handleSQLTimeoutException(SQLTimeoutException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("Timeout. Error while fetching data",HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ApiResponse<String> handleTransactionSystemException(TransactionSystemException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("An error occurred while saving your data. Try again", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ApiResponse<String> handleUnauthorized(HttpClientErrorException.Unauthorized exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("Unauthorized web access", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<String> handleAccessDeniedException(AccessDeniedException exception){
        log.error(exception.getMessage());

        return new ApiResponse<>("Unauthorized web access", HttpStatus.BAD_REQUEST);
    }
}