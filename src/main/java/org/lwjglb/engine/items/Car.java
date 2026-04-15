package org.lwjglb.engine.items;

import org.joml.Vector3f;
import org.lwjglb.engine.graph.Mesh;

/**
 * Author: Leon Wasiliew
 * Subject: INFT4000 - Special Topics 1
 * Creation Date: 2026-04-15
 * Modification Date: 2026-04-15
 * Description: Car is a subtype of Vehicle and represents
 * a ground entity (no vertical movement).
 */

public class Car extends Vehicle {

    private float groundY = 0.0f;  // Defaults to 0 on Y-axis (change to not clip tires through)

    /**
     * Default constructor for Car.
     * Initializes a car with default movement properties.
     * Calls the parent Car constructor.
     */
    public Car() {
        super();
    }

    /**
     * Parameterized constructor for Car.
     * Initializes a car with a single mesh.
     * Calls the parent Vehicle constructor.
     * @param mesh
     */
    public Car(Mesh mesh) {
        super(mesh);
    }

    /**
     * Parameterized constructor for Car.
     * Initializes a car with multiple meshes.
     * Calls the parent Vehicle constructor.
     * @param meshes
     */
    public Car(Mesh[] meshes) {
        super(meshes);
    }

    /**
     * Updates the movement state of the car.
     * Applies base Vehicle physics, locks vertical movement,
     * and enforces simple boundary bouncing on X and Z axes.
     * @param interval
     */
    @Override
    public void update(float interval) {

        // Applies vehicle physics (speed & acceleration)
        super.update(interval);

        Vector3f pos = getPosition();  // Current position
        Vector3f vel = getVelocity();  // Current velocity

        // Locks Y movement (car stays on the ground)
        pos.y = groundY;
        vel.y = 0;

        // Bounces X axis (boundary constraint)
        if (Math.abs(pos.x) > 15) {
            vel.x = -vel.x;
        }

        // Bounces Z axis (boundary constraint)
        if (Math.abs(pos.z) > 15) {
            vel.z = -vel.z;
        }
    }

    /**
     * Handles a basic collision event between a car and another game item.
     * @param other
     */
    @Override
    public void collide(Vehicle other) {

        Vector3f vel = getVelocity();  // Current velocity vector

        // Reverses horizontal direction (bounce)
        vel.x = -vel.x;
        vel.z = -vel.z;

        // Prints info to the console
        System.out.println("Car collision (bounce)");
    }
}
