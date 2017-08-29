package com.nixmash.shiro.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.shiro.auth.NixmashRealm;
import io.bootique.shiro.ShiroModule;

public class TestModule implements Module {

    @Override
    public void configure(Binder binder) {
        ShiroModule.extend(binder).addRealm(NixmashRealm.class);
    }
}
