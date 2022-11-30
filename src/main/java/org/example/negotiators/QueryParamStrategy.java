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
@Component("query_default_strategy")
public class QueryParamStrategy implements ContentNegotiationStrategy {

    //injected through constructor
    ApplicationProperties http_config_options;
    IStringToMediaTypeConverter stringToMediaTypes;
    MediaType defaulMediaType;

    @Autowired
    @Qualifier("supportedMediaTypes")
    List<String> supported;

    @Autowired
    public QueryParamStrategy(ApplicationProperties http_config_options, @Qualifier("str_media_map_with_aliases") IStringToMediaTypeConverter stringToMediaTypes){
        this.http_config_options = http_config_options;
        this.stringToMediaTypes = stringToMediaTypes;
        this.defaulMediaType = this.stringToMediaTypes.getFirstByQualityOrDefault(http_config_options.getHttpProperties().getDefaultMediaTypeString(), null);
    }


    @Override
    public List<MediaType> resolveMediaTypes(NativeWebRequest nativeWebRequest) throws HttpMediaTypeNotAcceptableException {
        String acceptTypes = nativeWebRequest.getHeader("Accept");
        String queryAcceptType = nativeWebRequest.getParameter(http_config_options.getHttpProperties().getFavourParamName()); //from query-params format=mediaType.
        List<MediaType> result = new ArrayList<>();

        //console log.
        System.out.println("Accept header: '" + acceptTypes + "'");
        System.out.println("mediaType: '" + ((queryAcceptType == null) ? "" : queryAcceptType) + "'");

        if( (queryAcceptType == null || queryAcceptType.length() == 0) && this.defaulMediaType != null) {
            result.add(this.defaulMediaType);
        }
        else if (queryAcceptType != null && queryAcceptType.length() > 0) {
            result.addAll(this.stringToMediaTypes.getMedias(queryAcceptType, this.defaulMediaType));
        }

        result.retainAll(supported);
        System.out.println("resolvedTypes: " + StringHelper.iterableToString(result));

        if(result.size() == 0)
            throw new HttpMediaTypeNotAcceptableException("Cannot accept mediaTypes.");

        return result;
    }
}