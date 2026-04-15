package org.lwjglb.engine.items;

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

    // TODO: Implement Car-specific logic
}
