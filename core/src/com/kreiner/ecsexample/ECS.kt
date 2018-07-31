package com.kreiner.ecsexample
class ECS{

    companion object {
        val manager = EntityManager()
        val systems = mutableListOf<System>()
    }

}