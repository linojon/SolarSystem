package com.cardbookvr.solarsystem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Bundle;

import com.cardbookvr.renderbox.IRenderBox;
import com.cardbookvr.renderbox.RenderBox;
import com.cardbookvr.renderbox.Time;
import com.cardbookvr.renderbox.Transform;
import com.cardbookvr.renderbox.components.Camera;
import com.cardbookvr.solarsystem.RenderBoxExt.components.Sphere;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

public class MainActivity extends CardboardActivity implements IRenderBox {
    private static final String TAG = "Solar System";
//    private Transform sphere;
    Planet[] planets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRestoreGLStateEnabled(false);
        cardboardView.setRenderer(new RenderBox(this, this));
        setCardboardView(cardboardView);
    }

    @Override
    public void setup() {
        //Sun
        Transform sun = new Transform()
            .setLocalScale(6.963f, 6.963f, 6.963f)
            .addComponent(new Sphere(R.drawable.sun_tex, false));

        //"Sun" light
        RenderBox.instance.mainLight.color = new float[]{1, 1, 0.8f, 1};

        //Stars in the sky
        Transform stars = new Transform();
        stars.setParent(RenderBox.mainCamera.transform, false);
        stars.setLocalScale(Camera.Z_FAR, Camera.Z_FAR, Camera.Z_FAR);
        stars.addComponent(new Sphere(R.drawable.milky_way_tex, false));

        setupPlanets(sun);

//        sphere = new Transform()
//            .setLocalPosition(-147.1f, 0, 0)
//            .rotate(0, 0, 180f)
//            .addComponent(new Sphere(R.drawable.earth_tex, R.drawable.earth_night_tex));
        //RenderBox.mainCamera.getTransform().setLocalPosition(-147.1f, 2f, -2f);
        goToPlanet(2);
    }

    @Override
    public void preDraw() {
        float dt = Time.getDeltaTime();
        for(int i = 0; i < planets.length; i++){
            planets[i].preDraw(dt);
        }
//        sphere.rotate(0, dt * 10f, 0);
    }

    @Override
    public void postDraw() {

    }

    public void setupPlanets(Transform origin) {
        float[] distances = new float[]{57.9f, 108.2f, 149.6f, 227.9f, 778.3f, 142.7f, 287.1f, 4497.1f, 591.3f};
        float[] scales = new float[]{0.00244f, 0.006052f, 0.005371f, 0.00339f, 0.0069911f, 0.0058232f, 0.0025362f, 0.024622f, 0.001186f};
        float[] rotations = new float[]{1.60558398f, 0.387850945f, 20, 91.94905328f, 229.8726332f, 221.7594814f, 126.365738f, 118.4265293f, 353.9822708f};
        float[] orbits = new float[]{1.071484534f, 0.419475608f, 0.258029293f, 0.137191446f, 7.945353196f, 3.19961229f, 1.121063157f, 0.571857166f, 0.380491642f};

        int[] texIds = new int[]{
                //Texture source: http://laps.noaa.gov/albers/sos/mercury/mercury/mercury_rgb_cyl_www.jpg
                R.drawable.mercury_tex,
                //Texture source: http://csdrive.srru.ac.th/55122420119/texture/venus.jpg
                R.drawable.venus_tex,
                //Texture source: http://www.solarsystemscope.com/nexus/content/tc-earth_texture/tc-earth_daymap.jpg
                //Alternate: http://eoimages.gsfc.nasa.gov/images/imagerecords/73000/73580/world.topo.bathy.200401.3x21600x10800.jpg
                R.drawable.earth_tex,
                //Texture source: http://lh5.ggpht.com/-2aLH6cYiaKs/TdOsBtnpRqI/AAAAAAAAAP4/bnMOdD9OMjk/s9000/mars%2Btexture.jpg
                R.drawable.mars_tex,
                //Texture source: http://laps.noaa.gov/albers/sos/jupiter/jupiter/jupiter_rgb_cyl_www.jpg
                R.drawable.jupiter_tex,
                //Texture source: http://www.solarsystemscope.com/nexus/content/planet_textures/texture_saturn.jpg
                R.drawable.saturn_tex,
                //Shockingly googling "uranus texture" is totally safe
                //Texture source: http://www.astrosurf.com/nunes/render/maps/full/uranus.jpg
                R.drawable.uranus_tex,
                //Texture source: http://www.solarsystemscope.com/nexus/content/planet_textures/texture_neptune.jpg
                R.drawable.neptune_tex,
                //Texture source: http://www.shatters.net/celestia/files/pluto.jpg};
                R.drawable.pluto_tex
        };

        planets = new Planet[distances.length + 1];
        for(int i = 0; i < distances.length; i++){
            if (i==2) {
                planets[i] = new Earth(distances[i], scales[i], rotations[i], orbits[i], texIds[i], R.drawable.earth_night_tex, origin);
            } else {
                planets[i] = new Planet(distances[i], scales[i], rotations[i], orbits[i], texIds[i], origin);
            }
        }

//		Create the moon
//		Texture source: https://farm1.staticflickr.com/120/263411684_ea405ffa8f_o_d.jpg
        planets[distances.length] = new Planet(15f, 0.005f, 0, -0.516058586f, R.drawable.moon_tex, planets[2].getTransform());
    }

    int currPlanet = 2;

    public void onCardboardTrigger(){
        if (++currPlanet >= planets.length)
            currPlanet = 0;
        goToPlanet(currPlanet);
    }


    void goToPlanet(int index){
        RenderBox.mainCamera.getTransform().setParent(planets[index].getOrbitransform(), false);
        RenderBox.mainCamera.getTransform().setLocalPosition(planets[index].distance, planets[index].radius * 1.5f, planets[index].radius * 2f);
    }

    public static int loadTexture(final int resourceId){
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(RenderBox.instance.mainActivity.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }
}
