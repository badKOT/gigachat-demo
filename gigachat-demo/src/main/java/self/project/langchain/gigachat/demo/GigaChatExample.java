package self.project.langchain.gigachat.demo;

import chat.giga.langchain4j.GigaChatChatModel;
import chat.giga.langchain4j.GigaChatChatRequestParameters;
import chat.giga.client.auth.AuthClient;
import chat.giga.client.auth.AuthClientBuilder;
import chat.giga.model.ModelName;
import chat.giga.model.Scope;

public class GigaChatExample {
    public static void firstMain(String[] args) {
        System.out.println("Sending a simple request...");
        GigaChatChatModel model = GigaChatChatModel.builder()
                .defaultChatRequestParameters(GigaChatChatRequestParameters.builder()
                        .modelName(ModelName.GIGA_CHAT_2)
                        .build())
                .authClient(AuthClient.builder()
                        .withOAuth(AuthClientBuilder.OAuthBuilder.builder()
                                .scope(Scope.GIGACHAT_API_PERS)
                                .authKey("MTM3YTUzYTEtNDMzMi00ZDMyLThhZjMtN2Y0MWZhOGNhNmQxOmFkYWQ1OTIwLTU1MDgtNDE1ZC05Y2NjLTBiZmFiZDBhZjBkOQ==")
                                .build())
                        .build())
                .logRequests(true)
                .logResponses(true)
                .build();

        String response = model.chat("хьюстон, прием, как слышно?");
        System.out.println("Should've gotten the response");
        System.out.println(response);
    }
}