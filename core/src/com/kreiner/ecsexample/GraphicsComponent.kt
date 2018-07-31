package com.kreiner.ecsexample

class GraphicsComponent: Component(){
    var type: DrawingType = DrawingType.SHAPE_RENDERER
}

enum class DrawingType{
    SHAPE_RENDERER
}