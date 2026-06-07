package client.gui;

import common.models.Flat;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.*;
import java.util.function.Consumer;

public class CanvasPane extends Pane {

    private static final Color[] PALETTE = {
            Color.web("#3498db"), Color.web("#e74c3c"), Color.web("#2ecc71"),
            Color.web("#f39c12"), Color.web("#9b59b6"), Color.web("#1abc9c"),
            Color.web("#e67e22"), Color.web("#34495e"), Color.web("#e91e63"),
            Color.web("#00bcd4")
    };

    private static final Color BACKGROUND = Color.web("#f0f0f0");
    private static final Color GRID_COLOR = Color.web("#d5d5d5");
    private static final double ANIM_APPEAR_SECS = 0.5;
    private static final double ANIM_REMOVE_SECS = 0.3;

    private final Canvas canvas;
    private final Map<Integer, DrawInfo> drawInfoMap = new LinkedHashMap<>();
    private final Map<String, Color> ownerColors = new HashMap<>();
    private Consumer<Flat> onFlatClick;

    private long lastNanos = -1;

    public CanvasPane() {
        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);

        canvas.widthProperty().addListener(o -> redraw(0));
        canvas.heightProperty().addListener(o -> redraw(0));

        canvas.setOnMouseClicked(e -> handleClick(e.getX(), e.getY()));

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastNanos < 0) { lastNanos = now; return; }
                double dt = (now - lastNanos) / 1_000_000_000.0;
                lastNanos = now;
                boolean needsWork = false;
                for (DrawInfo info : drawInfoMap.values()) {
                    if (info.removing) {
                        info.progress -= dt / ANIM_REMOVE_SECS;
                        if (info.progress < 0) info.progress = 0;
                        needsWork = true;
                    } else if (info.progress < 1.0) {
                        info.progress += dt / ANIM_APPEAR_SECS;
                        if (info.progress > 1.0) info.progress = 1.0;
                        needsWork = true;
                    }
                }
                drawInfoMap.entrySet().removeIf(e -> e.getValue().removing && e.getValue().progress <= 0);
                if (needsWork) redraw(0);
            }
        };
        timer.start();
    }

    public void setOnFlatClick(Consumer<Flat> handler) {
        this.onFlatClick = handler;
    }

    public void setFlats(List<Flat> flats) {
        Set<Integer> newIds = new HashSet<>();
        for (Flat flat : flats) {
            newIds.add(flat.getId());
        }

        // Mark removed flats
        for (Map.Entry<Integer, DrawInfo> entry : drawInfoMap.entrySet()) {
            if (!newIds.contains(entry.getKey()) && !entry.getValue().removing) {
                entry.getValue().removing = true;
            }
        }

        // Add/update flats
        for (Flat flat : flats) {
            DrawInfo info = drawInfoMap.get(flat.getId());
            if (info == null) {
                info = new DrawInfo(flat, 0.0, false);
                drawInfoMap.put(flat.getId(), info);
            } else {
                info.flat = flat;
                info.removing = false;
            }
            ownerColors.computeIfAbsent(flat.getOwnerLogin(), this::colorForOwner);
        }

        redraw(0);
    }

    private void redraw(double ignored) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        if (w <= 0 || h <= 0) return;

        gc.setFill(BACKGROUND);
        gc.fillRect(0, 0, w, h);

        drawGrid(gc, w, h);

        Collection<DrawInfo> infos = drawInfoMap.values();
        if (infos.isEmpty()) return;

        double[] bounds = computeBounds(infos);
        double minX = bounds[0], maxX = bounds[1], minY = bounds[2], maxY = bounds[3];

        double rangeX = Math.max(maxX - minX, 1);
        double rangeY = Math.max(maxY - minY, 1);
        double padding = 60;
        double scaleX = (w - padding * 2) / rangeX;
        double scaleY = (h - padding * 2) / rangeY;
        double scale = Math.min(scaleX, scaleY);

        gc.setFont(Font.font("System", FontWeight.BOLD, 11));
        gc.setTextAlign(TextAlignment.CENTER);

        for (DrawInfo info : infos) {
            double p = info.progress;
            if (p <= 0) continue;

            Flat flat = info.flat;
            double cx = (flat.getCoordinates().getX() - minX) * scale + padding + (w - padding * 2 - rangeX * scale) / 2;
            double cy = h - ((flat.getCoordinates().getY() - minY) * scale + padding + (h - padding * 2 - rangeY * scale) / 2);

            double size = Math.max(20, Math.min(70, Math.sqrt(flat.getArea()) * 1.5));
            double drawSize = size * p;
            double halfW = drawSize * 0.6;
            double halfH = drawSize * 0.45;

            Color base = ownerColors.getOrDefault(flat.getOwnerLogin(), PALETTE[0]);
            Color fill = new Color(base.getRed(), base.getGreen(), base.getBlue(), p);
            Color stroke = base.darker().deriveColor(0, 1, 0.7, p);

            // Building body
            gc.setFill(fill);
            gc.fillRect(cx - halfW, cy - halfH, halfW * 2, halfH * 2);

            // Roof (triangle)
            gc.setFill(fill.deriveColor(0, 1, 0.85, 1));
            double[] roofX = {cx - halfW - 4, cx + halfW + 4, cx};
            double[] roofY = {cy - halfH, cy - halfH, cy - halfH - drawSize * 0.3};
            gc.fillPolygon(roofX, roofY, 3);

            // Windows (simple grid)
            if (p > 0.6) {
                gc.setFill(new Color(1, 1, 0.8, 0.7 * p));
                int cols = Math.max(1, (int) (halfW / 6));
                int rows = Math.max(1, (int) (halfH / 6));
                double ww = halfW * 2 / (cols * 2 + 1);
                double wh = halfH * 2 / (rows * 2 + 1);
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        double wx = cx - halfW + ww * (2 * c + 1);
                        double wy = cy - halfH + wh * (2 * r + 1);
                        gc.fillRect(wx, wy, ww * 0.8, wh * 0.8);
                    }
                }
            }

            // Border
            gc.setStroke(stroke);
            gc.setLineWidth(1.5);
            gc.strokeRect(cx - halfW, cy - halfH, halfW * 2, halfH * 2);

            // Label
            if (p > 0.5) {
                gc.setFill(Color.WHITE);
                gc.setFont(Font.font("System", FontWeight.BOLD, Math.max(9, 11 * p)));
                gc.fillText("#" + flat.getId(), cx, cy + halfH * 0.35);
            }
        }
    }

    private void drawGrid(GraphicsContext gc, double w, double h) {
        gc.setStroke(GRID_COLOR);
        gc.setLineWidth(0.5);
        double step = 40;
        for (double x = 0; x < w; x += step) { gc.strokeLine(x, 0, x, h); }
        for (double y = 0; y < h; y += step) { gc.strokeLine(0, y, w, y); }
    }

    private double[] computeBounds(Collection<DrawInfo> infos) {
        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        for (DrawInfo info : infos) {
            if (info.removing && info.progress <= 0) continue;
            double x = info.flat.getCoordinates().getX();
            double y = info.flat.getCoordinates().getY();
            minX = Math.min(minX, x); maxX = Math.max(maxX, x);
            minY = Math.min(minY, y); maxY = Math.max(maxY, y);
        }
        if (minX == Double.MAX_VALUE) { minX = 0; maxX = 100; minY = 0; maxY = 100; }
        if (Math.abs(maxX - minX) < 1) { minX -= 50; maxX += 50; }
        if (Math.abs(maxY - minY) < 1) { minY -= 50; maxY += 50; }
        return new double[]{minX, maxX, minY, maxY};
    }

    private void handleClick(double mouseX, double mouseY) {
        if (drawInfoMap.isEmpty()) return;

        Collection<DrawInfo> infos = drawInfoMap.values();
        double[] bounds = computeBounds(infos);
        double minX = bounds[0], maxX = bounds[1], minY = bounds[2], maxY = bounds[3];
        double w = canvas.getWidth(), h = canvas.getHeight();
        double rangeX = Math.max(maxX - minX, 1);
        double rangeY = Math.max(maxY - minY, 1);
        double padding = 60;
        double scaleX = (w - padding * 2) / rangeX;
        double scaleY = (h - padding * 2) / rangeY;
        double scale = Math.min(scaleX, scaleY);

        DrawInfo best = null;
        double bestDist = Double.MAX_VALUE;

        for (DrawInfo info : infos) {
            if (info.removing || info.progress < 0.3) continue;
            double cx = (info.flat.getCoordinates().getX() - minX) * scale + padding + (w - padding * 2 - rangeX * scale) / 2;
            double cy = h - ((info.flat.getCoordinates().getY() - minY) * scale + padding + (h - padding * 2 - rangeY * scale) / 2);
            double size = Math.max(20, Math.min(70, Math.sqrt(info.flat.getArea()) * 1.5));
            double halfW = size * info.progress * 0.6;
            double halfH = size * info.progress * 0.45;

            if (mouseX >= cx - halfW && mouseX <= cx + halfW
                    && mouseY >= cy - halfH && mouseY <= cy + halfH) {
                double dist = Math.hypot(mouseX - cx, mouseY - cy);
                if (dist < bestDist) {
                    bestDist = dist;
                    best = info;
                }
            }
        }

        if (best != null && onFlatClick != null) {
            onFlatClick.accept(best.flat);
        }
    }

    private Color colorForOwner(String owner) {
        int hash = Math.abs(owner.hashCode());
        return PALETTE[hash % PALETTE.length];
    }

    private static class DrawInfo {
        Flat flat;
        double progress;
        boolean removing;

        DrawInfo(Flat flat, double progress, boolean removing) {
            this.flat = flat;
            this.progress = progress;
            this.removing = removing;
        }
    }
}
