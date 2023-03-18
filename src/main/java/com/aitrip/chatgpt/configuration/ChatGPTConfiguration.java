package com.aitrip.chatgpt.configuration;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;

@Configuration
public class ChatGPTConfiguration {

    @Value("${chatgpt.apiKey}")
    private String apiKey;
    @Value("${chatgpt.apiHost}")
    private String apiHost;

    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());

    @Bean
    public OpenAiClient openAiClient(@Autowired(required = false) Proxy proxy) {
        OpenAiClient.Builder builder = OpenAiClient.builder()
                .apiKey(apiKey)
                .interceptor(Arrays.asList(httpLoggingInterceptor))
                .apiHost(apiHost);
        if(proxy != null){
            builder.proxy(proxy);
        }
        return builder
                .build();
    }
    @Bean
    public OpenAiStreamClient openAiStreamClient(@Autowired(required = false) Proxy proxy) {
        OpenAiStreamClient.Builder builder = OpenAiStreamClient.builder().apiHost(apiHost).apiKey(apiKey);
        if(proxy != null){
            builder.proxy(proxy);
        }
        return builder.build();
    }

    @Profile("dev")
    @Bean
    public  Proxy proxy(@Value("${ip:127.0.0.1}") String ip,@Value("${port:7890}") Integer port){
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
    }

}
