package com.kreiner.ecsexample

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import kotlin.reflect.KClass

class InputSystem(): System(){
    override val worksOn = listOf(PhysicalComponent::class, KeyboardInputComponent::class)

    fun handleKeyInput(){
        ECS.manager.allEntities.values.forEach{ entity ->
            var hasAll = true
            worksOn.forEach {
                if(!entity.hasType(it)) hasAll = false
            }
            if(hasAll){
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

}