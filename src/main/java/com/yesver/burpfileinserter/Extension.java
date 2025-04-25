package com.yesver.burpfileinserter;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.yesver.burpfileinserter.providers.ContextMenuProvider;
import com.yesver.burpfileinserter.utils.Logger;

public class Extension implements BurpExtension {
    @Override
    public void initialize(MontoyaApi api) {
        ExtensionContext ctx = new ExtensionContext(
                api,
                new Logger(api)
        );

        api.extension().setName("Request File Inserter");
        api.userInterface().registerContextMenuItemsProvider(new ContextMenuProvider(ctx));
    }
}