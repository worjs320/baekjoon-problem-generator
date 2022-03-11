package com.pg.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ProblemParser {
    String problemNum;
    String url;

    public ProblemParser(String problemNum) {
        this.problemNum = problemNum;
        this.url = "https://www.acmicpc.net/problem/" + problemNum;
    }

    public void generateMdFile() throws IOException {
        Document doc = Jsoup.connect(url).get();

        String title = doc.select("#problem_title").text();
        Elements description = doc.select("#problem_description *");
        Elements input = doc.select("#problem_input p");
        Elements output = doc.select("#problem_output p");
        Elements example = doc.select("[id*=sampleinput], [id*=sampleoutput]");

        String filePath = System.getProperty("user.home") + "/Desktop/READEME.md";
        File file = new File(filePath);
        file.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(getFormattedTitle(title));
        writer.newLine();
        writer.write(getFormattedDescription(description));
        writer.newLine();
        writer.write(getFormattedInput(input));
        writer.newLine();
        writer.write(getFormattedOutput(output));
        writer.newLine();
        writer.write(getFormattedExample(example));

        writer.flush();
        writer.close();
    }

    public static String getFormattedTitle(String title) {
        return "# " + title + "\n";
    }

    public static String getFormattedDescription(Elements problem) {
        StringBuilder problemSb = new StringBuilder();
        problemSb.append("## 문제").append("\n\n");
        for (Element element : problem) {
            if (element.tagName().equals("p") && !element.text().equals("")) {
                problemSb.append(element.text()).append("\n");
            } else if (element.tagName().equals("img")) {
                problemSb.append("![]").append(element.absUrl("src")).append("\n");
            } else if (element.tagName().equals("pre")) {
                problemSb.append("```")
                        .append("\n")
                        .append(element.select("pre").textNodes().get(0))
                        .append("\n")
                        .append("```")
                        .append("\n");
            }
        }
        return problemSb.toString();
    }

    public static String getFormattedInput(Elements input) {
        StringBuilder inputSb = new StringBuilder();
        inputSb.append("## 입력").append("\n\n");
        for (Element element : input) {
            if (element.tagName().equals("p")) {
                inputSb.append(element.text()).append("\n");
            }
        }
        return inputSb.toString();
    }

    public static String getFormattedOutput(Elements output) {
        StringBuilder outputSb = new StringBuilder();
        outputSb.append("## 출력").append("\n\n");
        for (Element element : output) {
            if (element.tagName().equals("p")) {
                outputSb.append(element.text()).append("\n");
            }
        }
        return outputSb.toString();
    }

    public static String getFormattedExample(Elements example) {
        StringBuilder exampleSb = new StringBuilder();
        for (Element element : example) {
            exampleSb.append("### ").append(element.select(".headline > h2").textNodes().get(0))
                    .append("\n\n")
                    .append("```")
                    .append("\n")
                    .append(element.select("pre").text())
                    .append("\n")
                    .append("```")
                    .append("\n\n");
        }
        return exampleSb.toString();
    }
}
