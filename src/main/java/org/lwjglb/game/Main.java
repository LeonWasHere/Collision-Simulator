package org.lwjglb.game;

/**
 * Author: Leon Wasiliew
 * Subject: INFT4000 - Special Topics 1
 * Creation Date: 2026-04-15
 * Modification Date: 2026-04-15
 * Description: Main...
 * Resource:
 * - Russ
 * - https://lwjglgamedev.gitbooks.io/3d-game-development-with-lwjgl/content/
 *
 */

import org.lwjglb.engine.GameEngine;
import org.lwjglb.engine.IGameLogic;
import org.lwjglb.engine.Window;

public class Main {

    public static void main(String[] args) {

        // <RS> Added some handy directory and Java info
        System.out.println("Directory = " + System.getProperty("user.dir"));
        System.out.println("Java = " + System.getProperty("java.vendor"));
        System.out.println("Java = " + System.getProperty("java.version"));

        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            Window.WindowOptions opts = new Window.WindowOptions();
            opts.cullFace = false;
            opts.showFps = true;
            opts.compatibleProfile = true;
            opts.antialiasing = true;
            opts.frustumCulling = false;
            GameEngine gameEng = new GameEngine("Collision Simulator", 800, 800, vSync, opts, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Setup jFrame for controls
        GameGUI.guiSetup();
    }
}
