package ch.so.agi.healthcheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.so.agi.healthcheck.model.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

}
