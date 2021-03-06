package com.thrive.game

import com.badlogic.gdx.{Application, Gdx}
import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}

object Main extends App {
  val cfg = new LwjglApplicationConfiguration
  cfg.title = "Thrive"
  cfg.height = 800
  cfg.width = 1200
  cfg.forceExit = false

  val thriveApp: LwjglApplication = new LwjglApplication(
    new ThriveApp, cfg
  )
  thriveApp.setLogLevel(Application.LOG_DEBUG)
  thriveApp.debug("Meta", "starting app")
  val prefs = thriveApp.getPreferences("settings.pref")
  prefs.putInteger("Height", 200)
  val retrievedHeight = prefs.getInteger("Height")
  thriveApp.debug("Meta", retrievedHeight.toString)


}
