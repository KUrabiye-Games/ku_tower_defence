package com.kurabiye.kutd.model.MapTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.kurabiye.kutd.model.Map.GameMap;

public class MapTest {
    
    @Test
    public void testMapInitialization() {
        GameMap map = new GameMap(); // Create a map of size 10x10
        assertNotNull(map, "Map should be initialized");
        
    }

    


}
