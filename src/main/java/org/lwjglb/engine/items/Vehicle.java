package org.lwjglb.engine.items;

/**
 * Author: Leon Wasiliew
 * Subject: INFT4000 - Special Topics 1
 * Creation Date: 2026-04-15
 * Modification Date: 2026-04-15
 * Description: Vehicle is a subtype of GameItem and represents
 * movable entities in the scene such as Cars and Planes.
 */

import org.joml.Vector3f;
import org.lwjglb.engine.graph.Mesh;

public class Vehicle extends GameItem {

    protected float speed = 0.01f;           // Stores current forward movement speed
    protected float acceleration = 0.0f;     // Stores rate at which speed increases or decreases
    protected float collisionRadius = 2.0f;  // Stores the collision radius

    /**
     * Default constructor for Vehicle.
     * Initializes a vehicle with default movement properties.
     * Calls the parent GameItem constructor.
     */
    public Vehicle() {
        super();
    }

    /**
     * Parameterized constructor for Vehicle.
     * Initializes a vehicle with a single mesh.
     * Calls the parent GameItem constructor.
     * @param mesh
     */
    public Vehicle(Mesh mesh) {
        super(mesh);
    }

    /**
     * Parameterized constructor for Vehicle.
     * Initializes a vehicle with multiple meshes.
     * Calls the parent GameItem constructor.
     * @param meshes
     */
    public Vehicle(Mesh[] meshes) {
        super(meshes);
    }

    /**
     * Gets the current movement speed of the vehicle.
     * @return
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets the movement speed of the vehicle.
     * @param speed
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Gets the current acceleration value of the vehicle.
     * @return
     */
    public float getAcceleration() {
        return acceleration;
    }

    /**
     * Sets the acceleration value of the vehicle.
     * @param acceleration
     */
    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * Updates the movement state of the vehicle.
     * @param interval
     */
    public void update(float interval) {
        // Applies acceleration to speed
        speed += acceleration * interval;

        // Applies restrictions for possible speed
        if (speed < 0.0f) {
            speed = 0.0f;
        }
        if (speed > 0.5f) {
            speed = 0.5f;
        }

        // Applies basic movement
        getVelocity().z = speed;
    }

    /**
     * Handles a basic collision event between this game item and another.
     * @param other
     */
    public void collide(Vehicle other) {
        // Prints info to the console
        System.out.println("'" + this.getClass().getSimpleName() + "' collided with "
                + other.getClass().getSimpleName());
    }

    /**
     * Returns the collision radius.
     * @return
     */
    public float getCollisionRadius() {
        return collisionRadius;
    }

    /**
     * Sets the collision radius.
     * @param radius
     */
    public void setCollisionRadius(float radius) {
        this.collisionRadius = radius;
    }
}
