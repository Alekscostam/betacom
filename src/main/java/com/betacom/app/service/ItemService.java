package com.betacom.app.service;

import com.betacom.app.dto.ItemResponse;
import com.betacom.app.entity.Item;
import com.betacom.app.entity.User;
import com.betacom.app.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    public void addItem(UUID userId, String name) {
        User user = userService.getUserById(userId);
        Item item = new Item();
        item.setName(name);
        item.setOwner(user);
        itemRepository.save(item);
    }

    public List<ItemResponse> getItems(UUID userId) {
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(item -> new ItemResponse(item.getId(), item.getName()))
                .toList();
    }
}
