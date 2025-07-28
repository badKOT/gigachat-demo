package self.project.langchain.gigachat.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import self.project.langchain.gigachat.demo.GigaChatBasicToolExample.Counter;
import self.project.langchain.gigachat.demo.GigaChatBasicToolExample.Counter.CountResult;

public class GigaChatBasicToolExampleTest {
    private final Counter counter = new Counter();

    @ParameterizedTest
    @MethodSource("counterParamsProvider")
    void testCounter(String word, String ch, CountResult result) {
        if (result.count() == -1) {
            assertThrows(RuntimeException.class, () -> counter.count(word, ch));
        } else {
            assertEquals(result, counter.count(word, ch));
        }
    }

    static Stream<Arguments> counterParamsProvider() {
        return Stream.of(
            Arguments.arguments("strawberry", "r", new CountResult(3L)),
            Arguments.arguments("повторно", "о", new CountResult(3L)),
            Arguments.arguments("1234852593367", "5", new CountResult(2L)),
            Arguments.arguments("1234852593367", ";", new CountResult(-1L)),
            Arguments.arguments("1234852593367", ",", new CountResult(-1L)),
            Arguments.arguments("1234852593367", ".", new CountResult(-1L))
        );
    }
}
