package com.betacom.app.service;

import static org.junit.jupiter.api.Assertions.*;



import com.betacom.app.dto.ItemResponse;
import com.betacom.app.entity.Item;
import com.betacom.app.entity.User;
import com.betacom.app.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addItem_shouldSaveItemWithCorrectNameAndOwner() {
        UUID userId = UUID.randomUUID();
        String itemName = "Test Item";
        User user = new User();
        user.setId(userId);

        when(userService.getUserById(userId)).thenReturn(user);
        when(itemRepository.save(any(Item.class))).thenAnswer(i -> i.getArgument(0));
        itemService.addItem(userId, itemName);

        verify(userService).getUserById(userId);
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(itemCaptor.capture());
        Item savedItem = itemCaptor.getValue();
        assertEquals(itemName, savedItem.getName());
        assertEquals(user, savedItem.getOwner());
    }

    @Test
    void getItems_shouldReturnListOfItemResponses() {
        UUID userId = UUID.randomUUID();
        Item item1 = new Item();
        item1.setId(UUID.randomUUID());
        item1.setName("Item 1");
        Item item2 = new Item();
        item2.setId(UUID.randomUUID());
        item2.setName("Item 2");

        when(itemRepository.findAllByOwnerId(userId)).thenReturn(List.of(item1, item2));
        List<ItemResponse> result = itemService.getItems(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Item 1", result.get(0).getName());
        assertEquals("Item 2", result.get(1).getName());
        verify(itemRepository).findAllByOwnerId(userId);
    }
}
