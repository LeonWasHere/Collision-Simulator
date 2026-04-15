package org.lwjglb.engine.items;

/**
 * Author: Leon Wasiliew
 * Subject: INFT4000 - Special Topics 1
 * Creation Date: 2026-04-15
 * Modification Date: 2026-04-15
 * Description: Vehicle is a subtype of GameItem and represents
 * movable entities in the scene such as Cars and Planes.
 */

import org.lwjglb.engine.graph.Mesh;

public class Vehicle extends GameItem {

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
}
