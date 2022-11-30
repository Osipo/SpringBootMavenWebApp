package org.example.presenters;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;

/*
    Custom xml presenter. Return string as well-formed xml document.
 */
@Component("xml_presenter_simple")
public class SimpleXmlPresenter implements IXMLPresenter {
    @Override
    public String present(Object rawBody) {
        StringBuilder d = new StringBuilder();

        //attach prolog.
        d.append("<?xml version='1.0' encoding='UTF-8'?>\n");

        if(rawBody instanceof Number || rawBody instanceof String || rawBody instanceof Boolean || rawBody instanceof Character){
            d.append("<value>");
            d.append(rawBody);
            d.append("</value>");
        }
        else if(rawBody instanceof Iterable){
            d.append("<Items>\n");
            for(Object el : (Iterable) rawBody){
                resolveType(d, el);
            }
            d.append("</Items>");
        }
        return d.toString();
    }

    private void resolveType(StringBuilder sb, Object el){
        if(el instanceof Number || el instanceof String || el instanceof Boolean || el instanceof Character)
            sb.append("\t<Item>").append(el.toString()).append("</Item>\n");
    }
}
