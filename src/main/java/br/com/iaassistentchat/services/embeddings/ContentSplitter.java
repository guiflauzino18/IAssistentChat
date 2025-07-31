package br.com.iaassistentchat.services.embeddings;

import br.com.iaassistentchat.utils.ContentNormalizeHTML;
import br.com.iaassistentchat.utils.ContentNormalizeMD;
import br.com.iaassistentchat.utils.ContentNormalizeNoDiagram;
import br.com.iaassistentchat.utils.ContentNormalizeSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContentSplitter {

    @Autowired
    private ContentNormalizeSpace normalizeSpace;
    @Autowired
    private ContentNormalizeMD normalizeMD;
    @Autowired
    private ContentNormalizeNoDiagram normalizeNoDiagram;
    @Autowired
    private ContentNormalizeHTML normalizeHTML;

    //Split the page's content in chunks of X characters
    public List<String> split(String text, String source, int chunkSize, int overlap){

//      Limpa o texto retirando caracteres especiais, tags html e tag markdown
        text = contentNormalize(text);

        List<String> chunks = new ArrayList<>();

        //Quebra texto em pedaços menores
        for (int i = 0; i < text.length(); i += (chunkSize - overlap)){
            int end = Math.min(i + chunkSize, text.length());
            chunks.add(text.substring(i, end));
        }

        chunks = removeBlankChunks(chunks);
        chunks = addSourceInChunks(chunks, source);


        return  chunks;
    }

    private List<String> addSourceInChunks(List<String> chunks, String source){
        return chunks.stream()
                .map(chunk -> chunk + "- Empresa: "+source ).collect(Collectors.toList());

    }

    private List<String> removeBlankChunks(List<String> chunks){
        var mutableList = new ArrayList<>(chunks);

        mutableList.removeIf(String::isBlank);

        return mutableList;
    }

    private String contentNormalize(String content){

        content = normalizeSpace.normalize(content);
        content = normalizeMD.normalize(content);
        content = normalizeNoDiagram.normalize(content);
        content = normalizeHTML.normalize(content);

        return  content;
    }
}
