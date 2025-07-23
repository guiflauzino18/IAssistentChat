package br.com.iaassistentchat.services.embeddings;

import br.com.iaassistentchat.utils.ContentNormalizeMD;
import br.com.iaassistentchat.utils.ContentNormalizeSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContentSplitterService {

    @Autowired
    private ContentNormalizeSpace normalizeSpace;
    @Autowired
    private ContentNormalizeMD normalizeMD;

    //Split the page's content in chunks of X characters
    public List<String> split(String text, int chunkSize, int overlap){
        List<String> chunks = new ArrayList<>();
        text = contentNormalize(text);

        for (int i = 0; i < text.length(); i += (chunkSize - overlap)){
            int end = Math.min(i + chunkSize, text.length());
            chunks.add(text.substring(i, end));
        }

        return  chunks;
    }

    private String contentNormalize(String content){

        content = normalizeSpace.normalize(content);
        content = normalizeMD.normalize(content);

        return  content;
    }
}
