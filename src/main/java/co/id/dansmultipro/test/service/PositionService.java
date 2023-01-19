package co.id.dansmultipro.test.service;

import co.id.dansmultipro.test.model.response.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PositionService {

    private RestTemplate restTemplate;

    private final String URL = "http://dev3.dansmultipro.co.id/api/recruitment/positions";

    @Autowired
    public PositionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Position> getAll() {
        try {
            ResponseEntity<List<Position>> response = restTemplate
                    .exchange(URL.concat(".json"), HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<Position>>() {
                            });

        return response.getBody();
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Request failed");
        }

    }

    public Position getById(String id) {
        try {
            ResponseEntity<Position> response = restTemplate
                    .getForEntity(URL.concat("/".concat(id)), Position.class);

            return response.getBody();
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Request failed");
        }
    }
}
