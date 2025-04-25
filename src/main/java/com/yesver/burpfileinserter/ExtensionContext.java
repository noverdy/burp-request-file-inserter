package com.yesver.burpfileinserter;

import burp.api.montoya.MontoyaApi;
import com.yesver.burpfileinserter.utils.Logger;

public record ExtensionContext(MontoyaApi api, Logger logger) {}
