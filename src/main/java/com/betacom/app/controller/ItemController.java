package com.betacom.app.controller;

import com.betacom.app.dto.ItemRequest;
import com.betacom.app.dto.ItemResponse;
import com.betacom.app.service.ItemService;
import com.betacom.app.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<Void> addItem(@RequestHeader("Authorization") String token, @RequestBody ItemRequest request) {
        itemService.addItem(jwtService.getUserIdFromToken(token), request.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<ItemResponse> getItems(@RequestHeader("Authorization") String token) {
        return itemService.getItems(jwtService.getUserIdFromToken(token));
    }
}
