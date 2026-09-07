package com.takia.lets_chess.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.takia.lets_chess.Letschess;
import com.takia.lets_chess.block.ModBlocks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MiniChessboardScreen extends AbstractContainerScreen<MiniChessboardMenu> {
    private static final ResourceLocation CHESS_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("lets_chess", "textures/gui/container/mini_chessboard_chess.png");
    private static final ResourceLocation XIANGQI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("lets_chess", "textures/gui/container/mini_chessboard_xiangqi.png");
    private static final ResourceLocation FLIGHTCHESS_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("lets_chess", "textures/gui/container/mini_chessboard_flightchess.png");

    private static final ResourceLocation TAKE_BACK_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("lets_chess", "textures/gui/sprites/container/mini_chessboard/take_back.png");
    private static final ResourceLocation TAKE_BACK_HIGHLIGHTED =
            ResourceLocation.fromNamespaceAndPath("lets_chess", "textures/gui/sprites/container/mini_chessboard/take_back_highlighted.png");
    private static final ResourceLocation TAKE_BACK_PRESSED =
            ResourceLocation.fromNamespaceAndPath("lets_chess", "textures/gui/sprites/container/mini_chessboard/take_back_pressed.png");
    private static final ResourceLocation ARRANGE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("lets_chess", "textures/gui/sprites/container/mini_chessboard/arrange.png");
    private static final ResourceLocation ARRANGE_HIGHLIGHTED =
            ResourceLocation.fromNamespaceAndPath("lets_chess", "textures/gui/sprites/container/mini_chessboard/arrange_highlighted.png");
    private static final ResourceLocation ARRANGE_PRESSED =
            ResourceLocation.fromNamespaceAndPath("lets_chess", "textures/gui/sprites/container/mini_chessboard/arrange_pressed.png");

    private static final int MESSAGE_CENTER_X = 87;
    private static final int MESSAGE_CENTER_Y = 59;

    private CustomButton takeBackButton;
    private CustomButton arrangeButton;

    private Component currentMessage = Component.empty();
    private int messageDisplayTicks = 0;

    private final String boardType;
    private final ResourceLocation backgroundTexture;
    private final String titleKey;

    public MiniChessboardScreen(MiniChessboardMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = 59;
        this.titleLabelY = 13;

        this.boardType = determineBoardType();
        this.backgroundTexture = switch (boardType) {
            case "xiangqi" -> XIANGQI_TEXTURE;
            case "flightchess" -> FLIGHTCHESS_TEXTURE;
            default -> CHESS_TEXTURE;
        };
        this.titleKey = switch (boardType) {
            case "xiangqi" -> "container.lets_chess.mini_chessboard_xiangqi.gui_title";
            case "flightchess" -> "container.lets_chess.mini_chessboard_flightchess.gui_title";
            default -> "container.lets_chess.mini_chessboard_chess.gui_title";
        };

        Letschess.LOGGER.info("[Screen] 创建GUI: menuPos={}, boardType={}", menu.getChessboardPosition(), boardType);
    }

    private String determineBoardType() {
        var level = net.minecraft.client.Minecraft.getInstance().level;
        if (level == null) return "chess";
        var state = level.getBlockState(menu.getChessboardPosition());
        if (state.is(ModBlocks.MINI_CHESSBOARD_XIANGQI.get())) return "xiangqi";
        if (state.is(ModBlocks.MINI_CHESSBOARD_FLIGHTCHESS.get())) return "flightchess";
        return "chess";
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.takeBackButton = new CustomButton(x + 68, y + 28, 16, 16,
                this::onTakeBackClick,
                TAKE_BACK_TEXTURE, TAKE_BACK_HIGHLIGHTED, TAKE_BACK_PRESSED);
        this.addRenderableWidget(takeBackButton);

        this.arrangeButton = new CustomButton(x + 92, y + 28, 16, 16,
                this::onArrangeClick,
                ARRANGE_TEXTURE, ARRANGE_HIGHLIGHTED, ARRANGE_PRESSED);
        this.addRenderableWidget(arrangeButton);

        Letschess.LOGGER.info("[Screen] GUI初始化完成: leftPos={}, topPos={}, boardType={}", this.leftPos, this.topPos, boardType);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(backgroundTexture, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font,
                Component.translatable(titleKey),
                this.titleLabelX, this.titleLabelY, 4210752, false);

        guiGraphics.drawString(this.font, this.playerInventoryTitle,
                this.inventoryLabelX, this.inventoryLabelY, 4210752, false);

        if (currentMessage != null && !currentMessage.getString().isEmpty()) {
            int msgWidth = this.font.width(currentMessage);
            int msgX = MESSAGE_CENTER_X - msgWidth / 2;
            int msgY = MESSAGE_CENTER_Y - 4;
            guiGraphics.drawString(this.font, currentMessage, msgX, msgY, 0xFFFFFF, true);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        if (this.takeBackButton.isMouseOver(mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font,
                    Component.translatable("container.lets_chess.mini_chessboard.take_back"),
                    mouseX, mouseY);
        } else if (this.arrangeButton.isMouseOver(mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font,
                    Component.translatable("container.lets_chess.mini_chessboard.arrange"),
                    mouseX, mouseY);
        }

        if (messageDisplayTicks > 0) {
            messageDisplayTicks--;
            if (messageDisplayTicks == 0) {
                currentMessage = Component.empty();
            }
        }
    }

    public void setMessage(Component message) {
        Letschess.LOGGER.info("[Screen] 设置消息: {}", message.getString());
        this.currentMessage = message;
        this.messageDisplayTicks = 100;
    }

    private void onTakeBackClick() {
        Letschess.LOGGER.info("[Screen] 点击回收按钮, chessboardPos={}", this.menu.getChessboardPosition());
        this.menu.sendTakeBackPacket();
    }

    private void onArrangeClick() {
        Letschess.LOGGER.info("[Screen] 点击摆放按钮, chessboardPos={}", this.menu.getChessboardPosition());
        this.menu.sendArrangePacket();
    }

    private class CustomButton extends AbstractWidget {
        private ResourceLocation normalTexture;
        private ResourceLocation highlightedTexture;
        private ResourceLocation pressedTexture;
        private boolean isPressed = false;
        private long pressStartTime = 0;
        private static final long PRESS_DURATION_MS = 150;
        private final Runnable onPress;

        public CustomButton(int x, int y, int width, int height, Runnable onPress,
                            ResourceLocation normalTexture, ResourceLocation highlightedTexture,
                            ResourceLocation pressedTexture) {
            super(x, y, width, height, Component.empty());
            this.onPress = onPress;
            this.normalTexture = normalTexture;
            this.highlightedTexture = highlightedTexture;
            this.pressedTexture = pressedTexture;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            ResourceLocation texture;
            if (isPressed && pressedTexture != null) {
                long elapsed = System.currentTimeMillis() - pressStartTime;
                if (elapsed >= PRESS_DURATION_MS) {
                    isPressed = false;
                    texture = isHoveredOrFocused() ? highlightedTexture : normalTexture;
                } else {
                    texture = pressedTexture;
                }
            } else {
                texture = isHoveredOrFocused() ? highlightedTexture : normalTexture;
            }
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            guiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.width, this.height, 16, 16);
            RenderSystem.disableBlend();
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            if (pressedTexture != null) {
                this.isPressed = true;
                this.pressStartTime = System.currentTimeMillis();
            }
            this.onPress.run();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}
    }
}