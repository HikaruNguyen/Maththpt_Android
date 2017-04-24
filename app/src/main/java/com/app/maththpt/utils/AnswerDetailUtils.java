package com.app.maththpt.utils;

/**
 * Created by manhi on 12/1/2017.
 */

public class AnswerDetailUtils {
    private String doan1 = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Title</title>\n" +
            "    <script type=\"text/javascript\" async\n" +
            "            src=\"https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=MML_CHTML\">\n" +
            "    </script>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div class=\"cauhoi\">";

    public String answerDetail = "";

    private String doan2 = "</div>\n" +
            "</body>\n" +
            "</html>";

    public String htmlContain(String answerDetail) {
        return doan1 + answerDetail + doan2;
    }

}
