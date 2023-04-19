package ru.practikum;

public class DeleteRequest {
    public String id;

    public DeleteRequest() {
    }

    public DeleteRequest(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
