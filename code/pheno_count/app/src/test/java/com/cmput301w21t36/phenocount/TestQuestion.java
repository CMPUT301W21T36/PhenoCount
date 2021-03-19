package com.cmput301w21t36.phenocount;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestQuestion {

    private Question mockQuestion() {
        Question mockQue = new Question("What's your name?");
        return mockQue;
    }

    @Test
    void testGetText() {
        Question que = mockQuestion();

        assertEquals("What's your name?", que.getText());

        Question newQue = new Question("How old are you?");
        assertEquals("How old are you?", newQue.getText());
    }

    @Test
    void testSetID() {
        Question que = mockQuestion();
        que.setID("412");
        assertEquals("412", que.getID());
    }


    @Test
    void testGetID() {
        Question que = mockQuestion();
        //id is initialized as an empty string
        assertEquals("", que.getID());
        que.setID("333");
        assertEquals("333", que.getID());
    }



}