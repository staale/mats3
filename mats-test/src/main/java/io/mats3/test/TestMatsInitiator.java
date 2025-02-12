package io.mats3.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.mats3.MatsFactory;
import io.mats3.MatsInitiator;

public class TestMatsInitiator implements MatsInitiator {

    private final String _name;
    private final List<InitiateMessage> _initiateMessages = new ArrayList<>();
    private final Map<AttributeKey, Object> _attributes = new HashMap<>();

    private TestMatsInitiator(String name) {
        _name = name;
    }

    public static TestMatsInitiator create(String name) {
        return new TestMatsInitiator(name);
    }

    public static TestMatsInitiator create() {
        return create(TestMatsInitiator.class.getSimpleName());
    }

    public void assertContains(InitiateLambda initiateLambda) {
        List<InitiateMessage> verifyMessages = new ArrayList<>();
        initiateLambda.initiate(new RecordingInitiateLambda(
                () -> String.format("TestMsg%05d", verifyMessages.size()),
                _attributes,
                verifyMessages,
                false));
        if (verifyMessages.isEmpty()) {
            throw new IllegalStateException("No messages added, you must call one of send/request/publish in the"
                                            + " verify lambda to ensure a message is added");
        }

        List<String> errorMessages = new ArrayList<>();
        for (InitiateMessage initiateMessage : verifyMessages) {
            if (!initiateMessage.anyMatch(_initiateMessages)) {
                errorMessages.add(initiateMessage.getId() + " did not find matching message:");
                initiateMessage.addMismatchMessages(_initiateMessages, message -> errorMessages.add("  - " + message));
            }
        }
        if (!errorMessages.isEmpty()) {
            throw new AssertionError("Failed to match expected messages to actual messages: \n"
                                     + String.join("\n", errorMessages));
        }
    }

    public void reset() {
        _initiateMessages.clear();
    }

    public void addAttribute(Object value, String... name) {
        _attributes.put(new AttributeKey(value.getClass(), name), value);
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public MatsFactory getParentFactory() {
        return new TestMatsFactory();
    }

    @Override
    public void initiate(InitiateLambda lambda) {
        lambda.initiate(new RecordingInitiateLambda(
                () -> String.format("TestMsg%05d", _initiateMessages.size()),
                _attributes,
                _initiateMessages,
                true
        ));
    }

    @Override
    public void initiateUnchecked(InitiateLambda lambda) {
        initiate(lambda);
    }

    @Override
    public void close() {
        // Noop
    }

}
