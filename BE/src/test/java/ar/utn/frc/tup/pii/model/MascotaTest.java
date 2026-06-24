package ar.utn.frc.tup.pii.model;

import ar.utn.frc.tup.pii.enums.Especie;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MascotaTest {

    @Test
    void shouldCreateMascotaWithAllFields() {
        Mascota mascota = new Mascota(
                1L, "Firulais", Especie.PERRO, "Labrador",
                3, 25.5, LocalDate.of(2023, 6, 15), "Perro amigable");

        assertNotNull(mascota);
        assertEquals(1L, mascota.id());
        assertEquals("Firulais", mascota.nombre());
        assertEquals(Especie.PERRO, mascota.especie());
        assertEquals("Labrador", mascota.raza());
        assertEquals(3, mascota.edad());
        assertEquals(25.5, mascota.peso());
        assertEquals(LocalDate.of(2023, 6, 15), mascota.fechaNacimiento());
        assertEquals("Perro amigable", mascota.descripcion());
    }

    @Test
    void recordsWithSameFieldsShouldBeEqual() {
        Mascota m1 = new Mascota(1L, "Firulais", Especie.PERRO, null, null, null, null, null);
        Mascota m2 = new Mascota(1L, "Firulais", Especie.PERRO, null, null, null, null, null);

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void recordsWithDifferentFieldsShouldNotBeEqual() {
        Mascota m1 = new Mascota(1L, "Firulais", Especie.PERRO, null, null, null, null, null);
        Mascota m2 = new Mascota(2L, "Mishi", Especie.GATO, null, null, null, null, null);

        assertEquals(1L, m1.id());
        assertEquals(2L, m2.id());
    }
}
