package org.acme.openapi.swaggerui;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/fruits")
public class FruitResource {

    private Set<Fruit> fruits = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private int currentId = 3; // Starting ID for new fruits

    public FruitResource() {
        fruits.add(new Fruit(1, "Apple", "Winter fruit"));
        fruits.add(new Fruit(2, "Pineapple", "Tropical fruit"));
    }

    @GET
    public Set<Fruit> list() {
        return fruits;
    }

    @POST
    public Set<Fruit> add(Fruit fruit) {
        fruit.id = currentId++;
        fruits.add(fruit);
        return fruits;
    }

    @DELETE
    public Set<Fruit> delete(int id) {
        fruits.removeIf(fruit -> fruit.getId() == id);
        return fruits;
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, Fruit fruit) {
        for (Fruit existingFruit : fruits) {
            if (existingFruit.id == id) {
                existingFruit.name = fruit.name;
                existingFruit.description = fruit.description;
                return Response.ok(existingFruit).build();
            }
        }
        return Response.status(Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") int id) {
        Optional<Fruit> fruit = fruits.stream()
                                      .filter(existingFruit -> existingFruit.id == id)
                                      .findFirst();
        if (fruit.isPresent()) {
            return Response.ok(fruit.get()).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    // @PUT
    // @Path("/updateName/{id}")
    // public Response updateName(@PathParam("id") int id, Fruit fruit) {
    //     for (Fruit existingFruit : fruits) {
    //         if (existingFruit.id == id) {
    //             existingFruit.name = fruit.name;
    //             return Response.ok(existingFruit).build();
    //         }
    //     }
    //     return Response.status(Status.NOT_FOUND).build();
    // }
}
