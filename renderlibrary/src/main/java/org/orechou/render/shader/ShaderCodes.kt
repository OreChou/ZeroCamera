package org.orechou.render.shader

object ShaderCodes {

    const val TEXTURE_VERTEX_SHADER = "attribute vec4 " + ShaderParams.POSITION_ATTRIBUTE + ";\n" +
            "attribute vec2 " + ShaderParams.TEXTURE_COORD + ";\n" +
            "uniform mat4 " + ShaderParams.MATRIX_UNIFORM + ";\n" +
            "varying vec2 vTextureCoord;\n" +
            "void main() {\n" +
            "    vTextureCoord = (" + ShaderParams.MATRIX_UNIFORM + "*" + ShaderParams.TEXTURE_COORD + ").xy;\n" +
            "    gl_Position = " + ShaderParams.POSITION_ATTRIBUTE + ";\n" +
            "}\n"

    const val OES_TEXTURE_FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "uniform samplerExternalOES " + ShaderParams.TEXTURE_SAMPLER_UNIFORM + ";\n" +
            "varying vec2 vTextureCoord;\n" +
            "void main() {\n" +
            "    gl_FragColor = = texture2D(" + ShaderParams.TEXTURE_SAMPLER_UNIFORM + ", vTextureCoord);" +
            "}\n"

}