package com.kreiner.ecsexample

import com.badlogic.gdx.graphics.Color

class GraphicsComponent: Component(){
    var type: DrawingType = DrawingType.SHAPE_RENDERER
    var color: Color = Color.GREEN
}

enum class DrawingType{
    SHAPE_RENDERER
}