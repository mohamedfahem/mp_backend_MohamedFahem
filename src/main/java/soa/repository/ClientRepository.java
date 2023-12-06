package soa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soa.entities.Client;

import java.util.Date;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByNom(String nom);


}
