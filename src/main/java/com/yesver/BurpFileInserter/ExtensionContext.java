package com.yesver.BurpFileInserter;

import burp.api.montoya.MontoyaApi;
import com.yesver.BurpFileInserter.utils.Logger;

public record ExtensionContext(MontoyaApi api, Logger logger) {}
