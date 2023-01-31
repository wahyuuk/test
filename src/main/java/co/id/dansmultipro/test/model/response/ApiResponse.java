package co.id.dansmultipro.test.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse <T, C> {

    private C code;
    private String message;
    private T data;
}
