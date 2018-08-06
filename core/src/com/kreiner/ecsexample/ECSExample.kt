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


    override fun create() {
        batch = SpriteBatch()

        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        camera = OrthographicCamera(30f, 30 * (h / w))
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        camera.update()
        ECS.camera = camera

        BatchManager.shapeRenderer = ShapeRenderer()
        val button = EntityAssembler.createButton()
        button.second += KeyboardInputComponent()
        EventRegistry.registerMouseEvent(MouseEvent.HOVER_ENTER, button.first){
            val gc = button.second.getComponent<GraphicsComponent>(GraphicsComponent::class)
            gc!!.color = Color.BLUE
        }
        EventRegistry.registerMouseEvent(MouseEvent.HOVER, button.first){
            //println("hovering")
        }
        EventRegistry.registerMouseEvent(MouseEvent.HOVER_EXIT, button.first){
            val gc = button.second.getComponent<GraphicsComponent>(GraphicsComponent::class)
            gc!!.color = Color.GREEN
        }
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
