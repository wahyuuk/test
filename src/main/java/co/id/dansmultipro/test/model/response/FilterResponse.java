package co.id.dansmultipro.test.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class FilterResponse {

    private String findBy;
    private Map<String, Object> data;
}
