package com.kreiner.ecsexample

import kotlin.reflect.KClass

abstract class System {

    init{

    }

    abstract val worksOn: List<KClass<out Component>>

}