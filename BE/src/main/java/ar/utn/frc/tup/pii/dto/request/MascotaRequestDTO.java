package ar.utn.frc.tup.pii.dto.request;

import ar.utn.frc.tup.pii.enums.Especie;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MascotaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotNull(message = "La especie es obligatoria")
    private Especie especie;

    @Size(max = 100, message = "La raza no puede superar los 100 caracteres")
    private String raza;

    @Min(value = 0, message = "La edad no puede ser negativa")
    private Integer edad;

    @DecimalMin(value = "0.0", message = "El peso no puede ser negativo")
    private Double peso;

    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    private LocalDate fechaNacimiento;

    @Size(max = 500, message = "La descripcion no puede superar los 500 caracteres")
    private String descripcion;
}
