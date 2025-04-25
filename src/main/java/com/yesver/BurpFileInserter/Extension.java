package com.yesver.BurpFileInserter;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.yesver.BurpFileInserter.providers.ContextMenuProvider;
import com.yesver.BurpFileInserter.utils.Logger;

public class Extension implements BurpExtension {
    @Override
    public void initialize(MontoyaApi api) {
        ExtensionContext ctx = new ExtensionContext(
                api,
                new Logger(api)
        );

        api.extension().setName("Burp Request File Inserter");
        api.userInterface().registerContextMenuItemsProvider(new ContextMenuProvider(ctx));
    }
}