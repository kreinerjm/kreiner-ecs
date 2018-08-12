package com.kreiner.ecsexample

import com.badlogic.gdx.math.Vector2
import kotlin.reflect.KClass

class AISystem: System(){
    override val worksOn: List<KClass<out Component>> = listOf(EnemyComponent::class,PhysicalComponent::class)

    fun tick(){
        ECS.manager.allEntities.values.forEach{ entity ->
            if(entity.hasAll(worksOn)){
                val pc = entity.getComponent<PhysicalComponent>(PhysicalComponent::class)!!
                val ec = entity.getComponent<EnemyComponent>(EnemyComponent::class)!!

                val targetPos = Vector2(275f,275f)

                val angle = GameMath.getAngleBetween(Vector2(pc.x + pc.width/2f,pc.y + pc.height/2f),targetPos)

                //println(angle)

                if(angle >= 0.0){
                    val xDiff = targetPos.x - (pc.x + pc.width/2f)
                    val yDiff = targetPos.y - (pc.y + pc.height/2f)
                    val asq = Math.abs(xDiff * xDiff).toDouble()
                    val bsq = Math.abs(yDiff * yDiff).toDouble()
                    val dist = Math.sqrt(asq + bsq)
                    if(dist < ec.moveSpeed){
                        pc.x = targetPos.x - pc.width/2
                        pc.y = targetPos.y - pc.height/2
                    } else {
                        val stepX = (ec.moveSpeed * Math.cos(angle)).toFloat()
                        val stepY = (ec.moveSpeed * Math.sin(angle)).toFloat()
                        pc.x += stepX
                        pc.y += stepY
                    }
                }
            }
        }
    }
}