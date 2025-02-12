package io.mats3.test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.mats3.MatsEndpoint.ProcessContext;
import io.mats3.MatsInitiator.InitiateLambda;
import io.mats3.MatsInitiator.MessageReference;

public class TestProcessContext<R> implements ProcessContext<R> {

    private final List<InitiateMessage> _initiateMessages = new ArrayList<>();
    private final RecordingInitiateLambda _recordingInitiateLambda = new RecordingInitiateLambda(
            () -> String.format("TestMsg%05d", _initiateMessages.size()),
            null,
            _initiateMessages,
            true
    );

    @Override
    public void addBytes(String key, byte[] payload) {
        _recordingInitiateLambda.addBytes(key, payload);
    }

    @Override
    public void addString(String key, String payload) {
        _recordingInitiateLambda.addString(key, payload);
    }

    @Override
    public void setTraceProperty(String propertyName, Object propertyValue) {
        _recordingInitiateLambda.setTraceProperty(propertyName, propertyValue);
    }

    @Override
    public void logMeasurement(String metricId, String metricDescription, String baseUnit, double measure,
            String... labelKeyValue) {
        _recordingInitiateLambda.logMeasurement(metricId, metricDescription, baseUnit, measure, labelKeyValue);
    }

    @Override
    public void logTimingMeasurement(String metricId, String metricDescription, long nanos, String... labelKeyValue) {
        _recordingInitiateLambda.logTimingMeasurement(metricId, metricDescription, nanos, labelKeyValue);
    }

    @Override
    public byte[] stash() {
        throw new UnsupportedOperationException("stash is not implemented for TestProcessContext");
    }

    @Override
    public MessageReference request(String endpointId, Object requestDto) {
        return _recordingInitiateLambda.request(endpointId, requestDto);
    }

    @Override
    public MessageReference reply(R replyDto) {
        return _recordingInitiateLambda.reply(replyDto);
    }

    @Override
    public MessageReference next(Object nextDto) {
        return _recordingInitiateLambda.next(nextDto);
    }

    @Override
    public void nextDirect(Object nextDirectDto) {
        _recordingInitiateLambda.next(nextDirectDto);
    }

    @Override
    public MessageReference goTo(String endpointId, Object gotoDto) {
        return _recordingInitiateLambda.goTo(endpointId, gotoDto);
    }

    @Override
    public MessageReference goTo(String endpointId, Object gotoDto, Object initialTargetSto) {
        return _recordingInitiateLambda.goTo(endpointId, gotoDto);
    }

    @Override
    public void initiate(InitiateLambda lambda) {
        lambda.initiate(new RecordingInitiateLambda(
                () -> String.format("TestMsg%05d", _initiateMessages.size()),
                null,
                _initiateMessages,
                true
        ));
    }

    @Override
    public void doAfterCommit(Runnable runnable) {

    }

    @Override
    public <T> Optional<T> getAttribute(Class<T> type, String... name) {
        return Optional.empty();
    }

    @Override
    public String getTraceId() {
        return "";
    }

    @Override
    public String getEndpointId() {
        return "";
    }

    @Override
    public String getStageId() {
        return "";
    }

    @Override
    public String getFromAppName() {
        return "";
    }

    @Override
    public String getFromAppVersion() {
        return "";
    }

    @Override
    public String getFromStageId() {
        return "";
    }

    @Override
    public Instant getFromTimestamp() {
        return null;
    }

    @Override
    public String getInitiatingAppName() {
        return "";
    }

    @Override
    public String getInitiatingAppVersion() {
        return "";
    }

    @Override
    public String getInitiatorId() {
        return "";
    }

    @Override
    public Instant getInitiatingTimestamp() {
        return null;
    }

    @Override
    public String getMatsMessageId() {
        return "";
    }

    @Override
    public String getSystemMessageId() {
        return "";
    }

    @Override
    public boolean isNonPersistent() {
        return false;
    }

    @Override
    public boolean isInteractive() {
        return false;
    }

    @Override
    public boolean isNoAudit() {
        return false;
    }

    @Override
    public Set<String> getBytesKeys() {
        return Set.of();
    }

    @Override
    public byte[] getBytes(String key) {
        return new byte[0];
    }

    @Override
    public Set<String> getStringKeys() {
        return Set.of();
    }

    @Override
    public String getString(String key) {
        return "";
    }

    @Override
    public <T> T getTraceProperty(String propertyName, Class<T> clazz) {
        return null;
    }
}
