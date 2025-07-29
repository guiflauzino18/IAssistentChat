package br.com.iaassistentchat.utils;

import br.com.iaassistentchat.interfaces.Normalize;
import org.springframework.stereotype.Component;

@Component
public class ContentNormalizeNoDiagram  implements Normalize {
    @Override
    public String normalize(String content) {
        content = content.replaceAll("(?s)```.*?```", " ").trim();

        return content;
    }
}
