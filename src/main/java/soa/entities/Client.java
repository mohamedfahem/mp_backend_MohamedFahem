package soa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import soa.entities.Facture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String numero;
    private String adresse;
    private boolean actif;
    @Transient
    private BigDecimal chiffreAffaires;
    @Temporal(TemporalType.DATE)
    private Date dateInscription;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Facture> factures;

    // Constructeur par défaut pour JPA
    public Client() {
    }

    // Constructeur avec initialisation des listes
    public Client(String nom, String prenom, String numero, String adresse, Date dateInscription) {
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        this.adresse = adresse;
        this.dateInscription = dateInscription;
        this.factures = new ArrayList<>();
        this.chiffreAffaires = BigDecimal.ZERO;
        this.actif = false;
    }

    // Getters et Setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<Facture> getFactures() {
        return factures;
    }

    public void setFactures(List<Facture> factures) {
        this.factures = factures;
        updateChiffreAffaires();
    }

    public void addFacture(Facture facture) {
        factures.add(facture);
        updateChiffreAffaires();
    }

    public void removeFacture(Facture facture) {
        factures.remove(facture);
        updateChiffreAffaires();
    }

    public BigDecimal getChiffreAffaires() {
        return chiffreAffaires;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    // Méthode pour mettre à jour le chiffre d'affaires et l'état d'activité
    public void updateChiffreAffaires() {
        if (factures != null && !factures.isEmpty()) {
            Facture derniereFacture = factures.get(factures.size() - 1);
            actif = isFactureDansDerniereAnnee(derniereFacture);
        } else {
            actif = false;
        }

        // Mettez à jour le chiffre d'affaires
        if (factures != null) {
            chiffreAffaires = factures.stream()
                    .map(Facture::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            chiffreAffaires = BigDecimal.ZERO;
        }
    }

    // Méthode auxiliaire pour vérifier si la facture a été émise au cours de la dernière année
    private boolean isFactureDansDerniereAnnee(Facture facture) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1); // Soustraire une année à la date actuelle

        Date derniereAnnee = calendar.getTime();
        return facture.getDateFacturation().after(derniereAnnee);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", numero='" + numero + '\'' +
                ", adresse='" + adresse + '\'' +
                ", dateInscription=" + dateInscription +
                ", factures=" + factures +
                ", chiffreAffaires=" + chiffreAffaires +
                ", actif=" + actif +
                '}';
    }
}
