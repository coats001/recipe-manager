package org.amoscoats.recipemanager.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Unit Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private MethodArgumentNotValidException validationException;

    @Mock
    private BindingResult bindingResult;

    @Test
    @DisplayName("Should handle RuntimeException and return 404")
    void shouldHandleRuntimeException() {
        // Given
        RuntimeException exception = new RuntimeException("Recipe not found with id: 1");

        // When
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleRuntimeException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().message()).isEqualTo("Recipe not found with id: 1");
        assertThat(response.getBody().timestamp()).isNotNull();
        assertThat(response.getBody().timestamp()).isBefore(LocalDateTime.now().plusSeconds(1));
    }

    @Test
    @DisplayName("Should handle RuntimeException with different message")
    void shouldHandleRuntimeExceptionWithDifferentMessage() {
        // Given
        RuntimeException exception = new RuntimeException("Custom error message");

        // When
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleRuntimeException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Custom error message");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with single field error")
    void shouldHandleValidationExceptionWithSingleFieldError() {
        // Given
        FieldError fieldError = new FieldError("recipeRequest", "name", "Recipe name is required");
        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        // When
        ResponseEntity<Map<String, Object>> response =
                exceptionHandler.handleValidationExceptions(validationException);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo(400);
        assertThat(response.getBody().get("errors")).isInstanceOf(Map.class);

        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
        assertThat(errors).hasSize(1);
        assertThat(errors.get("name")).isEqualTo("Recipe name is required");
        assertThat(response.getBody().get("timestamp")).isInstanceOf(LocalDateTime.class);
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with multiple field errors")
    void shouldHandleValidationExceptionWithMultipleFieldErrors() {
        // Given
        FieldError fieldError1 = new FieldError("recipeRequest", "name", "Recipe name is required");
        FieldError fieldError2 = new FieldError("recipeRequest", "servings", "Servings must be at least 1");
        FieldError fieldError3 = new FieldError("recipeRequest", "vegetarian", "Vegetarian flag is required");

        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2, fieldError3));

        // When
        ResponseEntity<Map<String, Object>> response =
                exceptionHandler.handleValidationExceptions(validationException);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();

        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
        assertThat(errors).hasSize(3);
        assertThat(errors.get("name")).isEqualTo("Recipe name is required");
        assertThat(errors.get("servings")).isEqualTo("Servings must be at least 1");
        assertThat(errors.get("vegetarian")).isEqualTo("Vegetarian flag is required");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with empty errors")
    void shouldHandleValidationExceptionWithEmptyErrors() {
        // Given
        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of());

        // When
        ResponseEntity<Map<String, Object>> response =
                exceptionHandler.handleValidationExceptions(validationException);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();

        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
        assertThat(errors).isEmpty();
    }

    @Test
    @DisplayName("Should create ErrorResponse record correctly")
    void shouldCreateErrorResponseRecord() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        GlobalExceptionHandler.ErrorResponse errorResponse =
                new GlobalExceptionHandler.ErrorResponse(404, "Not found", now);

        // Then
        assertThat(errorResponse.status()).isEqualTo(404);
        assertThat(errorResponse.message()).isEqualTo("Not found");
        assertThat(errorResponse.timestamp()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should test ErrorResponse record equality")
    void shouldTestErrorResponseRecordEquality() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        GlobalExceptionHandler.ErrorResponse response1 =
                new GlobalExceptionHandler.ErrorResponse(404, "Not found", now);
        GlobalExceptionHandler.ErrorResponse response2 =
                new GlobalExceptionHandler.ErrorResponse(404, "Not found", now);
        GlobalExceptionHandler.ErrorResponse response3 =
                new GlobalExceptionHandler.ErrorResponse(500, "Server error", now);

        // Then
        assertThat(response1).isEqualTo(response2);
        assertThat(response1).isNotEqualTo(response3);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    @DisplayName("Should test ErrorResponse record toString")
    void shouldTestErrorResponseRecordToString() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        GlobalExceptionHandler.ErrorResponse response =
                new GlobalExceptionHandler.ErrorResponse(404, "Not found", now);

        // When
        String toString = response.toString();

        // Then
        assertThat(toString).contains("404");
        assertThat(toString).contains("Not found");
        assertThat(toString).contains(now.toString());
    }
}
