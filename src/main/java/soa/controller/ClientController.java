package soa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soa.entities.Client;
import soa.repository.ClientRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Client> getClientById(@PathVariable Long id) {
        return clientRepository.findById(id);
    }

    @PostMapping
    public Client saveClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @GetMapping(value = "/delete/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client updatedClient) {
        Optional<Client> optionalClient = clientRepository.findById(id);

        if (optionalClient.isPresent()) {
            Client existingClient = optionalClient.get();

            // Mettez à jour les propriétés du client existant avec les nouvelles valeurs
            existingClient.setNom(updatedClient.getNom());
            existingClient.setPrenom(updatedClient.getPrenom());
            existingClient.setNumero(updatedClient.getNumero());
            existingClient.setAdresse(updatedClient.getAdresse());
            existingClient.setDateInscription(updatedClient.getDateInscription());

            // Enregistrez les modifications dans la base de données
            clientRepository.save(existingClient);

            return new ResponseEntity<>(existingClient, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Client>> getClientsByNom(@RequestParam String nom) {
        List<Client> matchingClients = clientRepository.findByNom(nom);

        if (!matchingClients.isEmpty()) {
            return new ResponseEntity<>(matchingClients, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/chiffreAffaires")
    public List<Client> getClientChiffreAffaires() {
        List<Client> clients = clientRepository.findAll();
        for (Client client : clients) {
            client.updateChiffreAffaires(); // Ensure chiffreAffaires is up-to-date
        }
        return clients;
    }

    @GetMapping("/{id}/chiffreAffaires")
    public ResponseEntity<Client> getClientChiffreAffairesById(@PathVariable Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            client.updateChiffreAffaires();
            return new ResponseEntity<>(client, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/statutActivite")
    public ResponseEntity<String> getClientStatutActivite(@PathVariable Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            client.updateChiffreAffaires(); // Assurez-vous que le chiffre d'affaires est à jour
            String statutActivite = client.isActif() ? "Actif" : "Inactif";
            return new ResponseEntity<>(statutActivite, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
