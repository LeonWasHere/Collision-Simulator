package org.lwjglb.engine;

/**
 * Author: Leon Wasiliew
 * Subject: INFT4000 - Special Topics 1
 * Creation Date: 2026-04-15
 * Modification Date: 2026-04-15
 * Description: CollisionManager provides a simple distance-calculated collision
 * detection for Vehicle objects in the scene.
 */

import org.lwjglb.engine.items.Vehicle;

import java.util.List;

public class CollisionManager {

    private static float vehicleThreshold = 2.0f;  // Defines the distance before vehicles are colliding

    /**
     * Iterates through all vehicles and checks for collisions between them.
     * @param vehicles
     */
    public static void manageVehicleCollision(List<Vehicle> vehicles) {

        // Compares each vehicle with every other vehicle
        for (int i = 0; i < vehicles.size(); i++) {

            for (int j = i + 1; j < vehicles.size(); j++) {

                Vehicle vehicle1 = vehicles.get(i);  // Assigns first vehicle in the pair
                Vehicle vehicle2 = vehicles.get(j);  // Assigns second vehicles in the pair

                // Triggers collision methods (if the vehicles are close enough)
                if (checkVehicleCollision(vehicle1, vehicle2)) {
                    vehicle1.collide(vehicle2);
                    vehicle2.collide(vehicle1);
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
}
