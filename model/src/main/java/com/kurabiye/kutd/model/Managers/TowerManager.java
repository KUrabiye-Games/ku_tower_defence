package com.kurabiye.kutd.model.Managers;

import java.util.List;

import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.model.Player.Player;
import com.kurabiye.kutd.model.Player.UserPreference;
import com.kurabiye.kutd.model.Projectile.Projectile;
import com.kurabiye.kutd.model.Tile.Tile;
import com.kurabiye.kutd.model.Tile.TileFactory;
import com.kurabiye.kutd.model.Tower.ITower;
import com.kurabiye.kutd.model.Tower.Tower;
import com.kurabiye.kutd.model.Tower.TowerFactory;
import com.kurabiye.kutd.model.Tower.TowerType;
import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;

public class TowerManager {

    UserPreference userPreferences = UserPreference.getInstance(); // Singleton instance for user preferences

    private TowerFactory towerFactory = TowerFactory.getInstance(); // Singleton instance for tower factory

    // Tile Factory to build new Towers
    private TileFactory tileFactory; // Tile factory for creating tiles

    // Dynamic ArrayList to hold the towers
    private List<ITower> towers = new DynamicArrayList<>();

    private GameMap gameMap; // Reference to the game map
    private Player player; // Reference to the player
    


    private List<IEnemy> enemies; // List of enemies in the game, to be set externally


    private ProjectileManager projectileManager; // Reference to the projectile manager


    /**
     * Constructor for the TowerManager class.
     * @param gameMap
     * @param player
     * @param projectileManager
     * @param enemies
     */

    public TowerManager(GameMap gameMap, Player player, ProjectileManager projectileManager, List<IEnemy> enemies) {
        this.tileFactory = new TileFactory(); // Initialize the tile factory
        this.gameMap = gameMap; // Set the game map
        this.player = player; // Set the player
        this.projectileManager = projectileManager; // Set the projectile manager
        this.enemies = enemies; // Set the list of enemies
    }

    /**
     * @requires (x >= 0 && x < gameMapWidth) &&
     *           (y >= 0 && y < gameMapHeight) &&
     *           (type != null) &&
     *           (gameState == GameState.RUNNING) &&
     *           (!isCellOccupied(x, y)) &&
     *           (player.hasEnoughGold(type.getCost()))
     *
     * @modifies gameMap, player, towers
     *
     * @effects Places a tower of the given type at the specified (x, y) coordinates
     *          on the map. Deducts the corresponding amount of gold from the player.
     *          Adds the tower to the game state.
     */
    public boolean buildTower(int xCoordinate, int yCoordinate, TowerType towerType) {
        //check if the tile is buildable
        Tile tile = gameMap.getTile(xCoordinate, yCoordinate);
        if (tile == null) {
            return false; // Invalid tile coordinates
        }
        if (!tile.isBuildableTile()) {
            return false; // Tile is not buildable
        }

        int tileCode;
        switch (towerType) {
            case ARTILLERY:
                tileCode = 20; // Example tile code for tower type 0
                break;
            case MAGE:
                tileCode = 21; // Example tile code for tower type 1
                break;
            case ARCHER:
                tileCode = 26; // Example tile code for tower type 2
                break;
            default:
            return false; // Invalid tower type
        }

        // Check if the player has enough resources
        if(player.getCurrentGold() < userPreferences.getTowerConstructionCost()[towerType.getValue()][0]) { // Example cost check
            return false; // Not enough gold
        }

        player.buyTower(userPreferences.getTowerConstructionCost()[towerType.getValue()][0]); // Deduct cost from player's gold
        // Create the tower using the TowerFactory

        Tower tower = towerFactory.create(towerType); // Create the tower using the factory
        // Set the tower's coordinates
        tower.setTileCoordinate(new TilePoint2D(xCoordinate, yCoordinate));
        // Add the tower to the list of towers
        towers.add(tower);

        Tile towerTile = tileFactory.create(tileCode); // Create the tower tile using the factory
        gameMap.setTile(xCoordinate, yCoordinate, towerTile);

        return true;
    }

    public boolean sellTower(int xCoordinate, int yCoordinate) {
        // Logic to sell a tower
        // Check if the tower exists at the given coordinates

        // look for a tower in the list of towers
        for (ITower tower : towers) {
            if (tower.getTileCoordinate().getTileX() == xCoordinate && tower.getTileCoordinate().getTileY() == yCoordinate) {
                // Tower found, sell it
                player.sellTower(tower.getSellReturn()); // Add sell return to player's gold
                towers.remove(tower); // Remove the tower from the list
                Tile buildableTile = tileFactory.create(15); // Create a buildable tile using the factory
                gameMap.setTile(xCoordinate, yCoordinate, buildableTile);
                return true; // Tower sold successfully
            }
        }
        return false; // Tower not found at the given coordinates
    }


    public boolean upgradeTower(int xCoordinate, int yCoordinate) {
        // Logic to upgrade a tower
        // Check if the tower exists at the given coordinates

        // look for a tower in the list of towers
        for (ITower tower : towers) {
            if (tower.getTileCoordinate().getTileX() == xCoordinate && tower.getTileCoordinate().getTileY() == yCoordinate) {
                // Tower found, upgrade it
                if (tower.canUpgrade()) { // Upgrade the tower


                    int cost = userPreferences.getTowerConstructionCost()[tower.getTowerType().getValue()][tower.getTowerLevel() + 1]; // Get the cost of upgrading the tower

                    // Check if the player has enough resources
                    if(player.getCurrentGold() < cost) { // Example cost check
                        return false; // Not enough gold
                }

                    tower.upgrade(); // Upgrade the tower
                    player.buyTower(cost); // Deduct cost from player's gold

                     int tileCode;

                    switch (tower.getTowerType()) {
                        case ARTILLERY:
                        // TODO: Replace with actual tile codes for each tower type
                            tileCode = 20; // Example tile code for tower type 0
                            break;
                        case MAGE:
                        // TODO: Replace with actual tile codes for each tower type
                            tileCode = 21; // Example tile code for tower type 1
                            break;
                        case ARCHER:
                        // TODO: Replace with actual tile codes for each tower type
                            tileCode = 26; // Example tile code for tower type 2
                            break;
                        default:
                            return false; // Invalid tower type
                        }

                    Tile upgradedTile = tileFactory.create(tileCode); // Create the upgraded tower tile using the factory
                    gameMap.setTile(xCoordinate, yCoordinate, upgradedTile); // Update the tile on the game map
                    return true; // Tower upgraded successfully
                }
            }
        }
        return false; // Tower not found or cannot be upgraded
    }


    /**
     * Creates all the projectiles for the towers
     * @return
     */

    public void createProjectiles(double deltaTime) {
        
        // Towers look for targets and create projectiles
            for (ITower tower : towers) {
                // Check if the tower can attack     
                // Get the projectile from the tower
                Projectile projectile = tower.attack(enemies, deltaTime); // Attack enemies and get the projectile
                if (projectile != null) {
                    projectileManager.addProjectile(projectile); // Add the projectile to the list of projectiles
                }
            }
    }

    public List<ITower> getTowers() {
        return towers; // Return the list of towers
    }

}
