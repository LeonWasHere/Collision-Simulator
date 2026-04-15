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

    private float bound = 15.0f;  // Defines movement boundary

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
     * Adjusts velocity based on speed and applies full 3D boundary bouncing.
     * @param interval
     */
    @Override
    public void update(float interval) {

        // Applies acceleration to speed
        super.update(interval);

        Vector3f pos = getPosition();  // Current position
        Vector3f vel = getVelocity();  // Current velocity vector

        // Normalizes velocity when not zero (otherwise default forward Z movement)
        if (vel.length() != 0) {
            vel.normalize().mul(speed);
        } else {
            vel.z = speed;
        }

        // Defines boundary bouncing (X axis)
        if (Math.abs(pos.x) > bound) {
            vel.x = -vel.x;
        }

        // Defines boundary bouncing (Y axis)
        if (Math.abs(pos.y) > bound) {
            vel.y = -vel.y;
        }

        // Defines boundary bouncing (Z axis)
        if (Math.abs(pos.z) > bound) {
            vel.z = -vel.z;
        }
    }

    @Override
    public void collide(Vehicle other) {

        Vector3f vel = getVelocity();  // Current velocity vector

        // Drops downward (simulates altitude loss)
        vel.y = -0.05f;

        // Prints info to the console
        System.out.println("Plane collision (descending)");
    }
}
