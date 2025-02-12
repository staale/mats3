package io.mats3.test;

import static org.junit.Assert.*;

import org.junit.Test;

import io.mats3.MatsInitiator.InitiateLambda;

public class TestInitiatorTestMats {

    private static final String FROM = "FromId";
    private static final String TO = "ToEndpointId";
    private static final String REPLY_TO = "ReplyToEndpointId";
    private static final String REPLY_TO_STATE = "ReplyToState";
    private static final String MESSAGE = "Message";
    private static final String TRACE_ID = "traceId";
    public static final InitiateLambda DEFAULT_SEND =
            init -> init.traceId(TRACE_ID).from(FROM).to(TO).send(MESSAGE);
    public static final InitiateLambda DEFAULT_PUBLISH =
            init -> init.traceId(TRACE_ID).from(FROM).to(TO).publish(MESSAGE);
    public static final InitiateLambda DEFAULT_REQUEST =
            init -> init.traceId(TRACE_ID).from(FROM).to(TO).replyTo(REPLY_TO, REPLY_TO_STATE).request(MESSAGE);

    private final TestMatsInitiator _testInitiator = TestMatsInitiator.create();

    @Test
    public void failWhenTraceIdMissing() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> _testInitiator.initiate(init -> init
                        .from(FROM)
                        .to(TO)
                        .send(MESSAGE)
                ));

        assertTrue(exception.getMessage().contains("Missing 'traceId"));
    }

    @Test
    public void failWhenFromMissing() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> _testInitiator.initiate(init -> init
                        .traceId(TRACE_ID)
                        .to(TO)
                        .send(MESSAGE)
                ));

        assertTrue(exception.getMessage().contains("Missing 'from"));
    }

    @Test
    public void failWhenToMissing() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> _testInitiator.initiate(init -> init
                        .traceId(TRACE_ID)
                        .to(TO)
                        .send(MESSAGE)
                ));

        assertEquals("Missing 'from'", exception.getMessage());
    }

    @Test
    public void failOnWrongTo() {
        _testInitiator.initiate(DEFAULT_SEND);

        AssertionError assertionError = assertThrows(AssertionError.class, () ->
                _testInitiator.assertContains(init -> init.to("ExpectedTo").send(MESSAGE))
        );

        assertEquals("Failed to match expected messages to actual messages: \n"
                     + "TestMsg00000 did not find matching message:\n"
                     + "  - Expected to: [ExpectedTo] not found, all values found: [ToEndpointId]",
                assertionError.getMessage());
    }

    @Test
    public void failOnWrongFrom() {
        _testInitiator.initiate(DEFAULT_SEND);

        AssertionError assertionError = assertThrows(AssertionError.class, () ->
                _testInitiator.assertContains(init -> init.from("ExpectedFrom").send(MESSAGE))
        );

        assertEquals("Failed to match expected messages to actual messages: \n"
                     + "TestMsg00000 did not find matching message:\n"
                     + "  - Expected from: [ExpectedFrom] not found, all values found: [FromId]",
                assertionError.getMessage());
    }

    @Test
    public void failOnWrongReplyTo() {
        _testInitiator.initiate(DEFAULT_REQUEST);

        AssertionError assertionError = assertThrows(AssertionError.class, () ->
                _testInitiator.assertContains(init -> init.replyTo("ExpectedReplyTo", REPLY_TO_STATE).request(MESSAGE))
        );

        assertEquals("Failed to match expected messages to actual messages: \n"
                     + "TestMsg00000 did not find matching message:\n"
                     + "  - Expected replyTo: [ExpectedReplyTo] not found, all values found: [ReplyToEndpointId]",
                assertionError.getMessage());
    }

    @Test
    public void failOnWrongReplyToState() {
        _testInitiator.initiate(DEFAULT_REQUEST);

        AssertionError assertionError = assertThrows(AssertionError.class, () ->
                _testInitiator.assertContains(init -> init.replyTo(REPLY_TO, "ExpectedReplyToState").request(MESSAGE))
        );

        assertEquals("Failed to match expected messages to actual messages: \n"
                     + "TestMsg00000 did not find matching message:\n"
                     + "  - Expected replyToState: [ExpectedReplyToState] not found, all values found: [ReplyToState]",
                assertionError.getMessage());
    }

    @Test
    public void failOnWrongMessage() {
        _testInitiator.initiate(DEFAULT_SEND);

        AssertionError assertionError = assertThrows(AssertionError.class, () ->
                _testInitiator.assertContains(init -> init.send("ExpectedRequest"))
        );

        assertEquals("Failed to match expected messages to actual messages: \n"
                     + "TestMsg00000 did not find matching message:\n"
                     + "  - Expected requestDto: [ExpectedRequest] not found, all values found: [Message]",
                assertionError.getMessage());
    }

    @Test
    public void failOnWrongTraceId() {
        _testInitiator.initiate(DEFAULT_SEND);

        AssertionError assertionError = assertThrows(AssertionError.class, () ->
                _testInitiator.assertContains(init -> init.traceId("ExpectedTraceId").send(MESSAGE))
        );

        assertEquals("Failed to match expected messages to actual messages: \n"
                     + "TestMsg00000 did not find matching message:\n"
                     + "  - Expected traceId: [ExpectedTraceId] not found, all values found: [traceId]",
                assertionError.getMessage());
    }

    @Test
    public void failOnWrongState() {
        _testInitiator.initiate(DEFAULT_REQUEST);

        AssertionError assertionError = assertThrows(AssertionError.class, () ->
                _testInitiator.assertContains(init -> init.replyTo(REPLY_TO, "WrongState").request(MESSAGE))
        );

        assertEquals("Failed to match expected messages to actual messages: \n"
                     + "TestMsg00000 did not find matching message:\n"
                     + "  - Expected replyToState: [WrongState] not found, all values found: [ReplyToState]",
                assertionError.getMessage());
    }

    @Test
    public void failOnWrongStateAndReplyTo() {
        _testInitiator.initiate(DEFAULT_REQUEST);

        AssertionError assertionError = assertThrows(AssertionError.class, () ->
                _testInitiator.assertContains(init -> init.replyTo("WrongReplyTo", "WrongState").request(MESSAGE))
        );

        assertEquals("Failed to match expected messages to actual messages: \n"
                     + "TestMsg00000 did not find matching message:\n"
                     + "  - Expected replyTo: [WrongReplyTo] not found, all values found: [ReplyToEndpointId]\n"
                     + "  - Expected replyToState: [WrongState] not found, all values found: [ReplyToState]",
                assertionError.getMessage());
    }

    @Test
    public void failOnWrongRequestType() {
        _testInitiator.initiate(DEFAULT_REQUEST);

        AssertionError assertionError = assertThrows(AssertionError.class, () ->
                _testInitiator.assertContains(init -> init.replyTo(REPLY_TO, REPLY_TO_STATE).send(MESSAGE))
        );

        assertEquals("Failed to match expected messages to actual messages: \n"
                     + "TestMsg00000 did not find matching message:\n"
                     + "  - Expected requestType: [SEND] not found, all values found: [REQUEST]",
                assertionError.getMessage());
    }

    @Test
    public void failOnWrongRequestType2() {
        _testInitiator.initiate(DEFAULT_PUBLISH);

        AssertionError assertionError = assertThrows(AssertionError.class, () ->
                _testInitiator.assertContains(init -> init.send(MESSAGE))
        );

        assertEquals("Failed to match expected messages to actual messages: \n"
                     + "TestMsg00000 did not find matching message:\n"
                     + "  - Expected requestType: [SEND] not found, all values found: [PUBLISH]",
                assertionError.getMessage());
    }

    @Test
    public void failWhenNoRequestVerified() {
        IllegalStateException illegalStateException =
                assertThrows(IllegalStateException.class, () -> _testInitiator.assertContains(init -> { }));

        assertEquals("No messages added, you must call one of send/request/publish in the verify lambda"
             + " to ensure a message is added",
                illegalStateException.getMessage());
    }

}