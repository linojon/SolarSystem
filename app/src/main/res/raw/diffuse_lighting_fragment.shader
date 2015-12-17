precision mediump float;        // Set the default precision to medium. We don't need as high of a
                                // precision in the fragment shader.
uniform vec3 u_LightPos;        // The position of the light in eye space.
uniform vec4 u_LightCol;
uniform sampler2D u_Texture;    // The input texture.

varying vec3 v_Position;        // Interpolated position for this fragment.
                                // triangle per fragment.
varying vec3 v_Normal;          // Interpolated normal for this fragment.
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.

// The entry point for our fragment shader.
void main() {
    // Will be used for attenuation.
    float distance = length(u_LightPos - v_Position);

    // Doing lighting calculations in the fragment give us an interpolated normal--smoother shading
    // Get a lighting direction vector from the light to the vertex.

    vec3 lightVector = normalize(u_LightPos - v_Position);

    // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
    // pointing in the same direction then it will get max illumination.
    float diffuse = max(dot(v_Normal, lightVector), 0.01);

    // Add attenuation.
    //diffuse = diffuse * (1.0 / (1.0 + (0.001 * distance)));    //We might lose Pluto...

    // Add ambient lighting
    diffuse = diffuse + 0.025;  //very little ambient lighting.... this is space

    // Multiply the color by the diffuse illumination level and texture value to get final output color.
    gl_FragColor = texture2D(u_Texture, v_TexCoordinate) * u_LightCol * diffuse;
    //gl_FragColor = texture2D(u_Texture, v_TexCoordinate);
}