package com.kreiner.ecsexample

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import java.lang.System
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.kreiner.ecsexample.ECS.Companion.currentTime


class ECSExample : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var camera: OrthographicCamera

    object GlobalSystemAccess{
        lateinit var tileSystem: TileSystem
    }
    val drawingSystem = DrawingSystem()
    val inputSystem = InputSystem()

    val aiSystem = AISystem()
    val spawnerSystem = SpawnerSystem()

    lateinit var font: BitmapFont

    override fun create() {
        font = BitmapFont()
        batch = SpriteBatch()

        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        camera = OrthographicCamera(550f, 550f * (h / w))
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        camera.update()
        ECS.camera = camera

        BatchManager.shapeRenderer = ShapeRenderer()
        GlobalSystemAccess.tileSystem = TileSystem()

        val enemy = EntityAssembler.createEnemy()
        enemy.second.apply{
            val pc = getComponent<PhysicalComponent>(PhysicalComponent::class)!!
            val gc = getComponent<GraphicsComponent>(GraphicsComponent::class)!!
            gc.color = Color.BLACK
            pc.x = 220f
        }
        ECS.startTime = System.currentTimeMillis()

    }

    override fun render() {
        ECS.currentTime = System.currentTimeMillis() - ECS.startTime
        ECS.currentTimeSeconds = (currentTime/1000.0).toInt()
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        BatchManager.shapeRenderer.projectionMatrix = camera.combined
        spawnerSystem.tick()
        inputSystem.handleKeyInput()
        inputSystem.handleMouseInput()
        aiSystem.tick()
        drawingSystem.draw()
        batch.begin()
        batch.color = Color.PURPLE
        font.draw(batch,ECS.currentTimeSeconds.toString(),100f,100f)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
    }
}
