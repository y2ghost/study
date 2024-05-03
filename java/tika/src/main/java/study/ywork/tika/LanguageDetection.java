package study.ywork.tika;

import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;

public class LanguageDetection {
    public static void main(String args[]) {
        LanguageDetector detector = new OptimaizeLangDetector().loadModels();
        LanguageResult result = detector.detect("this is english");
        String language = result.getLanguage();
        System.out.println("Language of the given content is :" + language);
    }
}