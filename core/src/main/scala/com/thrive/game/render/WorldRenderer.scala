package com.thrive.game.render

import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch}
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.{Gdx, ScreenAdapter}
import com.thrive.game.controller.WorldController
import com.thrive.simulation.clock.Clock.dtFMT
import com.thrive.simulation.constants.Constants
import com.thrive.simulation.world.World
import org.joda.time.DateTime

case class WorldRenderer(worldController: WorldController)
  extends ScreenAdapter with Disposable {

  private val TAG = classOf[WorldRenderer].getName
  private lazy val batch = new SpriteBatch()
  private lazy val camera = new OrthographicCamera()
  private lazy val cameraGUI = new OrthographicCamera()

  private lazy val font = {
    val f = new BitmapFont()
    f.getData.setScale(1f)
    f.getRegion().getTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear)
    f
  }

  override def resize(width: Int, height: Int): Unit = {
    camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width
    camera.update()

    cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT
    cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / height.asInstanceOf[Float]) * width.asInstanceOf[Float]
    cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0)
  }

  override def show(): Unit = {
    cameraGUI.position.set(0, 0, 0)
    cameraGUI.setToOrtho(false, Constants.VIEWPORT_GUI_WIDTH,
      Constants.VIEWPORT_GUI_HEIGHT)
    camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT)
    camera.position.set(0, 0, 0)
    Gdx.gl.glClearColor(
      0.1f, 0.1f,
      0.25f, 0.1f
    )
  }

  override def render(delta: Float): Unit = {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    camera.update()

    val (world, time) = worldController.update(delta)
    renderPopulationSummary(world, time)
    renderTestObjects()
    renderGui()

  }

  private def renderGui(): Unit = {
    batch.setProjectionMatrix(cameraGUI.combined)
    batch.begin()

    batch.end()
  }

  private def renderPopulationSummary(world: World, time: DateTime): Unit = {
    batch.setProjectionMatrix(cameraGUI.combined)
    batch.begin()
    val summary = world.gridPopulations()
    val inRow = Constants.VIEWPORT_GUI_HEIGHT / 25
    for (((locName, population), i) <- summary.zipWithIndex) {

      val colNum = (i / inRow).floor
      val rowNum = i % inRow
      val yPos = 30 + (20 * rowNum)
      font.draw(batch, locName, 10 + 230 * colNum, yPos)
      font.draw(batch, population.toString, 200 + 230 * colNum, yPos)
      font.draw(batch, time.formatted(dtFMT.print(time)), 550, 600)
      font.draw(batch, s"Total Living: ${world.totalPopulation}", 100, 600)
    }
    batch.end()
  }

  private def renderTestObjects(): Unit = {
    batch.setProjectionMatrix(camera.combined)
    batch.begin()
    for (sprite <- worldController.testSprites) {
      sprite.draw(batch)
    }
    batch.end()
  }
}
