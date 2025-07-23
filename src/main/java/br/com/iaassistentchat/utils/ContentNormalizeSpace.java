package br.com.iaassistentchat.utils;

import br.com.iaassistentchat.interfaces.Normalize;
import org.springframework.stereotype.Component;

@Component
public class ContentNormalizeSpace implements Normalize{

    @Override
    public String normalize(String content) {
        return content.replaceAll("\\s+", " ").trim();
    }
}