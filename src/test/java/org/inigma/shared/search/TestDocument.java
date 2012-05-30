package org.inigma.shared.search;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class TestDocument {
    private String id;
    private String name;
    private int age;
    private TestAddress address = new TestAddress();
    private Date created;

    public TestAddress getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    public Date getCreated() {
        return created;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setAddress(TestAddress address) {
        this.address = address;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
