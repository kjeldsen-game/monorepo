package com.kjeldsen.security;

// @Component
public class AuthenticationFetcher {

  /*  @Autowired
    private AuthenticationManager authenticationManager;

    public static final String TOKEN_ENHANCE_USER_ID = "uid";

    public String getLoggedUserUsername() {
        Authentication authentication = getAuthentication();
        return authentication.getName();
    }

    public String getLoggedUserID() {
        Authentication authentication = this.getAuthentication();
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, ?> tokenData = parser.parseMap(JwtHelper.decode(details.getTokenValue()).getClaims());
        return (String) tokenData.get(TOKEN_ENHANCE_USER_ID);
        return "1";
    }

    private Authentication getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).orElseThrow(
            () -> new RuntimeException("Authentication can't be null"));
    }
    */
}
