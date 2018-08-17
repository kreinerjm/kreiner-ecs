package com.kreiner.ecsexample

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.GL20.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import java.lang.System
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute
import com.kreiner.ecsexample.ECS.Companion.currentTime
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.sun.javafx.geom.Vec3f
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld
import com.badlogic.gdx.math.collision.Ray
import java.util.TreeSet


class ECSExample : ApplicationAdapter() {

    lateinit var world: Array<Array<Array<ModelInstance?>>>
    lateinit var vertices: Array<Array<Array<Vertex?>>>

    val edges = mutableListOf<Edge>()

    lateinit var batch: SpriteBatch
    lateinit var camera: OrthographicCamera

    lateinit var modelBatch: ModelBatch
    lateinit var model: Model

    lateinit var environment: Environment

    object GlobalSystemAccess{
        lateinit var tileSystem: TileSystem
    }

    val modelBuilder = ModelBuilder()

    lateinit var font: BitmapFont

    lateinit var player: ModelInstance
    lateinit var elevator: ModelInstance

    var movePlayerToo = false

    var elevatorStartTime: Long = 0
    var elevatorMovingUp = false
    var elevatorMovingDown = false
    var elevatorStartY = 1
    var elevatorEndY = 2
    var elevatorY = 1

    var hideAbove = 4
    var hiding = true

    fun getAll(): MutableList<ModelInstance>{
        val toReturn = mutableListOf<ModelInstance>()
        world.forEach {
            it.forEach {
                it.forEach {
                    if(it != null){
                        toReturn += it
                    }
                }
            }
        }
        return toReturn
    }

