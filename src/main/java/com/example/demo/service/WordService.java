package com.example.demo.service;

import com.example.demo.model.Word;
import com.example.demo.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WordService {
    @Autowired
    private WordRepository repository;

    public List<Word> findAll() {
       return  repository.findAll();
    }

    public Optional<Word> findById(Long wordId) {
        return repository.findById(wordId);
    }

    public void delete(Word word) {
        repository.delete(word);
    }

    public Word save(Word word) {
        return repository.save(word);
    }
}
