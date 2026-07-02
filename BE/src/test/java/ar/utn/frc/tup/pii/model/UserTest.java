package ar.utn.frc.tup.pii.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {

    @Test
    void shouldCreateUserWithAllFields() {
        User user = new User(1L, "testuser", "test@test.com");

        assertNotNull(user);
        assertEquals(1L, user.id());
        assertEquals("testuser", user.username());
        assertEquals("test@test.com", user.email());
    }

    @Test
    void shouldCreateUserWithNullFields() {
        User user = new User(null, null, null);

        assertNotNull(user);
        assertEquals(null, user.id());
        assertEquals(null, user.username());
        assertEquals(null, user.email());
    }
}
