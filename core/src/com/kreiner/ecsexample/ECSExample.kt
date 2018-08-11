package com.kreiner.ecsexample

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class ECSExample : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var camera: OrthographicCamera

    val drawingSystem = DrawingSystem()
    val inputSystem = InputSystem()
    val tileSystem = TileSystem()


    override fun create() {
        batch = SpriteBatch()

        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        camera = OrthographicCamera(550f, 550f * (h / w))
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        camera.update()
        ECS.camera = camera

        BatchManager.shapeRenderer = ShapeRenderer()

    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        BatchManager.shapeRenderer.projectionMatrix = camera.combined
        inputSystem.handleKeyInput()
        inputSystem.handleMouseInput()
        drawingSystem.draw()
    }

    override fun dispose() {
        batch.dispose()
    }
}
