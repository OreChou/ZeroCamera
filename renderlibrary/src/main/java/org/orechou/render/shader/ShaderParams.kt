package org.orechou.render.shader

object ShaderParams {

    /**
     * Texture Shader Params
     */
    const val POSITION_ATTRIBUTE = "aPosition"
    const val TEXTURE_COORD = "aTextureCoord"
    const val MATRIX_UNIFORM = "uMatrix"

    /**
     * Fragment Shader Params
     */
    const val TEXTURE_SAMPLER_UNIFORM = "uTextureSampler"

    val SIMPLE_PROGRAM_HANDLE = hashMapOf(
        POSITION_ATTRIBUTE to BaseHandle.AttributeHandle(POSITION_ATTRIBUTE),
        TEXTURE_COORD to BaseHandle.AttributeHandle(TEXTURE_COORD),
        MATRIX_UNIFORM to BaseHandle.UniformHandle(MATRIX_UNIFORM),
        TEXTURE_SAMPLER_UNIFORM to BaseHandle.UniformHandle(TEXTURE_SAMPLER_UNIFORM)
    )

}