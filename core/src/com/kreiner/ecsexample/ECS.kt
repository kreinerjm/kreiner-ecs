package com.kreiner.ecsexample

import com.badlogic.gdx.graphics.OrthographicCamera

class ECS{

    companion object {
        val manager = EntityManager()
        val systems = mutableListOf<System>()
        lateinit var camera: OrthographicCamera
    }

}