/*
 * Copyright 2011 Nakatani Shuyo
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

package com.optimaize.langdetect.cybozu.util;

import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractor;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.ngram.OldNgramExtractor;
import org.jetbrains.annotations.NotNull;

import java.util.Formatter;
import java.util.List;
import java.util.Map;

/**
 * A place for sharing code.
 *
 * @author Nakatani Shuyo
 */
public class Util {

    private static final NgramExtractor ngramExtractor = NgramExtractors.standard();

    public static void addCharSequence(LangProfile langProfile, CharSequence text) {
        //TODO replace with new code.

//        List<String> old = OldNgramExtractor.extractNGrams(text, null);
//        List<String> nuu = ngramExtractor.extractGrams(text);
//
//        Set<String> oldSet = new HashSet<>(old);
//        Set<String> nuuSet = new HashSet<>(nuu);
//
//        ArrayList<String> justNuu = new ArrayList<>(nuu);
//        justNuu.removeAll(old);
//
//        ArrayList<String> justOld = new ArrayList<>(old);
//        justOld.removeAll(nuu);
//
//        System.out.println(text);

//        for (String s : ngramExtractor.extractGrams(text)) {
//            langProfile.add(s);
//        }
        for (String s : OldNgramExtractor.extractNGrams(text)) {
            langProfile.add(s);
        }
    }



    /**
     * unicode encoding (for verbose mode)
     */
    public static String unicodeEncode(String s) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            if (ch >= '\u0080') {
                String st = Integer.toHexString(0x10000 + (int) ch);
                while (st.length() < 4) st = "0" + st;
                buf.append("\\u").append(st.subSequence(1, 5));
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }


    /**
     * normalize probabilities and check convergence by the maximum probability
     * @return maximum of probabilities
     */
    public static double normalizeProb(double[] prob) {
        double maxp = 0, sump = 0;
        for(int i=0;i<prob.length;++i) sump += prob[i];
        for(int i=0;i<prob.length;++i) {
            double p = prob[i] / sump;
            if (maxp < p) maxp = p;
            prob[i] = p;
        }
        return maxp;
    }


    public static String wordProbToString(double[] prob, List<LdLocale> langlist) {
        Formatter formatter = new Formatter();
        for(int j=0;j<prob.length;++j) {
            double p = prob[j];
            if (p>=0.00001) {
                formatter.format(" %s:%.5f", langlist.get(j), p);
            }
        }
        return formatter.toString();
    }


    /**
     */
    public static double[] makeInternalPrioMap(@NotNull Map<LdLocale, Double> langWeightingMap,
                                                @NotNull List<LdLocale> langlist) {
        assert !langWeightingMap.isEmpty();
        double[] priorMap = new double[langlist.size()];
        double sump = 0;
        for (int i=0;i<priorMap.length;++i) {
            LdLocale lang = langlist.get(i);
            if (langWeightingMap.containsKey(lang)) {
                double p = langWeightingMap.get(lang);
                assert p>=0 : "Prior probability must be non-negative!";
                priorMap[i] = p;
                sump += p;
            }
        }
        assert sump > 0 : "Sum must be greater than zero!";
        for (int i=0;i<priorMap.length;++i) priorMap[i] /= sump;
        return priorMap;
    }

}