    override fun create() {
        world = Array(32){ x -> Array(32){ y -> Array(32){ z ->
            if(y == 0) getNewCube(x*5f,z*5f,0f) else null
                }
            }
        }

        vertices = Array(32){ x -> Array(32){ y -> Array(32){ z ->
            if(y == 0) Vertex("$x,$y,$z") else null
                }
            }
        }

        for(x in 0 until 36){
            for(z in 0 until 36){
                if(x < 35){
                    edges.add(Edge("$x,0,$z","${x+1},0,$z",1))
                }
                if(z < 35){
                    edges.add(Edge("$x,0,$z","$x,0,${z+1}",1))
                }
                if(x < 35 && z < 35){
                    edges.add(Edge("$x,0,$z","${x+1},0,${z+1}",1))
                }
            }
        }

        world[0][1][0] = getNewCube(0f,0f,5f)

        world[10][1][10] = getNewCube(50f,50f,5f)
        world[11][1][10] = getNewCube(55f,50f,5f)
        world[11][1][11] = getNewCube(55f,55f,5f)
        world[10][1][11] = getNewCube(50f,55f,5f)

        val toRemove = mutableListOf<Edge>()
        edges.forEach{
            if(it.v1 == "10,0,10" || it.v2 == "10,0,10"){
                toRemove.add(it)
            }
            if(it.v1 == "11,0,10" || it.v2 == "11,0,10"){
                toRemove.add(it)
            }
            if(it.v1 == "11,0,11" || it.v2 == "11,0,11"){
                toRemove.add(it)
            }
            if(it.v1 == "10,0,11" || it.v2 == "10,0,11"){
                toRemove.add(it)
            }
        }
        toRemove.forEach{
            edges.remove(it)
        }

        elevator = world[5][0][5]!!

        font = BitmapFont()
        batch = SpriteBatch()
        modelBatch = ModelBatch()

        elevator.materials.get(0).set(ColorAttribute.createDiffuse(Color.BLUE))

        player = getNewCube(25f - 1.25f,25f - 1.25f,5f,Color.RED)
        //modelInstances.add(player)

        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        camera = OrthographicCamera(50f, 50f * (h / w))
        camera.position.set(CAM_PATH_RADIUS, CAM_HEIGHT, 0f)
                .rotate(Vector3.Y, camPathAngle).add(Vector3(25f,5f,25f))
		camera.lookAt(25f,6.25f,25f)
		camera.near = 0.001f
		camera.far = 1000f
        camera.zoom = 1f
		camera.update()
        environment = Environment()
        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, -0.8f, 0.2f))

        BatchManager.shapeRenderer = ShapeRenderer()
        GlobalSystemAccess.tileSystem = TileSystem()
        ECS.startTime = System.currentTimeMillis()




    }

    fun getNewCube(x: Float, z: Float, y: Float, color: Color = Color.GREEN): ModelInstance{
        val size = if(color == Color.RED) 2.5f else 5f
        model = modelBuilder.createBox(size, size, size,
                Material(ColorAttribute.createDiffuse(color)),
                (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong())
        return ModelInstance(model,x+(size/2),y+(size/2),z+(size/2))
    }

    val CAM_PATH_RADIUS = 35f
    val CAM_HEIGHT = 20f
    var camPathAngle = 180f-45f

    fun isPlayerOnElevator(): Boolean{
        val elevatorPos = elevator.transform.getTranslation(Vector3())
        val playerPos = player.transform.getTranslation(Vector3())
        if(playerPos.x in elevatorPos.x-2.5f..elevatorPos.x+2.5f && playerPos.z in elevatorPos.z-2.5f..elevatorPos.z+2.5f){
            return true
        }
        return false
    }

    fun hitSomething(screenCoords: Vector2): Triple<Int,Int,Int>? {
        val pickRay = camera.getPickRay(screenCoords.x, screenCoords.y)
        // A bounding box for each of your minecraft blocks
        val position = Vector3()
        var lowestDist = Float.MAX_VALUE
        var toReturn: Triple<Int,Int,Int>? = null

        world.forEachIndexed { x,list1 ->
            list1.forEachIndexed { y,list2 ->
                list2.forEachIndexed { z,instance ->
                    if(instance != null){
                        val bb = instance.calculateBoundingBox (BoundingBox())
                        instance.transform.getTranslation(position)
                        position.add(bb.getCenter(Vector3()))
                        if(Intersector.intersectRayBoundsFast(pickRay, position, bb.getDimensions(Vector3()))){
                            val dist = pickRay.origin.dst2(position)
                            if(dist < lowestDist){
                                lowestDist = dist
                                toReturn = Triple(x,y,z)
                            }
                        }
                    }
                }
            }
        }
        return toReturn
    }



    override fun render() {

        val lookAt = Vector3(25f, 6.25f, 25f)
        //println(camPathAngle)

        ECS.currentTime = System.currentTimeMillis() - ECS.startTime
        ECS.currentTimeSeconds = (currentTime / 1000.0).toInt()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT or if (Gdx.graphics.getBufferFormat().coverageSampling) GL20.GL_COVERAGE_BUFFER_BIT_NV else 0)
        //println(camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))).toString()
//        Gdx.gl.glEnable(GL_BLEND)
//        Gdx.gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        modelBatch.begin(camera)
        getAll().forEach {
            if(!hiding){
                modelBatch.render(it, environment)
            } else {
                val position = it.transform.getTranslation(Vector3())
                //println(position.y)
                if(position.y < 2.5f + 5f * hideAbove){
                    modelBatch.render(it, environment)
                }
            }
        }

        modelBatch.render(player, environment)

        modelBatch.end()
//        Gdx.gl.glDisable(GL_BLEND)

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.zoom /= 1.1f
            camera.update()
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.zoom *= 1.1f
            camera.update()
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camPathAngle += 1.0f
            camera.position.set(CAM_PATH_RADIUS, CAM_HEIGHT, 0f)
                    .rotate(Vector3.Y, camPathAngle).add(lookAt)
            camera.up.set(Vector3.Y)
            camera.lookAt(lookAt)
            camera.update()
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camPathAngle -= 1.0f
            camera.position.set(CAM_PATH_RADIUS, CAM_HEIGHT, 0f)
                    .rotate(Vector3.Y, camPathAngle).add(lookAt)
            camera.up.set(Vector3.Y)
            camera.lookAt(lookAt)
            camera.update()
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)){
            player.transform.translate(0.1f,0f,0.1f)
            camera.translate(0.1f,0f,0.1f)
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)){
            player.transform.translate(0.1f,0f,-0.1f)
            camera.translate(0.1f,0f,-0.1f)
        } else if(Gdx.input.isKeyPressed(Input.Keys.S)){
            player.transform.translate(-0.1f,0f,-0.1f)
            camera.translate(-0.1f,0f,-0.1f)
        } else if(Gdx.input.isKeyPressed(Input.Keys.D)){
            player.transform.translate(-0.1f,0f,0.1f)
            camera.translate(-0.1f,0f,0.1f)
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP)){
            hideAbove++
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.PAGE_DOWN)){
            hideAbove--
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            hiding = !hiding
        }
        camera.update()

        val hitCoords = hitSomething(Vector2(Gdx.input.x + 0f,Gdx.input.y + 0f))
        val hitModel = if(hitCoords != null) world[hitCoords.first][hitCoords.second][hitCoords.third] else null


        getAll().forEach{
            it.materials[0].set(ColorAttribute.createDiffuse(Color.GREEN))
        }

        if(hitCoords != null){
            val START = "1,0,0"
            val END = "${hitCoords.first},${hitCoords.second},${hitCoords.third}"

            with (Graph(edges, false)) {  // undirected
                dijkstra(START)
                val path = getPath(END)
                path?.forEachIndexed{ idx,triple ->
                    world[triple.first][triple.second][triple.third]!!.materials[0].set(ColorAttribute.createDiffuse(Color.RED))
                    println("path cube $idx is ${triple.toString()}")
                }
            }
        }


        if(hitModel == elevator && Gdx.input.isKeyJustPressed(Input.Keys.E)){
            println(elevator.transform.getTranslation(Vector3()).y)
            println("player on elevator? ${isPlayerOnElevator()}")
            movePlayerToo = isPlayerOnElevator()
            if(elevatorY == elevatorStartY){
                elevatorStartTime = System.currentTimeMillis()
                elevatorMovingUp = true
            } else if(elevatorY == elevatorEndY){
                elevatorMovingDown = true
            }
        }

        var speed = 0.1f

        if(elevatorMovingUp){
            elevator.transform.translate(0f,speed,0f)
            if(movePlayerToo){
                player.transform.translate(0f,speed,0f)
            }
            if(elevator.transform.getTranslation(Vector3()).y >= 7.5f){
                elevatorMovingUp = false
                elevatorY = 2
            }

        } else if(elevatorMovingDown){
            elevator.transform.translate(0f,-speed,0f)
            if(movePlayerToo){
                player.transform.translate(0f,-speed,0f)
            }
            if(elevator.transform.getTranslation(Vector3()).y <= 2.5f){
                elevatorMovingDown = false
                elevatorY = 1
            }
        }



        //spawnerSystem.tick()
        //inputSystem.handleKeyInput()
        //inputSystem.handleMouseInput()
        //aiSystem.tick()
        //drawingSystem.draw()
        //batch.begin()
        //batch.color = Color.PURPLE
        batch.begin()
        font.draw(batch,ECS.currentTimeSeconds.toString(),100f,100f)
        batch.end()
        //batch.end()
    }

    override fun dispose() {
        batch.dispose()
        modelBatch.dispose()
        model.dispose()
    }
}

