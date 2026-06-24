package ar.utn.frc.tup.pii.dto.response;

import ar.utn.frc.tup.pii.enums.Especie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MascotaResponseDTO {

    private Long id;
    private String nombre;
    private Especie especie;
    private String raza;
    private Integer edad;
    private Double peso;
    private LocalDate fechaNacimiento;
    private String descripcion;
}
