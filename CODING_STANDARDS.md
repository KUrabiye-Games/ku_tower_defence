# Coding Standards and Guidelines

This document outlines the coding standards and best practices for our tower defense game project. Following these guidelines will help maintain code consistency, readability, and quality across the team.

## Table of Contents
1. [Naming Conventions](#naming-conventions)
2. [Documentation Standards](#documentation-standards)
3. [Code Organization](#code-organization)
4. [JavaFX Specific Guidelines](#javafx-specific-guidelines)
5. [Git Workflow](#git-workflow)
6. [Testing Guidelines](#testing-guidelines)

## Naming Conventions

### Classes and Interfaces
- Use **PascalCase** (also known as UpperCamelCase)
- Names should be nouns or noun phrases
- Be descriptive and avoid abbreviations
- Examples:
  - `Tower`, `Enemy`, `Projectile`, `GameController`
  - `TowerDefenseGame`, `EnemyPathfinder`

### Methods
- Use **camelCase** (starts with lowercase letter)
- Names should be verbs or verb phrases
- Be descriptive of the action being performed
- Examples:
  - `calculateDamage()`, `moveEnemy()`, `fireProjectile()`
  - `isEnemyInRange()`, `canAffordTower()`

### Variables
- Use **camelCase**
- Choose meaningful and descriptive names
- Avoid single letter variables except in very short blocks like loops
- Examples:
  - `enemyHealth`, `towerCost`, `gameScore`

### Constants
- Use **UPPER_SNAKE_CASE** (all uppercase with underscores)
- Declare as `static final`
- Examples:
  - `DEFAULT_ENEMY_SPEED`, `TOWER_DAMAGE_MULTIPLIER`, `MAX_PLAYER_LIVES`

### Static Variables
- Use **camelCase** prefixed with `s`
- Examples:
  - `sInstanceCount`, `sGameSettings`, `sEnemyTypes`

### Package Names
- Use lowercase, without underscores
- Use reversed domain name convention
- Examples:
  - `com.teamname.towerdefense`
  - `com.teamname.towerdefense.enemies`

### Resource Files
- Use **lowercase_with_underscores**
- Examples:
  - `tower_cannon.png`, `enemy_fast.png`
  - `victory_sound.mp3`, `background_music.mp3`

### Enums
- Enum types should use **PascalCase**
- Enum values should use **UPPER_SNAKE_CASE**
- Examples:
  ```java
  enum TowerType {
      BASIC_TOWER,
      MISSILE_TOWER,
      LASER_TOWER
  }
  ```

## Documentation Standards

### JavaDoc Requirements
- All public classes and interfaces must have JavaDoc comments
- All public and protected methods must have JavaDoc comments
- Include descriptions for all parameters, return values, and exceptions
- Example:
  ```java
  /**
   * Calculates damage that a tower will deal to an enemy.
   *
   * @param tower The Tower performing the attack
   * @param enemy The Enemy receiving the attack
   * @param distance The distance between tower and enemy
   * @return The amount of damage to be applied
   * @throws InvalidTargetException If the enemy is not a valid target
   */
  public double calculateDamage(Tower tower, Enemy enemy, double distance) {
      // Method implementation
  }
  ```

### Class-Level Documentation
- Include a description of the class's purpose
- Document the author(s) and creation date
- Include version information if applicable
- Example:
  ```java
  /**
   * Represents a tower in the tower defense game.
   * Towers can be placed on the map and attack enemies within range.
   *
   * @author Team Member Name
   * @version 1.0
   */
  public class Tower {
      // Class implementation
  }
  ```

### Code Comments
- Use comments to explain "why" not "what" (the code should be self-explanatory)
- Use TODO comments for planned work (include assignee if known)
- Use // for single-line comments and /* */ for multi-line comments
- Example:
  ```java
  // Calculate damage with distance falloff for fairness
  double damageWithFalloff = baseDamage * (1 - distance / range);
  
  /* 
   * Apply special effects based on tower type.
   * Each effect has its own handling logic.
   */
  applySpecialEffects(tower, enemy);
  
  // TODO: Implement critical hit chance (assigned to: TeamMember)
  ```

## Code Organization

### File Structure
- One primary class per file
- Inner classes and enums can be in the same file if they are closely related
- Group related files into packages
- Consider functional separation (UI, game logic, data models, etc.)

### Class Structure
- Order of elements:
  1. Static variables
  2. Instance variables
  3. Constructors
  4. Public methods
  5. Protected methods
  6. Private methods
  7. Inner classes and interfaces

### Method Length
- Keep methods focused and concise (ideally < 50 lines)
- Follow the Single Responsibility Principle
- Extract complex logic to helper methods with descriptive names

### Dependencies
- Minimize dependencies between packages
- Use interfaces to decouple implementation details
- Consider implementing a dependency injection pattern for complex systems

## JavaFX Specific Guidelines

### FXML Naming
- Use **lowercase_with_underscores** for FXML files
- Name should match the controller or scene purpose
- Examples: `main_menu.fxml`, `game_scene.fxml`, `tower_selection_panel.fxml`

### Controller Naming
- Controllers should end with "Controller"
- Examples: `GameController`, `MainMenuController`, `TowerSelectionController`

### JavaFX Components in Code
- UI components should be prefixed with their type
- Examples:
  - `btnStart` for a Button
  - `lblScore` for a Label
  - `paneGame` for a Pane

### Event Handler Methods
- Name handlers with "on" + element + action
- Examples:
  - `onStartButtonClick()`
  - `onEnemyDeath()`
  - `onTowerPlaced()`

### Scene Building
- Prefer FXML for complex UIs
- Use programmatic UI for dynamic elements
- Keep UI logic in controllers, game logic in model classes

## Git Workflow

### Branching Strategy
- Use `main` or `master` for stable releases
- Use `develop` as the integration branch
- Feature branches should be named `feature/feature-name`
- Bug fix branches should be named `bugfix/issue-description`

### Commit Messages
- Use the imperative mood ("Add feature" not "Added feature")
- Keep the first line under 50 characters
- Include the issue number if applicable
- Example: `Add tower upgrade functionality (#42)`

### Pull Requests
- Reference relevant issues
- Provide a clear description of changes
- Ensure all tests pass
- Request review from at least one team member

## Testing Guidelines

### Unit Tests
- Write tests for all non-trivial methods
- Name test classes as `[ClassUnderTest]Test`
- Name test methods as `test[MethodName]_[Scenario]`
- Example:
  ```java
  @Test
  public void testCalculateDamage_WithinRange() {
      // Test implementation
  }
  ```

### Integration Tests
- Test interactions between components
- Test game mechanics across multiple components
- Focus on user workflows and common game scenarios

### Test Coverage
- Aim for at least 80% test coverage on game logic
- Document any code that's deliberately not tested and why

---

These guidelines are meant to enhance our development workflow and code quality. While they should be followed in most cases, use your judgment when exceptions make sense. If you believe a guideline should be modified, please bring it up for team discussion.