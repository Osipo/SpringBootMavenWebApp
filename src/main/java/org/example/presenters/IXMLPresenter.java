package org.example.presenters;

//Cast body to the 'xml'

import org.springframework.stereotype.Service;

@Service
public interface IXMLPresenter {
    String present(Object rawBody);
}
