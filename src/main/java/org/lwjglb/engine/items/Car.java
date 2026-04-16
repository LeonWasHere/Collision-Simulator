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
     * Applies base Vehicle physics and enforces ground-only behavior.
     * @param interval
     */
    @Override
    public void update(float interval) {

        // Applies vehicle physics (speed & acceleration)
        super.update(interval);

        Vector3f pos = getPosition();  // Current position
        Vector3f vel = getVelocity();  // Current velocity

        // Locks Y movement to the ground
        pos.y = groundY;

        // Stops vertical movement
        vel.y = 0;

    }

    /**
     * Handles a basic collision event between a car and another vehicle.
     * @param other
     */
    @Override
    public void collide(Vehicle other) {

        Vector3f vel = getVelocity();  // Current velocity vector

        // Reverses horizontal movement
        vel.x = -vel.x;
        vel.z = -vel.z;

        // Optional debug
        // System.out.println("Car collision (bounce)");
    }
}
