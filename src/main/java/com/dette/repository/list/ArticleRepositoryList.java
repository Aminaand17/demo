package com.dette.repository.list;
import com.dette.entities.Article;

public class ArticleRepositoryList extends RepositoryListImpl<Article> {

    public Article selectByName(String nom) {
        return list.stream()
            .filter(article -> article.getNom().equals(nom))
            .findFirst()
            .orElse(null);
    }

    public void updateStock(String nom, int qteStock) {
        Article article = selectByName(nom);
        if (article != null) {
            article.setQteStock(qteStock);
        }
    }
}
