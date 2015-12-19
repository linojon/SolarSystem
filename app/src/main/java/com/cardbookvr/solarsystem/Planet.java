package com.cardbookvr.solarsystem;

import com.cardbookvr.renderbox.Transform;
import com.cardbookvr.solarsystem.RenderBoxExt.components.Sphere;

/**
 * Created by Jonathan on 12/18/2015.
 */
public class Planet {
    protected float rotation, orbit;
    protected Transform orbitTransform, transform;

    public float distance, radius;

    public Planet(float distance, float radius, float rotation, float orbit, Transform origin){
        setupPlanet(distance, radius, rotation, orbit, origin);
    }

    public Planet(float distance, float radius, float rotation, float orbit, int texId, Transform origin){
        setupPlanet(distance, radius, rotation, orbit, origin);
        transform.addComponent(new Sphere(texId));
    }

    public void setupPlanet(float distance, float radius, float rotation, float orbit, Transform origin){
        //Compress the distances and scale
        distance *= 0.5f;
        radius *= 100f;
        this.distance = distance;
        this.radius = radius;
        this.rotation = rotation;
        this.orbit = orbit;
        this.orbitTransform = new Transform();
        this.orbitTransform.setParent(origin, false);

        transform = new Transform()
                .setParent(orbitTransform, false)
                .setLocalPosition(distance, 0, 0)
                .setLocalRotation(180, 0, 0)
                .setLocalScale(radius, radius, radius);
    }

    public Transform getTransform(){return transform;}
    public Transform getOrbitransform(){return orbitTransform;}

    public void preDraw(float dt){
        orbitTransform.rotate(0, dt * orbit, 0);
        transform.rotate(0, dt * -rotation, 0);
    }
}
