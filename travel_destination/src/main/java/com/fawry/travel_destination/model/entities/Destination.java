package com.fawry.travel_destination.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Builder
@Entity
@Data
@AllArgsConstructor
@Table(name = "destinations")
public class Destination {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String country;

    private String capital;

    private String region;

    private Long population;

    private String currency;

    private String flagUrl;

    @OneToMany(mappedBy = "destination", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    @lombok.Builder.Default
    private java.util.List<UserDestination> userDestinations = new java.util.ArrayList<>();

    public java.util.List<UserDestination> getUserDestinations() {
        return userDestinations;
    }

    public void setUserDestinations(java.util.List<UserDestination> userDestinations) {
        this.userDestinations = userDestinations;
    }

    public Destination() {}

    public Long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }


}
