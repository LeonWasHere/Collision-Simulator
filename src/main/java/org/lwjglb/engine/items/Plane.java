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

    // private float maxSpeed = 0.05f;  // Keeps plane slow

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

        Vector3f pos = getPosition();  // Retrieves current plane position
        Vector3f vel = getVelocity();  // Retrieves current plane velocity

        // Applies velocity to position for 3D movement
        pos.x += vel.x;
        pos.y += vel.y;
        pos.z += vel.z;
    }

    @Override
    public void collide(Vehicle other) {

        Vector3f pos = getPosition();             // Stores the position of the vehicle
        Vector3f otherPos = other.getPosition();  // Stores the position of another vehicle
        Vector3f vel = getVelocity();             // Stores plane velocity

        // Calculates direction pointing away from the collision
        Vector3f pushDir = new Vector3f(pos).sub(otherPos);

        // Normalizes only if direction is not zero
        if (pushDir.lengthSquared() > 0) {
            pushDir.normalize();
        }

        // Calculates dor product to determine if plane is moving toward the collision
        float dot = vel.dot(pushDir);

        // Reflects velocity only when moving into collision
        if (dot < 0) {
            vel.sub(new Vector3f(pushDir).mul(2 * dot));
        }

        // Applies small offset to prevent repeated collision triggers
        pos.add(new Vector3f(pushDir).mul(0.2f));
    }
}
