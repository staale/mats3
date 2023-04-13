package io.mats3.test.abstractunit;

import io.mats3.MatsEndpoint;
import io.mats3.MatsFactory;
import io.mats3.spring.MatsClassMapping;
import io.mats3.spring.MatsClassMapping.MatsClassMappings;
import io.mats3.spring.MatsSpringAnnotationRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractMatsTestClassMapping {

    private static final Logger log = LoggerFactory.getLogger(AbstractMatsTestClassMapping.class);
    private final Object _prototype;

    protected MatsFactory _matsFactory;
    private final List<MatsClassMapping> _matsClassMappings = new ArrayList<>();

    protected AbstractMatsTestClassMapping(Object prototype) {
        _prototype = prototype;
        Class<?> prototypeClass = _prototype.getClass();
        ;
        if (prototypeClass.isAnnotationPresent(MatsClassMappings.class)) {
            Collections.addAll(_matsClassMappings, prototypeClass.getAnnotation(MatsClassMappings.class).value());
        }
        else if (prototypeClass.isAnnotationPresent(MatsClassMapping.class)) {
            _matsClassMappings.add(prototypeClass.getAnnotation(MatsClassMapping.class));
        }
        else {
            throw new IllegalArgumentException("Expected to find one or more MatsClassMapping annotations on type" +
                    " [" + prototypeClass.getName() + "].");
        }
    }

    /**
     * Return the endpointId for the {@link MatsClassMapping} that has been included. This will return the endpointId
     * of the first {@link MatsClassMapping} annotation if there are multiple annotations.
     */
    public String getEndpointId() {
        MatsClassMapping firstMapping = _matsClassMappings.get(0);
        return firstMapping.endpointId() == null ? firstMapping.value() : firstMapping.endpointId();
    }

    /**
     * Set the {@link MatsFactory} of this class {@link #_matsFactory}. Shall be implemented by the extending class.
     *
     * @param matsFactory
     *            instance to store internally.
     * @return <code>this</code> for chaining.
     */
    public abstract AbstractMatsTestClassMapping setMatsFactory(MatsFactory matsFactory);

    // ======================== JUnit Lifecycle methods ===============================================================

    private boolean isJunit() {
        return getClass().getName().contains(".junit.");
    }

    private String junitOrJupiter() {
        return isJunit() ? "JUnit" : "Jupiter";
    }

    protected String idThis() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(this));
    }

    /**
     * Registers a {@link MatsEndpoint} with the provided {@link MatsFactory}, notice that the {@link MatsFactory} is
     * not set or provided directly through this class through the use of the constructor or a method. It is up to the
     * extending class to provide this factory.
     * <p>
     * The created endpoint is created as a {@link MatsFactory#staged} endpoint, the reason behind this is that a staged
     * endpoint does not require a return unlike a {@link MatsFactory#single}.
     * <p>
     * This method should be called as a result of the following life cycle events for either JUnit or Jupiter:
     * <ul>
     * <li>Before - JUnit - Rule</li>
     * <li>BeforeEachCallback - Jupiter</li>
     * </ul>
     */
    public void before() {
        log.debug("+++ " + junitOrJupiter() + " +++ BEFORE on '" + idThis() + "'.");

        // ?: Is the mats factory defined?
        if (_matsFactory == null) {
            // -> No, then we can't continue. Throw hard.
            String testExecutionListener = isJunit()
                    ? "SpringInjectRulesTestExecutionListener"
                    : "SpringInjectExtensionsTestExecutionListener";
            throw new AbstractMatsTestEndpoint.MatsFactoryNotSetException("== " + getClass().getSimpleName() + " == : MatsFactory is"
                    + " not set, thus cannot complete setup of test endpoint.\n Provide me a MatsFactory either through"
                    + " setting it explicitly through setMatsFactory(...), or if in a Spring testing context, annotate"
                    + " your test class with '@SpringInjectRulesAndExtensions', or if that fails, "
                    + " '@TestExecutionListeners(listeners = " + testExecutionListener
                    + ".class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)' to inject it from the Spring Context.");
        }

        for (MatsClassMapping matsClassMapping : _matsClassMappings) {
            MatsSpringAnnotationRegistration.processMatsClassMapping(
                    _matsFactory, matsClassMapping, _prototype
            );
        }

        log.debug("--- " + junitOrJupiter() + " --- /BEFORE done on '" + idThis() + "'.");
    }

    /**
     * Shutdown and remove the endpoint from the {@link MatsFactory} after test and remove reference to endpoint from
     * field.
     * <p>
     * This method should be called as a result of the following life cycle events for either JUnit or Jupiter:
     * <ul>
     * <li>After - JUnit - Rule</li>
     * <li>AfterEachCallback - Jupiter</li>
     * </ul>
     */
    public void after() {
        log.debug("+++ " + junitOrJupiter() + " +++ AFTER on '" + idThis() + "'.");
        for (MatsClassMapping matsClassMapping : _matsClassMappings) {
            _matsFactory.getEndpoint(matsClassMapping.endpointId()).ifPresent(matsEndpoint ->  matsEndpoint.remove(30_000));
        }
        log.debug("--- " + junitOrJupiter() + " --- /AFTER done on '" + idThis() + "'.");
    }

}
