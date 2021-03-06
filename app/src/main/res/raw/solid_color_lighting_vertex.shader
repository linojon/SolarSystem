uniform mat4 u_MVP;
uniform mat4 u_MV;

attribute vec4 a_Position;
attribute vec3 a_Normal;

varying vec3 v_Position;
varying vec3 v_Normal;

void main() {
   // Transform the vertex into eye space.
   v_Position = vec3(u_MV * a_Position);

   // Transform the normal's orientation into eye space.
   v_Normal = vec3(u_MV * vec4(a_Normal, 0.0));

   // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
   gl_Position = u_MVP * a_Position;
}
