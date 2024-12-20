package com.dette.repository;
import java.util.List;
import com.dette.core.repository.Repository;
import com.dette.entities.Article;

public interface ArticleRepository extends Repository<Article>{
     void insert(Article article) ;
     List<Article> selectAll();
     void updateStock(String nom, int qte_stock);

    
} 


