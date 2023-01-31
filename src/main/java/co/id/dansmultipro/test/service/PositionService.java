package co.id.dansmultipro.test.service;

import co.id.dansmultipro.test.model.response.FilterResponse;
import co.id.dansmultipro.test.model.response.Position;
import co.id.dansmultipro.test.model.response.PositionLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

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

    public Set<PositionLocation> getByLocation() {
        try {
            ResponseEntity<List<Position>> response = restTemplate
                    .exchange(URL.concat(".json"), HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<Position>>() {
                            });

            Map<String, List<Position>> result =
                    response.getBody().stream()
                            .collect(Collectors.groupingBy(Position::getLocation));

            Set<PositionLocation> positionLocations =
                    response.getBody().stream()
                            .map(data -> {
                                PositionLocation positionLocation = PositionLocation
                                        .builder()
                                        .location(data.getLocation())
                                        .data(result.get(data.getLocation()))
                                        .build();

                                return positionLocation;
                            })
                            .collect(Collectors.toSet());

            return positionLocations;
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Request failed");
        }
    }

    public Set<FilterResponse> getByFilter(String key, String value) {
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate
                    .exchange(URL.concat(".json"), HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<Map<String, Object>>>() {
                            });

            Set<FilterResponse> res =
                    response.getBody().stream()
                            .filter(data -> data.get(key).toString().toLowerCase().contains(value.toLowerCase()))
                            .map(data -> {
                                FilterResponse filterResponse = FilterResponse.builder()
                                        .findBy(key)
                                        .data(data)
                                        .build();
                                return filterResponse;
                            }).collect(Collectors.toSet());

            return res;

        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Request failed");
        }
    }
}
