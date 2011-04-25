package org.inigma.shared.config;

/**
 * Hook for being notified of updates to a configuration.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public interface ConfigurationObserver {
    void onConfigurationUpdate(String key, Object original, Object current);
}
