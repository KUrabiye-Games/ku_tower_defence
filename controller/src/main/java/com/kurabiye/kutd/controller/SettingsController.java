package com.kurabiye.kutd.controller;

// import com.kurabiye.kutd.model.Player.UserPreference;
// import com.kurabiye.kutd.persistence.SettingsManager;
// import com.kurabiye.kutd.view.SettingsView;

// public class SettingsController extends Controller {
//     private SettingsView settingsView;
//     private SettingsManager settingsManager;
//     private UserPreference.Builder userPreferenceBuilder;

//     public SettingsController(SettingsView settingsView, SettingsPersistenceManager settingsManager) {
//         this.settingsView = settingsView;
//         this.settingsManager = settingsManager;

//         initialize();
//     }

//     @Override
//     protected void initialize() {
//         settingsManager.loadSettings();

//         this.userPreferenceBuilder = new UserPreference.Builder(UserPreference.getInstance());
//         populateFieldsFromSettings(UserPreference.getInstance());

//         settingsView.setOnUserNameChanged(name -> userPreferenceBuilder.setUserName(name));
    
//         settingsView.setOnMusicVolumeChanged(volume -> userPreferenceBuilder.setMusicVolume(volume));
//         settingsView.setOnSoundVolumeChanged(volume -> userPreferenceBuilder.setSoundVolume(volume));
    
//         settingsView.setOnNumberOfWavesChanged(waves -> userPreferenceBuilder.setNumberOfWaves(waves));
//         settingsView.setOnNumberOfGroupsPerWaveChanged(groups -> userPreferenceBuilder.setNumberOfGroupsPerWave(groups));
//         settingsView.setOnNumberOfEnemiesPerGroupChanged(enemies -> userPreferenceBuilder.setNumberOfEnemiesPerGroup(enemies));
    
//         settingsView.setOnDelayBetweenWavesChanged(delay -> userPreferenceBuilder.setDelayBetweenWaves(delay));
//         settingsView.setOnDelayBetweenGroupsChanged(delay -> userPreferenceBuilder.setDelayBetweenGroups(delay));
    
//         settingsView.setOnEnemyCompositionChanged(composition -> userPreferenceBuilder.setEnemyComposition(composition));
    
//         settingsView.setOnStartingGoldChanged(gold -> userPreferenceBuilder.setStartingGold(gold));
//         settingsView.setOnGoldPerEnemyChanged(goldPerEnemy -> userPreferenceBuilder.setGoldPerEnemy(goldPerEnemy));
    
//         settingsView.setOnStartingHealthChanged(health -> userPreferenceBuilder.setStartingHealth(health));
//         settingsView.setOnEnemyHealthChanged(enemyHealth -> userPreferenceBuilder.setEnemyHealth(enemyHealth));
    
//         settingsView.setOnDamageDealtChanged(damageDealt -> userPreferenceBuilder.setDamageDealt(damageDealt));
    
//         settingsView.setOnTowerConstructionCostChanged(costs -> userPreferenceBuilder.setTowerConstructionCost(costs));
//         settingsView.setOnTowerSellReturnChanged(sellReturns -> userPreferenceBuilder.setTowerSellReturn(sellReturns));
    
//         settingsView.setOnTowerEffectiveRangeChanged(ranges -> userPreferenceBuilder.setTowerEffectiveRange(ranges));
//         settingsView.setOnTowerRateOfFireChanged(rates -> userPreferenceBuilder.setTowerRateOfFire(rates));
    
//         settingsView.setOnArtilleryRangeChanged(range -> userPreferenceBuilder.setArtilleryRange(range));
    
//         settingsView.setOnEnemyMovementSpeedChanged(speeds -> userPreferenceBuilder.setEnemyMovementSpeed(speeds));
    
//         // Save button listener
//         settingsView.setOnSaveButtonClicked(this::saveSettings);

//         settingsView.setOnResetToDefaultButtonClicked(this::resetToDefaultSettings);
//     }

//     private void populateFieldsFromSettings(UserPreference preferences) {
//         settingsView.setUserName(preferences.getUserName());
//         settingsView.setMusicVolume(preferences.getMusicVolume());
//         settingsView.setSoundVolume(preferences.getSoundVolume());
        
//         settingsView.setNumberOfWaves(preferences.getNumberOfWaves());
//         settingsView.setNumberOfGroupsPerWave(preferences.getNumberOfGroupsPerWave());
//         settingsView.setNumberOfEnemiesPerGroup(preferences.getNumberOfEnemiesPerGroup());
        
//         settingsView.setDelayBetweenWaves(preferences.getDelayBetweenWaves());
//         settingsView.setDelayBetweenGroups(preferences.getDelayBetweenGroups());
        
//         settingsView.setEnemyComposition(preferences.getEnemyComposition());
        
//         settingsView.setStartingGold(preferences.getStartingGold());
//         settingsView.setGoldPerEnemy(preferences.getGoldPerEnemy());
        
//         settingsView.setStartingHealth(preferences.getStartingHealth());
//         settingsView.setEnemyHealth(preferences.getEnemyHealth());
        
//         settingsView.setDamageDealt(preferences.getDamageDealt());
        
//         settingsView.setTowerConstructionCost(preferences.getTowerConstructionCost());
//         settingsView.setTowerSellReturn(preferences.getTowerSellReturn());
        
//         settingsView.setTowerEffectiveRange(preferences.getTowerEffectiveRange());
//         settingsView.setTowerRateOfFire(preferences.getTowerRateOfFire());
        
//         settingsView.setArtilleryRange(preferences.getArtilleryRange());
//         settingsView.setEnemyMovementSpeed(preferences.getEnemyMovementSpeed());
//     }
    

//     private void saveSettings() {
//         UserPreference.applySettings(userPreferenceBuilder);
//         settingsManager.saveSettings(UserPreference.getInstance());
//         settingsView.showSaveSuccess();
//     }

//     private void resetToDefaultSettings() {
//         // Reset singleton instance to fresh defaults
//         UserPreference.resetInstance();
//         userPreferenceBuilder = new UserPreference.Builder(UserPreference.getInstance());
    
//         // Populate fields again with fresh default values
//         populateFieldsFromSettings(UserPreference.getInstance());
//     }
    
// }
