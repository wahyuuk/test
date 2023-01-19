package co.id.dansmultipro.test.model.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponse {

    private String token;
}
