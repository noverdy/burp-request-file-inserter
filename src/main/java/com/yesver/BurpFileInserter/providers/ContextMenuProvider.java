package com.yesver.BurpFileInserter.providers;

import burp.api.montoya.core.ToolType;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import com.yesver.BurpFileInserter.ExtensionContext;
import com.yesver.BurpFileInserter.actions.FileUploadHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;


public class ContextMenuProvider implements ContextMenuItemsProvider {
    private final ExtensionContext ctx;

    public ContextMenuProvider(ExtensionContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event) {
        if (!event.isFromTool(ToolType.REPEATER)) {
            return null;
        }

        JMenuItem uploadFileItem = createUploadMenuItem(
                ctx,
                "Add file (raw) to current cursor position",
                event,
                Function.identity()
        );

        JMenuItem uploadFileBase64Item = createUploadMenuItem(
                ctx,
                "Add file (base64) to current cursor position",
                event,
                bytes -> Base64.getEncoder().encode(bytes)
        );

        return List.of(uploadFileItem, uploadFileBase64Item);
    }

    private JMenuItem createUploadMenuItem(
            ExtensionContext ctx,
            String label,
            ContextMenuEvent event,
            Function<byte[], byte[]> dataProcessor
    ) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(new FileUploadHandler(ctx, event, dataProcessor));
        return item;
    }
}
