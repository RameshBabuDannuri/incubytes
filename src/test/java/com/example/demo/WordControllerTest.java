package com.example.demo;


import com.example.demo.controller.WordController;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Word;
import com.example.demo.service.WordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class WordControllerTest {

   private MockMvc mockMvc;

   ObjectMapper objectMapper= new ObjectMapper();
   ObjectWriter objectWriter = objectMapper.writer();

   @Mock
    WordService wordService;

   @InjectMocks
   private WordController wordController;

    Word word1 = new Word(1L,"Ramesh Babu");
    Word word2 = new Word(2L,"Spring framework");
    Word word3 = new Word(3L,"hibernate");
    Word word4 = new Word(4L,"spring boot");
    Word word5 = new Word(5L,"rest ful web services");
    Word word6 = new Word(6L,"mockito framwork");

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(wordController).build();
    }

    @Test
    public void getALlWords_success() throws Exception{
        List<Word> words = new ArrayList<>(Arrays.asList(word1,word2,word3,word4,word5,word6));

        Mockito.when(wordService.findAll()).thenReturn(words);

        mockMvc.perform
                        (MockMvcRequestBuilders
                .get("/api/v1/words")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].word",is("Spring framework")));


    }

    @Test
    public void getWordById_Success()  throws Exception{

        Mockito.when(wordService.findById(word1.getId())).thenReturn(Optional.ofNullable(word1));
        mockMvc.perform
                        (MockMvcRequestBuilders
                                .get("/api/v1/words/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.word",is("Ramesh Babu")));
    }

    @Test
    public void getWordBy_Fail() throws Exception{


        mockMvc.perform
                        (MockMvcRequestBuilders
                                .get("/api/v1/words/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Word not found for this id :: "+1, result.getResolvedException().getMessage()));


    }
    @Test
    public void createWord_Success() throws Exception{
        Word word = Word.builder()
                .id(1L)
                .word("spring data jpa")
                .build();
        Mockito.when(wordService.save(word)).thenReturn(word);

        String content   = objectWriter.writeValueAsString(word);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                                .post("/api/v1/words")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
                mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.word",is("spring data jpa")));

    }

    @Test
    public void updateWord_Success() throws Exception {
        Word word = Word.builder()
                .id(1L)
                .word("update spring data jpa")
                .build();
        Mockito.when(wordService.findById(word1.getId())).thenReturn(Optional.ofNullable(word1));
        Mockito.when(wordService.save(word)).thenReturn(word);

        String content   = objectWriter.writeValueAsString(word);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put( "/api/v1/words/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.word",is("update spring data jpa")));
    }

    @Test
    public void updateWord_Fail() throws Exception{

        Word word = Word.builder()
                .id(10L)
                .word("update spring data jpa")
                .build();

        String content   = objectWriter.writeValueAsString(word);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put( "/api/v1/words/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Word not found for this id :: "+word.getId(), result.getResolvedException().getMessage()));


    }
    @Test
    public void deleteWord_success() throws Exception{

        Mockito.when(wordService.findById(word1.getId())).thenReturn(Optional.ofNullable(word1));

        mockMvc.perform
                        (MockMvcRequestBuilders
                                .delete("/api/v1/words/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    @Test
    public void deleteWord_Fail() throws Exception{

        mockMvc.perform
                        (MockMvcRequestBuilders
                                .delete("/api/v1/words/7")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Word not found for this id :: 7", result.getResolvedException().getMessage()));


    }


}