class Edge(val v1: String, val v2: String, val dist: Int)

/** One vertex of the graph, complete with mappings to neighbouring vertices */
class Vertex(val name: String) : Comparable<Vertex> {

    var dist = Int.MAX_VALUE  // MAX_VALUE assumed to be infinity
    var previous: Vertex? = null
    val neighbours = HashMap<Vertex, Int>()

    fun printPath(): List<Triple<Int,Int,Int>> {
        val toReturn: MutableList<Triple<Int,Int,Int>> = mutableListOf()
        if (this == previous) {
            //print(name)
        }
        else if (previous == null) {
            //print("$name(unreached)")
        }
        else {
            previous!!.printPath().forEach{
                toReturn.add(it)
            }
            //print(" -> $name($dist)")
        }
        val split = name.split(",")
        val toAdd = Triple(split[0].toInt(),split[1].toInt(),split[2].toInt())
        toReturn.add(toAdd)
        return toReturn
    }

    override fun compareTo(other: Vertex): Int {
        if (dist == other.dist) return name.compareTo(other.name)
        return dist.compareTo(other.dist)
    }

    override fun toString() = "($name, $dist)"
}

class Graph(
        val edges: List<Edge>,
        val directed: Boolean,
        val showAllPaths: Boolean = false
) {
    // mapping of vertex names to Vertex objects, built from a set of Edges
    private val graph = HashMap<String, Vertex>(edges.size)

    init {
        // one pass to find all vertices
        for (e in edges) {
            if (!graph.containsKey(e.v1)) graph.put(e.v1, Vertex(e.v1))
            if (!graph.containsKey(e.v2)) graph.put(e.v2, Vertex(e.v2))
        }

        // another pass to set neighbouring vertices
        for (e in edges) {
            graph[e.v1]!!.neighbours.put(graph[e.v2]!!, e.dist)
            // also do this for an undirected graph if applicable
            if (!directed) graph[e.v2]!!.neighbours.put(graph[e.v1]!!, e.dist)
        }
    }

    /** Runs dijkstra using a specified source vertex */
    fun dijkstra(startName: String) {
        if (!graph.containsKey(startName)) {
            println("Graph doesn't contain start vertex '$startName'")
            return
        }
        val source = graph[startName]
        val q = TreeSet<Vertex>()

        // set-up vertices
        for (v in graph.values) {
            v.previous = if (v == source) source else null
            v.dist = if (v == source)  0 else Int.MAX_VALUE
            q.add(v)
        }

        dijkstra(q)
    }

    /** Implementation of dijkstra's algorithm using a binary heap */
    private fun dijkstra(q: TreeSet<Vertex>) {
        while (!q.isEmpty()) {
            // vertex with shortest distance (first iteration will return source)
            val u = q.pollFirst()
            // if distance is infinite we can ignore 'u' (and any other remaining vertices)
            // since they are unreachable
            if (u.dist == Int.MAX_VALUE) break

            //look at distances to each neighbour
            for (a in u.neighbours) {
                val v = a.key // the neighbour in this iteration

                val alternateDist = u.dist + a.value
                if (alternateDist < v.dist) { // shorter path to neighbour found
                    q.remove(v)
                    v.dist = alternateDist
                    v.previous = u
                    q.add(v)
                }
            }
        }
    }

    /** Prints a path from the source to the specified vertex */
    fun getPath(endName: String): List<Triple<Int,Int,Int>>? {
        if (!graph.containsKey(endName)) {
            println("Graph doesn't contain end vertex '$endName'")
            return null
        }
        //print(if (directed) "Directed   : " else "Undirected : ")
        return graph[endName]!!.printPath()
        //println()
        //if (showAllPaths) printAllPaths() else println()
    }

    /** Prints the path from the source to every vertex (output order is not guaranteed) */
    private fun printAllPaths() {
        for (v in graph.values) {
            v.printPath()
            println()
        }
        println()
    }
}