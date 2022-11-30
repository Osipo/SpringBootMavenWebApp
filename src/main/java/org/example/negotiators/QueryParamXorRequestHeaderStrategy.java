package org.example.negotiators;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.properties.ApplicationProperties;
import org.example.utils.IStringToMediaTypeConverter;
import org.example.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component("query_request_default_strategy")
public class QueryParamXorRequestHeaderStrategy implements ContentNegotiationStrategy {


    //injected through constructor
    ApplicationProperties http_config_options;
    IStringToMediaTypeConverter stringToMediaTypes;
    MediaType defaulMediaType;

    @Autowired
    @Qualifier("supportedMediaTypes")
    List<String> supported;

    @Autowired
    public QueryParamXorRequestHeaderStrategy(ApplicationProperties http_config_options, @Qualifier("str_media_map_with_aliases") IStringToMediaTypeConverter stringToMediaTypes){
        this.http_config_options = http_config_options;
        this.stringToMediaTypes = stringToMediaTypes;
        this.defaulMediaType = this.stringToMediaTypes.getFirstByQualityOrDefault(http_config_options.getHttpProperties().getDefaultMediaTypeString(), null);
    }

    /*
        Use next strategy:
        0. Retrieve from query param.
        1. If query param is absent retrieve from accept header
        2. else if accept header is also missing -> use default type.
        3. else if default type is missing too -> throw exception.
     */
    @Override
    public List<MediaType> resolveMediaTypes(NativeWebRequest nativeWebRequest) throws HttpMediaTypeNotAcceptableException {
        String acceptTypes = nativeWebRequest.getHeader("Accept");
        String queryAcceptType = nativeWebRequest.getParameter(http_config_options.getHttpProperties().getFavourParamName()); //from query-params format=mediaType.
        List<MediaType> result = new ArrayList<>();

        //console log.
        System.out.println("Accept header: '" + acceptTypes + "'");
        System.out.println("mediaType: '" + ((queryAcceptType == null) ? "" : queryAcceptType) + "'");

        if(queryAcceptType == null || queryAcceptType.length() == 0) { //if not 1. goto 2.
            if(acceptTypes == null || acceptTypes.length() == 0){ //if not 2 goto 3.
               if(this.defaulMediaType != null)
                   result.add(this.defaulMediaType); //3. add defult type if present.
            }
            else{

                //text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
               result.addAll(this.stringToMediaTypes.getMedias(acceptTypes, this.defaulMediaType)); //2. add types from 'Accept' request header.
            }
        }
        else {
            result.addAll(this.stringToMediaTypes.getMedias(queryAcceptType, this.defaulMediaType)); //1. add types from query parameter.
        }

        //result.retainAll(supported);
        System.out.println("resolvedTypes: " + StringHelper.iterableToString(result));
        System.out.println("supportedTypes: " + StringHelper.iterableToString(supported));

        //if three steps returns nothing => throw exception.
        if(result.size() == 0)
            throw new HttpMediaTypeNotAcceptableException("Cannot accept mediaTypes.");

        return result;
    }
}