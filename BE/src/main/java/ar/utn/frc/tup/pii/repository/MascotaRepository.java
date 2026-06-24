package ar.utn.frc.tup.pii.repository;

import ar.utn.frc.tup.pii.entity.MascotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MascotaRepository extends JpaRepository<MascotaEntity, Long> {
}
