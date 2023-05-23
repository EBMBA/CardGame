package com.example.usermicroservice.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.usermicroservice.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {
    private List<Integer> userIds;
    private List<String> usernames;
    private List<String> names;

    @BeforeEach
    void setUp() {
        userIds = List.of(1, 2, 3);
        usernames = List.of("testUser1", "testUser2", "testUser3");
        names = List.of("Test User 1", "Test User 2", "Test User 3");
    }

    @AfterEach
    void tearDown() {
        userIds = null;
        usernames = null;
        names = null;
    }

    @Test
    void testUser() {
        for (int i = 0; i < userIds.size(); i++) {
            User user = new User();
            user.setUserId(userIds.get(i));
            user.setUsername(usernames.get(i));
            user.setName(names.get(i));

            assertNotNull(user.getUserId());
            assertNotNull(user.getUsername());
            assertNotNull(user.getName());

            assertEquals(userIds.get(i).intValue(), user.getUserId().intValue());
            assertEquals(usernames.get(i), user.getUsername());
            assertEquals(names.get(i), user.getName());
        }
    }
}

