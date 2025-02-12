package io.mats3.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.mats3.MatsInitiator.KeepTrace;

class InitiateMessage {

    enum ReplyToType {
        REPLY_TO_ENDPOINT,
        REPLY_TO_SUBSCRIPTION
    }

    enum RequestType {
        REQUEST,
        SEND,
        PUBLISH,
        REPLY,
        NEXT,
        GOTO
    }

    private final RequestType _requestType;
    private final String _messageId;
    private final Object _requestDto;
    private final Object _initialTargetSto;
    private final CharSequence _traceId;
    private final KeepTrace _keepTrace;
    private final Long _timeToLiveMillis;
    private final Boolean _nonPersistent;
    private final Boolean _interactive;
    private final Boolean _noAudit;
    private final String _from;
    private final String _to;
    private final ReplyToType _replyToType;
    private final String _replyTo;
    private final Object _replySto;
    private final Map<String, Object> _traceProperties;
    private final Map<String, byte[]> _bytes;
    private final Map<String, Object> _strings;
    private final List<LogMeasurement> _logMeasurements;

    private InitiateMessage(RequestType requestType,
            String messageId,
            Object requestDto,
            Object initialTargetSto,
            CharSequence traceId,
            KeepTrace keepTrace,
            Long timeToLiveMillis,
            Boolean nonPersistent,
            Boolean interactive,
            Boolean noAudit,
            String from,
            String to,
            ReplyToType replyToType,
            String replyTo,
            Object replySto,
            Map<String, Object> traceProperties,
            Map<String, byte[]> bytes,
            Map<String, Object> strings,
            List<LogMeasurement> logMeasurements) {
        _requestType = requestType;
        _messageId = messageId;
        _requestDto = requestDto;
        _initialTargetSto = initialTargetSto;
        _traceId = traceId;
        _keepTrace = keepTrace;
        _timeToLiveMillis = timeToLiveMillis;
        _nonPersistent = nonPersistent;
        _interactive = interactive;
        _noAudit = noAudit;
        _from = from;
        _to = to;
        _replyToType = replyToType;
        _replyTo = replyTo;
        _replySto = replySto;
        _traceProperties = traceProperties;
        _bytes = bytes;
        _strings = strings;
        _logMeasurements = logMeasurements;
    }

    String getId() {
        return _messageId;
    }

    boolean anyMatch(List<InitiateMessage> candidates) {
        for (InitiateMessage candidate : candidates) {
            ArrayList<String> errorMessages = new ArrayList<>();
            addMismatchMessages(Collections.singletonList(candidate), errorMessages::add);
            if (errorMessages.isEmpty()) return true;
        }
        return false;
    }

    void addMismatchMessages(List<InitiateMessage> candidates, Consumer<String> errorMessages) {
        addMismatchIfPresent("requestType", _requestType, msg -> msg._requestType, candidates, errorMessages);
        addMismatchIfPresent("to", _to, msg -> msg._to, candidates, errorMessages);
        addMismatchIfPresent("from", _from, msg -> msg._from, candidates, errorMessages);
        addMismatchIfPresent("traceId", _traceId, msg -> msg._traceId, candidates, errorMessages);
        addMismatchIfPresent("replyTo", _replyTo, msg -> msg._replyTo, candidates, errorMessages);
        addMismatchIfPresent("replyToState", _replySto, msg -> msg._replySto, candidates, errorMessages);
        addMismatchIfPresent("replyToType", _replyToType, msg -> msg._replyToType, candidates, errorMessages);
        addMismatchIfPresent("requestDto", _requestDto, msg -> msg._requestDto, candidates, errorMessages);
        addMismatchIfPresent("initialTargetState", _initialTargetSto, msg -> msg._initialTargetSto, candidates, errorMessages);
        addMismatchIfPresent("keepTrace", _keepTrace, msg -> msg._keepTrace, candidates, errorMessages);
        addMismatchIfPresent("timeToLiveMillis", _timeToLiveMillis, msg -> msg._timeToLiveMillis, candidates, errorMessages);
        addMismatchIfPresent("nonPersistent", _nonPersistent, msg -> msg._nonPersistent, candidates, errorMessages);
        addMismatchIfPresent("interactive", _interactive, msg -> msg._interactive, candidates, errorMessages);
        addMismatchIfPresent("noAudit", _noAudit, msg -> msg._noAudit, candidates, errorMessages);
        addMismatchIfPresent("traceProperties", _traceProperties, msg -> msg._traceProperties, candidates, errorMessages);
        addMismatchIfPresent("bytes", _bytes, msg -> msg._bytes, candidates, errorMessages);
        addMismatchIfPresent("strings", _strings, msg -> msg._strings, candidates, errorMessages);
        addMismatchIfPresent("logMeasurements", _logMeasurements, msg -> msg._logMeasurements, candidates, errorMessages);

    }

    private <X> void addMismatchIfPresent(
            String field,
            X value,
            Function<InitiateMessage, X> extractor,
            List<InitiateMessage> candidates,
            Consumer<String> errorMessages) {
        if (value == null) {
            return;
        }
        if (value instanceof Collection<?> && ((Collection<?>) value).isEmpty()) {
            return;
        }
        if (value instanceof Map<?, ?> && ((Map<?, ?>) value).isEmpty()) {
            return;
        }
        Set<X> presentValues = candidates.stream().map(extractor).collect(Collectors.toSet());

        if (!presentValues.contains(value)) {
            errorMessages.accept("Expected " + field + ": [" + value + "] not found, all values found: " + presentValues);
        }
    }


