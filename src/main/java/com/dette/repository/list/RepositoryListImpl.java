package com.dette.repository.list;
import java.util.ArrayList;
import java.util.List;


import com.dette.core.repository.Repository;



public class RepositoryListImpl<T> implements Repository<T>{
     protected List<T>list = new ArrayList<>();

    @Override
    public void insert(T data ) {
        list.add(data);
    }

    @Override
    public List<T>selectAll(){
        return list;
    }



}
