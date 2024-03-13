/*
 * Copyright 2011 Fabian Kessler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.optimaize.langdetect.ngram;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Fabian Kessler
 */
public class NgramExtractorTest {

    @Test
    public void extractGrams_1() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.gramLength(1).extractGrams(text);
        assertEquals(ngrams.size(), text.length());
        assertEquals(ngrams, Arrays.asList("F","o","o"," ","b","a","r"));
    }

    @Test
    public void extractGrams_2() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.gramLength(2).extractGrams(text);
        assertEquals(ngrams.size(), text.length() -1);
        assertEquals(ngrams, Arrays.asList("Fo","oo","o "," b","ba","ar"));
    }

    @Test
    public void extractGrams_3() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.gramLength(3).extractGrams(text);
        assertEquals(ngrams.size(), text.length()-2);
    }

    @Test
    public void extractGrams_6() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.gramLength(6).extractGrams(text);
        assertEquals(ngrams.size(), text.length()-5);
    }

    @Test
    public void extractGrams_7() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.gramLength(7).extractGrams(text);
        assertEquals(ngrams.size(), text.length()-6);
    }

    @Test
    public void extractGrams_8() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.gramLength(8).extractGrams(text);
        assertTrue(ngrams.isEmpty());
    }



    @Test
    public void stressTestAlgo2() {
        NgramExtractor ngramExtractor = NgramExtractor.gramLengths(1, 2, 3);
        String text = "Foo bar hello world and so on nana nunu dada dudu asdf asdf akewf köjvnawer aisdfj awejfr iajdsöfj ewi adjsköfjwei ajsdökfj ief asd";
        StopWatch stopWatch = StopWatch.createStarted();
        for (int i=0; i<100000; i++) {
            ngramExtractor.extractGrams(text);
        }
        System.out.println(stopWatch); //876.6ms
    }


    @Test
    public void extractGrams_threeSizesAtOnce() {
        String text = "Foo bar";

        List<String> expected = NgramExtractor.gramLengths(1, 2, 3).extractGrams(text);
        Collections.sort(expected);

        List<String> separate = new ArrayList<>();
        separate.addAll(NgramExtractor.gramLength(1).extractGrams(text));
        separate.addAll(NgramExtractor.gramLength(2).extractGrams(text));
        separate.addAll(NgramExtractor.gramLength(3).extractGrams(text));
        Collections.sort(separate);

        assertEquals(expected, separate);
    }

    @Test
    public void extractGrams_threeSizesAtOnce_short() {
        List<String> ngrams = NgramExtractor.gramLengths(1, 2, 3).extractGrams("a");
        assertEquals(ngrams.size(), 1);

        ngrams = NgramExtractor.gramLengths(1, 2, 3).extractGrams("");
        assertEquals(ngrams.size(), 0);
    }



    @Test
    public void extractCountedGrams_single_1() {
        Map<String,Integer> grams = NgramExtractor.gramLength(1).extractCountedGrams("Foo");
        assertEquals(grams.size(), 2);
    }

    @Test
    public void extractCountedGrams_single_2() {
        Map<String,Integer> grams = NgramExtractor.gramLengths(2).extractCountedGrams("Foo bar");
        assertEquals(grams.size(), 6);

        grams = NgramExtractor.gramLengths(2).extractCountedGrams("aaaa");
        assertEquals(grams, Collections.singletonMap("aa",3));
    }

    @Test
    public void extractCountedGrams_list_1() {
        String text = "Foo bar dies ist ein längerer deutscher Text, und Texte sind üblicherweise auch gerne gross geschrieben und so nämlich.";

        Map<String,Integer> one = NgramExtractor.gramLength(1).extractCountedGrams(text);
        Map<String,Integer> two = NgramExtractor.gramLengths(2).extractCountedGrams(text);
        Map<String,Integer> three = NgramExtractor.gramLengths(3).extractCountedGrams(text);
        Map<String,Integer> combined = new HashMap<>();
        combined.putAll(one);
        combined.putAll(two);
        combined.putAll(three);

        Map<String,Integer> combined2 = NgramExtractor.gramLengths(1, 2, 3).extractCountedGrams(text);
        assertEquals(combined, combined2);
    }


    @Test
    public void extractGramsWithPadding_1() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.gramLength(1).textPadding(' ').extractGrams(text);
        assertEquals(ngrams.size(), text.length()+2);
        assertEquals(ngrams, Arrays.asList(" ","F","o","o"," ","b","a","r"," "));
    }

    @Test
    public void extractGramsWithPaddingAndFilter_1() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor
                .gramLength(1)
                .filter(StandardNgramFilter.getInstance())
                .textPadding(' ')
                .extractGrams(text);
        assertEquals(ngrams, Arrays.asList("F","o","o","b","a","r"));
    }

    @Test
    public void extractGramsWithPadding_2() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.gramLength(2).textPadding(' ').extractGrams(text);
        assertEquals(ngrams.size(), text.length() +1);
        assertEquals(ngrams, Arrays.asList(" F","Fo","oo","o "," b","ba","ar","r "));
    }

}