    static class Builder {

        private CharSequence _traceId;
        private KeepTrace _keepTrace;
        private Long _timeToLiveMillis;
        private Boolean _nonPersistent;
        private Boolean _interactive;
        private Boolean _noAudit;
        private String _from;
        private String _to;
        private ReplyToType _replyToType;
        private String _replyTo;
        private Object _replySto;
        private final Map<String, Object> _traceProperties = new HashMap<>();
        private final Map<String, byte[]> _bytes = new HashMap<>();
        private final Map<String, Object> _strings = new HashMap<>();
        private final List<LogMeasurement> _logMeasurements = new ArrayList<>();

        private final boolean _verifyInit;

        Builder(boolean verifyInit) {
            _verifyInit = verifyInit;
        }

        void traceId(CharSequence traceId) {
            _traceId = traceId;
        }

        void keepTrace(KeepTrace keepTrace) {
            _keepTrace = keepTrace;
        }

        void nonPersistent(long timeToLiveMillis) {
            _timeToLiveMillis = timeToLiveMillis;
            _nonPersistent = true;
        }

        void interactive() {
            _interactive = true;
        }

        void noAudit() {
            _noAudit = true;
        }

        void from(String initiatorId) {
            _from = initiatorId;
        }

        void to(String endpointId) {
            _to = endpointId;
        }

        void replyTo(String endpointId, Object replySto) {
            _replyToType = ReplyToType.REPLY_TO_ENDPOINT;
            _replyTo = endpointId;
            _replySto = replySto;
        }

        void replyToSubscription(String subscriptionId, Object replySto) {
            _replyToType = ReplyToType.REPLY_TO_SUBSCRIPTION;
            _replyTo = subscriptionId;
            _replySto = replySto;
        }

        void setTraceProperty(String key, Object value) {
            _traceProperties.put(key, value);
        }


        void addBytes(String key, byte[] payload) {
            _bytes.put(key, payload);
        }

        void addString(String key, String value) {
            _strings.put(key, value);
        }

        void logMeasurement(
                String metricId,
                String metricDescription,
                String baseUnit,
                double measure,
                String... labelKeyValue) {
            _logMeasurements.add(new LogMeasurement(metricId, metricDescription, baseUnit, measure, labelKeyValue));
        }


        void logTimingMeasurement(
                String metricId,
                String metricDescription,
                long nanos,
                String... labelKeyValue) {
            logMeasurement(metricId, metricDescription, "nanos", nanos, labelKeyValue);
        }

        InitiateMessage request(String messageId, Object requestDto, Object initialTargetSto) {
            return createMessage(RequestType.REQUEST, messageId, requestDto, initialTargetSto);
        }

        InitiateMessage send(String messageId, Object requestDto, Object initialTargetSto) {
            return createMessage(RequestType.SEND, messageId, requestDto, initialTargetSto);
        }

        InitiateMessage publish(String messageId, Object requestDto, Object initialTargetSto) {
            return createMessage(RequestType.PUBLISH, messageId, requestDto, initialTargetSto);
        }

        InitiateMessage goTo(String messageId, Object requestDto, Object initialTargetSto) {
            return createMessage(RequestType.GOTO, messageId, requestDto, initialTargetSto);
        }

        InitiateMessage reply(String messageId, Object requestDto) {
            return createMessage(RequestType.REPLY, messageId, requestDto, null);
        }

        InitiateMessage next(String messageId, Object requestDto) {
            return createMessage(RequestType.NEXT, messageId, requestDto, null);
        }

        private InitiateMessage createMessage(RequestType requestType, String messageId, Object requestDto,
                Object initialTargetSto) {
            if (_verifyInit && _traceId == null) throw new IllegalArgumentException("Missing 'traceId'");
            if (_verifyInit && _from == null) throw new IllegalArgumentException("Missing 'from'");
            if (_verifyInit && _to == null) throw new IllegalArgumentException("Missing 'to'");
            return new InitiateMessage(
                    requestType,
                    messageId,
                    requestDto,
                    initialTargetSto,
                    _traceId,
                    _keepTrace,
                    _timeToLiveMillis,
                    _nonPersistent,
                    _interactive,
                    _noAudit,
                    _from,
                    _to,
                    _replyToType,
                    _replyTo,
                    _replySto,
                    _traceProperties,
                    _bytes,
                    _strings,
                    _logMeasurements);
        }
    }

    private static class LogMeasurement {
        private final String _metricId;
        private final String _metricDescription;
        private final String _baseUnit;
        private final double _measure;
        private final String[] _labelKeyValue;

        LogMeasurement(String metricId, String metricDescription, String baseUnit, double measure,
                String... labelKeyValue) {
            _metricId = metricId;
            _metricDescription = metricDescription;
            _baseUnit = baseUnit;
            _measure = measure;
            _labelKeyValue = labelKeyValue;
        }
    }

}
