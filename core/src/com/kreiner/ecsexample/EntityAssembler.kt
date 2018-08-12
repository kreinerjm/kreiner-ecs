package com.kreiner.ecsexample

object EntityAssembler{

    fun createButton(): Pair<Int,Entity>{
        val toReturn = Entity()
        toReturn += MouseEventEmitter()
        toReturn += PhysicalComponent()
        toReturn += GraphicsComponent()
        val id = ECS.manager.addEntity(toReturn)
        return id to toReturn
    }

    fun createTile(): Pair<Int,Entity>{
        val toReturn = Entity()
        toReturn += MouseEventEmitter()
        toReturn += PhysicalComponent().apply{
            width = 50f
            height = 50f
        }
        toReturn += GraphicsComponent()
        val id = ECS.manager.addEntity(toReturn)
        return id to toReturn
    }

    fun createEnemy(): Pair<Int,Entity>{
        val toReturn = Entity()
        toReturn += PhysicalComponent()
        toReturn += GraphicsComponent()
        toReturn += EnemyComponent()
        val id = ECS.manager.addEntity(toReturn)
        return id to toReturn
    }

}