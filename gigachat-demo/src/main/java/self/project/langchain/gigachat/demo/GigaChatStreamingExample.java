package self.project.langchain.gigachat.demo;

import java.util.concurrent.TimeUnit;

import chat.giga.client.auth.AuthClient;
import chat.giga.client.auth.AuthClientBuilder;
import chat.giga.langchain4j.GigaChatChatRequestParameters;
import chat.giga.langchain4j.GigaChatStreamingChatModel;
import chat.giga.model.ModelName;
import chat.giga.model.Scope;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;

public class GigaChatStreamingExample {
    private final static String REQUEST = """
            Я хочу проверить потоковый ответ при использовании твоего API. 
            Расскажи мне что-нибудь. Правда ли, что Луна постепенно отдаляется от Земли? 
            А отдаляются ли планеты и звезды в солнечной системе друг от друга?
            """;
    public static void secondMain(String[] args) throws InterruptedException {
        GigaChatStreamingChatModel model = GigaChatStreamingChatModel.builder()
                .defaultChatRequestParameters(GigaChatChatRequestParameters.builder()
                        .modelName(ModelName.GIGA_CHAT_2)
                        .responseFormat(JsonSchema.builder()
                                    .rootElement(new JsonStringSchema())
                                    .build())
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

        model.chat(REQUEST, new StreamingChatResponseHandler() {
            public void onPartialResponse(String partialResponse) {
                System.out.println("! " + partialResponse);
            }
            public void onCompleteResponse(ChatResponse completeResponse) {
                System.out.println("< " + completeResponse);
            }
            public void onError(Throwable error) {
                System.err.println(error);
            }
        });

        TimeUnit.MINUTES.sleep(1);
    }
}