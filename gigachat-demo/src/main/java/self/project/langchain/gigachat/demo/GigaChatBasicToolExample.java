package self.project.langchain.gigachat.demo;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import chat.giga.client.auth.AuthClient;
import chat.giga.client.auth.AuthClientBuilder;
import chat.giga.langchain4j.GigaChatChatRequestParameters;
import chat.giga.langchain4j.GigaChatStreamingChatModel;
import chat.giga.model.ModelName;
import chat.giga.model.Scope;
import chat.giga.model.completion.ChatFunctionCallEnum;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;

public class GigaChatBasicToolExample {
    private final static String REQUEST = """
            Пожалуйста верни расстоние от Земли до Солнца в километрах.
            Для каждой цифры от 0 до 9, используй функцию charCounter ровно один раз, чтобы посчитать, сколько раз эта цифра присутствует в этом числе.
            """;

    public static void thirdMain(String[] args) throws InterruptedException {
        GigaChatStreamingChatModel model = GigaChatStreamingChatModel.builder()
                .defaultChatRequestParameters(GigaChatChatRequestParameters.builder()
                        .modelName(ModelName.GIGA_CHAT_2)
                        .responseFormat(JsonSchema.builder()
                                    .rootElement(new JsonStringSchema())
                                    .build())
                        .functionCall(ChatFunctionCallEnum.AUTO)
                        .build())
                .authClient(AuthClient.builder()
                        .withOAuth(AuthClientBuilder.OAuthBuilder.builder()
                                .scope(Scope.GIGACHAT_API_PERS)
                                .authKey("MTM3YTUzYTEtNDMzMi00ZDMyLThhZjMtN2Y0MWZhOGNhNmQxOmY1YzkwNTkzLWI5N2EtNDhiYy04OGQ5LTQ0ZDFkMDNkMzk4MQ==")
                                .build())
                        .build())
                .logRequests(true)
                .logResponses(true)
                .build();

        var tooled = AiServices.builder(Assistant.class)
                .streamingChatModel(model)
                .tools(new Counter())
                .build();

        tooled.chat(REQUEST)
                .onPartialResponse(System.out::println)
                .onCompleteResponse(rs -> {
                    
                })
                .onToolExecuted(tool -> System.out.println("tool was called with parameters " + tool.request() + " and returned " + tool.result()))
                .onError(e -> {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                })
                .start();

        TimeUnit.MINUTES.sleep(1);
    }

    interface Assistant {
        TokenStream chat(String message);
    }

    static class Counter {
        @Tool(name = "charCounter", value = "Считает количество цифр в числе или букв в слове (поддерживает русский и английский языки)")
        CountResult count(
            @P(value = "слово или число, в котором нужно осуществить подсчет символов", required = true) String text, 
            @P(value = "буква или цифра, которую нужно посчитать", required = true) String symbol) {
            System.out.println("function was called");
            if (text == null || text.isEmpty() || symbol == null) {
                return new CountResult(0L);
            }
            if (!stringIsAlphanumeric(symbol)) {
                throw new RuntimeException("Я еще учусь и пока умею искать только цифры и буквы. В аргументах передано " + symbol);
            }
            
            return new CountResult(Pattern.compile(symbol.toString()).matcher(text).results().count());
        }

        protected boolean stringIsAlphanumeric(String str) {
            return str.matches("[a-zA-Zа-яА-ЯйЙ0-9]+");
        }

        static record CountResult(@Description(value = "количество символов в слове или числе") Long count) {}
    }
}

/*
 * 149 597 870
 * 1
 * 1
 * 0
 * 0
 * 1
 * 1
 * 0
 * 2
 * 1
 * 2
 */