import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Class to implements index parsing from a html file, get the h2 - h6 as an
 * object with id and the text (content)
 *
 * @author Benediktus Devin
 * @version 1.2 (4/2/2015)
 */
public class IndexParsingTool {

    /**
     * Get the heads (h2 - h6) from file "filePath" (.html)
     *
     * @param filePath the path of the html file
     * @return the heads (array, 0 = h2, 1 = h3, 2 = h4, 3 = h5, 4 = h6) as an
     * array of Head type object(s)
     * @throws FileNotFoundException
     * @throws IOException
     */
    public LinkedList[] getHeads(String filePath) throws FileNotFoundException, IOException {
        BufferedReader r = new BufferedReader(new FileReader(new File(filePath)));//reader
        LinkedList<Head>[] heads = new LinkedList[5];//h2, h3, h4, h5, h6 sequentially
        int i, j;//counter
        for (i = 0; i < 5; i++) {
            heads[i] = new LinkedList<>();
        }
        String[] splitTag;//container for the splitted tag, index 1 is the id
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
                                    heads[0].add(new Head(text, id));
                                    break;
                                case "/h3":
                                    heads[1].add(new Head(text, id));
                                    break;
                                case "/h4":
                                    heads[2].add(new Head(text, id));
                                    break;
                                case "/h5":
                                    heads[3].add(new Head(text, id));
                                    break;
                                case "/h6":
                                    heads[4].add(new Head(text, id));
                                    break;
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
//        for (i = 0; i < heads.length; i++) {
//            if (heads[i].size() > 0) {
//                System.out.println("Head " + (i + 2));
//                for (j = 0; j < heads[i].size(); j++) {
//                    System.out.println("id = " + heads[i].get(j).getId() + ", Text = " + heads[i].get(i).getText());
//                }
//                System.out.println("-----------------------------");
//            }
//        }
        return heads;
    }

    /**
     * Class for a head in html, contains the text string and the id
     *
     * @author Benediktus Devin
     * @version 1 (4/2/2015)
     */
    class Head {

        /**
         * The head's text
         */
        private String text;
        /**
         * The head's id
         */
        private String id;

        /**
         * Constructor for the head
         *
         * @param text the text
         * @param id the id
         */
        public Head(String text, String id) {
            this.text = text;
            this.id = id;
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
}
