package edu.estu;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class App {


    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            // This program must have at least one command-line
            // argument to work with.
            System.out.println("The application expects an input file name.");
            return;
        }

        MyOptions bean = new MyOptions();
        CmdLineParser parser = new CmdLineParser(bean);
        try{
            parser.parseArgument(args);
        }catch (CmdLineException cmdLineException){
            System.err.println(cmdLineException.getMessage());
            return;
        }
        List<String> filenames = bean.filenames;
        ArrayList<Path> paths = new ArrayList<>();

        for (String filename : filenames) {
            paths.add(Paths.get(filename));
        }
        List<String> finalLLines = new ArrayList<>();
        for (Path path : paths) {
            if (Files.notExists(path)) {
                System.out.println("The file you entered does not exist!");
            }
            List<String> lines;
            try {
                lines=Files.readAllLines(path,StandardCharsets.UTF_8);
            }catch (IOException ioException){
                ioException.printStackTrace();
                return;
            }
            finalLLines.addAll(lines);
        }
        List<String> text = read(finalLLines);
        if(bean.unique){
            unique(text);
        }
        if(bean.reverse){
            reverse(text);
        }
        switch (bean.task){
            case "NumOfTokens" :
                if(bean.unique){
                    System.out.println("Number of Tokens: " + NumOfTokens(unique(text)));
                }
                else{
                    System.out.println("Number of Tokens: " + NumOfTokens(text));
                }
                break;
            case "FrequentTerms" :
                if(bean.reverse){
                    printMap(reverseMap(FrequentTerms(text)), bean.num);
                }
                else {
                    printMap(FrequentTerms(text), bean.num);
                }
                break;
            case "TermLengthStats" :
                if(bean.unique){
                    System.out.println(TermLengthStats(unique(text)));
                }
                else{
                    System.out.println(TermLengthStats(text));
                }
                break;
            case "TermsStartWith" :
                if(bean.reverse){
                    print(TermsStartWith(reverse(unique(text)), bean.startsWith), bean.num);
                }
                else {
                    print(TermsStartWith(unique(text),bean.startsWith), bean.num);
                }
                break;
            default:
        }














    }
    public static List<String> read(List<String> list){
        ArrayList<String> resultList = new ArrayList<>();
        StringTokenizer tokenizer;

        for (String string : list) {
            tokenizer = new StringTokenizer(string);

            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                token = token.toLowerCase(Locale.ROOT);
                token = token.trim();

                String[] tempList = token.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");

                ArrayList<String> midList = new ArrayList<>();

                for (String str : tempList) {
                    if (!str.equals("")) {
                        midList.add(str);
                    }
                }

                resultList.addAll(midList);
            }
        }

        Collections.sort(resultList);

        return resultList;
    }

    public static int NumOfTokens(List<String> list){
        return list.size();
    }


    public static Map<String,Integer> FrequentTerms(List<String> list){
        Map<String,Integer> result = new HashMap<>();


        for (String string : list) {
            if (!result.containsKey(string)) {
                result.put(string, 1);
            } else {
                result.put(string, result.get(string) + 1);
            }
        }
        return sortByValues(result,false);

    }

    public static Map<String,Integer> sortByValues(Map<String,Integer> map, boolean isReverse){
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if (!isReverse) {
                    if (!o2.getValue().equals(o1.getValue())) {
                        return (o2.getValue()).compareTo(o1.getValue());
                    } else {
                        return (o1.getKey().compareTo(o2.getKey()));
                    }
                } else {
                    if (!o2.getValue().equals(o1.getValue())) {
                        return (o1.getValue()).compareTo(o2.getValue());
                    } else {
                        return (o1.getKey().compareTo(o2.getKey()));
                    }
                }
            }
        });

        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> result : list) {
            temp.put(result.getKey(), result.getValue());
        }
        return temp;
    }
    public static String TermLengthStats(List<String> list){
        int max = 1;
        int min = 100;
        double average;
        double sum=0;
        for(String string: list){
            int length = string.length();
            sum += length;
            if(length<min) min=length;
            if(length>max) max=length;
        }
        average = sum/list.size();
        return "Max Token Length in Character: " + max + " Min Token Length: " + min + " Average Token Length: " + average;

    }
    public static List<String> TermsStartWith(List<String> list,String start){
        List<String> lastList = new ArrayList<>();
        for(String string: list){
            if(string.startsWith(start)){
                lastList.add(string);
            }
        }
        return lastList;
    }

    public static List<String> reverse(List<String> list){
        Collections.reverse(list);
        return list;
    }
    public static Map<String,Integer> reverseMap(Map<String,Integer> map){
        return sortByValues(map,true);
    }

    public static List<String> unique(List<String> list){
        List<String> lastList = new ArrayList<>(new HashSet<>(list));
        Collections.sort(lastList);
        return lastList;
    }
    public static void print(List<String> list, int count){
        for(String string: list){
            System.out.println(string + " ");
            count--;
            if(count<=0) break;
        }
    }
    public static void printMap(Map<String,Integer> map,int count){
        for (Map.Entry<String, Integer> list : map.entrySet()) {
            System.out.println(list.getKey()+" : "+list.getValue());
            count--;
            if(count<=0) break;
        }
        }
    }









