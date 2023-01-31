package co.id.dansmultipro.test.controller;

import co.id.dansmultipro.test.model.response.*;
import co.id.dansmultipro.test.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPositions() {
        return ResponseEntity.ok(positionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> getById(@PathVariable("id") String id) {
        return ResponseEntity.ok(positionService.getById(id));
    }

    @GetMapping("/by-location")
    public ResponseEntity<?> getByLocation() {
        ResponseWrapper<PositionLocation> res = new ResponseWrapper<>(positionService.getByLocation());

        return ResponseEntity.ok(ApiResponse
                .builder()
                .code("00")
                .message("SUCCESS")
                .data(res)
                .build());
    }

    @GetMapping("/by-filter")
    public ResponseEntity<?> getByFilter(
            @RequestParam(name = "findBy", required = true) String key,
            @RequestParam(name = "findValue", required = true) String value
    ) {

        ResponseWrapper<FilterResponse> res = new ResponseWrapper<>(positionService.getByFilter(key, value));

        return ResponseEntity.ok(ApiResponse
                .builder()
                .code(0)
                .message("ok")
                .data(res)
                .build());
    }
}
