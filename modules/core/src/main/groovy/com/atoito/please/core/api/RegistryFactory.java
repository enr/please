package com.atoito.please.core.api;

import com.atoito.please.core.impl.DefaultRegistry;

public class RegistryFactory {

    public static PleaseRegistry getRegistry() {
        return DefaultRegistry.INSTANCE;
    }
}
