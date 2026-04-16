package org.lwjglb.engine.items;

import org.joml.Vector3f;
import org.lwjglb.engine.graph.Mesh;

/**
 * Author: Leon Wasiliew
 * Subject: INFT4000 - Special Topics 1
 * Creation Date: 2026-04-15
 * Modification Date: 2026-04-15
 * Description: Plane is a subtype of Vehicle and represents
 * a flying entity (no restricted movement).
 */

public class Plane extends Vehicle {

    private float maxSpeed = 0.05f;  // Keeps plane slow

    /**
     * Default constructor for Plane.
     * Initializes a plane with default movement properties.
     * Calls the parent Vehicle constructor.
     */
    public Plane() {
        super();
    }

    /**
     * Parameterized constructor for Plane.
     * Initializes a plane with a single mesh.
     * Calls the parent Vehicle constructor.
     * @param mesh
     */
    public Plane(Mesh mesh) {
        super(mesh);
    }

    /**
     * Parameterized constructor for Plane.
     * Initializes a plane with a single mesh.
     * Calls the parent Vehicle constructor.
     * @param meshes
     */
    public Plane(Mesh[] meshes) {
        super(meshes);
    }

    /**
     * Updates the movement state of the plane.
     * Allows full 3D movement with speed limiting.
     * @param interval
     */
    @Override
    public void update(float interval) {

        // Applies acceleration to speed
        super.update(interval);

        Vector3f vel = getVelocity();  // Current velocity vector

        // Limits speed
        if (vel.length() > maxSpeed) {
            vel.normalize().mul(maxSpeed);
        }
    }

    /**
     * Handles a basic collision event between a plane and another vehicle.
     * @param other
     */
    @Override
    public void collide(Vehicle other) {

        Vector3f vel = getVelocity();  // Current velocity vector

        // Simulates downward drop on collision
        vel.y = -Math.abs(vel.y) - 0.02f;

        // Applies dampening for realism
        vel.x *= 0.8f;
        vel.y *= 0.8f;
        vel.z *= 0.8f;

        // Optional debug
        // System.out.println("Plane collision (descending)");
    }
}
