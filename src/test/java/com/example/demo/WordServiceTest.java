package com.example.demo;


import com.example.demo.model.Word;
import com.example.demo.repository.WordRepository;
import com.example.demo.service.WordService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class WordServiceTest {

    @Mock
    WordRepository wordRepository;

    @InjectMocks
    private WordService wordService;

    Word word1 = new Word(1L, "Ramesh Babu");
    Word word2 = new Word(2L, "Spring framework");
    Word word3 = new Word(3L, "hibernate");
    Word word4 = new Word(4L, "spring boot");
    Word word5 = new Word(5L, "rest ful web services");
    Word word6 = new Word(6L, "mockito framwork");

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getALlWords_success() throws Exception {
        List<Word> words = new ArrayList<>(Arrays.asList(word1, word2, word3, word4, word5, word6));
        Mockito.when(wordRepository.findAll()).thenReturn(words);
        List<Word> listwords = wordService.findAll();
        Assert.assertEquals(listwords.size(),6);

    }

    @Test
    public void getWordByID_success() throws Exception {
        List<Word> words = new ArrayList<>(Arrays.asList(word1, word2, word3, word4, word5, word6));
        Mockito.when(wordRepository.findById(1L)).thenReturn(Optional.ofNullable(words.get(0)));
        Optional<Word> word = wordService.findById(1L);
        Assert.assertEquals(word.get().getWord(),"Ramesh Babu");

    }

    @Test
    public void createWord_success() throws Exception {
        List<Word> words = new ArrayList<>(Arrays.asList(word1, word2, word3, word4, word5, word6));
        Mockito.when(wordRepository.save(word1)).thenReturn(word1);

        Word word = wordService.save(word1);
        Assert.assertEquals(word.getWord(),"Ramesh Babu");

    }


}
