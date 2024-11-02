package org.example.utils;

import java.nio.file.Paths;

/**
 * The {@code Config} class holds configuration constants that can be used across the application.
 */
public class Config {

    // Constant representing the default local storage path
    public static final String DEFAULT_LOCAL_STORAGE_PATH = Paths.get("src", "main", "resources", "localdata").toString();

    // Constant representing the name of the file used to store users
    public static final String DEFAULT_LOCAL_USER_STORAGE = "userData";

    // Constant representing the name of the file used to store friendship relations
    public static final String DEFAULT_LOCAL_FRIENDSHIP_STORAGE = "friendshipData";
}
