package com.kjeldsen.player.rest.providers;

import com.kjeldsen.player.rest.views.ResponseView;

public class ResponseViewProvider {

    public static ResponseView getView(String tokenIdentification, String identification  ) {
        if (tokenIdentification.equals(identification)) {
            return ResponseView.PRIVATE;
        } else {
            return ResponseView.PUBLIC;
        }
    }
}
