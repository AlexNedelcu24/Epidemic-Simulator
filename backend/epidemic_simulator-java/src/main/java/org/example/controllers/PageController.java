package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.controllers.requests.PageRequest;
import org.example.controllers.requests.UserRequest;
import org.example.domain.Page;
import org.example.domain.Simulation;
import org.example.repository.PageRepository;
import org.example.service.PageService;
import org.example.service.interfaces.IPageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/page")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PageController {

    private IPageService pageService;

    @PostMapping
    public ResponseEntity<?> getPage(@RequestBody PageRequest pageRequest) {

        Long id = pageRequest.getId();

        try {
            Page p = pageService.getPage(id);
            return ResponseEntity.ok(p);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
