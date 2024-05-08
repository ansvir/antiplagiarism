package com.example.antiplagiarism.repository;

public interface IRepository<ID, T> {

    void save(ID id, T type);

    T get(ID id);

}
