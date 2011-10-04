package com.atoito.please.core.api;

/**
 * Registry that want access to the classpath should implement this interface.
 *
 */
public interface ClassLoaderAwareRegistry {

    void setClassLoader(ClassLoader classLoader);
}
