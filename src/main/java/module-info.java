module com.optimaize.langdetector {
    requires org.jetbrains.annotations;

    exports com.optimaize.langdetect;
    exports com.optimaize.langdetect.frma;
    exports com.optimaize.langdetect.i18n;
    exports com.optimaize.langdetect.ngram;
    exports com.optimaize.langdetect.profiles;
    exports com.optimaize.langdetect.text;

    opens languages;
    opens languages.shorttext;
}