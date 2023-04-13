package io.mats3.test.jupiter;

import io.mats3.MatsFactory;
import io.mats3.test.abstractunit.AbstractMatsTestClassMapping;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.inject.Inject;

public class Extension_MatsClassMapping extends AbstractMatsTestClassMapping implements BeforeEachCallback,
        AfterEachCallback {

    private Extension_MatsClassMapping(Object prototype) {
        super(prototype);
    }

    /**
     * Sets the internal {@link MatsFactory} to be utilized for the creation of this endpoint.
     * <p>
     * If not utilized explicitly can also be injected/autowired through the use of the test execution listener
     * <code>SpringInjectRulesAndExtensions</code> should this Extension be utilized in a test where a Spring context
     * is in play.
     *
     * @param matsFactory
     *            to set.
     * @return this instance of the object.
     */
    @Inject
    @Override
    public Extension_MatsClassMapping setMatsFactory(MatsFactory matsFactory) {
        _matsFactory = matsFactory;
        return this;
    }

    /**
     * Creates a Jupiter Extension for a class annotated with {@link io.mats3.spring.MatsClassMapping}, based
     * on the provided prototype.
     *
     * The provided prototype is used as a base for instantiating the state and any injected values for each
     * {@link io.mats3.spring.MatsClassMapping.Stage} found within the {@link io.mats3.spring.MatsClassMapping}.
     *
     * @param matsClassMappingPrototype an initialized instance of the {@link io.mats3.spring.MatsClassMapping} instance,
     *                                 with all dependencies injected.
     * @return {@link Extension_Mats} without a predefined processLambda.
     */
    public static Extension_MatsClassMapping create(Object matsClassMappingPrototype) {
        return new Extension_MatsClassMapping(matsClassMappingPrototype);
    }

    /**
     * Convenience variant of {@link #create(String, Class, Class) create(endpointId, replyClass, incomingClass)} taking
     * a {@link Extension_Mats} as first argument for fetching the {@link MatsFactory}, for use in "pure Java"
     * environments (read as: non-Spring).
     */
    public static Extension_MatsClassMapping create(Extension_Mats matsRule, Object matsClassMappingPrototype) {
        Extension_MatsClassMapping extension_matsEndpoint = create(matsClassMappingPrototype);
        // Set MatsFactory from the supplied Extension_Mats
        extension_matsEndpoint.setMatsFactory(matsRule.getMatsFactory());
        return extension_matsEndpoint;
    }

    // ================== Jupiter LifeCycle ===========================================================================

    @Override
    public void beforeEach(ExtensionContext context) {
        super.before();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        super.after();
    }
}
