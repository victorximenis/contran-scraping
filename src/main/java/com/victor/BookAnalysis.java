package com.victor;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookAnalysis {

    private String regexPattern = "((Lei(\\.)? )|(Res(\\.)? ))(\\d{1,2}\\.)?\\d{1,3}\\/\\d{1,2}";

    public List<String> extractLawsAndResolutions(String text){

        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(text);

        List<String> results = new ArrayList<String>();
        while (matcher.find()) {
            results.add(matcher.group());
        }
        return results;
    }

    public Map<Integer, List<String>> readPdfFile(String path) {
        Map<Integer, List<String>> analise = new HashMap<Integer, List<String>>();
        File file = new File(path);
        PDDocument document = null;
        try {
            document = PDDocument.load(file);
            Splitter splitter = new Splitter();

            List<PDDocument> Pages = splitter.split(document);

            Iterator<PDDocument> iterator = Pages.listIterator();

            int i = 1;
            while(iterator.hasNext()) {
                PDDocument pd = iterator.next();
                PDFTextStripper tStripper = new PDFTextStripper();
                String pdfFileInText = tStripper.getText(pd);

                analise.put(i, extractLawsAndResolutions(pdfFileInText));
                pd.close();
                i++;
            }
            document.close();
            return analise;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
