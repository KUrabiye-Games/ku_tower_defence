package com.kurabiye.kutd.model;

import com.kurabiye.kutd.model.Tile;
import com.kurabiye.kutd.model.util.Point;

public class MapManager {

    private int width, height;
    private Tile[][] grid;
    private Point startPoint;
    private Point endPoint;

    public MapManager(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Tile[height][width];
    }

    // === Tile Placement ===
    public void placeTile(String type, int x, int y) {
        Point position = new Point(x, y);

        switch (type.toUpperCase()) {
            case "PATH":
                grid[y][x] = new PathTile(position);
                break;
            case "TOWER_SLOT":
                grid[y][x] = new TowerSlotTile(position);
                break;
            case "DECORATION":
                grid[y][x] = new DecorationTile(position);
                break;
            default:
                grid[y][x] = null; // empty
        }
    }

    public Tile getTile(int x, int y) {
        return grid[y][x];
    }

    public void setStartPoint(int x, int y) {
        if (isOnEdge(x, y)) {
            startPoint = new Point(x, y);
        } else {
            throw new IllegalArgumentException("Start point must be on the edge.");
        }
    }

    public void setEndPoint(int x, int y) {
        if (isOnEdge(x, y)) {
            endPoint = new Point(x, y);
        } else {
            throw new IllegalArgumentException("End point must be on the edge.");
        }
    }

    // === Validation ===

    public boolean isValidMap() {
        return startPoint != null &&
               endPoint != null &&
               hasContinuousPath() &&
               countTowerSlots() >= 4;
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;
    
        public Direction getOpposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }
    }

    private boolean hasContinuousPath() {
    if (startPoint == null || endPoint == null) return false;

    Set<Point> visited = new HashSet<>();
    Point currentPos = startPoint;

    while (true) {
        if (visited.contains(currentPos)) return false; // prevent loops
        visited.add(currentPos);

        Tile currentTile = getTile((int) currentPos.getX(), (int) currentPos.getY());
        if (!(currentTile instanceof PathTile currentPathTile)) return false;

        Direction exitDir = currentPathTile.getExitDirection();
        Point nextPos = getNextPosition(currentPos, exitDir);
        if (nextPos == null) return false;

        Tile nextTile = getTile((int) nextPos.getX(), (int) nextPos.getY());
        if (!(nextTile instanceof PathTile nextPathTile)) return false;

        // Must match exit→entry transition
        if (nextPathTile.getEntryDirection() != exitDir.getOpposite()) return false;

        if (nextPos.equals(endPoint)) return true;

        currentPos = nextPos;
    }
}

    private Point getNextPosition(Point current, Direction dir) {
        int x = (int) current.getX();
        int y = (int) current.getY();

        return switch (dir) {
            case UP -> isInBounds(x, y - 1) ? new Point(x, y - 1) : null;
            case DOWN -> isInBounds(x, y + 1) ? new Point(x, y + 1) : null;
            case LEFT -> isInBounds(x - 1, y) ? new Point(x - 1, y) : null;
            case RIGHT -> isInBounds(x + 1, y) ? new Point(x + 1, y) : null;
        };
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private boolean isOnEdge(int x, int y) {
        return x == 0 || y == 0 || x == width - 1 || y == height - 1;
    }

    private int countTowerSlots() {
        int count = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x] instanceof TowerSlotTile) count++;
            }
        }
        return count;
    }

    // === Export ===

    public CustomMap toCustomMap() {
        if (!isValidMap()) throw new IllegalStateException("Map is not valid.");

        CustomMap map = new CustomMap();
        map.setStartPoint(startPoint);
        map.setEndPoint(endPoint);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = grid[y][x];
                if (tile != null) {
                    map.addTile(tile);
                }
            }
        }

        return map;
    }
}