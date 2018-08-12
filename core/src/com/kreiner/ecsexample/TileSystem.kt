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
                    val gc = second.getComponent<GraphicsComponent>(GraphicsComponent::class)
                    if((i == 5 && j == 0) || (j == 5 && i == 0) || (j == 10 && i == 5) || (i == 10 && j == 5)){
                        println("added spawner component")
                        second.remove(second.getComponent<MouseEventEmitter>(MouseEventEmitter::class)!!)
                        second.add(SpawnerComponent())
                        gc!!.color = Color.RED
                    } else {
                        println("added regular tile component")
                        EventRegistry.registerMouseEvent(MouseEvent.HOVER_ENTER, first){
                            gc!!.color = Color.BLUE
                        }
                        EventRegistry.registerMouseEvent(MouseEvent.HOVER_EXIT, first){
                            gc!!.color = Color.GREEN
                        }
                    }
                }
                list.add(tile.second)
            }
            tiles.add(list)
        }
    }

    fun getCoords(entity: Entity): Pair<Int,Int>{
        tiles.forEachIndexed { rowIndex, row ->
            row.forEachIndexed{ colIndex, tile ->
                if(tile == entity){
                    return Pair(rowIndex,colIndex)
                }
            }
        }
        return Pair(-1,-1)
    }

    fun isTop(entity: Entity): Boolean{
        return getCoords(entity).first == 10
    }

    fun isLeft(entity: Entity): Boolean{
        return getCoords(entity).second == 0
    }

    fun isRight(entity: Entity): Boolean{
        return getCoords(entity).second == 10
    }

    fun isBot(entity: Entity): Boolean{
        return getCoords(entity).first == 0
    }

    fun left(entity: Entity): Entity?{
        val coords = getCoords(entity)
        if(coords.second != 0){
            return tiles[coords.first][coords.second-1]
        }
        return null
    }

    fun right(entity: Entity): Entity?{
        val coords = getCoords(entity)
        if(coords.second != 10){
            return tiles[coords.first][coords.second+1]
        }
        return null
    }

    fun below(entity: Entity): Entity?{
        val coords = getCoords(entity)
        if(coords.first != 0){
            return tiles[coords.first-1][coords.second]
        }
        return null
    }

    fun above(entity: Entity): Entity?{
        val coords = getCoords(entity)
        if(coords.first != 10){
            return tiles[coords.first+1][coords.second]
        }
        return null
    }
}