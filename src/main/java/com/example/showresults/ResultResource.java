package com.example.showresults;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.Document;

@Path("/result")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ResultResource {

    private static boolean authorization;
    @Path("/auth")
    @POST
    public Response auth(Credentials credentials) {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://auth:8080/Authentication/api/auth/auth");
            Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(credentials));
            if (response.getStatus() != 200) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            authorization = true;
            return Response.status(Response.Status.ACCEPTED).header("Authorization",true).build();
        }catch (Exception e){
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
    @Path("/analysis")
    @GET
    public Response getData(){
        if(!authorization)
            return Response.status(Response.Status.UNAUTHORIZED).build();
        try (MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://my-mongodb:27017"))){
            MongoDatabase database = mongoClient.getDatabase("data");
            MongoCollection<Document> collection = database.getCollection("analysis");
            Document lastDocument = collection.find().sort(Sorts.descending("$natural")).first();
            AnalysisResult analysisResult = new AnalysisResult(lastDocument.getDouble("avg"),lastDocument.getInteger("max"),
                    lastDocument.getInteger("min"));
            return Response.ok(analysisResult).header("Authorization", true).build();
        }catch (Exception e){
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
