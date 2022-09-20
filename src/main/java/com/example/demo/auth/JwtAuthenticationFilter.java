//package com.example.demo.auth;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@RequiredArgsConstructor
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final TokenProvider tokenProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
//        try{
//            //요청에서 토근 가져오기
//            String token=parseBearerToken(request);
//            //토큰 검사하기. JWT이므로 인가 서버에 요청하지 않고도 검증 가능
//            if(token!=null&&!token.equalsIgnoreCase("null")){
//                //student id 가져오기. 위조된 경우 예외처리
//                String studentId=tokenProvider.validateAndGetStudentId(token);
//                //인증 완료.
//                AbstractAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(studentId,null, AuthorityUtils.NO_AUTHORITIES);
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContext securityContext= SecurityContextHolder.createEmptyContext();
//                securityContext.setAuthentication(authentication);
//                SecurityContextHolder.setContext(securityContext);
//            }
//        } catch (Exception ex) {
//            logger.error("Could not set student authentication in security context",ex);
//        }
//
//        filterChain.doFilter(request,response);
//    }
//
//    private String parseBearerToken(HttpServletRequest request){
//        String bearerToken=request.getHeader("Authorization");
//
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//}
