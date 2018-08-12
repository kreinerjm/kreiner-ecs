package com.kreiner.ecsexample

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector3
import com.sun.javafx.geom.Vec2f
import kotlin.reflect.KClass

class InputSystem(): System(){
    override val worksOn = listOf(PhysicalComponent::class, KeyboardInputComponent::class)

    var leftPressed = false

    var hoveredEntity: Pair<Int, Entity>? = null

    fun handleKeyInput(){
        ECS.manager.allEntities.values.forEach{ entity ->
            if(entity.hasAll(worksOn)){
                val kic = entity.getComponent<KeyboardInputComponent>(KeyboardInputComponent::class)!!
                val pc = entity.getComponent<PhysicalComponent>(PhysicalComponent::class)!!
                when(kic.type){
                    InputType.CARDINAL_ARROW_KEYS -> {
                        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                            pc.x -= 1f
                        }
                        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                            pc.x += 1f
                        }
                        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                            pc.y += 1f
                        }
                        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                            pc.y -= 1f
                        }
                    }
                }

            }
        }
    }

    fun handleMouseInput(){
        val lastHovered = hoveredEntity
        val nextHovered = getHitEntity()

        if(lastHovered != nextHovered){
            if(lastHovered != null){
                EventRegistry.receiveMouseEvent(MouseEvent.HOVER_EXIT,lastHovered.first)
            }
            if(nextHovered != null){
                EventRegistry.receiveMouseEvent(MouseEvent.HOVER_ENTER,nextHovered.first)
            }
            hoveredEntity = nextHovered
        } else if(lastHovered != null){
            EventRegistry.receiveMouseEvent(MouseEvent.HOVER,lastHovered.first)
        }

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){

        } else if(leftPressed){
            leftPressed = false
        }
    }

    fun getHitEntity(): Pair<Int,Entity>?{
        val ptrX = Gdx.input.x.toFloat()
        val ptrY = Gdx.input.y.toFloat()
        var toReturn: Pair<Int, Entity>? = null
        ECS.manager.allEntities.filter{ it.value.hasType(PhysicalComponent::class) && it.value.hasType(MouseEventEmitter::class) }.forEach{ id, entity ->
            val pc = entity.getComponent<PhysicalComponent>(PhysicalComponent::class)!!
            val cam = ECS.camera
            val local = cam.unproject(Vector3(ptrX,ptrY,0f))
            if(local.x > pc.x && local.x < pc.x + pc.width && local.y > pc.y && local.y < pc.y + pc.height){
                toReturn = id to entity
            }
        }
        return toReturn
    }

}