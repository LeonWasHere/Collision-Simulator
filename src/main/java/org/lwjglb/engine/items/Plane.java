package org.lwjglb.engine.items;

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

    // TODO: Implement Plane-specific logic
}
