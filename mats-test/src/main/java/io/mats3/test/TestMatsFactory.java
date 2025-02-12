package io.mats3.test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import io.mats3.MatsEndpoint;
import io.mats3.MatsEndpoint.EndpointConfig;
import io.mats3.MatsEndpoint.ProcessSingleLambda;
import io.mats3.MatsEndpoint.ProcessTerminatorLambda;
import io.mats3.MatsFactory;
import io.mats3.MatsInitiator;
import io.mats3.MatsStage.StageConfig;

public class TestMatsFactory implements MatsFactory {
    @Override
    public FactoryConfig getFactoryConfig() {
        return new FactoryConfig() {
            @Override
            public FactoryConfig setName(String name) {
                throw new UnsupportedOperationException("setName is not implemented");
            }

            @Override
            public String getName() {
                return "TestMatsFactory";
            }

            @Override
            public int getNumberOfCpus() {
                return 1;
            }

            @Override
            public FactoryConfig setInitiateTraceIdModifier(Function<String, String> modifier) {
                throw new UnsupportedOperationException("setInitiateTraceIdModifier is not implemented");
            }

            @Override
            public FactoryConfig setMatsDestinationPrefix(String prefix) {
                throw new UnsupportedOperationException("setMatsDestinationPrefix is not implemented");
            }

            @Override
            public String getMatsDestinationPrefix() {
                return "#TEST#";
            }

            @Override
            public FactoryConfig setMatsTraceKey(String key) {
                throw new UnsupportedOperationException("setMatsTraceKey is not implemented");
            }

            @Override
            public String getMatsTraceKey() {
                return "traceId";
            }

            @Override
            public String getAppName() {
                return "Test";
            }

            @Override
            public String getAppVersion() {
                return "0.0.1";
            }

            @Override
            public String getSystemInformation() {
                return "TEST";
            }

            @Override
            public FactoryConfig setNodename(String nodename) {
                throw new UnsupportedOperationException("setNodename is not implemented");
            }

            @Override
            public String getCommonEndpointGroupId() {
                return "TEST";
            }

            @Override
            public FactoryConfig setCommonEndpointGroupId(String commonEndpointGroupId) {
                throw new UnsupportedOperationException("setCommonEndpointGroupId is not implemented");
            }

            @Override
            public String getNodename() {
                return "TEST";
            }

            @Override
            public String getMatsImplementationName() {
                return "TEST";
            }

            @Override
            public String getMatsImplementationVersion() {
                return "0.0.1";
            }

            @Override
            public <T> T instantiateNewObject(Class<T> type) {
                throw new UnsupportedOperationException("instantiateNewObject is not implemented");
            }

            @Override
            public FactoryConfig installPlugin(MatsPlugin plugin) throws IllegalStateException {
                throw new UnsupportedOperationException("instantiateNewObject is not implemented");
            }

            @Override
            public <T extends MatsPlugin> List<T> getPlugins(Class<T> filterByClass) {
                return Collections.emptyList();
            }

            @Override
            public boolean removePlugin(MatsPlugin instanceToRemove) {
                throw new UnsupportedOperationException("instantiateNewObject is not implemented");
            }

            @Override
            public FactoryConfig setAttribute(String key, Object value) {
                throw new UnsupportedOperationException("instantiateNewObject is not implemented");
            }

            @Override
            public FactoryConfig setConcurrency(int concurrency) {
                throw new UnsupportedOperationException("instantiateNewObject is not implemented");
            }

            @Override
            public FactoryConfig setInteractiveConcurrency(int concurrency) {
                throw new UnsupportedOperationException("instantiateNewObject is not implemented");
            }

            @Override
            public int getConcurrency() {
                return 1;
            }

            @Override
            public boolean isConcurrencyDefault() {
                return false;
            }

            @Override
            public int getInteractiveConcurrency() {
                return 1;
            }

            @Override
            public boolean isInteractiveConcurrencyDefault() {
                return false;
            }

            @Override
            public boolean isRunning() {
                return true;
            }

            @Override
            public <T> T getAttribute(String key) {
                return null;
            }
        };
    }

