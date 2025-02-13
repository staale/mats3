package io.mats3.test;

import org.junit.Test;

import io.mats3.test.MatsTestOutbox.MessageType;

public class Test_MockMatsInitiator {

    private static final String TRACE_ID = "traceId";
    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String MESSAGE = "message";
    private static final String STATE = "state";


    private final MatsTestOutbox _matsTestOutbox = null;

    @Test
    public void testMessageSentOk() {
        // :: Act - send a message
        _matsTestOutbox.initiator().initiateUnchecked(init -> init
                .traceId(TRACE_ID)
                .from(FROM)
                .to(TO)
                .send(MESSAGE, STATE));

        // :: Verify
        _matsTestOutbox.assertCountMessages(1);
        _matsTestOutbox.assertAtLeastOneMessageMatching(messageTest -> messageTest
                .traceId().isEqualTo(TRACE_ID)
                .from().isEqualTo(FROM)
                .to().isEqualTo(TO)
                .message().isEqualTo(MESSAGE)
                .messageType().isEqualTo(MessageType.SEND)
        );
    }

    @Test
    public void testAllTypesExceptReplySent() {
        // :: Act - send all except REPLY
        _matsTestOutbox.processContext().request(MESSAGE, STATE);
        _matsTestOutbox.processContext().next(MESSAGE);
        _matsTestOutbox.processContext().nextDirect(MESSAGE);
        _matsTestOutbox.processContext().goTo(TO, MESSAGE, STATE);
        _matsTestOutbox.processContext().initiate(init -> init.send(MESSAGE));
        _matsTestOutbox.processContext().initiate(init -> init.publish(MESSAGE));

        // :: Verify
        _matsTestOutbox.assertCountMessages(1);
        _matsTestOutbox.assertAllMessagesMatching(messageTest -> messageTest
                .to().isEqualTo(TO)
                .state().isEqualTo(STATE)
                .message().isEqualTo(MESSAGE)
                .messageType().matches("Should not call reply",
                        messageType -> messageType != MessageType.REPLY)
        );
    }

    @Test
    public void testNoMessagesSent() {
        // :: Verify
        _matsTestOutbox.assertNoMessagesSent();
    }
}
