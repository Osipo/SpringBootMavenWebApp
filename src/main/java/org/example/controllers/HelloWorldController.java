package org.example.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.builders.JdbcWorkerBuilder;
import org.example.data.IStringGen;
import org.example.data.dto.CommentDto;
import org.example.data.map.CommentMapper;
import org.example.managers.IDbWorkerManager;
import org.example.properties.ApplicationProperties;
import org.example.properties.DbProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HelloWorldController extends BaseController {

    IStringGen strGen;
    IDbWorkerManager dbManager;

    //@Autowired
    //CommentRepository commentRepository;


    @Autowired
    public HelloWorldController(ApplicationProperties configuration,
                                IStringGen strGen,
                                IDbWorkerManager dbManager
    )
    {
        super(configuration);
        this.strGen = strGen;
        this.dbManager = dbManager;

        //0 => sql-server, 1, 2 => sql-server/windows-auth, 3 => h2:mem:testdb, 4 => jdbc:h2:tcp::/localhost/~/test
        this.dbManager.setConnection(new JdbcWorkerBuilder(), this.configuration.getDbProperties().getConnections().get(DbProperties.SQL_SERVER_WINDOWS));
    }



    @GetMapping(value = "/hello", produces = {"application/json", "application/xml", "text/json", "text/xml"})
    public ResponseEntity<String> hello(@RequestHeader MultiValueMap<String, String> headers)
        throws InterruptedException, ExecutionException, CancellationException
    {
        //computing is async call join to await.
        CompletableFuture<String> result = strGen.sayHello();


        System.out.println("Current request thread: " + Thread.currentThread().getName());
        System.out.println("body computing in another async thread...");
        CompletableFuture.allOf(result).join();
        System.out.println("body computing completed at thread: " + Thread.currentThread().getName());
        String body = result.get();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping(value = "/hellolist", produces = {"application/json", "application/xml", "text/json", "text/xml"})
    public List<String> hellolist(@RequestHeader MultiValueMap<String, String> headers){
        return List.of("Dr", "Coomer", "requires", "five", "dollar", "coins");
    }



    @GetMapping(value = "/throw", produces = {"application/json", "application/xml", "text/json", "text/xml"})
    public String getException() throws Exception {
        throw new RuntimeException("Error thrown");
    }


    //JDBC Queries.
    //1. Raw data.
    @GetMapping(value = "/testquerylist", produces = {"application/json", "application/xml", "text/json", "text/xml"})
    public List<Map<String, Object>> getTestQueryList(){
        List<Map<String, Object>> result;

        result = dbManager.getDbWorker().queryList();
        /*
        result = dbManager.getDbWorker().query("SELECT * from Comments (nolock) order by CommentId DESC");

        if(result.size() > 0)
            System.out.println(result.get(0).get("CreatedAt").getClass().getName());
         */
        return result;
    }

    @GetMapping(value = "/testquerylist/{id:\\d+}", produces = {"application/json", "application/xml", "text/json", "text/xml"})
    public List<Map<String, Object>> getTestQueryList(@PathVariable Integer id){
        return dbManager.getDbWorker().queryList(id);
        //return dbManager.getDbWorker().queryId("SELECT * from Comments (nolock) where CommentId = ? ", id);
    }




    //2. With Mapping.
    @GetMapping(value = "/testquerylist2", produces = {"application/json", "application/xml", "text/json", "text/xml"})
    public List<CommentDto> getTestQueryList2(){
        CommentMapper m = new CommentMapper();
        return m.map(dbManager.getDbWorker().query("SELECT * from Comments (nolock) order by CommentId DESC"));
    }


}