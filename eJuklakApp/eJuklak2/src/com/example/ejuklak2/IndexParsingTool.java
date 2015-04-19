package com.example.ejuklak2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import android.content.Context;

/**
 * Class to implements index parsing from a html file, get the h2 - h6 as an
 * object with id and the text (content)
 *
 * @author Benediktus Devin
 * @version 1.2 (4/2/2015)
 */
public class IndexParsingTool {

    /**
     * The head for Bab
     */
    private LinkedList<Head> heads = new LinkedList<>();

    public IndexParsingTool(Context context, String fileName) throws FileNotFoundException, IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));//reader
    	init(r);
    }
    
    /**
     * Get the heads (h2 - h6) from file "filePath" (.html)
     *
     * @param filePath the path of the html file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void init(BufferedReader r) throws FileNotFoundException, IOException {
        int i, j;//counter
        int bab = 0, subbab1 = 0, subbab2 = 0;
        String[] splitTag;//container for the splitted tag, index 1 is the id
        String[] splitText;//container for the splitted text, front index element is used to determine the level
        String[] splitText2;//container for the splitted text, front index element is used to determine the level
        String input = new String(), tag = new String(), text = new String(), id = new String();
        boolean isTag = false, isHead = false;
        input = r.readLine();
        //get per line, stop ONLY at the end of file
        while (input != null) {
            //check per line
            for (i = 0; i < input.length(); i++) {
                if (isTag) {
                    if (input.charAt(i) == '>') {
                        isTag = false;//end of tag
                        //check tag's content
                        if (tag.startsWith("h1") || tag.startsWith("h2") || tag.startsWith("h3") || tag.startsWith("h4") || tag.startsWith("h5") || tag.startsWith("h6")) {
//                            System.out.println(tag);
                            isHead = true;//the next char(s) is a head element
                            splitTag = tag.split("\"");
                            id = splitTag[1];//get the id from the tag
                        } else if (tag.startsWith("/h1") || tag.startsWith("/h2") || tag.startsWith("/h3") || tag.startsWith("/h4") || tag.startsWith("/h5") || tag.startsWith("/h6")) {
                            switch (tag) {
                                case "/h2":
                                    //if not bab, such as Kata Pengantar
                                    if (text.startsWith("Bab") || text.startsWith("BAB")) {
                                        heads.add(new Head(text, id));
                                        bab++;
                                    } else {
                                        heads.add(new Head(text, id));
                                    }
                                    break;
                                case "/h3":
                                    subbab1 = (text.charAt(2) - 49);
                                    heads.get(bab).child.add(new Head(text, id));
                                    break;
                                case "/h4":
//                                    subbab2 = (text.charAt(4) - 49);
//                                    System.out.println(subbab2);
                                    heads.get(bab).child.get(subbab1).child.add(new Head(text, id));
                                    break;
//                                case "/h5":
//                                    heads.add(new Head(text, id));
//                                    break;
//                                case "/h6":
//                                    heads.add(new Head(text, id));
//                                    break;
                            }
                            text = new String();
                            id = new String();
                            isHead = false;
                        }
                        tag = new String();
                    } else {
                        tag += input.charAt(i) + "";//still in tag, get the tag
                    }
                } else if (isHead) {
                    if (input.charAt(i) != '<') {
                        text += input.charAt(i) + "";
                    } else {
                        isTag = true;//there is a tag inside the head
                    }
                }//if neither, check if it is going to be a tag, a head element, or neither
                if (input.charAt(i) == '<') {
                    //skip comments
                    if (input.charAt(i + 1) == '!') {
                        i = input.length();
                    } else {
                        isTag = true;//start getting the tag
                    }
                }
            }
            input = r.readLine();//get the next line
        }
        //uncomment to check content of the head(s)
//        for (i = 1; i < heads.size(); i++) {
//            System.out.println("BAB " + i);
////            System.out.print("Id : " + heads.get(i).getId());
//            System.out.println("Text : " + heads.get(i).getText());
//            for (j = 0; j < heads.get(i).child.size(); j++) {
////                System.out.print("Id : " + heads.get(i).child.get(j).getId());
//                System.out.println("Text : " + heads.get(i).child.get(j).getText());
//                //print subbab12
//                for (int a = 0; a < heads.get(i).child.get(j).child.size(); a++) {
////                    System.out.print("Id : " + heads.get(i).child.get(j).child.get(a).getId());
//                    System.out.println("Text : " + heads.get(i).child.get(j).child.get(a).getText());
//                }
//            }
//            System.out.println("-------------------------------------------------------------------------------------------------");
//        }
        //uncommnet to check the size of the heads
//        for(int a = 1; a < heads.size(); a++){
//            System.out.println("Bab : " + a);
//            System.out.println("Banyak subbab1 : " + heads.get(a).child.size());
//            for(int b = 0; b < heads.get(a).child.size(); b++){
//                System.out.println("Banyak subbab2 : " + heads.get(a).child.get(b).child.size());
//            }
//        }
    }

    public LinkedList<Head> getHead() {
    	return this.heads;
    }
    
    /**
     * Get Bab, start from index 1 for bab1
     *
     * @param bab the bab index
     * @return the bab
     */
    public Head getBab(int bab) {
        return heads.get(bab);
    }

    /**
     * Get subbab1, bab from 1, subbab1 from 1
     *
     * @param bab the bab index
     * @param subbab1 the subbab index
     * @return the subbab
     */
    public Head getSubbabLev1(int bab, int subbab1) {
        return heads.get(bab).child.get(subbab1 - 1);
    }

    /**
     * Get subbab2, bab from 1, subbab1 from 1, subbab2 from 1
     *
     * @param bab the bab index
     * @param subbab1 the subbab1 index
     * @param subbab2 the subbab2 index
     * @return the subbab2
     */
    public Head getSubbabLev2(int bab, int subbab1, int subbab2) {
        return heads.get(bab).child.get(subbab1 - 1).child.get(subbab2 - 1);
    }

}
