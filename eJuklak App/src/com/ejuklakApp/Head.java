package com.ejuklakApp;

import java.util.LinkedList;


/**
 * Class for a head in html, contains the text string and the id
 *
 * @author Benediktus Devin
 * @version 1 (4/2/2015)
 */
public class Head {

    /**
     * The head's text
     */
    private String text;
    /**
     * The head's id
     */
    private String id;
    /**
     * The head's child (such as 1.1, 1.1.1)
     */
    LinkedList<Head> child;

    /**
     * Constructor for the head
     *
     * @param text the text
     * @param id the id
     */
    public Head(String text, String id) {
        this.text = text;
        this.id = id;
        this.child = new LinkedList<>();
    }

    /**
     * Get the head's id
     *
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get the head's text
     *
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Set the head's id
     *
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set the head's text
     *
     * @param text the new text
     */
    public void setText(String text) {
        this.text = text;
    }
}
