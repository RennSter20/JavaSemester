package com.example.renatojava.javasemester.entity;

public class Change<T> {

    private T oldObject;
    private T newObject;

    public Change(T oldObject, T newObject) {
        this.oldObject = oldObject;
        this.newObject = newObject;
    }

    public T getOldObject() {
        return oldObject;
    }

    public void setOldObject(T oldObject) {
        this.oldObject = oldObject;
    }

    public T getNewObject() {
        return newObject;
    }

    public void setNewObject(T newObject) {
        this.newObject = newObject;
    }
}
