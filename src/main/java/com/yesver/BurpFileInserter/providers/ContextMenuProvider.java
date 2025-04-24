package com.yesver.BurpFileInserter.providers;

import burp.api.montoya.core.ToolType;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import com.yesver.BurpFileInserter.ExtensionContext;
import com.yesver.BurpFileInserter.actions.DeleteMultipartBoundaryHandler;
import com.yesver.BurpFileInserter.actions.FileUploadHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
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

        JMenuItem uploadFileItem = createMenuItem(
                "Add file (raw) to current cursor position",
                new FileUploadHandler(ctx, event)

        );

        JMenuItem uploadFileBase64Item = createMenuItem(
                "Add file (base64) to current cursor position",
                new FileUploadHandler(ctx, event, bytes -> Base64.getEncoder().encode(bytes))
        );

        JMenuItem deleteMultipartBoundaryItem = createMenuItem(
                "Delete multipart/form-data boundary data at cursor",
                new DeleteMultipartBoundaryHandler(ctx, event)
        );

        return List.of(
                uploadFileItem,
                uploadFileBase64Item,
                deleteMultipartBoundaryItem
        );
    }

    private JMenuItem createMenuItem(String label, ActionListener handler) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(handler);
        return item;
    }
}
