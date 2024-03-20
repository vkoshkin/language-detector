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

package com.optimaize.langdetect.ngram;

import com.optimaize.langdetect.cybozu.util.NGram;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nakatani Shuyo
 */
@Deprecated
public class OldNgramExtractor {

    /**
     * This was the method found in the <i>com.cybozu.labs.langdetect.Detector</i> class, it was used to extract
     * grams from the to-analyze text.
     * <p>
     * NOTE: although it adds the first ngram with space, it does not add the last n-gram with space. example: "foo" gives " fo" but not "oo "!.
     * It is not clear yet whether this is desired (and why) or a bug.
     * <p>
     * TODO replace this algorithm with a simpler, faster one that uses less memory: only by position shifting. also, the returned list size
     * can be computed before making it (based on text length and number of n-grams).
     */
    @NotNull
    @Deprecated
    public static List<String> extractNGrams(@NotNull CharSequence text) {
        List<String> list = new ArrayList<>();
        NGram ngram = new NGram();
        for (int i = 0; i < text.length(); ++i) {
            ngram.addChar(text.charAt(i));
            for (int n = 1; n <= NGram.N_GRAM; ++n) {
                String w = ngram.get(n);
                if (w != null) { //TODO this null check is ugly
                    list.add(w);
                }
            }
        }
        return list;
    }

}
