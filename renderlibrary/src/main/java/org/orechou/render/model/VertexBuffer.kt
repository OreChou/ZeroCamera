package org.orechou.render.model

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

object VertexBuffer {

    private val vertexData = floatArrayOf(
        1f, 1f, 1f, 1f,
        -1f, 1f, 0f, 1f,
        -1f, -1f, 0f, 0f,
        1f, 1f, 1f, 1f,
        -1f, -1f, 0f, 0f,
        1f, -1f, 1f, 0f
    )
    val vertexBuffer: FloatBuffer
    init {
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)
        vertexBuffer.position(0)
    }
}