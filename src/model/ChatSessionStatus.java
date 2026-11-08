package model;

/** A chat session is open for exactly as long as its order is unfulfilled. */
public enum ChatSessionStatus {
    ACTIVE,
    CLOSED
}
