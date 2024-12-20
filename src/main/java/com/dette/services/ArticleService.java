package com.dette.services;

import java.util.List;

import com.dette.entities.Article;

public interface ArticleService {

    void create(Article article);
    List<Article> findAll();
    void updateStock(String nom, int qte_stock);
    Article getArticleByNom(String nom);

    
}
