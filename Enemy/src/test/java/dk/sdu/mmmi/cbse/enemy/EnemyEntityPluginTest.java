/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import dk.sdu.mmmi.cbse.common.data.GameData;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nasib
 */
public class EnemyEntityPluginTest {
    
    public EnemyEntityPluginTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of start method, of class EnemyEntityPlugin.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        GameData gameData = null;
        Map<String, Entity> world = null;
        EnemyEntityPlugin instance = new EnemyEntityPlugin();
        instance.start(gameData, world);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createEnemyShip method, of class EnemyEntityPlugin.
     */
    @Test
    public void testCreateEnemyShip() {
        System.out.println("createEnemyShip");
        EnemyEntityPlugin instance = new EnemyEntityPlugin();
        boolean expResult = instance.equals(ENEMY);
        Entity result = instance.createEnemyShip();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stop method, of class EnemyEntityPlugin.
     */
    @Test
    public void testStop() {
        System.out.println("stop");
        GameData gameData = null;
        EnemyEntityPlugin instance = new EnemyEntityPlugin();
        instance.stop(gameData);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
