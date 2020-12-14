package org.andresoviedo.app.model3D.arcorehelpers;

import androidx.annotation.NonNull;

public class Animal {

    private final String name;
    private final String source3D;

    Animal(String name, String source3D) {
        this.name = name;
        this.source3D = source3D;
    }

    public String getName() {
        return name;
    }

    String getSource3D() {
        return source3D;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + source3D.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}

