package com.dette.repository.list;

import com.dette.entities.Client;
import com.dette.entities.Dette;
import com.dette.repository.DetteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DetteRepositoryList extends RepositoryListImpl<Dette> implements DetteRepository {

    @Override
    public List<Dette> listerDettesNonSoldees() {
        return selectAll().stream()
                .filter(dette -> dette.getMontantRestant() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public Dette findById(int id) {
        return selectAll().stream()
                .filter(dette -> dette.getId() == id)
                .findFirst()
                .orElse(null); 
    }

    @Override
    public void insert(Dette dette, String telephone) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void enregistrerPaiement(Dette dette, double montantPaiement) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'enregistrerPaiement'");
    }

    @Override
    public void validerRefuserDette(int idDette, boolean valider) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validerRefuserDette'");
    }

    @Override
    public List<Dette> listerDettesEnCours() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listerDettesEnCours'");
    }

    @Override
    public void afficherArticlesDette(int detteId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afficherArticlesDette'");
    }



    @Override
    public void faireDemandeDette(int clientId, double montant) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'faireDemandeDette'");
    }

    @Override
    public void afficherPaiementsDette(int detteId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afficherPaiementsDette'");
    }

    @Override
    public void listerDettesNonSoldees(int clientId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listerDettesNonSoldees'");
    }

    // @Override
    // public Dette findById(int id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findById'");
    // }

     
   

}
