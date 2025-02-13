package io.mats3.test;

import java.util.function.Function;
import java.util.function.Predicate;

import io.mats3.MatsEndpoint.ProcessContext;
import io.mats3.MatsInitiator;

/**
 * Helper to test sending of messages from Mats.
 *
 * This class can be used when you need either an {@link MatsInitiator} or a {@link ProcessContext} to pass on to
 * a method of service, to then test that it actually sends messages as expected, or not, during a service call.
 *
 * @author Ståle Undheim <stale.undheim@storebrand.no> 2025-02-13
 */
public class MatsTestOutbox {

    /**
     * The type of message sent. Note that {@link #NEXT}, {@link #NEXT_DIRECT}, {@link #REPLY} and {@link #GOTO} are
     * only possible from a {@link ProcessContext}. The enums here corresponds to one of the send methods in
     * {@link MatsInitiator} or {@link ProcessContext}.
     */
    public enum MessageType {
        PUBLISH, SEND, REQUEST, REPLY, NEXT, NEXT_DIRECT, GOTO
    }

    /**
     * Builder to match messages sent from Mats against.
     *
     * This is used from {@link #assertAtLeastOneMessageMatching(Function)} to build a test to verify against all messages sent by
     * the Mats initiator or process context. Note that this is a chain builder, you first have to pick a property to
     * test, then call a method on {@link PropertyAssertRule} to indicate what is expected from that field.
     */
    public interface MessageTestBuilder {
        PropertyAssertRule<String> traceId();
        PropertyAssertRule<String> from();
        PropertyAssertRule<String> to();
        PropertyAssertRule<Object> message();
        PropertyAssertRule<Object> state();
        PropertyAssertRule<MessageType> messageType();
    }

    /**
     * Rule to assert a property of a message sent from Mats.
     * @param <T> type of the value to assert.
     */
    public interface PropertyAssertRule<T> {
        MessageTestBuilder isEqualTo(T value);
        MessageTestBuilder matches(String description, Predicate<T> predicate);
        MessageTestBuilder matches(Predicate<T> predicate);
    }

    /**
     * Returns an initiator, that will capture all sent messages, such that they can be checked with the assert methods.
     *
     * @return MatsInitiator that captures all sent messages.
     */
    public MatsInitiator initiator() {
        return null;
    }

    /**
     * Returns a process context, that will capture all sent messages, such that they can be checked with the assert methods.
     *
     * @return a process context that captures all sent messages.
     * @param <X> the type of the process context.
     */
    public <X> ProcessContext<X> processContext() {
        return null;
    }

    /**
     * Assert that there where no messages sent during the test.
     */
    public void assertNoMessagesSent() {
        assertCountMessages(0);
    }

    /**
     * Assert that the given number of messages where sent during the test.
     *
     * @param messageCount that we expect to have been sent.
     */
    public void assertCountMessages(int messageCount) {

    }

    /**
     * Build an assert that we should match against 1 of the messages sent during the test.
     * <p>
     * This is a Builder that alternates between the types {@link MessageTestBuilder} and {@link PropertyAssertRule}.
     * This takes a function that accepts a {@link MessageTestBuilder}, and expects one in return, forcing the caller
     * to call an assert rule for each property to test.
     *
     * @param testBuilder to build the test to match against the messages sent.
     */
    public void assertAtLeastOneMessageMatching(Function<MessageTestBuilder, MessageTestBuilder> testBuilder) {

    }

    /**
     * Build an assert that we should match against all messages sent during the test.
     *
     * This requires all messages sent, to fulfill all criteria add to the {@link MessageTestBuilder}.
     * @param testBuilder to build the test to match against all the messages sent.
     */
    public void assertAllMessagesMatching(Function<MessageTestBuilder, MessageTestBuilder> testBuilder) {

    }

}
