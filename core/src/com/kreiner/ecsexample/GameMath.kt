package com.kreiner.ecsexample

import com.badlogic.gdx.math.Vector2

object GameMath{
    fun getAngleBetween(origin: Vector2, target: Vector2): Double{
        val xDiff = target.x - origin.x
        val yDiff = target.y - origin.y
        return if(xDiff != 0f){
            val actual = Math.atan((yDiff/xDiff).toDouble())
            if(yDiff == 0f && xDiff < 0.0){
                Math.PI
            } else if(yDiff == 0f && xDiff > 0.0){
                0.0
            } else if(xDiff < 0.0 && yDiff > 0.0){
                val expected = (Math.PI/2.0) + ((Math.PI/2.0) - Math.abs(actual))
                expected
            } else if(xDiff < 0.0 && yDiff < 0.0){
                Math.PI + actual
            } else if(xDiff > 0.0 && yDiff < 0.0){
                val expected = (3.0*Math.PI/2.0) + ((Math.PI/2.0) - Math.abs(actual))
                expected
            } else {
                actual
            }
        } else if(yDiff > 0.0) {
            Math.PI/2.0
        } else if(yDiff < 0.0){
            3.0 * Math.PI / 2.0
        } else {
            -1.0
        }
    }
}