package org.orechou.render.model

interface Render {

    fun prepare()

    fun draw(textureSurface: org.orechou.support.model.TextureSurface)

    fun finish()

}