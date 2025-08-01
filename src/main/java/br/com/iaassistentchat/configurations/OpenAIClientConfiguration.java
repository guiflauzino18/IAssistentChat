//package br.com.iaassistentchat.configurations;
//
//import io.micrometer.observation.ObservationRegistry;
//import io.netty.channel.ChannelOption;
//import io.netty.handler.timeout.ReadTimeoutHandler;
//import io.netty.handler.timeout.WriteTimeoutHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.document.MetadataMode;
//import org.springframework.ai.model.tool.DefaultToolCallingManager;
//import org.springframework.ai.model.tool.ToolCallingManager;
//import org.springframework.ai.openai.OpenAiChatModel;
//import org.springframework.ai.openai.OpenAiChatOptions;
//import org.springframework.ai.openai.OpenAiEmbeddingModel;
//import org.springframework.ai.openai.OpenAiEmbeddingOptions;
//import org.springframework.ai.openai.api.OpenAiApi;
//import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
//import org.springframework.ai.tool.resolution.ToolCallbackResolver;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.retry.support.RetryTemplate;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.web.client.DefaultResponseErrorHandler;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.netty.http.client.HttpClient;
//import reactor.netty.tcp.TcpClient;
//
//
//@Configuration
//public class OpenAIClientConfiguration {
//
//    @Value("${spring.ai.openai.base-url}")
//    private String baseURL;
//    @Value("${spring.ai.openai.api-key}")
//    private String apiKey;
//    @Value("${spring.ai.openai.chat.options.model}")
//    private String model;
//    @Value("${spring.ai.openai.chat.completions-path}")
//    private String completionsPath;
//    @Value("${spring.ai.openai.embedding.embeddings-path}")
//    private String embeddingPath;
//    @Value("${spring.ai.openai.embedding.options.model}")
//    private String embeddingModel;
//
//    private Logger logger = LoggerFactory.getLogger(OpenAIClientConfiguration.class);
//
//    @Bean
//    public OpenAiEmbeddingModel openAiEmbeddingModel(){
//        logger.info("Configurando EmbbeddingModel. Model: %s".formatted(embeddingModel));
//        return new OpenAiEmbeddingModel(
//                openAiApi(), MetadataMode.ALL,OpenAiEmbeddingOptions.builder().model(embeddingModel).build()
//        );
//    }
//
//
//    @Bean
//    public ChatModel chatModel(ToolCallingManager toolCallingManager, RetryTemplate retryTemplate, ObservationRegistry observationRegistry){
//
//        return new OpenAiChatModel(
//                openAiApi(),
//                OpenAiChatOptions.builder().model(model).build(),
//                toolCallingManager,retryTemplate,observationRegistry
//        );
//    }
//
//    private OpenAiApi openAiApi(){
//        System.out.println("--------------------------------"+apiKey);
//        return new OpenAiApi(
//                baseURL,
//                () -> apiKey
//                ,
//                new LinkedMultiValueMap<>(),
//                completionsPath,
//                embeddingPath,
//                RestClient.builder(), customWebClient(),
//                new DefaultResponseErrorHandler()
//        );
//    }
//
//
//    private WebClient.Builder customWebClient() {
//        TcpClient tcpClient = TcpClient.create()
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60 * 1000)
//                .doOnConnected(conn -> conn
//                        .addHandlerLast(new ReadTimeoutHandler(300))
//                        .addHandlerLast(new WriteTimeoutHandler(300)));
//
//        HttpClient httpClient = HttpClient.from(tcpClient);
//
//        return WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector(httpClient));
//
//    }
//
//    @Bean
//    public ObservationRegistry observationRegistry() {
//        return ObservationRegistry.create();
//    }
//
//    @Bean
//    public ToolCallingManager toolCallingManager(
//            ObservationRegistry observationRegistry,
//            ToolCallbackResolver toolCallbackResolver,
//            ToolExecutionExceptionProcessor toolExecutionExceptionProcessor
//    ) {
//        return new DefaultToolCallingManager(
//                observationRegistry,
//                toolCallbackResolver,
//                toolExecutionExceptionProcessor
//        );
//    }
//}
