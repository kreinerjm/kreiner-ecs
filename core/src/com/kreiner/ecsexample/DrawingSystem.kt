package com.kreiner.ecsexample

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.sun.javafx.geom.Vec4f

class DrawingSystem: System(){
    override val worksOn = listOf(PhysicalComponent::class,GraphicsComponent::class)

    fun draw(){
        val toDraw = mutableListOf<Entity>()

        ECS.manager.allEntities.values.forEach{ entity ->
            if(entity.hasAll(worksOn)){
                toDraw += entity
            }
        }

        val renderer = BatchManager.shapeRenderer
        renderer.begin(ShapeRenderer.ShapeType.Filled)
        toDraw.forEach{
            val gc = it.getComponent<GraphicsComponent>(GraphicsComponent::class)!!
            val pc = it.getComponent<PhysicalComponent>(PhysicalComponent::class)!!
            renderer.color = gc.color
            renderer.rect(pc.x,pc.y,pc.width,pc.height)
        }
        renderer.end()

        renderer.color = Color.BLACK
        renderer.begin(ShapeRenderer.ShapeType.Line)
        toDraw.forEach{
            val pc = it.getComponent<PhysicalComponent>(PhysicalComponent::class)!!
            renderer.rect(pc.x,pc.y,pc.width,pc.height)
        }
        renderer.end()
    }
}