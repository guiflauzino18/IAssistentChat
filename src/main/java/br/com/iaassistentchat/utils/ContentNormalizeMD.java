package br.com.iaassistentchat.utils;

import br.com.iaassistentchat.interfaces.Normalize;
import org.springframework.stereotype.Component;

@Component
public class ContentNormalizeMD  implements Normalize {
    @Override
    public String normalize(String content) {

        content = removeLineBreak(content);
        content = removeTabs(content);
        content = removeSpecialCharacter(content);

        return content;

    }

    private String removeLineBreak(String content){
        return content.replaceAll("\\n+", " ").trim();
    }

    private String removeTabs(String content){
        return content.replaceAll("\\t+", " ").trim();
    }

    private String removeSpecialCharacter(String content){
        //content = content.replaceAll("#+", " ").trim();
        content = content.replaceAll("-+", " ").trim();
        content = content.replaceAll("\\|+", " ").trim();
        content = content.replaceAll("(?s)\\{.*?}", " ").trim();

        return content;

    }
}