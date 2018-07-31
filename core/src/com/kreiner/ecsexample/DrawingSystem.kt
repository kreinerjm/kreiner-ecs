package com.kreiner.ecsexample

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import javax.swing.plaf.basic.BasicTableHeaderUI
import kotlin.reflect.KClass

class DrawingSystem: System(){
    override val worksOn = listOf(PhysicalComponent::class,GraphicsComponent::class)

    fun draw(){
        ECS.manager.allEntities.values.forEach{ entity ->
            var hasAll = true
            worksOn.forEach {
                if(!entity.hasType(it)) hasAll = false
            }
            if(hasAll){
                val gc = entity.getComponent<GraphicsComponent>(GraphicsComponent::class)!!
                val pc = entity.getComponent<PhysicalComponent>(PhysicalComponent::class)!!
                when(gc.type){
                    DrawingType.SHAPE_RENDERER -> {
                        val renderer = BatchManager.shapeRenderer
                        renderer.begin(ShapeRenderer.ShapeType.Filled)
                        renderer.rect(pc.x,pc.y,pc.width,pc.height)
                        renderer.end()
                    }
                }

            }
        }
    }
}