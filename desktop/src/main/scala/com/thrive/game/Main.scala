package com.thrive.game

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}

object Main extends App {
    val cfg = new LwjglApplicationConfiguration
  cfg.title = "abcgame"
    cfg.height = 480
    cfg.width = 800
    cfg.forceExit = false
    new LwjglApplication(new ThriveApp, cfg)
}
