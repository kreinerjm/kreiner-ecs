package com.kreiner.ecsexample

import com.badlogic.gdx.graphics.OrthographicCamera

class ECS{

    companion object {
        val manager = EntityManager()
        val systems = mutableListOf<System>()
        lateinit var camera: OrthographicCamera
        var startTime: Long = 0
        var currentTime: Long = 0
        var currentTimeSeconds: Int = 0
        var lastWaveSpawnTimeSeconds: Int = 0
        var currentWave = 1
        var spawnerLevel = 1
    }

}