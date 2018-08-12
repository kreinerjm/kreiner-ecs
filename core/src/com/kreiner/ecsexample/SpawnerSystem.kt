package com.kreiner.ecsexample

import com.badlogic.gdx.graphics.Color
import com.kreiner.ecsexample.ECS.Companion.currentWave
import com.kreiner.ecsexample.ECS.Companion.spawnerLevel
import java.awt.Event
import kotlin.reflect.KClass

class SpawnerSystem: System(){
    override val worksOn: List<KClass<out Component>> = listOf(SpawnerComponent::class)

    fun tick(){
        println("ticking spawner system")
        if(ECS.currentTimeSeconds - ECS.lastWaveSpawnTimeSeconds == 5 && ECS.lastWaveSpawnTimeSeconds != ECS.currentTimeSeconds){
            println("new wave spawned!")
            ECS.lastWaveSpawnTimeSeconds = ECS.currentTimeSeconds
            val spawners = mutableListOf<Entity>()
            ECS.manager.allEntities.forEach{
                if(it.value.hasAll(worksOn)){
                    println("adding spawner with id ${it.key}")
                    spawners.add(it.value)
                }
            }
            spawners.forEach{
                val spawnerPC = it.getComponent<PhysicalComponent>(PhysicalComponent::class)!!
                val coords = spawnerPC.x to spawnerPC.y
                val enemy = EntityAssembler.createEnemy()
                enemy.second.apply{
                    val pc = getComponent<PhysicalComponent>(PhysicalComponent::class)!!
                    val gc = getComponent<GraphicsComponent>(GraphicsComponent::class)!!
                    gc.color = Color.BLACK
                    pc.x = coords.first
                    pc.y = coords.second
                }
            }
            ECS.currentWave++
            if((ECS.currentWave - 1) % 2 == 0){
                val entitiesToChange = mutableListOf<Entity>()
                ECS.manager.allEntities.forEach{
                    println("is this being reached?")
                    if(it.value.hasAll(worksOn) && it.value.getComponent<SpawnerComponent>(SpawnerComponent::class)!!.level == ECS.spawnerLevel){
                        val tileSystem = ECSExample.GlobalSystemAccess.tileSystem
                        when{
                            tileSystem.isLeft(it.value) || tileSystem.isRight(it.value) -> {
                                if(tileSystem.above(it.value) != null){
                                    entitiesToChange.add(tileSystem.above(it.value)!!)
                                }
                                if(tileSystem.below(it.value) != null){
                                    entitiesToChange.add(tileSystem.below(it.value)!!)
                                }
                            }
                            tileSystem.isTop(it.value) || tileSystem.isBot(it.value) -> {
                                if(tileSystem.left(it.value) != null){
                                    entitiesToChange.add(tileSystem.left(it.value)!!)
                                }
                                if(tileSystem.right(it.value) != null){
                                    entitiesToChange.add(tileSystem.right(it.value)!!)
                                }
                            }
                        }
                    }
                }
                ECS.spawnerLevel++
                entitiesToChange.forEach{
                    it.add(SpawnerComponent().apply{
                        level = ECS.spawnerLevel
                    })
                    EventRegistry.unregisterAll(it.id)
                    it.getComponent<GraphicsComponent>(GraphicsComponent::class)!!.color = Color.RED
                }
            }
        }
    }
}