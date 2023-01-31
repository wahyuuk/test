package co.id.dansmultipro.test.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PositionLocation {

    private String location;
    private List<Position> data;
}
