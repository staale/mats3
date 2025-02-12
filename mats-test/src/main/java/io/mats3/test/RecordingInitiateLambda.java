package io.mats3.test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import io.mats3.MatsEndpoint.ProcessLambda;
import io.mats3.MatsInitiator.KeepTrace;
import io.mats3.MatsInitiator.MatsInitiate;
import io.mats3.MatsInitiator.MessageReference;

final class RecordingInitiateLambda implements MatsInitiate {
    private final Supplier<String> _messageIdSupplier;
    private final Map<AttributeKey, Object> _attributes;
    private final List<InitiateMessage> _initiateMessages;
    private final boolean _verifyInit;
    private InitiateMessage.Builder _builder;

    public RecordingInitiateLambda(
            Supplier<String> messageIdSupplier,
            Map<AttributeKey, Object> attributes,
            List<InitiateMessage> initiateMessages, boolean verifyInit) {
        _messageIdSupplier = messageIdSupplier;
        _attributes = attributes;
        _initiateMessages = initiateMessages;
        _verifyInit = verifyInit;
        _builder = new InitiateMessage.Builder(_verifyInit);
    }

    @Override
    public MatsInitiate traceId(CharSequence traceId) {
        _builder.traceId(traceId);
        return this;
    }

    @Override
    public MatsInitiate keepTrace(KeepTrace keepTrace) {
        _builder.keepTrace(keepTrace);
        return this;
    }

    @Override
    public MatsInitiate nonPersistent() {
        return nonPersistent(0);
    }

    @Override
    public MatsInitiate nonPersistent(long timeToLiveMillis) {
        _builder.nonPersistent(timeToLiveMillis);
        return this;
    }

    @Override
    public MatsInitiate interactive() {
        _builder.interactive();
        return this;
    }

    @Override
    public MatsInitiate noAudit() {
        _builder.noAudit();
        return this;
    }

    @Override
    public MatsInitiate from(String initiatorId) {
        _builder.from(initiatorId);
        return this;
    }

    @Override
    public MatsInitiate to(String endpointId) {
        _builder.to(endpointId);
        return this;
    }

    @Override
    public MatsInitiate replyTo(String endpointId, Object replySto) {
        _builder.replyTo(endpointId, replySto);
        return this;
    }

    @Override
    public MatsInitiate replyToSubscription(String endpointId, Object replySto) {
        _builder.replyToSubscription(endpointId, replySto);
        return this;
    }

    @Override
    public MatsInitiate setTraceProperty(String propertyName, Object propertyValue) {
        _builder.setTraceProperty(propertyName, propertyValue);
        return this;
    }

    @Override
    public MatsInitiate addBytes(String key, byte[] payload) {
        _builder.addBytes(key, payload);
        return this;
    }

    @Override
    public MatsInitiate addString(String key, String payload) {
        _builder.addString(key, payload);
        return this;
    }

    @Override
    public MatsInitiate logMeasurement(
            String metricId,
            String metricDescription,
            String baseUnit,
            double measure,
            String... labelKeyValue) {
        _builder.logMeasurement(metricId, metricDescription, baseUnit, measure, labelKeyValue);
        return this;
    }

    @Override
    public MatsInitiate logTimingMeasurement(String metricId, String metricDescription, long nanos,
            String... labelKeyValue) {
        _builder.logTimingMeasurement(metricId, metricDescription, nanos, labelKeyValue);
        return this;
    }

    @Override
    public MessageReference request(Object requestDto) {
        return request(requestDto, null);
    }

    @Override
    public MessageReference request(Object requestDto, Object initialTargetSto) {
        String messageId = _messageIdSupplier.get();
        _initiateMessages.add(_builder.request(messageId, requestDto, initialTargetSto));
        _builder = new InitiateMessage.Builder(_verifyInit);
        return () -> messageId;
    }


    public MessageReference reply(Object requestDto) {
        String messageId = _messageIdSupplier.get();
        _initiateMessages.add(_builder.reply(messageId, requestDto));
        _builder = new InitiateMessage.Builder(_verifyInit);
        return () -> messageId;
    }

    public MessageReference next(Object requestDto) {
        String messageId = _messageIdSupplier.get();
        _initiateMessages.add(_builder.next(messageId, requestDto));
        _builder = new InitiateMessage.Builder(_verifyInit);
        return () -> messageId;
    }


    public MessageReference goTo(Object requestDto, Object initialTargetSto) {
        String messageId = _messageIdSupplier.get();
        _initiateMessages.add(_builder.goTo(messageId, requestDto, initialTargetSto));
        _builder = new InitiateMessage.Builder(_verifyInit);
        return () -> messageId;
    }

    @Override
    public MessageReference send(Object messageDto) {
        return send(messageDto, null);
    }

    @Override
    public MessageReference send(Object messageDto, Object initialTargetSto) {
        String messageId = _messageIdSupplier.get();
        _initiateMessages.add(_builder.send(messageId, messageDto, initialTargetSto));
        _builder = new InitiateMessage.Builder(_verifyInit);
        return () -> messageId;
    }

    @Override
    public MessageReference publish(Object messageDto) {
        return publish(messageDto, null);
    }

    @Override
    public MessageReference publish(Object messageDto, Object initialTargetSto) {
        String messageId = _messageIdSupplier.get();
        _initiateMessages.add(_builder.publish(messageId, messageDto, initialTargetSto));
        _builder = new InitiateMessage.Builder(_verifyInit);
        return () -> messageId;
    }

    @Override
    public <R, S, I> void unstash(byte[] stash, Class<R> replyClass, Class<S> stateClass, Class<I> incomingClass,
            ProcessLambda<R, S, I> lambda) {
        throw new UnsupportedOperationException("Not implemented in test initiator.");
    }

    @Override
    public <T> Optional<T> getAttribute(Class<T> type, String... name) {
        return Optional.ofNullable(_attributes.get(new AttributeKey(type, name)))
                .map(type::cast);
    }
}
