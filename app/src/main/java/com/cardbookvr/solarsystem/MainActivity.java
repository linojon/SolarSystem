package com.cardbookvr.solarsystem;

import android.os.Bundle;

import com.cardbookvr.renderbox.IRenderBox;
import com.cardbookvr.renderbox.RenderBox;
import com.cardbookvr.renderbox.Transform;
import com.cardbookvr.solarsystem.RenderBoxExt.components.Sphere;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

public class MainActivity extends CardboardActivity implements IRenderBox {
    private static final String TAG = "Solar System";
    private Transform sphere;

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
        sphere = new Transform();
        float[] color = new float[]{1, 1, 0.5f, 1};
        sphere.addComponent(new Sphere(color));
        sphere.setLocalPosition(2.0f, -2.f, 5.0f);
    }

    @Override
    public void preDraw() {

    }

    @Override
    public void postDraw() {

    }
}
