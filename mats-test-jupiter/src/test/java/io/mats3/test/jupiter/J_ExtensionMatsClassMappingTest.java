package io.mats3.test.jupiter;

import io.mats3.MatsEndpoint.ProcessContext;
import io.mats3.spring.MatsClassMapping;
import io.mats3.spring.MatsClassMapping.Stage;
import io.mats3.util.MatsFuturizer.Reply;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * Illustrate some of the features of {@link Extension_MatsEndpoint}.
 * <p>
 * Also illustrates the usage of {@link Extension_MatsEndpoint} in combination with {@link Extension_Mats}.
 *
 * @author Kevin Mc Tiernan, 2020-10-20, kmctiernan@gmail.com
 */
public class J_ExtensionMatsClassMappingTest {

    @RegisterExtension
    public static final Extension_Mats MATS = Extension_Mats.create();

    @RegisterExtension
    public final Extension_MatsClassMapping _extensionMatsClassMapping = Extension_MatsClassMapping.create(
            MATS, new MappedEndpoint(() -> "Dep")
    );

    @RegisterExtension
    public final Extension_MatsEndpoint<String, String> _leafEndpoint = Extension_MatsEndpoint.create(
            MATS, "leaf", String.class, String.class
    );

    @MatsClassMapping
    public static class MappedEndpoint {

        private transient Supplier<String> _dependency;
        private String _request;

        // Default constructor needed for Jackson
        public MappedEndpoint() {
            this(null);
        }

        public MappedEndpoint(Supplier<String> dependency) {
            _dependency = dependency;
        }

        @Stage(Stage.INITIAL)
        public void receiveRequestAndMakeAdditionalCall(ProcessContext<?> processContext, String request) {
            _request = request;
            processContext.request("leaf", request);
        }

        @Stage(10)
        public String receiveResponseAndCreateReply(String incoming) {
            return "req:" + _request + ",dep:" + _dependency.get() + ",resp:" + incoming;
        }
    }

    @Test
    public void sendRequestToMatsClassMapping() throws InterruptedException, ExecutionException, TimeoutException {
        // :: Setup
        _leafEndpoint.setProcessLambda((ctx, msg) -> msg + "|" + msg);

        // :: Act
        String reply = MATS.getMatsFuturizer().futurizeNonessential(
                getClass().getSimpleName() + "_changeProcessorMidTestTest",
                        getClass().getSimpleName(),
                        _extensionMatsClassMapping.getEndpointId(),
                        String.class,
                        "Hello")
                        .thenApply(Reply::getReply)
                        .get(10, TimeUnit.SECONDS);

        // :: Verify
        Assertions.assertEquals("req:Hello,dep:Dep,resp:Hello|Hello", reply);
    }
}
