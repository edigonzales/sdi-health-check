package ch.so.agi.healthcheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.so.agi.healthcheck.model.Run;

public interface RunRepository extends JpaRepository<Run, Long> {

}
