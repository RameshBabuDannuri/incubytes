package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Word;
import com.example.demo.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class WordController {
    @Autowired
    private WordService service;

    @GetMapping("/words")
    public List<Word> getAllWords() {
        return service.findAll();
    }

    @GetMapping("/words/{id}")
    public ResponseEntity<Word> getWordById(@PathVariable(value = "id") Long wordId)
            throws ResourceNotFoundException {
        Optional<Word> optionalWord =  service.findById(wordId);

        if( !optionalWord.isPresent()) {
            throw  new ResourceNotFoundException("Word not found for this id :: " + wordId);
        }
        return ResponseEntity.ok().body(optionalWord.get());
    }

    @PostMapping("/words")
    public Word createWord(@Valid @RequestBody Word word) {
        return service.save(word);
    }

    @PutMapping("/words/")
    public ResponseEntity<Word> updateWord(@Valid @RequestBody Word wordDetails) throws ResourceNotFoundException {

        Optional<Word> optionalWord =  service.findById(wordDetails.getId());

        if( !optionalWord.isPresent()) {
            throw  new ResourceNotFoundException("Word not found for this id :: " + wordDetails.getId());
        }
        optionalWord.get().setWord(wordDetails.getWord());
        final Word updateWord = service.save(optionalWord.get());
        return ResponseEntity.ok(updateWord);
    }

    @DeleteMapping("/words/{id}")
    public Map<String, Boolean> deleteWord(@PathVariable(value = "id") Long wordId)
            throws ResourceNotFoundException {
        Word word = (Word) service.findById(wordId)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found for this id :: " + wordId));

        service.delete(word);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
