package _03_jeopardy_api;

import _03_jeopardy_api.data_transfer_objects.ClueWrapper;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Random;

/*
Now lets use a Jeopardy API to make a (modified) game.
To simplify things a little bit, we will just ask the user one question from each available point category
 */

public class JeopardyApi {

    private final WebClient webClient;

    private static final String baseUrl = "http://jservice.io/api/clues";

    public JeopardyApi() {
        webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public ClueWrapper getClue(int value) {

        //create the request URL
        //can be found in the documentation: http://jservice.io/
            //enter to code from the NewsAPI example to make the request
        Flux<ClueWrapper> clueWrapperFlux = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("value", value)
                        .build())
                .retrieve()
                /*.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
                        clientResponse -> Mono.empty())*/
                .bodyToFlux(ClueWrapper.class);

            //1
            //uncomment the next line to see the actual JSON response -
            // this is what was inputted into jsonschema2pojo.com
            //System.out.println(jsonArray);

            //2
            //Use http://www.jsonschema2pojo.org/ to generate your POJOs
            //and place them in the cat_facts_API.pojo package
            //select Target Language = java, Source Type = JSON, Annotation Style = Gson


        List<ClueWrapper> clueWrapper =  clueWrapperFlux.collectList().block();

        //3
            //Get a random number less than the size of the jsonArray
            int index = new Random().nextInt(clueWrapper.size());

            //deserialize the response into a java object using the class you just created
            ClueWrapper clue = clueWrapper.get(index);

            //4
            //return the clue at that index in the jsonArray
            return clue;


    }
}
