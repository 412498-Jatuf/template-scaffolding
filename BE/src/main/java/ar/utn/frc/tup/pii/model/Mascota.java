package ar.utn.frc.tup.pii.model;

import ar.utn.frc.tup.pii.enums.Especie;

import java.time.LocalDate;

public record Mascota(
        Long id,
        String nombre,
        Especie especie,
        String raza,
        Integer edad,
        Double peso,
        LocalDate fechaNacimiento,
        String descripcion
) {}
