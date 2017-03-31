package com.app.maththpt.utils;

/**
 * Created by manhi on 12/1/2017.
 */

public class MathUtils {
    private String doan1 = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Title</title>\n" +
            "    <script type=\"text/javascript\" async\n" +
            "            src=\"mathjax/2.7-latest/MathJax.js?config=MML_CHTML\">\n" +
            "    </script>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div class=\"cauhoi\">\n" +
            "    <div class=\"cauhoi_text\">";

    public String question = "";

    private String doan2 = "</div>\n" +
            "    <div class=\"cauhoi_anh\">";

    private String doan2_1 = "<img width=\"100%\" src='";
    private String doan2_2 = "<img src='";

    public String image = "";

    private String doan3 = "'/>" +
            "</div>\n" +
            "</div>\n" +
            "<div class=\"dapan\">\n" +
            "    <div id=\"answer_1\">\n" +
            "        <input type=\"radio\" name=\"answer\" value=\"1\">" +
            "<b>A. </b>";


    public String answer1 = "";

    private String doan4 = "</div>\n" +
            "\n" +
            "    <div id=\"answer_2\">\n" +
            "        <input type=\"radio\" name=\"answer\" value=\"2\">" +
            "<b>B. </b>";

    public String answer2 = "";

    private String doan5 = "</div>\n" +
            "    <div id=\"answer_3\">\n" +
            "        <input type=\"radio\" name=\"answer\" value=\"3\">" +
            "<b>C. </b>";

    public String answer3 = "";

    private String doan6 = "</div>\n" +
            "    <div id=\"answer_4\">\n" +
            "        <input type=\"radio\" name=\"answer\" value=\"4\">" +
            "<b>D. </b>";

    public String answer4 = "";

    private String doan7 = "</div>\n" +
            "\n" +
            "\n" +
            "</div>\n" +
            "\n" +
            "<script>\n" +
            "    function checkAnswer() {\n" +
            "        var radios = document.getElementsByName('answer');\n" +
            "        var count = -1;\n" +
            "        for (var i = 0, length = radios.length; i < length; i++) {\n" +
            "            if (radios[i].checked) {\n" +
            "                // do whatever you want with the checked radio\n" +
            "                count = i;\n" +
            "//                return (radios[i].value);\n" +
            "            }\n" +
            "        }\n" +
            "        return count;\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "    function setColor(_select, _true) {\n _dung = parseInt(_true);" +
            "        for (var i = 1; i <= 4; i++) {\n" +
            "            document.getElementById('answer_' + i).style.backgroundColor = 'white';\n" +
            "        }\n" +
            "        _select = parseInt(checkAnswer()) + 1;\n" +
            "        if (_select == _true) {\n" +
            "            document.getElementById('answer_' + _dung).style.backgroundColor = '#2ecc71';\n" +
            "        } else {\n if (_select > 0)" +
            "            document.getElementById('answer_' + _select).style.backgroundColor = '#e74c3c';\n" +
            "            document.getElementById('answer_' + _dung).style.backgroundColor = '#2ecc71';\n" +
            "\n" +
            "        }\n" +
            "    }\n" +
            "   function resetColor() {\n" +
            "        for (var i = 1; i <= 4; i++) {\n" +
            "            document.getElementById('answer_' + i).style.backgroundColor = 'white';\n" +
            "        }\n" +
            "    }" +
            "</script>\n" +
            "<script>\n" +
            "    var rad = document.getElementsByName('answer');\n" +
            "    var prev = null;\n" +
            "    for (var i = 0; i < rad.length; i++) {\n" +
            "        rad[i].onclick = function () {\n" +
            "            (prev) ? console.log(prev.value) : null;\n" +
            "            if (this !== prev) {\n" +
            "                prev = this;\n" +
            "            }\n" +
            "//            console.log(this.value)\n" +
            "            Android.storeText(this.value);\n" +
            "        };\n" +
            "    }\n" +
            "</script>\n" +
            "</body>\n" +
            "</html>";

    public String htmlContain() {
        if (image == null || image.trim().isEmpty() || !image.trim().startsWith("data")) {
            return doan1 + question + doan2 + doan2_2 + image + doan3
                    + answer1 + doan4 + answer2 + doan5 + answer3 + doan6 + answer4 + doan7;
        } else {
            return doan1 + question + doan2 + doan2_1 + image + doan3
                    + answer1 + doan4 + answer2 + doan5 + answer3 + doan6 + answer4 + doan7;
        }
    }

    public String htmlContain(
            String question, String image, String answer1,
            String answer2, String answer3, String answer4) {
        return doan1 + question + doan2 + image + doan3
                + answer1 + doan4 + answer2 + doan5 + answer3 + doan6 + answer4 + doan7;
    }
}
