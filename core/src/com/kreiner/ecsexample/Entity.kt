package com.kreiner.ecsexample

import kotlin.reflect.KClass

class Entity: ArrayList<Component>(){

    override fun add(element: Component): Boolean {
        val toReturn = super.add(element)
//        ECS.systems.forEach { system ->
//            if(this@Entity.filter{
//                var passed = true
//                system.worksOn.forEach{
//                    if(!hasType(it)){
//                        passed = false
//                    }
//                }
//                passed
//            }.size == system.worksOn.size){
//                //ECS.manager.filteredEntities[system::class].add()
//            }
//        }
        return toReturn
    }

    fun hasType(type: KClass<out Any>): Boolean{
        forEach { if(it::class == type) return true }
        return false
    }

    fun <T>getComponent(type: KClass<out Component>): T?{
        forEach{
            if(it::class == type){
                return it as T
            }
        }
        return null
    }

}