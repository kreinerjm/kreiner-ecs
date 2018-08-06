package com.kreiner.ecsexample

object EventRegistry{

    val mouseEvents: MutableMap<MouseEvent, MutableMap<Int,() -> Unit>> = mutableMapOf()

    init{
        MouseEvent.values().forEach{
            mouseEvents[it] = mutableMapOf()
        }
    }

    fun receiveMouseEvent(event: MouseEvent, id: Int){
        if(mouseEvents[event]!!.contains(id)){
            mouseEvents[event]!![id]!!.invoke()
        }
    }

    fun registerMouseEvent(event: MouseEvent, id: Int, lambda: () -> Unit){
        mouseEvents[event]!![id] = lambda
    }

}