package com.example.library.application.controller;

import com.example.library.domain.model.Category;
import com.example.library.domain.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<Category> create(@RequestParam String name) {
        return new ResponseEntity<>(categoryService.create(name), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id,
                                           @RequestParam String name) {
        return ResponseEntity.ok(categoryService.update(id, name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/byFilter")
    public ResponseEntity<List<Category>> getByFilter(
            @RequestParam(required = false) String name) {

        return ResponseEntity.ok(categoryService.findByFilter(name));
    }
}