    @Override
    public <R, S> MatsEndpoint<R, S> staged(String endpointId, Class<R> replyClass, Class<S> stateClass) {
        throw new UnsupportedOperationException("staged is not implemented for TestMatsFactory");
    }

    @Override
    public <R, S> MatsEndpoint<R, S> staged(String endpointId, Class<R> replyClass, Class<S> stateClass,
            Consumer<? super EndpointConfig<R, S>> endpointConfigLambda) {
        throw new UnsupportedOperationException("staged is not implemented for TestMatsFactory");
    }

    @Override
    public <R, I> MatsEndpoint<R, Void> single(String endpointId, Class<R> replyClass, Class<I> incomingClass,
            ProcessSingleLambda<R, I> processor) {
        throw new UnsupportedOperationException("single is not implemented for TestMatsFactory");
    }

    @Override
    public <R, I> MatsEndpoint<R, Void> single(String endpointId, Class<R> replyClass, Class<I> incomingClass,
            Consumer<? super EndpointConfig<R, Void>> endpointConfigLambda,
            Consumer<? super StageConfig<R, Void, I>> stageConfigLambda, ProcessSingleLambda<R, I> processor) {
        throw new UnsupportedOperationException("single is not implemented for TestMatsFactory");
    }

    @Override
    public <S, I> MatsEndpoint<Void, S> terminator(String endpointId, Class<S> stateClass, Class<I> incomingClass,
            ProcessTerminatorLambda<S, I> processor) {
        throw new UnsupportedOperationException("terminator is not implemented for TestMatsFactory");
    }

    @Override
    public <S, I> MatsEndpoint<Void, S> terminator(String endpointId, Class<S> stateClass, Class<I> incomingClass,
            Consumer<? super EndpointConfig<Void, S>> endpointConfigLambda,
            Consumer<? super StageConfig<Void, S, I>> stageConfigLambda, ProcessTerminatorLambda<S, I> processor) {
        throw new UnsupportedOperationException("terminator is not implemented for TestMatsFactory");
    }

    @Override
    public <S, I> MatsEndpoint<Void, S> subscriptionTerminator(String endpointId, Class<S> stateClass,
            Class<I> incomingClass, ProcessTerminatorLambda<S, I> processor) {
        throw new UnsupportedOperationException("subscriptionTerminator is not implemented for TestMatsFactory");
    }

    @Override
    public <S, I> MatsEndpoint<Void, S> subscriptionTerminator(String endpointId, Class<S> stateClass,
            Class<I> incomingClass, Consumer<? super EndpointConfig<Void, S>> endpointConfigLambda,
            Consumer<? super StageConfig<Void, S, I>> stageConfigLambda, ProcessTerminatorLambda<S, I> processor) {
        throw new UnsupportedOperationException("subscriptionTerminator is not implemented for TestMatsFactory");
    }

    @Override
    public List<MatsEndpoint<?, ?>> getEndpoints() {
        return Collections.emptyList();
    }

    @Override
    public Optional<MatsEndpoint<?, ?>> getEndpoint(String endpointId) {
        return Optional.empty();
    }

    @Override
    public MatsInitiator getDefaultInitiator() {
        return TestMatsInitiator.create();
    }

    @Override
    public MatsInitiator getOrCreateInitiator(String name) {
        return TestMatsInitiator.create(name);
    }

    @Override
    public List<MatsInitiator> getInitiators() {
        return Collections.emptyList();
    }

    @Override
    public void start() {

    }

    @Override
    public void holdEndpointsUntilFactoryIsStarted() {

    }

    @Override
    public boolean waitForReceiving(int timeoutMillis) {
        return true;
    }

    @Override
    public boolean stop(int gracefulShutdownMillis) {
        return true;
    }
}
