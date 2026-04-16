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

        // Compares each vehicle with every other vehicle
        for (int i = 0; i < vehicles.size(); i++) {

            for (int j = i + 1; j < vehicles.size(); j++) {

                Vehicle vehicle1 = vehicles.get(i);  // Assigns first vehicle in the pair
                Vehicle vehicle2 = vehicles.get(j);  // Assigns second vehicles in the pair

                // Generates a unique key for this pair to track logging
                String key = generateLogKey(vehicle1, vehicle2);

                // Triggers collision methods (if the vehicles are close enough)
                if (checkVehicleCollision(vehicle1, vehicle2)) {
                    vehicle1.collide(vehicle2);
                    vehicle2.collide(vehicle1);

                    // Logs the collision only the first time it occurs
                    if (!activeCollisions.contains(key)) {
                        System.out.println("Collision Occurred: " +
                                vehicle1.getClass().getSimpleName() +
                                " <--> " + vehicle2.getClass().getSimpleName() +
                                " at position (" +
                                vehicle1.getPosition().x +
                                ", " +
                                vehicle1.getPosition().y +
                                ", " +
                                vehicle1.getPosition().z +
                                ")"
                        );
                        // Marks this collision as logged
                        activeCollisions.add(key);
                    }
                } else {
                    // Removes entry when collision ends
                    activeCollisions.remove(key);
                }
            }
        }
    }

    /**
     * Determines whether two vehicles are colliding based on distance.
     * @param vehicle1
     * @param vehicle2
     * @return
     */
    private static boolean checkVehicleCollision(Vehicle vehicle1, Vehicle vehicle2) {

        // Calculates the distance between the two vehicle positions
        float distance = vehicle1.getPosition().distance(vehicle2.getPosition());

        return distance < vehicleThreshold;  // Returns boolean determining collision
    }

    // Creates a consistent pair key
    private static String generateLogKey(Vehicle v1, Vehicle v2) {

        int id1 = System.identityHashCode(v1);  // ...
        int id2 = System.identityHashCode(v2);  // ...

        // ...
        if (id1 < id2) {
            return id1 + "_" + id2;
        } else {
            return id2 + "_" + id1;
        }
    }
}
