package com.lcwd.electronic.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public  class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        //Authorisation

        String requestHeader = request.getHeader("Authorisation");
        logger.info("Header : {} ", requestHeader);




        String username = null;
        String token  = null;

        if(requestHeader!=null && requestHeader.startsWith("Bearer"))
        {
            token = requestHeader.substring(7);
            try{
                username = this.helper.getUsernameFromToken(token);

            }catch(IllegalArgumentException e){
                logger.info("Inllegal argument while fetching the username");
                e.printStackTrace();

            }catch(ExpiredJwtException e){
                logger.info("Given jwt token is expired");
                e.printStackTrace();

            }catch (MalformedJwtException e)
            {
                logger.info("Someone has changed the token. Invalid Token !!");
                e.printStackTrace();
            }

        }
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validToken= this.jwtHelper.validateToken(token, userDetails);

            if(validToken)
            {
                //set the authentication

                UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }else{
                logger.info("Validation fails !!");
            }
        }
        filterChain.doFilter(request, response);

    }
}
