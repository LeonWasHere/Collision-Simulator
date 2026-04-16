package org.lwjglb.engine;

/**
 * Author: Leon Wasiliew
 * Subject: INFT4000 - Special Topics 1
 * Creation Date: 2026-04-15
 * Modification Date: 2026-04-15
 * Description: CollisionManager provides a simple distance-calculated collision
 * detection for Vehicle objects in the scene.
 * It evaluates all vehicle pairs, triggers their collision,
 * and suppresses repeated log messages for the same collision pair.
 */

import org.joml.Vector3f;
import org.lwjglb.engine.items.Plane;
import org.lwjglb.engine.items.Vehicle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollisionManager {

    private static float vehicleThreshold = 2.0f;  // Defines the distance before vehicles are colliding

    // Tracks active collisions to prevent repeated logging for the same vehicles
    private static final Set<String> activeCollisions = new HashSet<>();

    /**
     * Iterates through all vehicles and checks for collisions between them.
     * Each unique pair is evaluated once per update cycle.
     * @param vehicles
     */
    public static void manageVehicleCollision(List<Vehicle> vehicles) {

        // Loops through all vehicles
        for (int i = 0; i < vehicles.size(); i++) {
            for (int j = i + 1; j < vehicles.size(); j++) {

                // Retrieves two vehicles being compared
                Vehicle vehicle1 = vehicles.get(i);
                Vehicle vehicle2 = vehicles.get(j);

                // Generates a key for the vehicle pair
                String key = generateLogKey(vehicle1, vehicle2);

                // Checks if the vehicles are colliding
                if (check3DCollision(vehicle1, vehicle2)) {

                    // Triggers collision
                    vehicle1.collide(vehicle2);
                    vehicle2.collide(vehicle1);

                    // Logs collision once per active pair
                    if (!activeCollisions.contains(key)) {
                        System.out.println("Collision Occurred: " +
                                vehicle1.getClass().getSimpleName() +
                                " <--> " +
                                vehicle2.getClass().getSimpleName());

                        activeCollisions.add(key);
                    }

                } else {
                    // Removes key when vehicles are no longer colliding
                    activeCollisions.remove(key);
                }
            }
        }
    }

    /**
     * Determines whether two vehicles are colliding based on 3D distance.
     * @param a
     * @param b
     * @return
     */
    private static boolean check3DCollision(Vehicle a, Vehicle b) {

        // Gets both positions of both vehicles
        Vector3f pa = a.getPosition();
        Vector3f pb = b.getPosition();

        // Calculates axis difference
        float dx = pa.x - pb.x;
        float dy = pa.y - pb.y;
        float dz = pa.z - pb.z;

        // Calculates squared distance between vehicles
        float distSquared = dx * dx + dy * dy + dz * dz;

        // Computes combined collision radius with buffer
        float radius = (a.getCollisionRadius() + b.getCollisionRadius()) * 1.1f;

        // Compares squared values
        return distSquared < radius * radius;
    }

    // Creates a consistent pair key
    private static String generateLogKey(Vehicle v1, Vehicle v2) {

        int id1 = System.identityHashCode(v1);  // Stores unique ID for first vehicle
        int id2 = System.identityHashCode(v2);  // Stores unique ID for second vehicle

        // Ensures consistent ordering of pairs
        if (id1 < id2) {
            return id1 + "_" + id2;
        } else {
            return id2 + "_" + id1;
        }
    }
}
