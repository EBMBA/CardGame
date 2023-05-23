package com.example.usermicroservice.Repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.usermicroservice.model.User;
import com.example.usermicroservice.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testFindByUsername() {
        // Create a test user
        User user = new User();
        user.setUsername("testUser");
        user.setName("Test User");
        userRepository.save(user);

        // Perform the findByUsername operation
        Optional<User> foundUser = userRepository.findByUsername("testUser");

        // Assert that the user is found
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
        assertEquals("Test User", foundUser.get().getName());
    }

    @Test
    void testFindByUserId() {
        // Create a test user
        User user = new User();
        user.setUsername("testUser");
        user.setName("Test User");
        userRepository.save(user);

        // Perform the findByUserId operation
        Optional<User> foundUser = userRepository.findByUserId(user.getUserId());

        // Assert that the user is found
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
        assertEquals("Test User", foundUser.get().getName());
    }
}

