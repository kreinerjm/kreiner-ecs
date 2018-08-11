package com.kreiner.ecsexample

import com.badlogic.gdx.graphics.Color

class TileSystem {
    val tiles = mutableListOf<MutableList<Entity>>()

    init{
        for(i in 0..10){
            val list = mutableListOf<Entity>()
            for(j in 0..10){
                val tile = EntityAssembler.createTile().apply{
                    val pc = second.getComponent<PhysicalComponent>(PhysicalComponent::class)!!
                    pc.apply{
                        x = j * 50f
                        y = i * 50f
                    }
                }
                EventRegistry.registerMouseEvent(MouseEvent.HOVER_ENTER, tile.first){
                    val gc = tile.second.getComponent<GraphicsComponent>(GraphicsComponent::class)
                    gc!!.color = Color.BLUE
                }
                EventRegistry.registerMouseEvent(MouseEvent.HOVER_EXIT, tile.first){
                    val gc = tile.second.getComponent<GraphicsComponent>(GraphicsComponent::class)
                    gc!!.color = Color.GREEN
                }
                list.add(tile.second)
            }
            tiles.add(list)
        }
    }
}