package co.id.dansmultipro.test.controller;

import co.id.dansmultipro.test.model.response.Position;
import co.id.dansmultipro.test.model.response.PositionLocation;
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
    public ResponseEntity<List<Position>> getAllPositions() {
        return ResponseEntity.ok(positionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> getById(@PathVariable("id") String id) {
        return ResponseEntity.ok(positionService.getById(id));
    }

    @GetMapping("/by-location")
    public ResponseEntity<?> getByLocation() {
        return ResponseEntity.ok(positionService.getByLocation());
    }
}
