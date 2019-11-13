package org.orechou.render.shader

import org.orechou.render.util.GLES20Utils

abstract class BaseHandle(val name: String) {

    private var handle = 0

    abstract fun load(program: Int)

    class AttributeHandle(name: String) : BaseHandle(name) {
        override fun load(program: Int) {
            GLES20Utils.getAttribLocation(program, name)
        }
    }

    class UniformHandle(name: String) : BaseHandle(name) {
        override fun load(program: Int) {
            GLES20Utils.getUniformLocation(program, name)
        }
    }
}