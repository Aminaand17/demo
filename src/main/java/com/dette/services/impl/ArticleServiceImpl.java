package com.dette.services.impl;
import java.util.List;

import com.dette.entities.Article;
import com.dette.repository.ArticleRepository;
import com.dette.repository.DetteRepository;
import com.dette.repository.list.ArticleRepositoryList;
import com.dette.services.ArticleService;

public class ArticleServiceImpl implements ArticleService{

      private ArticleRepository articleRepository ;


    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
  

    public void create(Article article) {
        articleRepository.insert(article);
    }

    public List<Article> findAll() {
        return articleRepository.selectAll();
    }

    public void updateStock(String nom, int qte_stock) {
        articleRepository.updateStock(nom, qte_stock);
    }

    public Article getArticleByNom(String nom) {
        for (Article article : findAll()) {
            if (article.getNom().equalsIgnoreCase(nom)) {
                return article;
            }
        }
        return null; 
    }
    
    
}
