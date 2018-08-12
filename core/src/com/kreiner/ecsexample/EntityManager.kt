package com.kreiner.ecsexample

import kotlin.reflect.KClass

class EntityManager{

    val allEntities = HashMap<Int, Entity>()
    val filteredEntities = HashMap<KClass<out System>,MutableList<Int>>()
    val idManager = EntityIdManager()

    fun addEntity(entity: Entity): Int{
        val next = idManager.getNext()
        allEntities.put(next,entity)
        entity.id = next
        return next
    }

    fun removeEntity(key: Int){
        allEntities.remove(key)
        idManager.removed.add(key)
    }

    inner class EntityIdManager{
        val removed = mutableListOf<Int>()
        var current = 0
        fun getNext(): Int{
            if(removed.size > 0){
                return removed.removeAt(0)
            } else {
                return current++
            }
        }
    }

}