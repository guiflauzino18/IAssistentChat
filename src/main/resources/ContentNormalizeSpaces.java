package br.com.iaassistentchat.utils;

import br.com.iaassistentchat.interfaces.Normalize;

public class ContentNormalizeSpaces implements Normalize {

    @Override
    public String normalize(String content) {
        return content.replaceAll("\\s+", " ").trim();
    }
}
