package com.project.linguaitalianaua.service;

import com.project.linguaitalianaua.model.Sentence;
import com.project.linguaitalianaua.model.Word;
import com.project.linguaitalianaua.repository.SentencesRepository;
import com.project.linguaitalianaua.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WordSentenceServiceTest {

    @Mock
    private WordRepository wordRepository;

    @Mock
    private SentencesRepository sentencesRepository;

    private WordService wordService;

    private SentenceService sentenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wordService = new WordService(wordRepository);
        sentenceService = new SentenceService(sentencesRepository);
    }

    @Test
    void findById_ShouldReturnWord_WhenIdExists() {
        Long id = 1L;
        Word word = new Word();
        word.setId(id);
        when(wordRepository.findById(id)).thenReturn(Optional.of(word));

        Word result = wordService.findById(id);

        assertEquals(word, result);
        verify(wordRepository, times(1)).findById(id);
    }

    @Test
    void findById_ShouldReturnSentence_WhenIdExists() {
        Long id = 1L;
        Sentence sentence = new Sentence();
        sentence.setId(id);
        when(sentencesRepository.findById(id)).thenReturn(Optional.of(sentence));

        Sentence result = sentenceService.findById(id);

        assertEquals(sentence, result);
        verify(sentencesRepository, times(1)).findById(id);
    }

    @Test
    void findById_ShouldReturnNull_WhenIdDoesNotExist() {
        Long id = 1L;
        when(wordRepository.findById(id)).thenReturn(Optional.empty());

        Word result = wordService.findById(id);

        assertNull(result);
        verify(wordRepository, times(1)).findById(id);
    }

    @Test
    void getRandomWord_ShouldReturnRandomWord_WhenWordsExist() {
        List<Word> words = new ArrayList<>();
        words.add(new Word());
        words.add(new Word());
        words.add(new Word());
        when(wordRepository.findAll()).thenReturn(words);
        Random random = new Random();
        int randomIndex = random.nextInt(words.size());

        Word result = wordService.getRandomWord();

        assertEquals(words.get(randomIndex), result);
        verify(wordRepository, times(1)).findAll();
    }

    @Test
    void getRandomSentence_ShouldReturnRandomSentence_WhenSentencesExist() {
        List<Sentence> sentences = new ArrayList<>();
        sentences.add(new Sentence());
        sentences.add(new Sentence());
        sentences.add(new Sentence());
        when(sentencesRepository.findAll()).thenReturn(sentences);
        Random random = new Random();
        int randomIndex = random.nextInt(sentences.size());

        Sentence result = sentenceService.getRandomSentence();

        assertNotEquals(sentences.get(randomIndex), result);
        verify(sentencesRepository, times(1)).findAll();
    }

    @Test
    void getRandomWord_ShouldReturnNull_WhenNoWordsExist() {
        when(wordRepository.findAll()).thenReturn(new ArrayList<>());

        Word result = wordService.getRandomWord();

        assertNull(result);
        verify(wordRepository, times(1)).findAll();
    }

    @Test
    void deleteWord_ShouldDeleteWord_WhenIdExists() {
        Long id = 1L;
        Word word = new Word();
        word.setId(id);

        when(wordRepository.findById(id)).thenReturn(Optional.of(word));
        doNothing().when(wordRepository).deleteById(id);

        wordService.delete(id);

        verify(wordRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteSentence_ShouldDeleteSentence_WhenIdExists() {
        Long id = 1L;
        Sentence sentence = new Sentence();
        sentence.setId(id);

        when(sentencesRepository.findById(id)).thenReturn(Optional.of(sentence));
        doNothing().when(sentencesRepository).deleteById(id);

        sentenceService.delete(id);

        verify(sentencesRepository, times(1)).deleteById(id);
    }

}
