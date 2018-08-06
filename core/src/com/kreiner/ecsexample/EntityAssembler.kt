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

}