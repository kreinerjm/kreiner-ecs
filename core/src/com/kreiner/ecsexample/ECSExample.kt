package com.kreiner.ecsexample

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class ECSExample : ApplicationAdapter() {
    lateinit var batch: SpriteBatch

    val drawingSystem = DrawingSystem()
    val inputSystem = InputSystem()

    override fun create() {
        batch = SpriteBatch()
        BatchManager.shapeRenderer = ShapeRenderer()
        ECS.manager.addEntity(Entity().apply{
            add(GraphicsComponent())
            add(PhysicalComponent())
            add(KeyboardInputComponent())
        })
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        inputSystem.handleKeyInput()
        drawingSystem.draw()
    }

    override fun dispose() {
        batch.dispose()
    }
}
