package com.example.serv1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Entity(name = "MyClient")
@Table(name = "clients")
@AllArgsConstructor
@NoArgsConstructor
public class MyClient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "client_generator")
    @SequenceGenerator(name = "client_generator",initialValue = 1,allocationSize = 1)
    private long id;

    private String email;

    private String name;

    private String surName;

    private int age;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyClient client)) return false;
        return getId() == client.getId() && getAge() == client.getAge() && Objects.equals(getEmail(), client.getEmail()) && Objects.equals(getName(), client.getName()) && Objects.equals(getSurName(), client.getSurName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getName(), getSurName(), getAge());
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surName='" + surName + '\'' +
                ", age=" + age +
                '}';
    }
}
