package com.aharrar.cinema.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.aharrar.cinema.entities.Seance;

@RepositoryRestResource
public interface SeanceRepository extends JpaRepository<Seance,Long>{

}
